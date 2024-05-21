package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.dto.DataRequest;
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
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    public DataResponse studentDeleteAll( DataRequest dataRequest) {
        List AllstudentId = dataRequest.getList("studentId");  //获取student_id值
        Student s= null;
        Optional<Student> op;
        for (int i=0;i< AllstudentId.size();i++) {
            if(AllstudentId != null) {
                op= studentRepository.findById(AllstudentId.indexOf(i));   //查询获得实体对象
                if(op.isPresent()) {
                    s = op.get();
                }
            } else {
                return CommonMethod.getReturnMessageError("无学生实体传入");
            }
            if(s != null) {
                Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); //查询对应该学生的账户
                if(uOp.isPresent()) {
                    userRepository.delete(uOp.get()); //删除对应该学生的账户
                }
                Person p = s.getPerson();
                studentRepository.delete(s);    //首先数据库永久删除学生信息
                personRepository.delete(p);   // 然后数据库永久删除学生信息
            } else {
                return CommonMethod.getReturnMessageError("学生Id传入错误");
            }
        }

        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
}
