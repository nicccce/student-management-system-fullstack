package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.CourseRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.service.CourseService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {
    /**
     * 根据前端的筛选数据获取课程列表
     * @param dataRequest 前端请求参数，包含筛选数据
     * @param numName 前端的查询框数据
     * @return 查询到的课程信息
     */
    @PostMapping("/getCourseListByFilter/{numName}")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseListByFilterAndNumName(@Valid @RequestBody Request<Map<String, CourseRequest>> dataRequest, @PathVariable String numName) {
        if (numName == null) {
            numName = "";
        }
        CourseRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List<CourseRequest> dataList = courseService.getCourseListByFilterAndNumName(filterCriteria, numName);
        return CommonMethod.getReturnData(dataList);  // 按照测试框架规范返回 Map 的列表
    }
    @Autowired
    CourseService courseService;
    /**
     * 根据前端的筛选数据获取课程列表
     * @param dataRequest 前端请求参数，包含需要查询的课程编号或者名称
     * @return 查询到的课程信息
     */
    @PostMapping("/getCourseListByFilter/")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseListByFilter(@Valid @RequestBody Request<Map<String, CourseRequest>> dataRequest) {
        String numName = "";
        CourseRequest filterCriteria = dataRequest.getData().get("filterCriteria");
        List<CourseRequest> dataList = courseService.getCourseListByFilterAndNumName(filterCriteria, numName);
        return CommonMethod.getReturnData(dataList);  // 按照测试框架规范返回 Map 的列表
    }

    /**
     * getSelectedCourseListExcl 前端下载导出选中的课程基本信息 Excel 表数据
     * @param dataRequest 前端传入需要生成的课程信息列表
     * @return 生成的 Excel 文件流
     */
    @PostMapping("/getSelectedCourseListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedCourseListExcl(@Valid @RequestBody Request<Map<String, List<CourseRequest>>> dataRequest) {
        List<CourseRequest> selectedList = dataRequest.getData().get("selectedCourse");
        return courseService.getSelectedCourseListExcl(selectedList);
    }

    /**
     * courseEditSave 前端课程信息提交服务
     * 前端将CourseRequest对象打包成一个Json对象作为参数传回后端，后端直接可以获得对应的CourseRequest对象，从中获取属性值并保存到数据库中。
     * 如果是添加一条记录，courseId为空，首先创建Course对象，然后复制相关属性，保存到数据库中。
     * 如果是编辑原来的信息，courseId不为空，则查询出对应的Course对象，再复制相关属性，保存后修改数据库信息。
     *
     * @param dataRequest 前端传来的CourseRequest对象
     * @return 新建或修改课程的主键 courseId 返回给前端
     */
    @PostMapping("/courseEditSave")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditSave(@Valid @RequestBody Request<Map<String,CourseRequest>> dataRequest) {
            CourseRequest courseRequest = dataRequest.getData().get("course");
            if (courseRequest == null){
                return CommonMethod.getReturnMessageError("修改内容无效，请重试！");
            }
            Course course = new Course(courseRequest);
        System.out.println(course);
            return courseService.courseEditSave(course);
    }

    @GetMapping("/getStudent/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseStudent(@PathVariable Integer courseId){
        return courseService.getCourseStudent(courseId);
    }

    @GetMapping("/getTeacher/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseTeacher(@PathVariable Integer courseId) {
        return courseService.getCourseTeacher(courseId);
    }

    @PostMapping("/editStudent")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditStudent(@Valid @RequestBody DataRequest dataRequest){
        return courseService.courseEditStudent(dataRequest.getList("studentList"), dataRequest.getInteger("courseId"));
    }

    @PostMapping("/editTeacher")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditTeacher(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseEditTeacher(dataRequest.getList("teacherList"), dataRequest.getInteger("courseId"));
    }

    /**
     * 获取课程的类型列表。
     *
     * @return 包含课程类型的数据响应对象
     */
    @GetMapping("/type")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseTypeList() {
        List<String> courseTypeList = courseService.getCourseTypeList();
        return CommonMethod.getReturnData(courseTypeList);
    }

    /**
     * courseDeleteAll 删除课程信息Web服务
     * Course页面的列表里点击删除按钮则可以删除已经存在的课程信息，
     * 前端会将该记录的id回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应先删除与课程关联的其他实体，如用户和人员，再删除课程
     *
     * @param dataRequest 前端传递的课程的主键 course_id
     * @return 正常操作
     */
    @DeleteMapping("/courseDeleteAll")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseDeleteAll(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseDeleteAll(dataRequest.getList("courseId"));
    }

    /**
     * getCourseInfo 前端点击课程列表时前端获取课程详细信息请求服务
     * @param dataRequest 从前端获取 courseId 查询课程信息的主键 courseId
     * @return  根据courseId从数据库中查出数据，存在Map对象里，并返回给前端
     */
    @PostMapping("/getCourseInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getCourseInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        return CommonMethod.getReturnData(courseService.getCourseInfo(courseId)); // 这里返回包含课程信息的 CourseResponse 对象
    }
}
