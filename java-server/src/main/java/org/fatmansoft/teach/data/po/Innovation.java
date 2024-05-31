package org.fatmansoft.teach.data.po;

import org.fatmansoft.teach.data.dto.FamilyRequest;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;
import javax.validation.constraints.Size;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.InnovationRequest;

@Entity
@Table(name = "innovation")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Innovation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer innovationId;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    //（姓名、学号、）项目名称、项目类型、指导老师、队伍名次、队伍名称

    private String innovationName;
    private String innovationType;

    private String instructor;

    private String teamPosition;
    private String teamName;


    public Innovation(InnovationRequest innovationRequest) {
        setInnovationId(innovationRequest.getInnovationId());
        /*setName(innovationRequest.getName());
        setNum(innovationRequest.getNum());*/
        setInnovationName(innovationRequest.getInnovationName());
        setInnovationType(innovationRequest.getInnovationType());
        setInstructor(innovationRequest.getInstructor());
        setTeamPosition(innovationRequest.getTeamPosition());
        setTeamName(innovationRequest.getTeamName());
        /*setMotherName(innovationRequest.getMotherName());
        setMotherOccupation(innovationRequest.getMotherOccupation());
        setMotherAge(innovationRequest.getMotherAge());
        setMotherContact(innovationRequest.getMotherContact());*/
    }
}


