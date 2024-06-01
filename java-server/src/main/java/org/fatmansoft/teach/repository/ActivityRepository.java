package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Activity;
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
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "select max(activityId) from Activity")
    Integer getMaxId();

    Optional<Activity> findByPersonPersonId(Integer personId);
    Optional<Activity> findByPersonNum(String num);

    List<Activity> findByPersonName(String name);

    @Query(value = "from Activity where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Activity> findActivityListByNumName(String numName);

    List<Activity> findAll();


    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */

    default List<Activity> findByExample(Activity filterCriteria, String numName) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("activityType", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("activityDate", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("activityName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("activityContent", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
                //.withMatcher("activityDate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                //.withMatcher("teamPosition", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Activity> example = Example.of(filterCriteria, matcher);
        List<Activity> activityByExample = findAll(example);

        List<Activity> activityByNumName = findActivityListByNumName(numName);

        // 使用stream过滤出两个列表中的重叠数据
        List<Activity> matchedActivity = activityByExample.stream()
                .filter(activityByNumName::contains)
                .collect(Collectors.toList());

        return matchedActivity;
    }

}
