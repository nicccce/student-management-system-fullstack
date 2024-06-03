package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.po.Course;
import org.fatmansoft.teach.data.po.Score;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.data.vo.OptionItem;
import org.fatmansoft.teach.data.vo.OptionItemList;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.ScoreRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem(s.getStudentId(), s.getPerson().getNum(), s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }

    @PostMapping("/getCourseItemOptionList")
    public OptionItemList getCourseItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Course> sList = courseRepository.findAll();  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Course c : sList) {
            itemList.add(new OptionItem(c.getCourseId(), c.getNum(), c.getNum()+"-"+c.getName()));
        }
        return new OptionItemList(0, itemList);
    }

    @PostMapping("/getScoreList")
    public DataResponse getScoreList(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;
        Integer courseId = dataRequest.getInteger("courseId");
        if(courseId == null)
            courseId = 0;
        List<Score> sList = scoreRepository.findByStudentCourse(studentId, courseId);  //数据库查询操作
        List dataList = new ArrayList();
        Map m;
        for (Score s : sList) {
            m = new HashMap();
            m.put("scoreId", s.getScoreId());
            m.put("studentId",s.getStudent().getStudentId());
            m.put("courseId",s.getCourse().getCourseId());
            m.put("studentNum",s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("className",s.getStudent().getClassName());
            m.put("courseNum",s.getCourse().getNum());
            m.put("courseName",s.getCourse().getName());
            m.put("credit",s.getCourse().getCredit());
            m.put("mark",s.getMark());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/scoreSave")
    public DataResponse scoreSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Integer courseId = dataRequest.getInteger("courseId");
        Integer mark = dataRequest.getInteger("mark");
        Integer scoreId = dataRequest.getInteger("scoreId");
        Optional<Score> op;
        Score s = null;
        if(scoreId != null) {
            op= scoreRepository.findById(scoreId);
            if(op.isPresent())
                s = op.get();
        }
        if(s == null) {
            s = new Score();
            s.setStudent(studentRepository.findById(studentId).get());
            s.setCourse(courseRepository.findById(courseId).get());
        }
        s.setMark(mark);
        scoreRepository.save(s);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/scoreDelete")
    public DataResponse scoreDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer scoreId = dataRequest.getInteger("scoreId");
        Optional<Score> op;
        Score s = null;
        if(scoreId != null) {
            op= scoreRepository.findById(scoreId);
            if(op.isPresent()) {
                s = op.get();
                scoreRepository.delete(s);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

}
