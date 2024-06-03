package com.teach.javafxclient.model;

public class LeaveInfo extends Student{
    private Integer leaveInfoId;
    private String reason;
    private String destination;
    private String phone;
    private String backTime;
    private String back;
    private String opinion;
    public LeaveInfo(){super();}
    public LeaveInfo(Integer studentId, Integer personId,String num, String name,Integer leaveInfoId){
        super(studentId,personId,num,name);
        this.leaveInfoId = leaveInfoId;
    }

    public Integer getLeaveInfoId() {
        return leaveInfoId;
    }

    public void setLeaveInfoId(Integer leaveInfoId) {
        this.leaveInfoId = leaveInfoId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
