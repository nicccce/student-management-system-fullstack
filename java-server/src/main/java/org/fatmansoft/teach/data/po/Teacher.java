package org.fatmansoft.teach.data.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.TeacherRequest;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(	name = "teacher",
        uniqueConstraints = {
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;

    @OneToOne
    @JoinColumn(name="person_id")
    private Person person;

    @Size(max = 20)
    private String position;

    @Size(max = 50)
    private String qualification;

    private LocalDateTime joinDate;

    private @Version Long version;

    public Teacher (TeacherRequest teacherRequest) {
        person = new Person();
        person.setName(teacherRequest.getName());
        person.setNum(teacherRequest.getNum());
        person.setDept(teacherRequest.getDept());
        person.setCard(teacherRequest.getCard());
        person.setGender(teacherRequest.getGender());
        person.setBirthday(teacherRequest.getBirthday());
        person.setEmail(teacherRequest.getEmail());
        person.setPhone(teacherRequest.getPhone());
        person.setAddress(teacherRequest.getAddress());
        person.setPersonId(teacherRequest.getPersonId());
        person.setIntroduce(teacherRequest.getIntroduce());
        setTeacherId(teacherRequest.getTeacherId());
        setJoinDate(teacherRequest.getJoinDate());
        setQualification(teacherRequest.getQualification());
        setPosition(teacherRequest.getPosition());

    }

}
