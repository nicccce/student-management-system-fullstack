package org.fatmansoft.teach.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.HonorRequest;
import org.fatmansoft.teach.data.dto.LeaveInfoRequest;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "honor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Honor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer honor_Id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;
    @Size(max = 20)
    private String title;   //名称
    @Size(max = 25)
    private String time;    //获奖时间
    @Size(max = 25)
    private String level;   //奖的层次
    @Size(max = 25)
    private String type;    //什么类型的奖项
    @Size(max = 20)
    private String host;    //颁奖机构

    public Honor (HonorRequest honorRequest) {
        setStudent(honorRequest.getStudent());
        setHost(honorRequest.getHost());
        setType(honorRequest.getHonorType());
        setLevel(honorRequest.getLevel());
        setTime(honorRequest.getHonorTime());
        setTitle(honorRequest.getHonorName());
    }


}
