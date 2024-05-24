package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;

/**
 * 教师数据表格类
 */
public class TeacherEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private Integer teacherId;//教师ID
    private Integer personId; //个人Id
    private String position; //职位
    private String qualification;// 学历
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


   public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (dept == null || dept.isEmpty())
                && (position == null || position.isEmpty())
                && (qualification == null || qualification.isEmpty())
                && (teacherId == null)
                && (card == null || card.isEmpty())
                && (gender == null || gender.isEmpty())
                && (genderName == null || genderName.isEmpty())
                && (birthday == null || birthday.isEmpty())
                && (email == null || email.isEmpty())
                && (phone == null || phone.isEmpty())
                && (address == null || address.isEmpty())
                && (introduce == null || introduce.isEmpty())
                && personId == null;
    }


    public void empty() {
        num = null;
        name = null;
        dept = null;
        position = null;
        qualification = null;
        teacherId = null;
        card = null;
        gender = null;
        genderName = null;
        birthday = null;
        email = null;
        phone = null;
        address = null;
        introduce = null;
        personId = null;
    }

    /**
     * 构造一个教师数据表格对象
     *
     * @param position       职业
     * @param qualification   学历
     * @param teacherId   教师ID
     * @param personId    个人ID
     * @param num         学号
     * @param name        姓名
     * @param dept        部门
     * @param card        卡号
     * @param gender      性别
     * @param genderName  性别名称
     * @param birthday    生日
     * @param email       邮箱
     * @param phone       电话
     * @param address     地址
     * @param introduce   介绍
     */
    public TeacherEntity(String position, String qualification, Integer teacherId, Integer personId, String num, String name, String dept, String card, String gender, String genderName, String birthday, String email, String phone, String address, String introduce) {
        this.position = position != null ? position : "";
        this.qualification = qualification != null ? qualification : "";
        this.teacherId = teacherId != null ? teacherId : 0;
        this.personId = personId != null ? personId : 0;
        this.num = num != null ? num : "";
        this.name = name != null ? name : "";
        this.dept = dept != null ? dept : "";
        this.card = card != null ? card : "";
        this.gender = gender != null ? gender : "";
        this.genderName = genderName != null ? genderName : "";
        this.birthday = birthday != null ? birthday : "";
        this.email = email != null ? email : "";
        this.phone = phone != null ? phone : "";
        this.address = address != null ? address : "";
        this.introduce = introduce != null ? introduce : "";
    }

    public TeacherEntity() {
    }

    public boolean isSelect() {
        return select.get();
    }

    public SimpleBooleanProperty selectProperty() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getPersonId() {
        return personId;
    }

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "TeacherEntity{" +
                "select=" + select +
                ", teacherId=" + teacherId +
                ", personId=" + personId +
                ", position='" + position + '\'' +
                ", qualification='" + qualification + '\'' +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", card='" + card + '\'' +
                ", gender='" + gender + '\'' +
                ", genderName='" + genderName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}
