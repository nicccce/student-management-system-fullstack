package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Fee 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  Fee 表中的最大的fee_id;    JPQL 注解
 * Optional<Fee> findByStudentIdAndDay(Integer studentId, String day);  根据student_id 和day 查询获得Option<Fee>对象,  命名规范
 * List<Fee> findListByStudent(Integer studentId);  查询学生（student_id）所有的消费记录  JPQL 注解
 */
public interface FeeRepository extends JpaRepository<Fee,Integer> {
    @Query(value = "select max(feeId) from Fee  ")
    Integer getMaxId();

    Optional<Fee> findByStudentIdAndDay(Integer studentId, String day);

    @Query(value= "from Fee where studentId=?1 order by day")
    List<Fee> findListByStudent(Integer studentId);
}
