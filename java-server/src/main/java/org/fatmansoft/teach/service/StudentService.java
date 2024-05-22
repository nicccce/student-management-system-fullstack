package org.fatmansoft.teach.service;

import org.apache.commons.beanutils.BeanUtils;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.User;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
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
                personRepository.delete(p);     // 然后数据库永久删除人员信息
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
//        try {
            // 创建或更新学生实体
            Student student = new Student(request.getData().get("newStudent"));
            studentRepository.save(student);

            return CommonMethod.getReturnMessageOK("学生信息保存成功");

//        } catch (Exception e) {
//            return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
//        }
    }

}