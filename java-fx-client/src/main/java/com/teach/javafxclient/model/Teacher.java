package com.teach.javafxclient.model;

import java.time.LocalDateTime;

public class Teacher extends Person{
    private Integer teacherId;//教师ID
    private Integer personId; //个人Id
    private String position; //职位
    private String qualification;// 学历


    public Teacher () { super();}

    public Teacher(Integer personId, String num, String name, Integer teacherId, Integer personId1, String position, String qualification) {
        super(personId, num, name);
        this.teacherId = teacherId;
        this.personId = personId1;
        this.position = position;
        this.qualification = qualification;

    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public Integer getPersonId() {
        return personId;
    }

    @Override
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


}
