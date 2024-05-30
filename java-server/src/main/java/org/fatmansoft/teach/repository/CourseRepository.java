package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.Teacher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Course 数据操作接口，主要实现Course数据的查询操作
 */

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

    @Query(value = "from Course where ?1='' or num like %?1% or name like %?1% ")
    List<Course> findCourseListByNumName(String numName);

    List<Course> findAll();


    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */
    default List<Course> findByExample(Course filterCriteria, String numName) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("location", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("beginTime", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("endTime", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("credit", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("schedule");;

        Example<Course> example = Example.of(filterCriteria, matcher);
        List<Course> courseByExample = findAll(example);

        List<Course> coursesByNumName = findCourseListByNumName(numName);

        // 使用 Stream 过滤出两个列表中的重叠数据，并根据课程时间进行筛选
        List<Course> matchedCourses = courseByExample.stream()
                .filter(course -> coursesByNumName.contains(course))
                .filter(course -> ((filterCriteria.getSchedule() == 0L)||(course.getSchedule() & filterCriteria.getSchedule()) == filterCriteria.getSchedule()))
                .collect(Collectors.toList());
        return matchedCourses;
    }


    Optional<Course> findByNum(String num);

    List<Course> findByStudents(Student student);

    List<Course> findByTeachers(Teacher teacher);

}
