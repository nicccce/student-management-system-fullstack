package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Person;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {
    private Integer teacherId;//教师ID
    private Integer personId; //个人Id
    private String position; //职位
    private String qualification;// 学历
    private LocalDateTime joinDate; //入职时间
    private String num; // 学号
    private String name; // 姓名
    private String dept; // 部门
    private String card; // 卡号
    private String gender; // 性别
    private String genderName; // 性别名称
    private String birthday; // 生日
    private String email; // 邮箱
    private String phone; // 电话
    private String address; // 地址
    private String introduce; // 介绍
}
