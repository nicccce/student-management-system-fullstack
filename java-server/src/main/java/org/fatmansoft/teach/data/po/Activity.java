package org.fatmansoft.teach.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.ActivityRequest;
import org.fatmansoft.teach.data.dto.InnovationRequest;

import javax.persistence.*;

@Entity
@Table(name = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    //活动名称、活动类型、活动内容、活动日期
    private String activityName;
    private String activityType;
    private String activityContent;
    private String activityDate;

    public Activity(ActivityRequest activityRequest) {
        setActivityId(activityRequest.getActivityId());
        /*setName(innovationRequest.getName());
        setNum(innovationRequest.getNum());*/
        setActivityName(activityRequest.getActivityName());
        setActivityType(activityRequest.getActivityType());
        setActivityContent(activityRequest.getActivityContent());
        setActivityDate(activityRequest.getActivityDate());

    }
}
