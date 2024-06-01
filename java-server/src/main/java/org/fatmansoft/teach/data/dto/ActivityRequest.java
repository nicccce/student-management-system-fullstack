package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.util.ComDataUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {
    private Integer activityId;
    private String activityName;
    private String activityType;
    private String activityTypeName;
    private String activityContent;
    private String activityDate;

    private String num;
    private String name;

    public ActivityRequest(Activity activity) {
        Person person = activity.getPerson();
        setActivityId(activity.getActivityId());
        /*setName(innovation.getName());
        setNum(innovation.getNum());*/
        setActivityName(activity.getActivityName());
        setActivityType(activity.getActivityType());
        setActivityContent(activity.getActivityContent());
        setActivityDate(activity.getActivityDate());

        /*setMotherName(family.getMotherName());
        setMotherOccupation(family.getMotherOccupation());
        setMotherAge(family.getMotherAge());
        setMotherContact(family.getMotherContact());*/
        setNum(person.getNum());
        setName(person.getName());
        setActivityTypeName(ComDataUtil.getInstance().getDictionaryLabelByValue("ACT", activityType));

    }
}
