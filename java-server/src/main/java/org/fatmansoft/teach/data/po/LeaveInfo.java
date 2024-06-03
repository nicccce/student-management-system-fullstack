package org.fatmansoft.teach.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.LeaveInfoRequest;
import org.fatmansoft.teach.data.dto.StudentRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LeaveInfo")
public class LeaveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LeaveInfoId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="student_id")
    private Student student;
    //请假理由
    @Column(name = "reason")
    private String reason;
    //目的地
    @Column(name = "destination")
    private String destination;
    //联系方式
    @Column(name = "phone")
    private String phone;
    //返校时间
    @Column(name = "backTime")
    private String backTime;
    //是否销假
    @Column(name = "back")
    private String back;
    //辅导员意见
    @Column(name = "opinion")
    private String opinion;

    public LeaveInfo (LeaveInfoRequest leaveInfoRequest) {
 setStudent(leaveInfoRequest.getStudent());
setReason(leaveInfoRequest.getReason());
setBack(leaveInfoRequest.getBack());
setBackTime(leaveInfoRequest.getBackTime());
        setPhone(leaveInfoRequest.getPhone());
        setOpinion(leaveInfoRequest.getOpinion());
        setDestination(leaveInfoRequest.getDestination());
        setLeaveInfoId(leaveInfoRequest.getLeaveInfoId());
    }
}
