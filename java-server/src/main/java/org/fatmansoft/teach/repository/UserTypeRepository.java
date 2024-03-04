package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.EUserType;
import org.fatmansoft.teach.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserType 数据操作接口，主要实现UserType数据的查询操作
 * UserType findByName(EUserType name);  根据name查询获得UserType对象 命名规范
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(EUserType name);
}