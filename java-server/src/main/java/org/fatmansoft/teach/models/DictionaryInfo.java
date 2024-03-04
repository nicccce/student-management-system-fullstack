package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
/**
 * DictionaryInfo 课程表实体类  保存数据字典的的基本信息信息， 数据库表名 dictionary
 * Integer id 数据字典表  dictionary 主键 id
 * Integer pid  父节点ID  所属于的字典类型的id
 * String value 字典值
 * String label 字典名
 */
@Entity
@Table(	name = "dictionary",
        uniqueConstraints = {
        })
public class DictionaryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer pid;

    @Size(max = 40)
    private String value;

    @Size(max = 40)
    private String label;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}