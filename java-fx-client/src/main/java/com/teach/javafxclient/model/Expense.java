package com.teach.javafxclient.model;

public class Expense extends Person {
    private Integer expenseId;
    private String expenseType;
    private String expenseContent;
    private String expenseDate;
    private String expenseNum;
    //private String teamName;
    /*private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/

    public Expense() {
        super();
    }

    public Expense(Integer personId, String num, String name, Integer expenseId) {
        super(personId, num, name);
        this.expenseId = expenseId;
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
}



