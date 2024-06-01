package org.fatmansoft.teach.data.dto;import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Family;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.util.ComDataUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InnovationRequest {
    private Integer innovationId;
    private String innovationName;
    private String innovationType;
    private String innovationTypeName;
    private String instructor;
    private String teamPosition;
    private String teamName;
   /* private String motherName;
    private String motherOccupation;
    private String motherAge;
    private String motherContact;*/
    private String num;
    private String name;

    public InnovationRequest(Innovation innovation) {
        Person person = innovation.getPerson();
        setInnovationId(innovation.getInnovationId());
        /*setName(innovation.getName());
        setNum(innovation.getNum());*/
        setInnovationName(innovation.getInnovationName());
        setInnovationType(innovation.getInnovationType());
        setInstructor(innovation.getInstructor());
        setTeamPosition(innovation.getTeamPosition());
        setTeamName(innovation.getTeamName());
        /*setMotherName(family.getMotherName());
        setMotherOccupation(family.getMotherOccupation());
        setMotherAge(family.getMotherAge());
        setMotherContact(family.getMotherContact());*/
        setNum(person.getNum());
        setName(person.getName());
        setInnovationTypeName(ComDataUtil.getInstance().getDictionaryLabelByValue("INO", innovationType));

    }
}
