package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.DictionaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * DictionaryInfo 数据操作接口，主要实现DictionaryInfo数据的查询操作
 * Integer getMaxId()  dictionary 表中的最大的id;    JPQL 注解
 * List<DictionaryInfo> findRootList();  查询获取顶层的数据字典（所有数据字典的code集合） JPQL 注解
 * List<DictionaryInfo> findByPid(Integer pid); 查询获取pid的子数据字典集合  命名规范
 * List<DictionaryInfo>getDictionaryInfoList(String code);查询父节点字典值为code的所有的数据字典集合  SQL原生查询
 */
public interface DictionaryInfoRepository extends JpaRepository<DictionaryInfo,Integer> {
    @Query(value = "select max(id) from DictionaryInfo  ")
    Integer getMaxId();
    @Query(value= " from DictionaryInfo where pid is null")
    List<DictionaryInfo> findRootList();

    List<DictionaryInfo> findByPid(Integer pid);

    @Query(value = "select d.* from dictionary d, dictionary f where f.id = d.pid and f.value = ?1", nativeQuery = true)
    List<DictionaryInfo>getDictionaryInfoList(String code);


}
