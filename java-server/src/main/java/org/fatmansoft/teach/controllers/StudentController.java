package org.fatmansoft.teach.controllers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.data.vo.OptionItem;
import org.fatmansoft.teach.data.vo.OptionItemList;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.service.StudentService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.MultipartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.*;
import java.util.*;
/**
 *  StudentController 主要是为学生管理数据管理提供的Web请求服务
 *
 */

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {
    //Java 对象的注入 我们定义的这下Java的操作对象都不能自己管理是由有Spring框架来管理的， StudentController 中要使用StudentRepository接口的实现类对象，
    // 需要下列方式注入，否则无法使用， studentRepository 相当于StudentRepository接口实现对象的一个引用，由框架完成对这个引用的赋值，
    // StudentController中的方法可以直接使用
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private ScoreRepository scoreRepository;  //成绩数据操作自动注入
    @Autowired
    private FeeRepository feeRepository;  //消费数据操作自动注入
    @Autowired
    private BaseService baseService;   //基本数据处理数据操作自动注入
    @Autowired
    private StudentService studentService; //注入学生服务


    /**
     *  获取 person 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 person_id
     * @return
     */
    public synchronized Integer getNewPersonId(){  //synchronized 同步方法
        Integer  id = personRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    /**
     *  获取 user 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 user_id
     * @return
     */
    public synchronized Integer getNewUserId(){
        Integer  id = userRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    /**
     *  获取 student 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 student_id
     * @return id
     */
    public synchronized Integer getNewStudentId(){
        Integer  id = studentRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    /**
     *  获取 fee 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 fee_id
     * @return
     */
    public synchronized Integer getNewFeeId(){
        Integer  id = feeRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };


    /**
     * getMapFromStudent 将学生表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromStudent(Student s) {
        Map m = new HashMap();
        Person p;
        if(s == null)
            return m;
        m.put("major",s.getMajor());
        m.put("className",s.getClassName());
        p = s.getPerson();
        if(p == null)
            return m;
        m.put("studentId", s.getStudentId());
        m.put("personId", p.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("dept",p.getDept());
        m.put("card",p.getCard());
        String gender = p.getGender();
        m.put("gender",gender);
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", p.getBirthday());  //时间格式转换字符串
        m.put("email",p.getEmail());
        m.put("phone",p.getPhone());
        m.put("address",p.getAddress());
        m.put("introduce",p.getIntroduce());
        return m;
    }

    /**
     *  getStudentMapList 根据输入参数查询得到学生数据的 Map List集合 参数为空 查出所有学生， 参数不为空，查出人员编号或人员名称 包含输入字符串的学生
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getStudentMapList(String numName) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromStudent(sList.get(i)));
        }
        return dataList;
    }

    /**
     *  getStudentList 学生管理 点击查询按钮请求
     *  前台请求参数 numName 学号或名称的 查询串
     * 返回前端 存储学生信息的 MapList 框架会自动将Map转换成用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
     * @return
     */
    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem(s.getStudentId(), s.getPerson().getNum(), s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }

    /**
     * 根据前端的数据获取学生列表，若传回来的是空串则返回全体学生，否则返回根据学号姓名查询的学生数据
     * @param dataRequest 前端请求参数，包含需要查询的学生学号或者姓名
     * @return 查询到的学生信息
     */
    @PostMapping("/getStudentList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getStudentMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * studentDelete 删除学生信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端studentId 药删除的学生的主键 student_id
     * @return  正常操作
     */

    @PostMapping("/studentDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");  //获取student_id值
        Student s= null;
        Optional<Student> op;
        if(studentId != null) {
            op= studentRepository.findById(studentId);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); //查询对应该学生的账户
            if(uOp.isPresent()) {
                userRepository.delete(uOp.get()); //删除对应该学生的账户
            }
            Person p = s.getPerson();
            studentRepository.delete(s);    //首先数据库永久删除学生信息
            personRepository.delete(p);   // 然后数据库永久删除学生信息
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    /**
     * getStudentInfo 前端点击学生列表时前端获取学生详细信息请求服务
     * @param dataRequest 从前端获取 studentId 查询学生信息的主键 student_id
     * @return  根据studentId从数据库中查出数据，存在Map对象里，并返回前端
     */
    @PostMapping("/getStudentInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Student s= null;
        Optional<Student> op;
        if(studentId != null) {
            op= studentRepository.findById(studentId); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromStudent(s)); //这里回传包含学生信息的Map对象
    }

    /**
     * studentEditSave 前端学生信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Person, User,Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * studentId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * @return  新建修改学生的主键 student_id 返回前端
     */
    @PostMapping("/studentEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer studentId = dataRequest.getInteger("studentId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取属性的值
        Student s= null;
        Person p;
        User u;
        Optional<Student> op;
        Integer personId;
        if(studentId != null) {
            op= studentRepository.findById(studentId);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if(nOp.isPresent()) {
            if(s == null || !s.getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新学号已经存在，不能添加或修改！");
            }
        }
        if(s == null) {
            personId = getNewPersonId(); //获取Person新的主键，这个是线程同步问题;
            p = new Person();
            p.setPersonId(personId);
            p.setNum(num);
            p.setType("1");
            personRepository.saveAndFlush(p);  //插入新的Person记录
            String password = encoder.encode("123456");
            u= new User();
            u.setUserId(getNewUserId());
            u.setPerson(p);
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            userRepository.saveAndFlush(u); //插入新的User记录
            s = new Student();   // 创建实体对象
            s.setStudentId(getNewStudentId());
            s.setPerson(p);
            studentRepository.saveAndFlush(s);  //插入新的Student记录
        }else {
            p = s.getPerson();
            personId = p.getPersonId();
        }
        if(!num.equals(p.getNum())) {   //如果人员编号变化，修改人员编号和登录账号
            Optional<User>uOp = userRepository.findByPersonPersonId(personId);
            if(uOp.isPresent()) {
                u = uOp.get();
                u.setUserName(num);
                userRepository.saveAndFlush(u);
            }
            p.setNum(num);  //设置属性
        }
        p.setName(CommonMethod.getString(form,"name"));
        p.setDept(CommonMethod.getString(form,"dept"));
        p.setCard(CommonMethod.getString(form,"card"));
        p.setGender(CommonMethod.getString(form,"gender"));
        p.setBirthday(CommonMethod.getString(form,"birthday"));
        p.setEmail(CommonMethod.getString(form,"email"));
        p.setPhone(CommonMethod.getString(form,"phone"));
        p.setAddress(CommonMethod.getString(form,"address"));
        personRepository.save(p);  // 修改保存人员信息
        s.setMajor(CommonMethod.getString(form,"major"));
        s.setClassName(CommonMethod.getString(form,"className"));
        studentRepository.save(s);  //修改保存学生信息
        return CommonMethod.getReturnData(s.getStudentId());  // 将studentId返回前端
    }


    /**
     * getStudentScoreList 将Score对象列表集合转换成Score Map对象列表集合
     * @param sList
     * @return
     */
    public List getStudentScoreList(List<Score>sList){
        List list = new ArrayList();
        if(sList == null || sList.size() == 0)
            return list;
        Map m;
        Course c;
        for(Score s:sList){
            m = new HashMap();
            c = s.getCourse();
            m.put("studentNum",s.getStudent().getPerson().getNum());
            m.put("scoreId",s.getScoreId());
            m.put("courseNum", c.getNum());
            m.put("courseName", c.getName());
            m.put("credit", c.getCredit());
            m.put("mark", s.getMark());
            m.put("ranking", s.getRanking());
            list.add(m);
        }
        return list;
    }

    /**
     * getStudentMarkList 计算学生的的成绩等级
     * @param sList 学生成绩列表
     * @return 成绩等级Map对象列表
     */
    public List getStudentMarkList(List<Score> sList){
        String title[]={"优","良","中","及格","不及格"};
        int count[]= new int[5];
        List list = new ArrayList();
        if(sList == null || sList.size() == 0)
            return list;
        Map m;
        Course c;
        for(Score s:sList){
            c = s.getCourse();
            if(s.getMark() >= 90)
                count[0]++;
            if(s.getMark() >= 80)
                count[1]++;
            if(s.getMark() >= 70)
                count[2]++;
            if(s.getMark() >= 60)
                count[3]++;
            else
                count[4]++;
        }
        for(int i = 0; i < 5;i++) {
            m = new HashMap();
            m.put("title", title[i]);
            m.put("value", count[i]);
            list.add(m);
        }
        return list;
    }

    /**
     * getStudentFeeList 获取学生的消费Map对象列表集合
     * @param studentId
     * @return
     */
    public List getStudentFeeList(Integer studentId){
        List<Fee> sList =feeRepository.findListByStudent(studentId);  // 查询某个学生消费记录集合
        List list = new ArrayList();
        if(sList == null || sList.size() == 0)
            return list;
        Map m;
        Course c;
        for(Fee s:sList){
            m = new HashMap();
            m.put("title",s.getDay());
            m.put("value",s.getMoney());
            list.add(m);
        }
        return list;
    }

    /**
     * getStudentIntroduceData 前端获取学生个人简历数据请求服务
     * @param dataRequest 从前端获取 studentId 查询学生信息的主键 student_id
     * @return  根据studentId从数据库中查出相关数据，存在Map对象里，并返回前端
     */
    @PostMapping("/getStudentIntroduceData")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getStudentIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);  // 查询获得 user对象
        if(!uOp.isPresent())
            return CommonMethod.getReturnMessageError("用户不存在！");
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonPersonId(u.getUserId());  // 查询获得 Student对象
        if(!sOp.isPresent())
            return CommonMethod.getReturnMessageError("学生不存在！");
        Student s= sOp.get();
        Map info = getMapFromStudent(s);  // 查询学生信息Map对象
        List<Score> sList = scoreRepository.findByStudentStudentId(s.getStudentId()); //获得学生成绩对象集合
        Map data = new HashMap();
        data.put("info",info);
        data.put("scoreList",getStudentScoreList(sList));
        data.put("markList",getStudentMarkList(sList));
        data.put("feeList",getStudentFeeList(s.getStudentId()));
        return CommonMethod.getReturnData(data);//将前端所需数据保留Map对象里，返还前端
    }

    /**
     * saveStudentIntroduce 前端学生个人简介信息introduce提交服务
     * @param dataRequest 从前端获取 studentId student表 student_id introduce 学生个人简介信息
     * @return  操作正常
     */

    @PostMapping("/saveStudentIntroduce")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse saveStudentIntroduce(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        String introduce = dataRequest.getString("introduce");
        Optional<Student> sOp= studentRepository.findById(studentId);
        if(!sOp.isPresent())
            return CommonMethod.getReturnMessageError("学生不存在！");
        Student s= sOp.get();
        Person p = s.getPerson();
        p.setIntroduce(introduce);
        personRepository.save(p);
        return CommonMethod.getReturnMessageOK();
    }

    /** importFeeData 前端上传消费流水Excl表数据服务
      * @param barr  文件二进制数据
     * @param uploader  上传者
     * @param studentIdStr  student 主键
     * @param fileName  前端上传的文件名
     * @return
     */

    @PostMapping(path = "/importFeeData")
    public DataResponse importFeeData(@RequestBody byte[] barr,
                                   @RequestParam(name = "uploader") String uploader,
                                      @RequestParam(name = "studentId") String studentIdStr,
                                   @RequestParam(name = "fileName") String fileName) {
        try {
            Integer studentId = Integer.parseInt(studentIdStr);
            InputStream in = new ByteArrayInputStream(barr);
            XSSFWorkbook workbook = new XSSFWorkbook(in);  //打开Excl数据流
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            Cell cell;
            int i;
            i = 1;
            String day, money;
            Optional<Fee> fOp;
            Double dMoney;
            Fee f;
            rowIterator.next();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                cell = row.getCell(0);
                if (cell == null)
                    break;
                day = cell.getStringCellValue();  //获取一行消费记录 日期 金额
                cell = row.getCell(1);
                money = cell.getStringCellValue();
                fOp = feeRepository.findByStudentIdAndDay(studentId, day);  //查询是否存在记录
                if (!fOp.isPresent()) {
                    f = new Fee();
                    f.setFeeId(getNewFeeId());
                    f.setDay(day);
                    f.setStudentId(studentId);  //不存在 添加
                } else {
                    f = fOp.get();  //存在 更新
                }
                if (money != null && money.length() > 0)
                    dMoney = Double.parseDouble(money);
                else
                    dMoney = 0d;
                f.setMoney(dMoney);
                feeRepository.save(f);
                System.out.println(i++);
            }
            workbook.close();  //关闭Excl输入流
            return CommonMethod.getReturnMessageOK();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonMethod.getReturnMessageError("上传错误！");
        }
    }

    /**
     * getStudentListExcl 前端下载导出学生基本信息Excl表数据
     * @param dataRequest
     * @return
     */
    @PostMapping("/getStudentListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getStudentListExcl(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List list = getStudentMapList(numName);
        Integer widths[] = {8, 20, 10, 15, 15, 15, 25, 10, 15, 30, 20, 30};
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "学院", "专业", "班级", "证件号码", "性别","出生日期","邮箱","电话","地址"};
        String outPutSheetName = "student.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 11);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        Map m;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                m = (Map) list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(CommonMethod.getString(m,"num"));
                cell[2].setCellValue(CommonMethod.getString(m,"name"));
                cell[3].setCellValue(CommonMethod.getString(m,"dept"));
                cell[4].setCellValue(CommonMethod.getString(m,"major"));
                cell[5].setCellValue(CommonMethod.getString(m,"className"));
                cell[6].setCellValue(CommonMethod.getString(m,"card"));
                cell[7].setCellValue(CommonMethod.getString(m,"genderName"));
                cell[8].setCellValue(CommonMethod.getString(m,"birthday"));
                cell[9].setCellValue(CommonMethod.getString(m,"email"));
                cell[10].setCellValue(CommonMethod.getString(m,"phone"));
                cell[11].setCellValue(CommonMethod.getString(m,"address"));
            }
        }
        try {
            StreamingResponseBody stream = outputStream -> {
                wb.write(outputStream);
            };
            return ResponseEntity.ok()
                    .contentType(CommonMethod.exelType)
                    .body(stream);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * getStudentIntroducePdf 生成获取个人简历的PDF数据流服务
     * @param dataRequest  studentId 学生主键
     * @return  返回PDF文件二进制数据
     */
    @PostMapping("/getStudentIntroducePdf")
    public ResponseEntity<StreamingResponseBody> getStudentIntroducePdf(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Student s = studentRepository.getById(studentId);  //查询获得Student对象
        Map info = getMapFromStudent(s); //获得学生信息
        String content = (String)info.get("introduce");  // 个人简历的HTML字符串
        content = CommonMethod.addHeadInfo(content,"<style> html { font-family: \"SourceHanSansSC\", \"Open Sans\";}  </style> <meta charset='UTF-8' />  <title>Insert title here</title>");  // 插入由HTML转换PDF需要的头信息
        System.out.println(content);
        content = CommonMethod.removeErrorString(content,"&nbsp;","style=\"font-family: &quot;&quot;;\""); //删除无法转化不合法的HTML标签
        content= CommonMethod.replaceNameValue(content,info); //将HTML中标记串${name}等替换成学生实际的信息
        return baseService.getPdfDataFromHtml(content); //生成学生简历PDF二进制数据
    }
    @Value("${attach.folder}")    //环境配置变量获取
    private String attachFolder;  //服务器端数据存储
    @PostMapping("/getStudentPdf")
    public ResponseEntity<StreamingResponseBody> getStudentPdf(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        String filePath = attachFolder + "pdf/" + studentId +".pdf";
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            try {
                InputStream inputStream = new FileInputStream(file);

                StreamingResponseBody responseBody = outputStream -> {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                };

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "个人简历");

                return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * studentDeleteAll 删除学生信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端studentId 药删除的学生的主键 student_id
     * @return  正常操作
     */
    @DeleteMapping ("/studentDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentDeleteAll(dataRequest);
    }

    /**
     * studentInsert 增添学生信息Web服务 Student页面的列表里点击添加按钮则可以添加学生信息
     *
     * @param dataRequest  前端学生实体信息
     * @return  正常操作
     */
    @PostMapping ("/studentInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentInsert (@Valid @RequestBody Request<Map<String, StudentRequest>> dataRequest) {
        return studentService.studentInsert(dataRequest);
    }

    /**
     * 获取学生的部门列表。
     *
     * @return 包含学生部门的数据响应对象
     */
    @GetMapping("/dept")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentDeptList() {
        List<String> deptList = studentService.getStudentDeptList();
        return CommonMethod.getReturnData(deptList);
    }

    /**
     * 获取学生的专业列表。
     *
     * @return 包含学生专业的数据响应对象
     */
    @GetMapping("/major")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentMajorList() {
        List<String> majorList = studentService.getStudentMajorList();
        return CommonMethod.getReturnData(majorList);
    }

    /**
     * 获取学生的班级列表。
     *
     * @return 包含学生班级的数据响应对象
     */
    @GetMapping("/class")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentClassNameList() {
        List<String> classNameList = studentService.getStudentClassNameList();
        return CommonMethod.getReturnData(classNameList);
    }

    /**
     * 根据前端的筛选数据获取学生列表
     * @param dataRequest 前端请求参数，包含筛选数据
     * @param numName 前端的查询框数据
     * @return 查询到的学生信息
     */
    @PostMapping("/getStudentListByFilter/{numName}")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentListByFilterAndNumName(@Valid @RequestBody Request<Map<String,StudentRequest>> dataRequest,@PathVariable String numName) {
        if (numName == null){
            numName="";
        }
        StudentRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List dataList = studentService.getStudentListByFilterAndNumName(filterCriteria,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * 根据前端的筛选数据获取学生列表
     * @param dataRequest 前端请求参数，包含需要查询的学生学号或者姓名
     * @return 查询到的学生信息
     */
    @PostMapping("/getStudentListByFilter/")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentListByFilter(@Valid @RequestBody Request<Map<String,StudentRequest>> dataRequest) {
        String numName = "";
        StudentRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List dataList = studentService.getStudentListByFilterAndNumName(filterCriteria,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * getStudentListExcl 前端下载导出选中的学生基本信息Excl表数据
     * @param dataRequest 前端传入需要生成的学生信息列表
     * @return 生成的Excel文件流
     */
    @PostMapping("/getSelectedStudentListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedStudentListExcl(@Valid @RequestBody Request<Map<String,List<StudentRequest>>> dataRequest) {
        List<StudentRequest> selectedList =dataRequest.getData().get("selectedStudent");
        return studentService.getSelectedStudentListExcl(selectedList);
    }


    @PostMapping(path = "/importByExcel")
    public DataResponse importByExcel(@RequestBody byte[] barr,
                                      @RequestParam(name = "uploader") String uploader,
                                      @RequestParam(name = "fileName") String fileName) {
        try {
            MultipartFile studentExcel = MultipartFileUtils.convertToMultipartFile(barr,fileName);
            return studentService.importStudentByExcel(studentExcel);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonMethod.getReturnMessageError("上传错误！");
        }
    }
}
