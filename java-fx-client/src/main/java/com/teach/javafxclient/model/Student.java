package com.teach.javafxclient.model;

/**
 * Student学生表实体类 保存每个学生的西悉尼 继承Person类，
 * Integer studentId 用户表 student 主键 student_id
 * String major 专业
 * String className 班级
 *
 */

public class Student extends Person{
    private Integer studentId;
    private String major;
    private String className;
    public Student(){
        super();
    }
    public Student(Integer studentId,Integer personId,String num, String name){
        super(personId,num,name);
        this.studentId = studentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
