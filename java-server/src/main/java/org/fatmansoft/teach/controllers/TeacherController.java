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
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
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
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private BaseService baseService;   //基本数据处理数据操作自动注入


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
    public synchronized Integer getNewTeacherId(){
        Integer  id = teacherRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    /**
     * getMapFromStudent 将老师表属性数据转换复制MAp集合里
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
        m.put("joinDate",s.getJoinDate());

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
     *  getStudentMapList 根据输入参数查询得到老师数据的 Map List集合 参数为空 查出所有学生， 参数不为空，查出人员编号或人员名称 包含输入字符串的学生
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

//    /**
//     *  getStudentList 教师管理 点击查询按钮请求
//     *  前台请求参数 numName 学号或名称的 查询串
//     * 返回前端 存储教师信息的 MapList 框架会自动将Map转换成用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
//     * @return
//     */
//    @PostMapping("/getStudentItemOptionList")
//    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
//        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
//        OptionItem item;
//        List<OptionItem> itemList = new ArrayList();
//        for (Student s : sList) {
//            itemList.add(new OptionItem(s.getStudentId(), s.getPerson().getNum(), s.getPerson().getNum()+"-"+s.getPerson().getName()));
//        }
//        return new OptionItemList(0, itemList);
//    }

    @PostMapping("/getTeacherList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getTeacherMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * studentDelete 删除学生信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端studentId 药删除的学生的主键 student_id
     * @return  正常操作
     */

    @PostMapping("/teacherDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");  //获取teacher_id值
        Teacher s= null;
        Optional<Teacher> op;
        if(teacherId != null) {
            op= teacherRepository.findById(teacherId);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); //查询对应该教师的账户
            if(uOp.isPresent()) {
                userRepository.delete(uOp.get()); //删除对应该教师的账户
            }
            Person p = s.getPerson();
            teacherRepository.delete(s);    //首先数据库永久删除教师信息
            personRepository.delete(p);   // 然后数据库永久删除教师信息
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
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
            op= teacherRepository.findById(teacherId); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromTeacher(s)); //这里回传包含学生信息的Map对象
    }

    /**
     * teacherEditSave 前端学生信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Person, User,Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * teacherId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * @return  新建修改学生的主键 teacher_id 返回前端
     */
    @PostMapping("/teacherEditSave")
    @PreAuthorize(" hasRole('ADMIN') or hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
    public DataResponse teacherEditSave(@Valid @RequestBody DataRequest dataRequest) {
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
                return CommonMethod.getReturnMessageError("已经存在，不能添加或修改！");
            }
        }
        if(s == null) {
            personId = getNewPersonId(); //获取Person新的主键，这个是线程同步问题;
            p = new Person();
            p.setPersonId(personId);
            p.setNum(num);
            p.setType("2");
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
            teacherRepository.saveAndFlush(s);  //插入新的Teacher记录
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
        s.setPosition(CommonMethod.getString(form,"position"));
        s.setQualification(CommonMethod.getString(form,"qualification"));
        s.setJoinDate(CommonMethod.getLocalDateTime(form,"joinDate"));
        teacherRepository.save(s);  //修改保存学生信息
        return CommonMethod.getReturnData(s.getTeacherId());  // 将studentId返回前端
    }


    /**
     * getStudentScoreList 将Score对象列表集合转换成Score Map对象列表集合
     * @param sList
     * @return
     */
    public List getStudentScoreList(List<Score>sList) {
        List list = new ArrayList();
        if (sList == null || sList.size() == 0)
            return list;
        Map m;
        Course c;
        for (Score s : sList) {
            m = new HashMap();
            c = s.getCourse();
            m.put("studentNum", s.getStudent().getPerson().getNum());
            m.put("scoreId", s.getScoreId());
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
     * teacherDeleteAll 删除教师信息Web服务 Teacher页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Teacher关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端TeacherId 药删除的学生的主键 teacher_id
     * @return  正常操作
     */
    @DeleteMapping ("/teacherDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.teacherDeleteAll(dataRequest);
    }
    /**
     * teacherInsert 增添教师信息Web服务 Teacher页面的列表里点击添加按钮则可以添加教师信息
     *
     * @param dataRequest  前端教师实体信息
     * @return  正常操作
     */
    @PostMapping ("/teacherInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherInsert (@Valid @RequestBody Request<Map<String, TeacherRequest>> dataRequest) {
        return teacherService.teacherInsert(dataRequest);
    }


}



