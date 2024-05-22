package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.util.ComDataUtil;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private String major; // 专业
    private String className; // 班级
    private Integer studentId; // 学生ID
    private Integer personId; // 个人ID
    private String num; // 学号
    private String name; // 姓名
    private String dept; // 部门
    private String card; // 卡号
    private String gender; // 性别
    private String genderName; // 性别名称
    private String birthday; // 生日
    private String email; // 邮箱
    private String phone; // 电话
    private String address; // 地址
    private String introduce; // 介绍

    public  StudentRequest (Student student) {
        Person person = student.getPerson();
        setMajor(student.getMajor());
        setClassName(student.getClassName());
        setStudentId(student.getStudentId());
        setPersonId(person.getPersonId());
        setNum(person.getNum());
        setName(person.getName());
        setDept(person.getDept());
        setCard(person.getCard());
        setGender(person.getGender());
        setBirthday(person.getBirthday());
        setEmail(person.getEmail());
        setPhone(person.getPhone());
        setAddress(person.getAddress());
        setIntroduce(person.getIntroduce());
        setGenderName(ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender));
    }

}
