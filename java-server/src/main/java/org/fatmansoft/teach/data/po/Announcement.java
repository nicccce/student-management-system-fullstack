package org.fatmansoft.teach.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.AnnouncementRequest;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "announcement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    private String announcementContent;

    private String beginTime;

    private String endTime;


    public Announcement (AnnouncementRequest announcementRequest) {
        course = new Course();
        course.setName(announcementRequest.getName());
        setAnnouncementId(announcementRequest.getAnnouncementId());
        setAnnouncementContent(announcementRequest.getAnnouncementContent());
        setBeginTime(announcementRequest.getBeginTime());
        setEndTime(announcementRequest.getEndTime());
    }

}
