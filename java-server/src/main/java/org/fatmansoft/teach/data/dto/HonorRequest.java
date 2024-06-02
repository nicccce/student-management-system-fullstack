package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Honor;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.util.ComDataUtil;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorRequest {
    private Integer HonorId;
    private Student student;
    private String num; // 专业
    private String name; // 班级
    private String honorName; // 学生ID
    private String honorTime; // 个人ID
    private String honorType; // 学号
    private String level; // 姓名
    private String levelName; // 姓名
    private String host; // 部门



    public HonorRequest (Honor honor) {
        setHonorId(honor.getHonor_Id());
        setNum(honor.getStudent().getPerson().getNum());
        setName(honor.getStudent().getPerson().getName());
        setHonorName(honor.getTitle());
        setHonorTime(honor.getTime());
        setHonorType(honor.getType());
        setHost(honor.getHost());
        setLevelName(ComDataUtil.getInstance().getDictionaryLabelByValue("LEV", level));
    }


}
