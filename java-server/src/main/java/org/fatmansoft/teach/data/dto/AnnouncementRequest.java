package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Announcement;
import org.fatmansoft.teach.data.po.Course;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequest {
    private Integer announcementId;

    private String announcementContent;

    private String beginTime;

    private String endTime;

    private String num;

    private String name;

    public AnnouncementRequest(Announcement announcement) {
        setAnnouncementId(announcement.getAnnouncementId());
        setAnnouncementContent(announcement.getAnnouncementContent());
        setBeginTime(announcement.getBeginTime());
        setEndTime(announcement.getEndTime());
        Course course = announcement.getCourse();
        setNum(course.getNum());
        setName(course.getName());
    }
}
