package org.fatmansoft.teach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/**
 * SQL原生查询服务 提供自己出的数据库表和数据的操作
 */
@Service
public class SysSqlService {
    @Autowired
    private EntityManager entityManager;


    @Transactional
    @Modifying
    public int dropDatabaseTable(String tableName){
        String sql= "DROP TABLE IF EXISTS "+ tableName;
        Query query = this.entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

    @Transactional
    @Modifying
    public int createDatabaseTable(String sql){
        Query query = this.entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

    public boolean isExit(String tableName, String dataTime) {
        String sql = "select id from " + tableName + " where data_time='" + dataTime + "'";
        Query query = this.entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list != null && list.size() >0 ) {
           return true;
        }else
            return false;
    }

    public Integer getRecordTotal(String tableName) {
        int ret = 0;
        String sql = "select count(*) from " + tableName ;
        Query query = this.entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list != null && list.size() >0 ) {
            BigInteger b = (BigInteger)list.get(0);
            if(b != null)
                ret = b.intValue();
        }
        return ret;
    }
    public Integer getDayRecordTotal(String tableName, String dStr) {
        int ret = 0;
        String sql = "select count(*) from " + tableName + " where data_time like '" + dStr + "%'";
        Query query = this.entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list != null && list.size() >0 ) {
            BigInteger b = (BigInteger)list.get(0);
            if(b != null)
                ret = b.intValue();
        }
        return ret;
    }
    public String getDayLastDateTime(String tableName) {
        int ret = 0;
        String sql = "select max(data_time) from " + tableName ;
        Query query = this.entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list != null && list.size() >0 ) {
            return list.get(0).toString();
        }
        return "";
    }
    public Object[] getLastRecord(String tableName) {
        int ret = 0;
        String sql = "select * from " + tableName +" order by data_time desc limit 0,1";
        Query query = this.entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        if(list != null && list.size() >0 ) {
            return (Object[])list.get(0);
        }
        return null;
    }
    public List getDayRecordList(String tableName, String dStr) {
        int ret = 0;
        String sql = "select * from " + tableName +" where data_time like '" + dStr + "%'";
        Query query = this.entityManager.createNativeQuery(sql);
        return  query.getResultList();
    }

    public List getResultList(String sql) {
        Query query = this.entityManager.createNativeQuery(sql);
        return  query.getResultList();
    }

    @Transactional
    @Modifying
    public String executeUpdate(String sql){
        Query query;
        query = this.entityManager.createNativeQuery(sql);
        query.executeUpdate();
        return null;
    }

}
