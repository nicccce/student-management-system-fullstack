package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class AnnouncementEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 课程编号
    private String name;//课程名称
    private Integer announcementId;
    private String announcementContent;
    private String beginTime;
    private String endTime;

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (announcementId == null )
                && (announcementContent == null || announcementContent.isEmpty())
                && (beginTime == null || beginTime.isEmpty())
                && (endTime == null || endTime.isEmpty());
    }

    public void empty() {
        num = null;
        name = null;
        announcementId = null;
        announcementContent = null;
        beginTime = null;
        endTime = null;

    }

    public AnnouncementEntity() {
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

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
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

    @Override
    public String toString() {
        return "AnnouncementEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", announcementId=" + announcementId +
                ", announcementContent='" + announcementContent + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
