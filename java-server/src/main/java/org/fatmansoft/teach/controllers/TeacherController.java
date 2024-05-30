package org.fatmansoft.teach.controllers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.data.vo.OptionItem;
import org.fatmansoft.teach.data.vo.OptionItemList;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.service.TeacherService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //教师数据操作自动注入
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
    private TeacherRepository teacherRepository;


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
     *  获取 teacher 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 teacher_id
     * @return id
     */
    public synchronized Integer getNewTeacherId(){
        Integer  id = teacherRepository.getMaxId();  // 查询最大的id
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
     * getMapFromStudent 将教师表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromTeacher(Teacher s) {
        Map m = new HashMap();
        Person p;
        if(s == null)
            return m;
        m.put("position",s.getPosition());
        m.put("qualification",s.getQualification());
        p = s.getPerson();
        if(p == null)
            return m;
        m.put("teacherId", s.getTeacherId());
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
     *  getStudentMapList 根据输入参数查询得到教师数据的 Map List集合 参数为空 查出所有教师， 参数不为空，查出人员编号或人员名称 包含输入字符串的教师
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getTeacherMapList(String numName) {
        List dataList = new ArrayList();
        List<Teacher> sList = teacherRepository.findTeacherListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromTeacher(sList.get(i)));
        }
        return dataList;
    }


    @PostMapping("/getTeacherList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getTeacherMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }



    /**
     * getStudentInfo 前端点击教师列表时前端获取教师详细信息请求服务
     * @param dataRequest 从前端获取 teacherId 查询教师信息的主键 teacher_id
     * @return  根据teacherId从数据库中查出数据，存在Map对象里，并返回前端
     */

    @PostMapping("/getTeacherInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");
        Teacher s= null;
        Optional<Teacher> op;
        if(teacherId != null) {
            op= teacherRepository.findById(teacherId); //根据教师主键从数据库查询教师的信息
            if(op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromTeacher(s)); //这里回传包含教师信息的Map对象
    }

    /**
     * teacherEditSave 前端教师信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Person, User,Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * teacherId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * @return  新建修改教师的主键 teacher_id 返回前端
     */
    @PostMapping("/teacherEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer teacherId = dataRequest.getInteger("teacherId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取属性的值
        Teacher s= null;
        Person p;
        User u;
        Optional<Teacher> op;
        Integer personId;
        if(teacherId != null) {
            op= teacherRepository.findById(teacherId);  //查询对应数据库中主键为id的值的实体对象
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
            s = new Teacher();   // 创建实体对象
            s.setTeacherId(getNewTeacherId());
            s.setPerson(p);
            teacherRepository.saveAndFlush(s);  //插入新的Student记录
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
        s.setQualification(CommonMethod.getString(form,"qualification"));
        s.setPosition(CommonMethod.getString(form,"position"));
        teacherRepository.save(s);  //修改保存教师信息
        return CommonMethod.getReturnData(s.getTeacherId());  // 将teacherId返回前端
    }


    /**
     * teacherDeleteAll 删除教师信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的教师信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端teacherId 药删除的教师的主键 teacher_id
     * @return  正常操作
     */
    @DeleteMapping ("/teacherDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.teacherDeleteAll(dataRequest);
    }

    /**
     * teacherInsert 增添教师信息Web服务 Student页面的列表里点击添加按钮则可以添加教师信息
     *
     * @param dataRequest  前端教师实体信息
     * @return  正常操作
     */
    @PostMapping ("/teacherInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherInsert (@Valid @RequestBody Request<Map<String, TeacherRequest>> dataRequest) {
        return teacherService.teacherInsert(dataRequest);
    }

    /**
     * 获取教师的部门列表。
     *
     * @return 包含教师部门的数据响应对象
     */
    @GetMapping("/dept")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherDeptList() {
        List<String> deptList = teacherService.getTeacherDeptList();
        return CommonMethod.getReturnData(deptList);
    }

    /**
     * 获取教师的专业列表。
     *
     * @return 包含教师专业的数据响应对象
     */
    @GetMapping("/position")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherPositionList() {
        List<String> positionList = teacherService.getTeacherPositionList();
        return CommonMethod.getReturnData(positionList);
    }

    /**
     * 获取教师的班级列表。
     *
     * @return 包含教师班级的数据响应对象
     */
    @GetMapping("/qualification")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentClassNameList() {
        List<String> qualificationList = teacherService.getTeacherQualificationList();
        return CommonMethod.getReturnData(qualificationList);
    }

    /**
     * 根据前端的筛选数据获取教师列表
     * @param dataRequest 前端请求参数，包含筛选数据
     * @param numName 前端的查询框数据
     * @return 查询到的教师信息
     */
    @PostMapping("/getTeacherListByFilter/{numName}")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherListByFilterAndNumName(@Valid @RequestBody Request<Map<String,TeacherRequest>> dataRequest,@PathVariable String numName) {
        if (numName == null){
            numName="";
        }
        TeacherRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List dataList = teacherService.getTeacherListByFilterAndNumName(filterCriteria,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * 根据前端的筛选数据获取教师列表
     * @param dataRequest 前端请求参数，包含需要查询的教师学号或者姓名
     * @return 查询到的教师信息
     */
    @PostMapping("/getTeacherListByFilter/")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherListByFilter(@Valid @RequestBody Request<Map<String,TeacherRequest>> dataRequest) {
        String numName = "";
        TeacherRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List dataList = teacherService.getTeacherListByFilterAndNumName(filterCriteria,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    /**
     * getStudentListExcl 前端下载导出选中的教师基本信息Excl表数据
     * @param dataRequest 前端传入需要生成的教师信息列表
     * @return 生成的Excel文件流
     */
    @PostMapping("/getSelectedTeacherListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedTeacherListExcl(@Valid @RequestBody Request<Map<String,List<TeacherRequest>>> dataRequest) {
        List<TeacherRequest> selectedList =dataRequest.getData().get("selectedTeacher");
        return teacherService.getSelectedTeacherListExcl(selectedList);
    }
}



