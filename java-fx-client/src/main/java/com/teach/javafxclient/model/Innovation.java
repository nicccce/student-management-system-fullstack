package com.teach.javafxclient.model;

public class Innovation extends Person {
    private Integer innovationId;
    private String innovationName;
    private String innovationType;
    private String instructor;
    private String teamPosition;
    private String teamName;
    /*private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/

    public Innovation() {
        super();
    }

    public Innovation(Integer personId, String num, String name, Integer innovationId) {
        super(personId, num, name);
        this.innovationId = innovationId;
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
}



