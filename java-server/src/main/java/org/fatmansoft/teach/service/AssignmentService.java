package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.AssignmentRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Assignment;
import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
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
public class AssignmentService {
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    public DataResponse assignmentDeleteAll(DataRequest dataRequest) {
        List<Integer> allAssignmentIds = dataRequest.getList("assignmentId");

        if (allAssignmentIds == null || allAssignmentIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无公告实体传入");
        }

        for (Integer assignmentId : allAssignmentIds) {
            Optional<Assignment> op = assignmentRepository.findById(assignmentId);
            if (op.isPresent()) {
                Assignment i = op.get();
                assignmentRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("日常活动ID传入错误：" + assignmentId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse assignmentInsert(@Valid Request<Map<String, AssignmentRequest>> assignmentRequest) {
        Request<Map<String, AssignmentRequest>> request = new Request<>((assignmentRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Assignment assignment = new Assignment(request.getData().get("newAssignment"));
        String num = request.getData().get("newAssignment").getNum();
        Optional<Course> nOp = courseRepository.findByNum(num);
        if (nOp.isPresent() || num == null || num.isEmpty()) {
            assignment.setCourse(nOp.get());
            assignmentRepository.save(assignment);
            return CommonMethod.getReturnMessageOK("公告信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("课程编号不存在,无法添加公告信息");
        }


        //} catch (ConstraintViolationException e) {
        //   return CommonMethod.getReturnMessageError(e.getMessage());
        // } catch (Exception e) {
        //  return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        // }
    }

    public ResponseEntity<StreamingResponseBody> getSelectedAssignmentListExcl(List<AssignmentRequest> list){
        Integer widths[] = {8,20, 10, 30, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","课程编号", "课程名称", "作业内容", "提交方式", "开始时间","结束时间"};
        String outPutSheetName = "assignment.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 5);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        AssignmentRequest assignmentRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                assignmentRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(assignmentRequest.getNum());
                cell[2].setCellValue(assignmentRequest.getName());
                cell[3].setCellValue(assignmentRequest.getAssignmentContent());
                cell[4].setCellValue(assignmentRequest.getSubmissionMethod());
                cell[5].setCellValue(assignmentRequest.getBeginTime());
                cell[6].setCellValue(assignmentRequest.getEndTime());
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


