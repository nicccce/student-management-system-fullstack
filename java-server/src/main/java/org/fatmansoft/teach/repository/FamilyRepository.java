package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Family;
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
public interface FamilyRepository extends JpaRepository<Family, Integer> {

    @Query(value = "select max(familyId) from Family")
    Integer getMaxId();

    Optional<Family> findByPersonPersonId(Integer personId);
    Optional<Family> findByPersonNum(String num);

    List<Family> findByPersonName(String name);

    @Query(value = "from Family where :numName='' or person.num like %:numName% or person.name like %:numName%")
    List<Family> findFamilyListByNumName(@Param("numName") String numName);

    List<Family> findAll();

    List<Family> findByPersonNumOrPersonName(String personNum, String personName);

    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */


}
