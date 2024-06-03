package org.fatmansoft.teach.data.po;

import javax.persistence.*;

@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    private Course course;

    private String content;

    private String submissionMethod;


}
