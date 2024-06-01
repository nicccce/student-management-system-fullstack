package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class InnovationEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 学号
    private String name; // 姓名
    private Integer innovationId;
    private String innovationName;
    private String innovationType;
    private String innovationTypeName;



    private String instructor;
    private String teamPosition;
    private String teamName;
    /*private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (innovationId == null )
                && (innovationName == null || innovationName.isEmpty())
                && (innovationType == null || innovationType.isEmpty())
                && (innovationTypeName == null || innovationTypeName.isEmpty())
                && (instructor == null || instructor.isEmpty())
                && (teamPosition == null || teamPosition.isEmpty())
                && (teamName == null || teamName.isEmpty());
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
        innovationId = null;
        innovationName = null;
        innovationType = null;
        instructor = null;
        teamPosition = null;
        teamName = null;
        innovationTypeName = null;
        /*motherName = null;
        motherOccupation = null;
        motherAge = null;
        motherContact = null;
        address = null;*/
    }

    public InnovationEntity() {
    }

    public String getInnovationTypeName() {
        return innovationTypeName;
    }

    public void setInnovationTypeName(String innovationTypeName) {
        this.innovationTypeName = innovationTypeName;
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

    public Integer getInnovationId() {
        return innovationId;
    }

    public void setInnovationId(Integer innovationId) {
        this.innovationId = innovationId;
    }

    public String getInnovationName() {
        return innovationName;
    }

    public void setInnovationName(String innovationName) {
        this.innovationName = innovationName;
    }

    public String getInnovationType() {
        return innovationType;
    }

    public void setInnovationType(String innovationType) {
        this.innovationType = innovationType;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTeamPosition() {
        return teamPosition;
    }

    public void setTeamPosition(String teamPosition) {
        this.teamPosition = teamPosition;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "InnovationEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", innovationId=" + innovationId +
                ", innovationName='" + innovationName + '\'' +
                ", innovationType='" + innovationType + '\'' +
                ", innovationTypeName='" + innovationTypeName + '\'' +
                ", instructor='" + instructor + '\'' +
                ", teamPosition='" + teamPosition + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
