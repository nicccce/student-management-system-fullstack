package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.service.CoursewareService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courseware")
public class CoursewareController {
    @Autowired
    CoursewareService coursewareService;

    @PostMapping("/getFileNames/{courseNum}")
    public DataResponse getFileNames(@PathVariable String courseNum){
        return CommonMethod.getReturnData(coursewareService.getFileNames(courseNum));
    }

    @GetMapping("/getCourseware/{courseNum}")
    public ResponseEntity<StreamingResponseBody> getFileByCourseNumAndFileName(@PathVariable String courseNum, @RequestParam String fileName){
        return coursewareService.getFileByCourseNumAndFileName(courseNum,fileName);
    }

    @GetMapping("/changeCoursewareName/{courseNum}")
    public DataResponse changeCoursewareNameByCourseNumAndFileName(@PathVariable String courseNum, @RequestParam String fileName,@RequestParam String newFileName){
        return coursewareService.changeCoursewareNameByCourseNumAndFileName(courseNum,fileName,newFileName);
    }

    @DeleteMapping("/deleteCourseware/{courseNum}")
    public DataResponse deleteCourseware(@PathVariable String courseNum, @RequestParam String fileName) {
        return coursewareService.deleteCourseware(courseNum, fileName);
    }

}
