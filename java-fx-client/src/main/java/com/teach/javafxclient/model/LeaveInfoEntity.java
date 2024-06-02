package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class LeaveInfoEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private Integer leaveInfoId;

    private String num; // 学号

    private String name; // 姓名

    private String reason; // 部门

    private String opinion; // 专业

    private String destination; // 班级

    private String phone; // 学生ID

    private String backTime; // 卡号

    private String back; // 性别



    /**
     * 判断对象是否为空
     * @return 为空返回true
     */
    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (reason == null || reason.isEmpty())
                && (opinion == null || opinion.isEmpty())
                && (destination == null || destination.isEmpty())
                && (backTime == null)
                && (leaveInfoId == null)
                && (back == null || back.isEmpty());

    }

    /**
     * 清空对象
     */
    public void empty() {
        leaveInfoId = null;
        num = null;
        name = null;
        reason = null;
        opinion = null;
        destination = null;
        backTime = null;
        back = null;

    }


    public LeaveInfoEntity(String studentNum, String studentName, String reason,String opinion, String destination, String backTime, String back,String backName) {
        this.num = studentNum != null ? studentNum : "";
        this.name = studentName != null ? studentName : "";
        this.leaveInfoId = leaveInfoId != null ? leaveInfoId : 0;

        this.reason = reason != null ? reason : "";
        this.opinion = opinion != null ? opinion : "";
        this.destination = destination != null ? destination : "";
        this.backTime = backTime != null ? backTime : "";
        this.back = back != null ? back : "";
    }

    /**
     * 默认构造方法
     */
    public LeaveInfoEntity() {
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

    public Integer getLeaveInfoId() {
        return leaveInfoId;
    }

    public void setLeaveInfoId(Integer leaveInfoId) {
        this.leaveInfoId = leaveInfoId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

}
