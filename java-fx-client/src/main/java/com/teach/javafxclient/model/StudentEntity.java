package com.teach.javafxclient.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.teach.javafxclient.controller.config.SimpleBooleanPropertyFilter;
import com.teach.javafxclient.request.DataResponse;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生数据表格类
 */
public class StudentEntity {

    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中

    private String num; // 学号

    private String name; // 姓名

    private String dept; // 部门

    private String major; // 专业

    private String className; // 班级

    private Integer studentId; // 学生ID

    private String card; // 卡号

    private String gender; // 性别

    private String genderName; // 性别名称

    private String birthday; // 生日

    private String email; // 邮箱

    private String phone; // 电话

    private String address; // 地址

    private String introduce; // 介绍

    private Integer personId; // 个人ID

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (dept == null || dept.isEmpty())
                && (major == null || major.isEmpty())
                && (className == null || className.isEmpty())
                && (studentId == null)
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
        major = null;
        className = null;
        studentId = null;
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
     * 构造一个学生数据表格对象
     *
     * @param major       专业
     * @param className   班级
     * @param studentId   学生ID
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
    public StudentEntity(String major, String className, Integer studentId, Integer personId, String num, String name, String dept, String card, String gender, String genderName, String birthday, String email, String phone, String address, String introduce) {
        this.major = major != null ? major : "";
        this.className = className != null ? className : "";
        this.studentId = studentId != null ? studentId : 0;
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

    /**
     * 默认构造方法
     */
    public StudentEntity() {
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

    /**
     * 获取专业
     *
     * @return 专业
     */
    public String getMajor() {
        return major;
    }

    /**
     * 设置专业
     *
     * @param major 专业
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 获取班级
     *
     * @return 班级
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置班级
     *
     * @param className 班级
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取学生ID
     *
     * @return 学生ID
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * 设置学生ID
     *
     * @param studentId 学生ID
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * 获取个人ID
     *
     * @return 个人ID
     */
    public Integer getPersonId() {
        return personId;
    }

    /**
     * 设置个人ID
     *
     * @param personId 个人ID
     */
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    /**
     * 获取学号
     *
     * @return 学号
     */
    public String getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    /**
     * 设置学号
     *
     * @param num 学号
     */
    public void setNum(String num) {
        this.num = num;
    }

    /**
     * 获取姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取部门
     *
     * @return 部门
     */
    public String getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept 部门
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * 获取卡号
     *
     * @return 卡号
     */
    public String getCard() {
        return card;
    }

    /**
     * 设置卡号
     *
     * @param card 卡号
     */
    public void setCard(String card) {
        this.card = card;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取性别名称
     *
     * @return 性别名称
     */
    public String getGenderName() {
        return genderName;
    }

    /**
     * 设置性别名称
     *
     * @param genderName 性别名称
     */
    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    /**
     * 获取生日
     *
     * @return 生日
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取邮箱
     *
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取电话
     *
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取地址
     *
     * @return 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取介绍
     *
     * @return 介绍
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置介绍
     *
     * @param introduce 介绍
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "studentDataTable{" +
                "selected=" + select +
                ", major='" + major + '\'' +
                ", className='" + className + '\'' +
                ", studentId='" + studentId + '\'' +
                ", personId='" + personId + '\'' +
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