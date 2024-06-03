package com.teach.javafxclient.model;

public class Honor extends Student{
    private Integer honorId;
    private String honorName;
    private String honorTime;
    private String level;
    private String honorType;
    private String host;

    public Honor(){super();}
    public Honor(Integer studentId, Integer personId,String num, String name,Integer leaveInfoId){
        super(studentId,personId,num,name);
        this.honorId = leaveInfoId;
    }
}
