package org.fatmansoft.teach.data.dto;

import org.fatmansoft.teach.data.po.Course;

import java.time.LocalDateTime;

public class CourseRequest {
    private Integer courseId;

    private String num;


    private String name;

    /**
     * 通过二进制存储课程日程安排
     */
    private long schedule;

    /**
     * 课程开始日期
     */
    private String beginTime;

    /**
     * 课程结束日期
     */
    private String endTime;

    /**
     * 课程类型
     */
    private String  type;

    /**
     * 课程学分
     */
    private Integer credit;

    /**
     * 上课地点
     */
    private String location;

    private Integer studentNumber;

    private String department; // 开课单位

    /**
     * 课程介绍
     */
    private String introduction;


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public long getSchedule() {
        return schedule;
    }

    public void setSchedule(long schedule) {
        this.schedule = schedule;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public CourseRequest() {
    }

    public CourseRequest(Integer courseId, String num, String name, long schedule, String beginTime, String endTime, String type, Integer credit, String location, String introduction, Integer studentNumber) {
        this.courseId = (courseId != null) ? courseId : 0;
        this.num = (num != null) ? num : "";
        this.name = (name != null) ? name : "";
        this.schedule = schedule;
        this.beginTime = (beginTime != null) ? beginTime : "";
        this.endTime = (endTime != null) ? endTime : "";
        this.type = (type != null) ? type : "";
        this.credit = (credit != null) ? credit : 0;
        this.location = (location != null) ? location : "";
        this.introduction = (introduction != null) ? introduction : "";
        this.studentNumber = (studentNumber != null) ? studentNumber : 0;
    }

    public CourseRequest(Course course) {
        this.courseId = (course.getCourseId() != null) ? course.getCourseId() : 0;
        this.num = (course.getNum() != null) ? course.getNum() : "";
        this.name = (course.getName() != null) ? course.getName() : "";
        this.schedule = course.getSchedule();
        this.beginTime = (course.getBeginTime() != null) ? course.getBeginTime() : "";
        this.endTime = (course.getEndTime() != null) ? course.getEndTime() : "";
        this.type = (course.getType() != null) ? course.getType() : "";
        this.credit = (course.getCredit() != null) ? course.getCredit() : 0;
        this.location = (course.getLocation() != null) ? course.getLocation() : "";
        this.introduction = (course.getIntroduction() != null) ? course.getIntroduction() : "";
        this.studentNumber = (course.getStudents() != null) ? course.getStudents().size() : 0;
        this.department = course.getDepartment();
    }

    @Override
    public String toString() {
        return "CourseRequest{" +
                "courseId=" + courseId +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", schedule=" + schedule +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type='" + type + '\'' +
                ", credit=" + credit +
                ", location='" + location + '\'' +
                ", studentNumber=" + studentNumber +
                ", department='" + department + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
