package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.fatmansoft.teach.data.po.Student;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface LeaveInfoRepository extends JpaRepository<LeaveInfo,Integer> {
//    Optional<LeaveInfo> findByPersonNum(String num);
  List<LeaveInfo> findAll();
//    @Query(value = "from LeaveInfo where ?1='' or person.num like %?1% or person.name like %?1% ")
//    List<LeaveInfo> findLeaveInfoListByNumName(String numName);

//    //default List<LeaveInfo> findByExample(LeaveInfo filterCriteria, String numName) {
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnoreNullValues()
//                .withMatcher("back", ExampleMatcher.GenericPropertyMatchers.exact())
//                .withMatcher("student.name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
//                .withMatcher("student.num", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
//                .withMatcher("backTime", ExampleMatcher.GenericPropertyMatchers.exact());
//
//        Example<LeaveInfo> example = Example.of(filterCriteria, matcher);
//        List<LeaveInfo> LeaveInfoByExample = findAll(example);
//
//        List<LeaveInfo> LeaveInfoByNumName = findLeaveInfoListByNumName(numName);
//
//        // 使用stream过滤出两个列表中的重叠数据
//        List<LeaveInfo> matchedLeaveInfo = LeaveInfoByExample.stream()
//                .filter(LeaveInfoByNumName::contains)
//                .collect(Collectors.toList());
//
//        return matchedLeaveInfo;
//    }

    Optional<LeaveInfo> findLeaveInfoByStudentStudentId(Integer studentId);
  @Query(value = "from LeaveInfo where ?1=''")
  List<LeaveInfo> findLeaveInfoListByStudentId(String StudentId);

}
