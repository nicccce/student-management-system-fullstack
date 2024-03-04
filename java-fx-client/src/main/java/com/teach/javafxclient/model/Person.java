package com.teach.javafxclient.model;

/**
* Person人员表实体类 保存人员的基本信息信息，
* Integer personId 人员表 person 主键 person_id
* String num 人员编号
* String name 人员名称
* String type 人员类型  0管理员 1学生 2教师
* String dept 学院
* String card 身份证号
* String gender 性别  1 男 2 女
* String birthday 出生日期
* String email 邮箱
* String phone 电话
* String address 地址
* String introduce 个人简介
*/
public class Person {
    private Integer personId;
    private String num;
    private String name;
    private String dept;
    private String card;
    private String gender;
    private String genderName;
    private String birthday;
    private String email;
    private String phone;
    private String address;
    private String introduce;

    public Person(){

    }
    public Person(Integer personId,String num, String name){
        this.personId = personId;
        this.num = num;
        this.name = name;
    }
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
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

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }
    public String toString(){
        return num + "-" + name;
    }
}
