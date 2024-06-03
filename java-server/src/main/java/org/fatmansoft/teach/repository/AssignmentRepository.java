package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Announcement;
import org.fatmansoft.teach.data.po.Assignment;
import org.fatmansoft.teach.data.po.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findAllByCourse(Course course);
}
