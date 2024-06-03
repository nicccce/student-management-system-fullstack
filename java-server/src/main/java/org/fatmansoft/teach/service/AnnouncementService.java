package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.ActivityRequest;
import org.fatmansoft.teach.data.dto.AnnouncementRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Announcement;
import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.po.Person;
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
public class AnnouncementService {
    @Autowired
    AnnouncementRepository announcementRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    public DataResponse announcementDeleteAll(DataRequest dataRequest) {
        List<Integer> allAnnouncementIds = dataRequest.getList("announcementId");

        if (allAnnouncementIds == null || allAnnouncementIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无公告实体传入");
        }

        for (Integer announcementId : allAnnouncementIds) {
            Optional<Announcement> op = announcementRepository.findById(announcementId);
            if (op.isPresent()) {
                Announcement i = op.get();
                announcementRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("日常活动ID传入错误：" + announcementId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse announcementInsert(@Valid Request<Map<String, AnnouncementRequest>> announcementRequest) {
        Request<Map<String, AnnouncementRequest>> request = new Request<>((announcementRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Announcement announcement = new Announcement(request.getData().get("newAnnouncement"));
        String num = request.getData().get("newAnnouncement").getName();
        Optional<Course> nOp = courseRepository.findByName(num);
        if (nOp.isPresent() || num == null || num.isEmpty()) {
            announcement.setCourse(nOp.get());
            announcementRepository.save(announcement);
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

    public ResponseEntity<StreamingResponseBody> getSelectedAnnouncementListExcl(List<AnnouncementRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","课程编号", "课程名称", "公告内容","开始时间","结束时间"};
        String outPutSheetName = "announcement.xlsx";
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
        AnnouncementRequest announcementRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                announcementRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(announcementRequest.getNum());
                cell[2].setCellValue(announcementRequest.getName());
                cell[3].setCellValue(announcementRequest.getAnnouncementContent());
                cell[4].setCellValue(announcementRequest.getBeginTime());
                cell[5].setCellValue(announcementRequest.getEndTime());
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


