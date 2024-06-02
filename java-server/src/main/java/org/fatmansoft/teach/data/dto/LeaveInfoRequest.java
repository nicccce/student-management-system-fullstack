package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.util.ComDataUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveInfoRequest {
    private Integer LeaveInfoId;
    private Student student;
    private String num; // 专业
    private String name; // 班级
    private String reason; // 学生ID
    private String opinion; // 个人ID
    private String destination; // 学号
    private String phone; // 姓名
    private String backTime; // 部门
    private String back; // 卡号
    private String backName; // 性别


    public LeaveInfoRequest (LeaveInfo leaveInfo) {
        setLeaveInfoId(leaveInfo.getLeaveInfoId());
        setNum(leaveInfo.getStudent().getPerson().getNum());
        setName(leaveInfo.getStudent().getPerson().getName());
        setReason(leaveInfo.getReason());
        setOpinion(leaveInfo.getOpinion());
        setDestination(leaveInfo.getDestination());
        setPhone(leaveInfo.getPhone());
        setBackTime(leaveInfo.getBackTime());
        setBack(leaveInfo.getBack());
        setPhone(leaveInfo.getPhone());

        setBackName(ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", back));
    }


}
