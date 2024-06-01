package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.*;
import org.fatmansoft.teach.data.po.Family;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.FamilyRepository;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;

@Service
public class InnovationService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InnovationRepository innovationRepository;

    public DataResponse innovationDeleteAll(DataRequest dataRequest) {
        List<Integer> allInnovationIds = dataRequest.getList("innovationId");

        if (allInnovationIds == null || allInnovationIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无创新项目实体传入");
        }

        for (Integer innovationId : allInnovationIds) {
            Optional<Innovation> op = innovationRepository.findById(innovationId);
            if (op.isPresent()) {
                Innovation i = op.get();
                innovationRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("创新项目ID传入错误：" + innovationId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse innovationInsert(@Valid Request<Map<String, InnovationRequest>> innovationRequest) {
        Request<Map<String, InnovationRequest>> request = new Request<>((innovationRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Innovation innovation = new Innovation(request.getData().get("newInnovation"));
        String num = request.getData().get("newInnovation").getNum();
        Optional<Person> nOp = personRepository.findByNum(num);
        if (nOp.isPresent() || num == null || num.isEmpty()) {
        /*   if(nFp.isPresent()) {
                return CommonMethod.getReturnMessageError("创新实践信息已存在,无法添加学生创新实践信息,请转到修改页面修改");
            } //ToDo：疑似不满足多对一关系，如果有时间可以修改*/
            innovation.setPerson(nOp.get());
            innovationRepository.save(innovation);
            return CommonMethod.getReturnMessageOK("创新实践信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("学号不存在,无法添加学生创新实践信息");
        }


        //} catch (ConstraintViolationException e) {
        //   return CommonMethod.getReturnMessageError(e.getMessage());
        // } catch (Exception e) {
        //  return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        // }
    }



    private Map<String, Object> getMapFromInnovation(Innovation i) {
        Map<String, Object> m = new HashMap<>();
        Person p = i.getPerson();
        if (i != null) {
            m.put("innovationId", i.getInnovationId());
            /*m.put("name", i.getName());
            m.put("num", i.getNum());*/
            m.put("innovationName", i.getInnovationName());
            m.put("innovationType", i.getInnovationType());
            m.put("instructor", i.getInstructor());
            m.put("teamPosition", i.getTeamPosition());
            m.put("teamName", i.getTeamName());
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


    public List<InnovationRequest> getInnovationListByFilterAndNumName(InnovationRequest filterCriteria, String numName){
        Innovation filterCriteriaInnovation = new Innovation(filterCriteria);
        List<Innovation> matchedInnovation =  innovationRepository.findByExample(filterCriteriaInnovation, numName);
        List<InnovationRequest> matchedInnovationRequest = new ArrayList<>(){};
        for (Innovation innovation:
                matchedInnovation) {
            matchedInnovationRequest.add(new InnovationRequest(innovation));
        }
        return matchedInnovationRequest;
    }

    public ResponseEntity<StreamingResponseBody> getSelectedInnovationListExcl(List<InnovationRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "项目名称","项目种类","指导老师","队伍名次","队伍名称"};
        String outPutSheetName = "innovation.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 7);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        InnovationRequest innovationRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                innovationRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(innovationRequest.getNum());
                cell[2].setCellValue(innovationRequest.getName());
                cell[3].setCellValue(innovationRequest.getInnovationName());
                cell[4].setCellValue(innovationRequest.getInnovationTypeName());
                cell[5].setCellValue(innovationRequest.getInstructor());
                cell[6].setCellValue(innovationRequest.getTeamPosition());
                cell[7].setCellValue(innovationRequest.getTeamName());
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
