package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.ActivityRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.InnovationRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class ActivityService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public DataResponse activityDeleteAll(DataRequest dataRequest) {
        List<Integer> allActivityIds = dataRequest.getList("activityId");

        if (allActivityIds == null || allActivityIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无日常活动实体传入");
        }

        for (Integer activityId : allActivityIds) {
            Optional<Activity> op = activityRepository.findById(activityId);
            if (op.isPresent()) {
                Activity i = op.get();
                activityRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("日常活动ID传入错误：" + activityId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse activityInsert(@Valid Request<Map<String, ActivityRequest>> activityRequest) {
        Request<Map<String, ActivityRequest>> request = new Request<>((activityRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Activity activity = new Activity(request.getData().get("newActivity"));
        String num = request.getData().get("newActivity").getNum();
        Optional<Person> nOp = personRepository.findByNum(num);
        if (nOp.isPresent() || num == null || num.isEmpty()) {
/*            if(nFp.isPresent()) {
                return CommonMethod.getReturnMessageError("日常活动信息已存在,无法添加学生日常活动信息,请转到修改页面修改");
            } //ToDo：疑似不满足多对一关系，如果有时间可以修改*/
            activity.setPerson(nOp.get());
            activityRepository.save(activity);
            return CommonMethod.getReturnMessageOK("日常活动信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("学号不存在,无法添加学生日常活动信息");
        }


        //} catch (ConstraintViolationException e) {
        //   return CommonMethod.getReturnMessageError(e.getMessage());
        // } catch (Exception e) {
        //  return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        // }
    }



    private Map<String, Object> getMapFromActivity(Activity a) {
        Map<String, Object> m = new HashMap<>();
        Person p = a.getPerson();
        if (a != null) {
            m.put("activityId", a.getActivityId());
            /*m.put("name", i.getName());
            m.put("num", i.getNum());*/
            m.put("activityName", a.getActivityName());
            m.put("activityType", a.getActivityType());
            m.put("activityContent", a.getActivityContent());
            m.put("activityDate", a.getActivityDate());
           // m.put("teamName", i.getTeamName());
            /*m.put("motherName", f.getMotherName());
            m.put("motherOccupation", f.getMotherOccupation());
            m.put("motherAge", f.getMotherAge());
            m.put("motherContact", f.getMotherContact());*/
            if (p != null) {
                m.put("personId", p.getPersonId());
                m.put("personNum", p.getNum());
                m.put("personName", p.getName());
            }
        }
        return m;
    }


    public List<ActivityRequest> getActivityListByFilterAndNumName(ActivityRequest filterCriteria, String numName){
        Activity filterCriteriaActivity = new Activity(filterCriteria);
        List<Activity> matchedActivity =  activityRepository.findByExample(filterCriteriaActivity, numName);
        List<ActivityRequest> matchedActivityRequest = new ArrayList<>(){};
        for (Activity activity:
                matchedActivity) {
            matchedActivityRequest.add(new ActivityRequest(activity));
        }
        return matchedActivityRequest;
    }

    public ResponseEntity<StreamingResponseBody> getSelectedActivityListExcl(List<ActivityRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "活动名称","活动类型","活动内容","活动日期"};
        String outPutSheetName = "activity.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 6);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        ActivityRequest activityRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                activityRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(activityRequest.getNum());
                cell[2].setCellValue(activityRequest.getName());
                cell[3].setCellValue(activityRequest.getActivityName());
                cell[4].setCellValue(activityRequest.getActivityTypeName());
                cell[5].setCellValue(activityRequest.getActivityContent());
                cell[6].setCellValue(activityRequest.getActivityDate());
                //cell[7].setCellValue(innovationRequest.getTeamName());
                /*cell[8].setCellValue(innovationRequest.getBirthday());
                cell[9].setCellValue(innovationRequest.getEmail());
                cell[10].setCellValue(innovationRequest.getPhone());
                cell[11].setCellValue(innovationRequest.getAddress());*/
            }
        }
        try {
            StreamingResponseBody stream = outputStream -> {
                wb.write(outputStream);
            };
            return ResponseEntity.ok()
                    .contentType(CommonMethod.exelType)
                    .body(stream);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
