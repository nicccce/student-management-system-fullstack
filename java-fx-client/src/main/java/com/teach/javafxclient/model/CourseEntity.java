package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;
import java.util.List;

public class CourseEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private Integer courseId;

    private String num;


    private String name;

    /**
     * 通过二进制存储课程日程安排
     */
    private long schedule;

    private String department;

    /**
     * 课程开始日期
     */
    private String beginTime;

    /**
     * 课程结束日期
     */
    private String endTime;

    private Integer studentNumber;

    private String teachers;

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

    private String scheduleString;

    private String filterStudentNumName;

    private String filterTeacherNumName;

    /**
     * 课程介绍
     */
    private String introduction;

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (schedule == 0L)
                && (department == null || department.isEmpty())
                && (beginTime == null || beginTime.isEmpty())
                && (endTime == null || endTime.isEmpty())
                && (studentNumber == null)
                && (teachers == null || teachers.isEmpty())
                && (type == null || type.isEmpty())
                && (credit == null)
                && (location == null || location.isEmpty())
                && (scheduleString == null || scheduleString.isEmpty())
                && (filterStudentNumName == null || filterStudentNumName.isEmpty())
                && (filterTeacherNumName == null || filterTeacherNumName.isEmpty());
    }

    public void empty() {
        num = null;
        name = null;
        schedule = 0L;
        department = null;
        beginTime = null;
        endTime = null;
        studentNumber = null;
        teachers = null;
        type = null;
        credit = null;
        location = null;
        scheduleString = null;
        filterTeacherNumName = null;
        filterStudentNumName = null;
    }

    public String getFilterStudentNumName() {
        return filterStudentNumName;
    }

    public void setFilterStudentNumName(String filterStudentNumName) {
        this.filterStudentNumName = filterStudentNumName;
    }

    public String getFilterTeacherNumName() {
        return filterTeacherNumName;
    }

    public void setFilterTeacherNumName(String filterTeacherNumName) {
        this.filterTeacherNumName = filterTeacherNumName;
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

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
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

    public String getScheduleString() {
        return scheduleString;
    }

    public void setScheduleString(String scheduleString) {
        this.scheduleString = scheduleString;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public CourseEntity(SimpleBooleanProperty select, Integer courseId, String num, String name, long schedule, String beginTime, String endTime, Integer studentNumber, String teachers, String type, Integer credit, String location, String scheduleString, String introduction) {
        this.select = select;
        this.courseId = courseId;
        this.num = num;
        this.name = name;
        this.schedule = schedule;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.studentNumber = studentNumber;
        this.teachers = teachers;
        this.type = type;
        this.credit = credit;
        this.location = location;
        this.scheduleString = scheduleString;
        this.introduction = introduction;
    }

    public CourseEntity() {
        schedule = 0L;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "courseId=" + courseId +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", schedule=" + schedule +
                ", department='" + department + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", studentNumber=" + studentNumber +
                ", teachers='" + teachers + '\'' +
                ", type='" + type + '\'' +
                ", credit=" + credit +
                ", location='" + location + '\'' +
                ", scheduleString='" + scheduleString + '\'' +
                ", filterStudentNumName='" + filterStudentNumName + '\'' +
                ", filterTeacherNumName='" + filterTeacherNumName + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
