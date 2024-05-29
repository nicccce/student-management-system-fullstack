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

@Entity
@Table(name = "family")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Family {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer familyId;

        @OneToOne
        @JoinColumn(name="person_id")
        private Person person;

        @Size(max = 100)
        private String Address;

        private String familySize;

        @Size(max = 50)
        private String fatherName;

        @Size(max = 50)
        private String fatherOccupation;

        private String fatherAge;

        @Size(max = 20)
        private String fatherContact;

        @Size(max = 50)
        private String motherName;

        @Size(max = 50)
        private String motherOccupation;

        private String motherAge;

        @Size(max = 20)
        private String motherContact;


        public Family(FamilyRequest familyRequest) {
                setFamilyId(familyRequest.getFamilyId());
                setAddress(familyRequest.getFamilyAddress());
                setFamilySize(familyRequest.getFamilySize());
                setFatherName(familyRequest.getFatherName());
                setFatherOccupation(familyRequest.getFatherOccupation());
                setFatherAge(familyRequest.getFatherAge());
                setFatherContact(familyRequest.getFatherContact());
                setMotherName(familyRequest.getMotherName());
                setMotherOccupation(familyRequest.getMotherOccupation());
                setMotherAge(familyRequest.getMotherAge());
                setMotherContact(familyRequest.getMotherContact());
        }
}


