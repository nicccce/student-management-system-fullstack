package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.*;
import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.po.Teacher;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;
    public List<CourseRequest> getCourseListByFilterAndNumName(CourseRequest filterCriteria, String numName){
        Course filterCriteriaCourse = new Course(filterCriteria);
        List<Course> matchedCourse =  courseRepository.findByExample(filterCriteriaCourse, numName);
        List<CourseRequest> matchedCourseRequest = new ArrayList<>(){};
        for (Course course:
                matchedCourse) {
            matchedCourseRequest.add(new CourseRequest(course));
        }
        return matchedCourseRequest;
    }

    public DataResponse getCourseStudent(Integer courseId){
        Optional<Course> course = courseRepository.findById(courseId);
        List<Student> studentList;
        List<StudentRequest> studentRequestList = new ArrayList<>(){};
        if (course.isPresent()) {
            studentList = course.get().getStudents();
            for (Student student :
                    studentList) {
                studentRequestList.add(new StudentRequest(student));
            }
            return  CommonMethod.getReturnData(studentRequestList);
        }else {
            return CommonMethod.getReturnMessageError("课程不存在！");
        }

    }

    public DataResponse getCourseTeacher(Integer courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        List<Teacher> teacherList;
        List<TeacherRequest> teacherRequestList = new ArrayList<>();
        if (courseOptional.isPresent()) {
            teacherList = courseOptional.get().getTeachers();
            for (Teacher teacher : teacherList) {
                teacherRequestList.add(new TeacherRequest(teacher));
            }
            return CommonMethod.getReturnData(teacherRequestList);
        } else {
            return CommonMethod.getReturnMessageError("课程不存在！");
        }
    }

    public ResponseEntity<StreamingResponseBody> getSelectedCourseListExcl(List<CourseRequest> list) {
        Integer widths[] = {8, 20, 10, 15, 15, 15, 25, 10, 40, 30, 20};
        int i, j, k;
        String titles[] = {"序号","课程编号", "课程名称", "课程类型", "课程学分", "上课地点", "开始时间", "结束时间","课程介绍", "学生人数", "开课单位"};
        String outPutSheetName = "course.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 11);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        CourseRequest courseRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                courseRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(courseRequest.getNum());
                cell[2].setCellValue(courseRequest.getName());
                cell[3].setCellValue(courseRequest.getType());
                cell[4].setCellValue(courseRequest.getCredit());
                cell[5].setCellValue(courseRequest.getLocation());
                cell[6].setCellValue(courseRequest.getBeginTime().toString());
                cell[7].setCellValue(courseRequest.getEndTime().toString());
                cell[8].setCellValue(courseRequest.getIntroduction());
                cell[9].setCellValue(courseRequest.getStudentNumber());
                cell[9].setCellValue(courseRequest.getDepartment());
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

    public DataResponse courseEditSave(Course course) {
        Integer courseId = course.getCourseId();
        String num = course.getNum();
        if (courseId==null) {
            // 检查课程编号是否已经存在
            Optional<Course> numExists = courseRepository.findByNum(num);
            if (numExists.isPresent() && (!course.getNum().equals(num))) {
                return CommonMethod.getReturnMessageError("课程编号已经存在，无法添加！");
            }
            courseRepository.save(course);  // 新增课程信息
            return CommonMethod.getReturnData(course.getCourseId());  // 将courseId返回给前端
        }else {
            Optional<Course> numExists = courseRepository.findByNum(num);
            if (numExists.isPresent() && (!numExists.get().getCourseId().equals(courseId))) {
                return CommonMethod.getReturnMessageError("课程编号已经存在，无法修改！");
            }
            Optional<Course> courseToSaveO = courseRepository.findById(courseId);
            if (courseToSaveO.isPresent()) {
                Course courseToSave = courseToSaveO.get();
                courseToSave.merge(course);
                courseRepository.save(courseToSave);  // 更改课程信息
                return CommonMethod.getReturnMessageOK();
            }
            return CommonMethod.getReturnMessageError("保存失败，请重试！");
        }
    }

    public DataResponse courseEditStudent(List<Integer> studentIdList,Integer courseId){
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()){
            Course course = courseOptional.get();
            course.getStudents().clear();
            for (Integer studentId:
                 studentIdList) {
                Optional<Student> student = studentRepository.findById(studentId);
                student.ifPresent(value -> course.getStudents().add(value));
            }
            return CommonMethod.getReturnMessageOK();
        }else {
            return CommonMethod.getReturnMessageError("课程不存在，请重试。");
        }
    }

    public DataResponse courseEditTeacher(List<Integer> teacherIdList, Integer courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.getTeachers().clear();
            for (Integer teacherId : teacherIdList) {
                Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
                teacherOptional.ifPresent(value -> course.getTeachers().add(value));
            }
            return CommonMethod.getReturnMessageOK();
        } else {
            return CommonMethod.getReturnMessageError("课程不存在，请重试。");
        }
    }

    /**
     * 获取所有课程的类型列表，去重后返回。
     *
     * @return 课程的类型列表
     */
    public List<String> getCourseTypeList() {
        List<Course> courseList = courseRepository.findAll();
        Set<String> uniqueCourseTypes = new HashSet<>();

        for (Course course : courseList) {
            String courseType = course.getType();
            uniqueCourseTypes.add(courseType);
        }

        List<String> courseTypeList = new ArrayList<>(uniqueCourseTypes);
        return courseTypeList;
    }
    public DataResponse courseDeleteAll(List<Integer> allCourseIds) {

        if (allCourseIds == null || allCourseIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("未传入课程实体");
        }

        for (Integer courseId : allCourseIds) {
            Optional<Course> courseOptional = courseRepository.findById(courseId);   // 查询获得课程对象
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();

                // 删除与课程关联的教师信息
                course.getTeachers().clear();

                // 删除与课程关联的学生信息
                course.getStudents().clear();

                // 删除课程
                courseRepository.delete(course);
            } else {
                return CommonMethod.getReturnMessageError("课程ID传入错误：" + courseId);
            }
        }

        return CommonMethod.getReturnMessageOK();  // 通知前端操作正常
    }

    public CourseRequest getCourseInfo(Integer courseId){
        CourseRequest course = null;
        Optional<Course> optionalCourse;
        if (courseId != null) {
            optionalCourse = courseRepository.findById(courseId); // 根据课程主键从数据库查询课程信息
            if (optionalCourse.isPresent()) {
                course = new CourseRequest(optionalCourse.get());
                return course;
            }
        }
        return new CourseRequest();
    }
}
