package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.HonorRequest;

import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Honor;

import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.HonorRepository;
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
public class HonorService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    HonorRepository honorRepository;
    @Autowired
    PersonRepository personRepository;
    public DataResponse honorInsert(@Valid Request<Map<String, HonorRequest>> honorRequest) {
        Request<Map<String, HonorRequest>> request = new Request<>((honorRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Honor honor = new Honor(request.getData().get("newHonor"));
        String studentNum = request.getData().get("newHonor").getNum();
        Optional<Student> nOp = studentRepository.findByPersonNum(studentNum);
        if (nOp.isPresent() || studentNum == null || studentNum.isEmpty()) {
            honor.setStudent(nOp.get());
            honorRepository.save(honor);
            return CommonMethod.getReturnMessageOK("学生荣誉信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("学号不存在,无法添加");
        }

    }
    public DataResponse honorDeleteAll(DataRequest dataRequest) {
        List<Integer> allHonorIds = dataRequest.getList("HonorId");

        if (allHonorIds == null || allHonorIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无请假信息实体传入");
        }

        for (Integer HonorId : allHonorIds) {
            Optional<Honor> op = honorRepository.findById(HonorId);
            if (op.isPresent()) {
                Honor i = op.get();
                honorRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("HonorID传入错误：" + HonorId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }
    public ResponseEntity<StreamingResponseBody> getSelectedHonorListExcl(List<HonorRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","姓名","学号",  "荣誉名称","获奖时间","获奖类型","奖项级别","颁奖人"};
        String outPutSheetName = "honor.xlsx";
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
        HonorRequest honorRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                honorRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(honorRequest.getName());
                cell[2].setCellValue(honorRequest.getNum());
                cell[3].setCellValue(honorRequest.getHonorName());
                cell[4].setCellValue(honorRequest.getHonorTime());
                cell[5].setCellValue(honorRequest.getHonorType());
                cell[6].setCellValue(honorRequest.getLevel());
                cell[7].setCellValue(honorRequest.getHost());
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
