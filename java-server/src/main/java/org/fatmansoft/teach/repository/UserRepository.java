package org.fatmansoft.teach.repository;


import java.util.Optional;

import org.fatmansoft.teach.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * User 数据操作接口，主要实现User数据的查询操作
 * Optional<User> findByUserName(String userName);  根据username查询获得Option<User>对象,  命名规范
 * Optional<User> findByPersonNum(String perNum);  根据关联的Person的num查询获得Option<User>对象  命名规范
 * Optional<User> findByPersonPersonId(Integer personId); 根据关联的Person的personId查询获得Option<User>对象  命名规范
 * Integer getMaxId()  user 表中的最大的user_id;    JPQL 注解
 * Optional<User> findByUserId(Integer userId);  根据userId查询获得Option<User>对象,  命名规范
 * Boolean existsByUserName(String userName);  判断userName的用户是否存在 命名规范
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByPersonNum(String perNum);
    Optional<User> findByPersonPersonId(Integer personId);
    @Query(value = "select max(userId) from User  ")
    Integer getMaxId();

    Optional<User> findByUserId(Integer userId);

    Boolean existsByUserName(String userName);
}