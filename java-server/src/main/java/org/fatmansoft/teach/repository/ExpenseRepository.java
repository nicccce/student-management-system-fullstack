package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.*;
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
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query(value = "select max(expenseId) from Expense")
    Integer getMaxId();

    Optional<Expense> findByPersonPersonId(Integer personId);
    Optional<Expense> findByPersonNum(String num);

    List<Expense> findByPersonName(String name);

    @Query(value = "from Expense where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Expense> findExpenseListByNumName(String numName);

    List<Expense> findAll();


    /**
     * 通过QueryByExample实现对前端筛选数据的动态查询
     * @param filterCriteria 前端的筛选数据
     * @param numName 前端的查询数据
     * @return
     */

    default List<Expense> findByExample(Expense filterCriteria, String numName) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("expenseType", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("expenseDate", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("person.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("person.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("expenseContent", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("expenseNum", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        //.withMatcher("activityDate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        //.withMatcher("teamPosition", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Expense> example = Example.of(filterCriteria, matcher);
        List<Expense> expenseByExample = findAll(example);

        List<Expense> expenseByNumName = findExpenseListByNumName(numName);

        // 使用stream过滤出两个列表中的重叠数据
        List<Expense> matchedExpense = expenseByExample.stream()
                .filter(expenseByNumName::contains)
                .collect(Collectors.toList());

        return matchedExpense;
    }

}
