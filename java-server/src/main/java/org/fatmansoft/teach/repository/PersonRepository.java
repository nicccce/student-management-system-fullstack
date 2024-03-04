package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Person 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  person 表中的最大的person_id;    JPQL 注解
 * Optional<Person> findByNum(String num);  根据num查询获得Option<Person>对象,  命名规范
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query(value = "select max(personId) from Person  ")
    Integer getMaxId();
    Optional<Person> findByNum(String num);
}
