package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Student 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  Student 表中的最大的student_id;    JPQL 注解
 * Optional<Student> findByPersonPersonId(Integer personId); 根据关联的Person的personId查询获得Option<Student>对象 命名规范
 * Optional<Student> findByPersonNum(String num); 根据关联的Person的num查询获得Option<Student>对象  命名规范
 * List<Student> findByPersonName(String name); 根据关联的Person的name查询获得List<Student>对象集合  可能存在相同姓名的多个学生 命名规范
 * List<Student> findStudentListByNumName(String numName); 根据输入的参数 如果参数为空，查询所有的学生，输入参数不为空，查询学号或姓名包含输入参数串的所有学生集合
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> , QueryByExampleExecutor<Student> {
    @Query(value = "select max(studentId) from Student  ")
    Integer getMaxId();
    Optional<Student> findByPersonPersonId(Integer personId);
    Optional<Student> findByPersonNum(String num);
    List<Student> findByPersonName(String name);


    @Query(value = "from Student where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    List<Student> findAll();

    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */
    default List<Student> findByExample(Student filterCriteria, String numName) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("person.gender", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.birthday", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("className", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("major", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.dept", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.phone", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.address", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Student> example = Example.of(filterCriteria, matcher);
        List<Student> studentByExample = findAll(example);

        List<Student> studentsByNumName = findStudentListByNumName(numName);

        // 使用stream过滤出两个列表中的重叠数据
        List<Student> matchedStudent = studentByExample.stream()
                .filter(studentsByNumName::contains)
                .collect(Collectors.toList());

        return matchedStudent;
    }
}
