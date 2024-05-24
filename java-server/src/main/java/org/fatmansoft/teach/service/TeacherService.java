package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    public DataResponse teacherDeleteAll(DataRequest dataRequest) {
        List<Integer> allTeacherIds = dataRequest.getList("teacherId");  // 获取studentId值

        if (allTeacherIds == null || allTeacherIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无教师实体传入");
        }

        for (Integer teacherId : allTeacherIds) {
            Optional<Teacher> op = teacherRepository.findById(teacherId);   // 查询获得实体对象
            if (op.isPresent()) {
                Teacher s = op.get();
                Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); // 查询对应该学生的账户
                if (uOp.isPresent()) {
                    userRepository.delete(uOp.get()); // 删除对应该学生的账户
                }
                Person p = s.getPerson();
                teacherRepository.delete(s);    // 首先数据库永久删除教师信息
                personRepository.delete(p);     // 然后数据库永久删除人员信息
            } else {
                return CommonMethod.getReturnMessageError("教师ID传入错误：" + teacherId);
            }
        }

        return CommonMethod.getReturnMessageOK();  // 通知前端操作正常
    }
    public DataResponse teacherInsert(Request<Map<String, TeacherRequest>> dataRequest) {
        Request<Map<String,TeacherRequest>> request = new Request<>(dataRequest.getData());
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        try {
            // 创建或更新学生实体
            Teacher teacher = new Teacher(request.getData().get("newTeacher"));
            String num = teacher.getPerson().getNum();
            Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
            if(nOp.isPresent()||teacher.getPerson().getNum()==""||teacher.getPerson().getNum()==null) {
                return CommonMethod.getReturnMessageError("新学号已经存在或无效，不能添加或修改！");
            }if (teacher.getPerson().getEmail()!=""&&!EmailValidator.isValidEmail(teacher.getPerson().getEmail())){
                return CommonMethod.getReturnMessageError("不是合法的邮箱地址！");
            }
            personRepository.save(teacher.getPerson());
            teacherRepository.save(teacher);
            String password = encoder.encode("123456");
            User u= new User();
            u.setUserId(teacher.getPerson().getPersonId());
            u.setPerson(teacher.getPerson());
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            userRepository.saveAndFlush(u); //插入新的User记录
            return CommonMethod.getReturnMessageOK("教师信息保存成功");

        }catch (ConstraintViolationException e){
            return CommonMethod.getReturnMessageError(e.getMessage());
        } catch (Exception e) {
            return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        }
    }


}
