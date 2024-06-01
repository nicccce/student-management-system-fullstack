package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class ExpenseEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 学号
    private String name; // 姓名
    private Integer expenseId;
    private String expenseType;
    private String expenseTypeName;
    private String expenseContent;
    private String expenseDate;
    private String expenseNum;
    //private String teamName;
    /*private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (expenseId == null )
                && (expenseType == null || expenseType.isEmpty())
                && (expenseTypeName == null || expenseTypeName.isEmpty())
                && (expenseContent == null || expenseContent.isEmpty())
                && (expenseDate == null || expenseDate.isEmpty())
                && (expenseNum == null || expenseNum.isEmpty());
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
        expenseId = null;
        expenseType = null;
        expenseContent = null;
        expenseDate = null;
        expenseNum = null;
        //teamName = null;
        expenseTypeName = null;
        /*motherName = null;
        motherOccupation = null;
        motherAge = null;
        motherContact = null;
        address = null;*/
    }

    public ExpenseEntity() {
    }

    public String getExpenseTypeName() {
        return expenseTypeName;
    }

    public void setExpenseTypeName(String expenseTypeName) {
        this.expenseTypeName = expenseTypeName;
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

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseContent() {
        return expenseContent;
    }

    public void setExpenseContent(String expenseContent) {
        this.expenseContent = expenseContent;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseNum() {
        return expenseNum;
    }

    public void setExpenseNum(String expenseNum) {
        this.expenseNum = expenseNum;
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", expenseId=" + expenseId +
                ", expenseType='" + expenseType + '\'' +
                ", expenseTypeName='" + expenseTypeName + '\'' +
                ", expenseContent='" + expenseContent + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                ", expenseNum='" + expenseNum + '\'' +
                '}';
    }
}
