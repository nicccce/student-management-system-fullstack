package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Assignment;
import org.fatmansoft.teach.data.po.Course;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    private String assignmentContent;

    private String submissionMethod;

    private String beginTime;

    private String endTime;

    private String num;

    private String name;

    public AssignmentRequest(Assignment assignment) {
        setAssignmentId(assignment.getAssignmentId());
        setAssignmentContent(assignment.getAssignmentContent());
        setSubmissionMethod(assignment.getSubmissionMethod());
        setBeginTime(assignment.getBeginTime());
        setEndTime(assignment.getEndTime());
        Course course = assignment.getCourse();
        setNum(course.getNum());
        setName(course.getName());
    }
}
