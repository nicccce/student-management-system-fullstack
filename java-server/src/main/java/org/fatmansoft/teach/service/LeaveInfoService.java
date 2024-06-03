package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.*;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.LeaveInfoRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LeaveInfoService {
    @Autowired
    LeaveInfoRepository leaveInfoRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    PersonRepository personRepository;
//    public List<LeaveInfoRequest> getLeaveInfoListByFilterAndNumName(LeaveInfoRequest filterCriteria, String numName){
//        LeaveInfo filterCriteriaStudent = new LeaveInfo(filterCriteria);
//        List<LeaveInfo> matchedLeaveInfo =  leaveInfoRepository.findByExample(filterCriteriaStudent, numName);
//        List<LeaveInfoRequest> matchedLeaveInfoRequest = new ArrayList<>(){};
//        for (LeaveInfo leaveInfo:
//                matchedLeaveInfo) {
//            matchedLeaveInfoRequest.add(new LeaveInfoRequest(leaveInfo));
//        }
//        return matchedLeaveInfoRequest;
//    }


    public DataResponse leaveInfoInsert(@Valid Request<Map<String, LeaveInfoRequest>> leaveInfoRequest) {
        Request<Map<String, LeaveInfoRequest>> request = new Request<>((leaveInfoRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        LeaveInfo leaveInfo = new LeaveInfo(request.getData().get("newLeaveInfo"));
        String studentNum = request.getData().get("newLeaveInfo").getNum();
        Student student;
        Optional<Student> nOp = studentRepository.findByPersonNum(studentNum);
        student = nOp.get();
        Optional<LeaveInfo> nFp = leaveInfoRepository.findLeaveInfoByStudentStudentId(student.getStudentId());
        if (nOp.isPresent() || studentNum == null || studentNum.isEmpty()) {
//            if (nFp.isPresent()) {
//                return CommonMethod.getReturnMessageError("学生请假信息已存在,无法添加,请转到修改页面修改");
//            } //ToDo：疑似不满足多对一关系，如果有时间可以修改
            leaveInfo.setStudent(nOp.get());
            leaveInfoRepository.save(leaveInfo);
            return CommonMethod.getReturnMessageOK("学生请假信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("学号不存在,无法添加");
        }

    }
    public DataResponse leaveInfoDeleteAll(DataRequest dataRequest) {
        List<Integer> allLeaveInfoIds = dataRequest.getList("LeaveInfoId");

        if (allLeaveInfoIds == null || allLeaveInfoIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无请假信息实体传入");
        }

        for (Integer LeaveInfoId : allLeaveInfoIds) {
            Optional<LeaveInfo> op = leaveInfoRepository.findById(LeaveInfoId);
            if (op.isPresent()) {
                LeaveInfo i = op.get();
                leaveInfoRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("LeaveInfoID传入错误：" + LeaveInfoId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }
    public ResponseEntity<StreamingResponseBody> getSelectedLeaveInfoListExcl(List<LeaveInfoRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","姓名","学号",  "请假原因","目的地","返回时间","是否销假","联系方式"};
        String outPutSheetName = "leaveInfo.xlsx";
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
        LeaveInfoRequest leaveInfoRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                leaveInfoRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(leaveInfoRequest.getName());
                cell[2].setCellValue(leaveInfoRequest.getNum());
                cell[3].setCellValue(leaveInfoRequest.getReason());
                cell[4].setCellValue(leaveInfoRequest.getDestination());
                cell[5].setCellValue(leaveInfoRequest.getBackTime());
                cell[6].setCellValue(leaveInfoRequest.getBack());
                cell[7].setCellValue(leaveInfoRequest.getPhone());
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
