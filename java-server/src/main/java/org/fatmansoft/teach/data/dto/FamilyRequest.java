package org.fatmansoft.teach.data.dto;import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Family;
import org.fatmansoft.teach.data.po.Person;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyRequest {
    private Integer familyId;
    private String familyAddress;
    private String familySize;
    private String fatherName;
    private String fatherOccupation;
    private String fatherAge;
    private String fatherContact;
    private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;
    private String num;
    private String name;

    public FamilyRequest(Family family) {
        Person person = family.getPerson();
        setFamilyId(family.getFamilyId());
        setFamilyAddress(family.getAddress());
        setFamilySize(family.getFamilySize());
        setFatherName(family.getFatherName());
        setFatherOccupation(family.getFatherOccupation());
        setFatherAge(family.getFatherAge());
        setFatherContact(family.getFatherContact());
        setMotherName(family.getMotherName());
        setMotherOccupation(family.getMotherOccupation());
        setMotherAge(family.getMotherAge());
        setMotherContact(family.getMotherContact());
        setNum(person.getNum());
        setName(person.getName());

    }
}
