package org.fatmansoft.teach.data.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private boolean fullTime;


}
