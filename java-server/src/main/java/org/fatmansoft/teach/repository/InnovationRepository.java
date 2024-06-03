package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Family;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Student;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface InnovationRepository extends JpaRepository<Innovation, Integer> {

    @Query(value = "select max(innovationId) from Innovation")
    Integer getMaxId();

    Optional<Innovation> findByPersonPersonId(Integer personId);
    Optional<Innovation> findByPersonNum(String num);

    List<Innovation> findByPersonName(String name);


    @Query(value = "from Innovation where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Innovation> findInnovationListByNumName(String numName);

    List<Innovation> findAll();




    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */

    default List<Innovation> findByExample(Innovation filterCriteria, String numName) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("innovationType", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("innovationName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("instructor", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("teamName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("teamPosition", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Innovation> example = Example.of(filterCriteria, matcher);
        List<Innovation> innovationByExample = findAll(example);

        List<Innovation> innovationByNumName = findInnovationListByNumName(numName);

        // 使用stream过滤出两个列表中的重叠数据
        List<Innovation> matchedInnovation = innovationByExample.stream()
                .filter(innovationByNumName::contains)
                .collect(Collectors.toList());

        return matchedInnovation;
    }

}
