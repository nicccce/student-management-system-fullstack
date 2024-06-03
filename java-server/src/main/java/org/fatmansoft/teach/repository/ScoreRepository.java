package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Score 数据操作接口，主要实现Score数据的查询操作
 * List<Score> findByStudentStudentId(Integer studentId);  根据关联的Student的student_id查询获得List<Score>对象集合,  命名规范
 */

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {
    @Query(value = "select max(scoreId) from Score  ")
    Integer getMaxId();
    List<Score> findByStudentStudentId(Integer studentId);
    @Query(value="from Score where (?1=0 or student.studentId=?1 and ?2=0 or course.courseId=?2)" )
    List<Score> findByStudentCourse(Integer studentId, Integer courseId);
    @Query(value="from Score where (student.studentId=?1 and course.courseId=?2)" )
    Optional<Score> findByStudentIdCourse(Integer studentId, Integer courseId);

}
