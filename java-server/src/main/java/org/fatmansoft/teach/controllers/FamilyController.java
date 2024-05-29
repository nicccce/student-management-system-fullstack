package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.FamilyRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.service.FamilyService;
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
import java.util.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/family")
public class FamilyController {
    @Autowired
    FamilyService familyService;
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
    private FamilyRepository familyRepository;


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
    public synchronized Integer getNewFamilyId(){
        Integer  id = familyRepository.getMaxId();  // 查询最大的id
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
    public Map getMapFromFamily(Family s) {
        Map m = new HashMap();
        Person p;
        if(s == null)
            return m;
        m.put("familySize",s.getFamilySize());
        m.put("fatherName",s.getFatherName());
        p = s.getPerson();
        if(p == null)
            return m;
        m.put("familyId", s.getFamilyId());
        m.put("personId", p.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("fatherOccupation",s.getFatherOccupation());
        m.put("fatherAge",s.getFatherAge());
        m.put("fatherContact",s.getFatherContact());
        m.put("motherName",s.getMotherName());
        m.put("motherOccupation",s.getMotherOccupation());
        m.put("address",s.getAddress());
        m.put("motherAge",s.getMotherAge());
        m.put("motherContact",s.getMotherContact());
        return m;
    }

    /**
     *  getStudentMapList 根据输入参数查询得到教师数据的 Map List集合 参数为空 查出所有教师， 参数不为空，查出人员编号或人员名称 包含输入字符串的教师
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getFamilyMapList(String numName) {
        List dataList = new ArrayList();
        List<Family> sList = familyRepository.findFamilyListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromFamily(sList.get(i)));
        }
        return dataList;
    }


    @PostMapping("/getFamilyList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getFamilyList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getFamilyMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }



    /**
     * getStudentInfo 前端点击教师列表时前端获取教师详细信息请求服务
     * @param dataRequest 从前端获取 teacherId 查询教师信息的主键 teacher_id
     * @return  根据teacherId从数据库中查出数据，存在Map对象里，并返回前端
     */

    @PostMapping("/getFamilyInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getFamilyInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer familyId = dataRequest.getInteger("familyId");
        Family s= null;
        Optional<Family> op;
        if(familyId != null) {
            op= familyRepository.findById(familyId); //根据教师主键从数据库查询教师的信息
            if(op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromFamily(s)); //这里回传包含教师信息的Map对象
    }

    /**
     * teacherEditSave 前端教师信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Person, User,Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * teacherId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * @return  新建修改教师的主键 teacher_id 返回前端
     */
    @PostMapping("/familyEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse familyEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer familyId = dataRequest.getInteger("familyId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取属性的值
        Family s= null;
        Person p;
        User u;
        Optional<Family> op;
        Integer personId;
        if(familyId != null) {
            op= familyRepository.findById(familyId);  //查询对应数据库中主键为id的值的实体对象
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
            p = s.getPerson();
            personId = p.getPersonId();


        s.setAddress(CommonMethod.getString(form,"address"));
        s.setFamilySize(CommonMethod.getString(form,"familySize"));
        s.setFatherName(CommonMethod.getString(form,"fatherName"));
        s.setFatherOccupation(CommonMethod.getString(form,"fatherOccupation"));
        s.setFatherAge(CommonMethod.getString(form,"fatherAge"));
        s.setFatherContact(CommonMethod.getString(form,"fatherContact"));
        s.setMotherName(CommonMethod.getString(form,"motherName"));
        s.setMotherOccupation(CommonMethod.getString(form,"motherOccupation"));
        s.setMotherAge(CommonMethod.getString(form,"motherAge"));
        s.setMotherContact(CommonMethod.getString(form,"motherContact"));
        familyRepository.save(s);  //修改保存教师信息
        return CommonMethod.getReturnData(s.getFamilyId());  // 将teacherId返回前端
    }


    /**
     * teacherDeleteAll 删除教师信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的教师信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端teacherId 药删除的教师的主键 teacher_id
     * @return  正常操作
     */
    @DeleteMapping("/familyDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse familyDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return familyService.familyDeleteAll(dataRequest);
    }

    /**
     * teacherInsert 增添教师信息Web服务 Student页面的列表里点击添加按钮则可以添加教师信息
     *
     * @param dataRequest  前端教师实体信息
     * @return  正常操作
     */
    @PostMapping ("/familyInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse familyInsert (@Valid @RequestBody Request<Map<String, FamilyRequest>> dataRequest) {
        return familyService.familyInsert(dataRequest);
    }

   /* *//**
     * 获取教师的部门列表。
     *
     * @return 包含教师部门的数据响应对象
     *//*
    @GetMapping("/dept")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherDeptList() {
        List<String> deptList = teacherService.getTeacherDeptList();
        return CommonMethod.getReturnData(deptList);
    }

    *//**
     * 获取教师的专业列表。
     *
     * @return 包含教师专业的数据响应对象
     *//*
    @GetMapping("/position")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherPositionList() {
        List<String> positionList = teacherService.getTeacherPositionList();
        return CommonMethod.getReturnData(positionList);
    }

    *//**
     * 获取教师的班级列表。
     *
     * @return 包含教师班级的数据响应对象
     *//*
    @GetMapping("/qualification")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentClassNameList() {
        List<String> qualificationList = teacherService.getTeacherQualificationList();
        return CommonMethod.getReturnData(qualificationList);
    }

    *//**
     * 根据前端的筛选数据获取教师列表
     * @param dataRequest 前端请求参数，包含筛选数据
     * @param numName 前端的查询框数据
     * @return 查询到的教师信息
     *//*
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

    *//**
     * 根据前端的筛选数据获取教师列表
     * @param dataRequest 前端请求参数，包含需要查询的教师学号或者姓名
     * @return 查询到的教师信息
     *//*
    @PostMapping("/getTeacherListByFilter/")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherListByFilter(@Valid @RequestBody Request<Map<String,TeacherRequest>> dataRequest) {
        String numName = "";
        TeacherRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List dataList = teacherService.getTeacherListByFilterAndNumName(filterCriteria,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    *//**
     * getStudentListExcl 前端下载导出选中的教师基本信息Excl表数据
     * @param dataRequest 前端传入需要生成的教师信息列表
     * @return 生成的Excel文件流
     *//*
    @PostMapping("/getSelectedTeacherListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedTeacherListExcl(@Valid @RequestBody Request<Map<String,List<TeacherRequest>>> dataRequest) {
        List<TeacherRequest> selectedList =dataRequest.getData().get("selectedTeacher");
        return teacherService.getSelectedTeacherListExcl(selectedList);
    }*/
}
