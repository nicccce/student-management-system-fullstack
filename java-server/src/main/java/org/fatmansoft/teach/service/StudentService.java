package org.fatmansoft.teach.service;

import org.apache.commons.beanutils.BeanUtils;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.po.EUserType;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.User;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    public DataResponse studentDeleteAll(DataRequest dataRequest) {
        List<Integer> allStudentIds = dataRequest.getList("studentId");  // 获取studentId值

        if (allStudentIds == null || allStudentIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无学生实体传入");
        }

        for (Integer studentId : allStudentIds) {
            Optional<Student> op = studentRepository.findById(studentId);   // 查询获得实体对象
            if (op.isPresent()) {
                Student s = op.get();
                Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); // 查询对应该学生的账户
                if (uOp.isPresent()) {
                    userRepository.delete(uOp.get()); // 删除对应该学生的账户
                }
                Person p = s.getPerson();
                studentRepository.delete(s);    // 首先数据库永久删除学生信息
                //personRepository.delete(p);     // 然后数据库永久删除人员信息(不需要加，因为数据库中设计过了)
            } else {
                return CommonMethod.getReturnMessageError("学生ID传入错误：" + studentId);
            }
        }

        return CommonMethod.getReturnMessageOK();  // 通知前端操作正常
    }
    public DataResponse studentInsert(Request<Map<String, StudentRequest>> dataRequest) {
        Request<Map<String,StudentRequest>> request = new Request<>(dataRequest.getData());
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        try {
            // 创建或更新学生实体
            Student student = new Student(request.getData().get("newStudent"));
            String num = student.getPerson().getNum();
            Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
            if(nOp.isPresent()||student.getPerson().getNum()==""||student.getPerson().getNum()==null) {
                return CommonMethod.getReturnMessageError("新学号已经存在或无效，不能添加或修改！");
            }if (student.getPerson().getEmail()!=""&&!EmailValidator.isValidEmail(student.getPerson().getEmail())){
                return CommonMethod.getReturnMessageError("不是合法的邮箱地址！");
            }
            studentRepository.save(student);
            String password = encoder.encode("123456");
            User u= new User();
            u.setUserId(student.getPerson().getPersonId());
            u.setPerson(student.getPerson());
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            userRepository.saveAndFlush(u); //插入新的User记录
            return CommonMethod.getReturnMessageOK("学生信息保存成功");

        }catch (ConstraintViolationException e){
            return CommonMethod.getReturnMessageError(e.getMessage());
        } catch (Exception e) {
            return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        }
    }

    /**
     * 获取所有学生的部门列表，去重后返回。
     *
     * @return 学生的部门列表
     */
    public List<String> getStudentDeptList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueDepts = new HashSet<>();

        for (Student student : studentList) {
            String dept = student.getPerson().getDept();
            uniqueDepts.add(dept);
        }

        List<String> deptList = new ArrayList<>(uniqueDepts);
        return deptList;
    }

    /**
     * 获取所有学生的专业列表，去重后返回。
     *
     * @return 学生的专业列表
     */
    public List<String> getStudentMajorList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueMajors = new HashSet<>();

        for (Student student : studentList) {
            String major = student.getMajor();
            uniqueMajors.add(major);
        }

        List<String> majorList = new ArrayList<>(uniqueMajors);
        return majorList;
    }

    /**
     * 获取所有学生的班级列表，去重后返回。
     *
     * @return 学生的班级列表
     */
    public List<String> getStudentClassNameList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueClassNames = new HashSet<>();

        for (Student student : studentList) {
            String className = student.getClassName();
            uniqueClassNames.add(className);
        }

        List<String> classNameList = new ArrayList<>(uniqueClassNames);
        return classNameList;
    }

    public List<StudentRequest> getStudentListByFilterAndNumName(StudentRequest filterCriteria, String numName){
        Student filterCriteriaStudent = new Student(filterCriteria);
        List<Student> matchedStudent =  studentRepository.findByExample(filterCriteriaStudent, numName);
        List<StudentRequest> matchedStudentRequest = new ArrayList<>(){};
        for (Student student:
             matchedStudent) {
            matchedStudentRequest.add(new StudentRequest(student));
        }
        return matchedStudentRequest;
    }
}

