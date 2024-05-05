package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.Teacher;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService {
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

}
