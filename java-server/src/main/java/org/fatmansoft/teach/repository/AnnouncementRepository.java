package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Announcement;
import org.fatmansoft.teach.data.po.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findAllByCourse(Course course);
}
