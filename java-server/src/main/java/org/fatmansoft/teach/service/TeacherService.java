package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.Teacher;
import org.fatmansoft.teach.data.po.User;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List getTeacherMapList(String numName){
        List dataList = new ArrayList();
        List<Teacher> teacherList = teacherRepository.findTeacherListByNumName(numName);  //数据库查询操作
        if(teacherList == null || teacherList.size() == 0)
            return dataList;
        for(int i = 0; i < teacherList.size();i++) {
            dataList.add(getMapFromTeacher(teacherList.get(i)));
        }
        return dataList;
    }

    public Map getMapFromTeacher(Teacher teacher) {
        Map m = new HashMap();
        Person person;
        if(teacher == null)
            return m;
        m.put("position",teacher.getPosition());
        m.put("joinDate",teacher.getJoinDate());
        person = teacher.getPerson();
        if(person == null)
            return m;
        m.put("qualification", teacher.getQualification());
        m.put("personId", person.getPersonId());
        m.put("num",person.getNum());
        m.put("name",person.getName());
        m.put("dept",person.getDept());
        m.put("card",person.getCard());
        String gender = person.getGender();
        m.put("gender",gender);
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", person.getBirthday());  //时间格式转换字符串
        m.put("email",person.getEmail());
        m.put("phone",person.getPhone());
        m.put("address",person.getAddress());
        m.put("introduce",person.getIntroduce());
        return m;
    }

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
//        try {
        // 创建或更新教师实体
        Teacher teacher = new Teacher(request.getData().get("newTeacher"));
        teacherRepository.save(teacher);

        return CommonMethod.getReturnMessageOK("教师信息保存成功");

//        } catch (Exception e) {
//            return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
//        }
    }


}
