package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class HonorEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private Integer honorId;

    private String num; // 学号

    private String name; // 姓名

    private String honorName; // 部门

    private String honorTime; // 专业

    private String levelName; // 班级

    private String honorType; // 学生ID

    private String host; // 卡号



    /**
     * 判断对象是否为空
     * @return 为空返回true
     */
    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (honorName == null || honorName.isEmpty())
                && (honorTime == null || honorTime.isEmpty())
                && (levelName == null || levelName.isEmpty())
                && (honorType == null)
                && (honorId == null)
                && (host == null || host.isEmpty());

    }

    /**
     * 清空对象
     */
    public void empty() {
        honorId = null;
        num = null;
        name = null;
        honorName = null;
        honorTime = null;
        levelName = null;
        honorType = null;
        host = null;

    }


    public HonorEntity(String studentNum, String studentName, String honorName,String honorTime, String levelName, String honorType, String host) {
        this.num = studentNum != null ? studentNum : "";
        this.name = studentName != null ? studentName : "";
        this.honorId = honorId != null ? honorId : 0;
        this.honorName = honorName != null ? honorName : "";
        this.honorTime = honorTime != null ? honorTime : "";
        this.levelName = levelName != null ? levelName : "";
        this.honorType = honorType != null ? honorType : "";
        this.host = host != null ? host : "";
    }
    public HonorEntity() {
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

    public Integer getHonorId() {
        return honorId;
    }

    public void setHonorId(Integer honorId) {
        this.honorId = honorId;
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

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getHonorTime() {
        return honorTime;
    }

    public void setHonorTime(String honorTime) {
        this.honorTime = honorTime;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getHonorType() {
        return honorType;
    }

    public void setHonorType(String honorType) {
        this.honorType = honorType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "HonorEntity{" +
                "select=" + select +
                ", honorId=" + honorId +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", honorName='" + honorName + '\'' +
                ", honorTime='" + honorTime + '\'' +
                ", levelName='" + levelName + '\'' +
                ", honorType='" + honorType + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
