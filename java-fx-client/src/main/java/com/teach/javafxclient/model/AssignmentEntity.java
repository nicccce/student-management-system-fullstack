package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class AssignmentEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 课程编号
    private String name;//课程名称
    private Integer assignmentId;
    private String assignmentContent;

    private String submissionMethod;
    private String beginTime;
    private String endTime;

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (assignmentId == null )
                && (assignmentContent == null || assignmentContent.isEmpty())
                && (submissionMethod == null || submissionMethod.isEmpty())
                && (beginTime == null || beginTime.isEmpty())
                && (endTime == null || endTime.isEmpty());


    }

    public void empty() {
        num = null;
        name = null;
        assignmentId = null;
        assignmentContent = null;
        submissionMethod = null;
        beginTime = null;
        endTime = null;

    }

    public AssignmentEntity() {
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

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentContent() {
        return assignmentContent;
    }

    public void setAssignmentContent(String assignmentContent) {
        this.assignmentContent = assignmentContent;
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

    public String getSubmissionMethod() {
        return submissionMethod;
    }

    public void setSubmissionMethod(String submissionMethod) {
        this.submissionMethod = submissionMethod;
    }

    @Override
    public String toString() {
        return "AssignmentEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", assignmentId=" + assignmentId +
                ", assignmentContent='" + assignmentContent + '\'' +
                ", submissionMethod='" + submissionMethod + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
