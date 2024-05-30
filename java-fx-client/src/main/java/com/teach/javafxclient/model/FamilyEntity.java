package com.teach.javafxclient.model;

import javafx.beans.property.SimpleBooleanProperty;

public class FamilyEntity {
    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中
    private String num; // 学号
    private String name; // 姓名
    private Integer familyId;
    private String address;
    private String familySize;
    private String fatherName;
    private String fatherOccupation;
    private String fatherAge;
    private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;

    public boolean isEmpty() {
        return (num == null || num.isEmpty())
                && (name == null || name.isEmpty())
                && (familyId == null )
                && (familySize == null || familySize.isEmpty())
                && (fatherName == null || fatherName.isEmpty())
                && (fatherOccupation == null || fatherOccupation.isEmpty())
                && (fatherAge == null || fatherAge.isEmpty())
                && (fatherContact == null || fatherContact.isEmpty())
                && (motherName == null || motherName.isEmpty())
                && (motherOccupation == null || motherOccupation.isEmpty())
                && (motherAge == null || motherAge.isEmpty())
                && (address == null || address.isEmpty())
                && (motherContact == null || motherContact.isEmpty());
    }

    /**
     * 清空对象
     */
    public void empty() {
        num = null;
        name = null;
        familyId = null;
        familySize = null;
        fatherName = null;
        fatherOccupation = null;
        fatherAge = null;
        fatherContact = null;
        motherName = null;
        motherOccupation = null;
        motherAge = null;
        motherContact = null;
        address = null;
    }

    public FamilyEntity() {
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

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFamilySize() {
        return familySize;
    }

    public void setFamilySize(String familySize) {
        this.familySize = familySize;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getFatherAge() {
        return fatherAge;
    }

    public void setFatherAge(String fatherAge) {
        this.fatherAge = fatherAge;
    }

    public String getFatherContact() {
        return fatherContact;
    }

    public void setFatherContact(String fatherContact) {
        this.fatherContact = fatherContact;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getMotherAge() {
        return motherAge;
    }

    public void setMotherAge(String motherAge) {
        this.motherAge = motherAge;
    }

    public String getMotherContact() {
        return motherContact;
    }

    public void setMotherContact(String motherContact) {
        this.motherContact = motherContact;
    }

    @Override
    public String toString() {
        return "FamilyEntity{" +
                "select=" + select +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", familyId=" + familyId +
                ", address='" + address + '\'' +
                ", familySize='" + familySize + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", fatherOccupation='" + fatherOccupation + '\'' +
                ", fatherAge='" + fatherAge + '\'' +
                ", fatherContact='" + fatherContact + '\'' +
                ", motherName='" + motherName + '\'' +
                ", motherOccupation='" + motherOccupation + '\'' +
                ", motherAge='" + motherAge + '\'' +
                ", motherContact='" + motherContact + '\'' +
                '}';
    }
}
