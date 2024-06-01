package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class ActivityEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 学号
    private String name; // 姓名
    private Integer activityId;
    private String activityName;
    private String activityType;
    private String activityTypeName;
    private String activityContent;
    private String activityDate;
    //private String teamName;
    /*private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (activityId == null )
                && (activityName == null || activityName.isEmpty())
                && (activityType == null || activityType.isEmpty())
                && (activityTypeName == null || activityTypeName.isEmpty())
                && (activityContent == null || activityContent.isEmpty())
                && (activityDate == null || activityDate.isEmpty());
               // && (teamName == null || teamName.isEmpty());
                /*&& (motherName == null || motherName.isEmpty())
                && (motherOccupation == null || motherOccupation.isEmpty())
                && (motherAge == null || motherAge.isEmpty())
                && (address == null || address.isEmpty())
                && (motherContact == null || motherContact.isEmpty());*/
    }

    /**
     * 清空对象
     */
    public void empty() {
        num = null;
        name = null;
        activityId = null;
        activityName = null;
        activityType = null;
        activityContent = null;
        activityDate = null;
        //teamName = null;
        activityTypeName = null;
        /*motherName = null;
        motherOccupation = null;
        motherAge = null;
        motherContact = null;
        address = null;*/
    }

    public ActivityEntity() {
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityType='" + activityType + '\'' +
                ", activityTypeName='" + activityTypeName + '\'' +
                ", activityContent='" + activityContent + '\'' +
                ", activityDate='" + activityDate + '\'' +
                '}';
    }
}
