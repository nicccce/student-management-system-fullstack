package org.fatmansoft.teach.data.po;

import org.fatmansoft.teach.data.dto.AssignmentRequest;

import javax.persistence.*;

@Entity
@Table(	name = "assignment",
        uniqueConstraints = {
        })
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    private Course course;

    private String assignmentContent;

    private String submissionMethod;

    private String beginTime;

    private String endTime;

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getAssignmentContent() {
        return assignmentContent;
    }

    public void setAssignmentContent(String assignmentContent) {
        this.assignmentContent = assignmentContent;
    }

    public String getSubmissionMethod() {
        return submissionMethod;
    }

    public void setSubmissionMethod(String submissionMethod) {
        this.submissionMethod = submissionMethod;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Assignment() {
    }

    public Assignment(Integer assignmentId, Course course, String assignmentContent, String submissionMethod, String beginTime, String endTime) {
        this.assignmentId = assignmentId;
        this.course = course;
        this.assignmentContent = assignmentContent;
        this.submissionMethod = submissionMethod;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentId +
                ", course=" + course +
                ", assignmentContent='" + assignmentContent + '\'' +
                ", submissionMethod='" + submissionMethod + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public Assignment (AssignmentRequest assignmentRequest) {
        course = new Course();
        course.setName(assignmentRequest.getName());
        setAssignmentId(assignmentRequest.getAssignmentId());
        setAssignmentContent(assignmentRequest.getAssignmentContent());
        setBeginTime(assignmentRequest.getBeginTime());
        setEndTime(assignmentRequest.getEndTime());
        setSubmissionMethod(assignmentRequest.getSubmissionMethod());
    }
}
