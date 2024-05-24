package org.fatmansoft.teach.repository;

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

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    @Query(value = "select max(teacherId) from Teacher  ")
    Integer getMaxId();
    Optional<Teacher> findByPersonPersonId(Integer personId);
    Optional<Teacher> findByPersonNum(String num);
    List<Teacher> findByPersonName(String name);
    @Query(value = "from Teacher where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Teacher> findTeacherListByNumName(String numName);
    @Query(value = "from Student where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    List<Teacher> findAll();

    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */
    default List<Teacher> findByExample(Teacher filterCriteria, String numName) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("person.gender", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.birthday", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("position", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("qualification", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.dept", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.phone", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.address", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Teacher> example = Example.of(filterCriteria, matcher);
        List<Teacher> teacherByExample = findAll(example);

        List<Teacher> teachersByNumName = findTeacherListByNumName(numName);

        // 使用stream过滤出两个列表中的重叠数据
        List<Teacher> matchedTeacher = teacherByExample.stream()
                .filter(teachersByNumName::contains)
                .collect(Collectors.toList());

        return matchedTeacher;
    }
}
