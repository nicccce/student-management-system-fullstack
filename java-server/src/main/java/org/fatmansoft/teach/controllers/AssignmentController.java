package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.AssignmentRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.AssignmentService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {
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
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    private StudentRepository studentRepository;

    /**
     * getMapFromStudent 将教师表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromAssignment(Assignment s) {
        Map m = new HashMap();
        Course p;
        if(s == null)
            return m;
        p = s.getCourse();
        if(p == null)
            return m;
        m.put("assignmentId", s.getAssignmentId());
        m.put("courseId", p.getCourseId());
        m.put("num",p.getNum()); //课程编号
        m.put("name",p.getName()); //课程名称
        m.put("assignmentContent",s.getAssignmentContent());
        m.put("submissionMethod",s.getSubmissionMethod());
        m.put("beginTime",s.getBeginTime());
        m.put("endTime",s.getEndTime());
        return m;
    }

    /**
     *  getStudentMapList 根据输入参数查询得到教师数据的 Map List集合 参数为空 查出所有教师， 参数不为空，查出人员编号或人员名称 包含输入字符串的教师
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getAssignmentMapList(String numName) {
        List dataList = new ArrayList();
        List<Course> list = courseRepository.findCourseListByNumName(numName);
        for (Course c: list) {
            List<Assignment> sList = assignmentRepository.findAllByCourse(c);
            if(sList == null || sList.size() == 0)
                continue;
            for(int i = 0; i < sList.size();i++) {
                dataList.add(getMapFromAssignment(sList.get(i)));
            }
        }
        return dataList;
    }


    @PostMapping("/getAssignmentList")
    public DataResponse getAssignmentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getAssignmentMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * 教师端初始化查询所需方法
     * @param dataRequest
     * @return
     */
    @PostMapping("/getAssignmentListByUserId")
    public DataResponse getAssignmentListByUserId(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("userId");
        Optional<User> op = userRepository.findByUserId(CommonMethod.getUserId());
        User u = op.get();
        String s = u.getPerson().getNum();
        Optional<Teacher> ot = teacherRepository.findByPersonNum(s);
        Teacher teacher = ot.get();
        List<Course> courses = courseRepository.findByTeachers(teacher);

        List dataList = new ArrayList();
        for (Course c: courses) {
            List<Assignment> sList = assignmentRepository.findAllByCourse(c);
            if(sList == null || sList.size() == 0)
                continue;
            for(int i = 0; i < sList.size();i++) {
                dataList.add(getMapFromAssignment(sList.get(i)));
            }
        }

        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/getAssignmentListByStudentUserId")
    public DataResponse getAssignmentListByStudentUserId(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("userId");
        Optional<User> op = userRepository.findByUserId(CommonMethod.getUserId());
        User u = op.get();
        String s = u.getPerson().getNum();
        Optional<Student> ot = studentRepository.findByPersonNum(s);
        Student student = ot.get();
        List<Course> courses = courseRepository.findByStudents(student);

        List dataList = new ArrayList();
        for (Course c: courses) {
            List<Assignment> sList = assignmentRepository.findAllByCourse(c);
            if(sList == null || sList.size() == 0)
                continue;
            for(int i = 0; i < sList.size();i++) {
                dataList.add(getMapFromAssignment(sList.get(i)));
            }
        }

        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    /**
     * getStudentInfo 前端点击教师列表时前端获取教师详细信息请求服务
     * @param dataRequest 从前端获取 teacherId 查询教师信息的主键 teacher_id
     * @return  根据teacherId从数据库中查出数据，存在Map对象里，并返回前端
     */

    @PostMapping("/getAssignmentInfo")
    public DataResponse getAssignmentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer assignmentId = dataRequest.getInteger("assignmentId");
        Assignment s= null;
        Optional<Assignment> op;
        if(assignmentId != null) {
            op= assignmentRepository.findById(assignmentId);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromAssignment(s)); //这里回传包含教师信息的Map对象
    }

    /**
     * teacherEditSave 前端教师信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Person, User,Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * teacherId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * @return  新建修改教师的主键 teacher_id 返回前端
     */
    @PostMapping("/assignmentEditSave")
    public DataResponse assignmentEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer assignmentId = dataRequest.getInteger("assignmentId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取课程编号的值
        Assignment s= null;
        Person p;
        User u;
        Optional<Assignment> op;
        Integer personId;
        if(assignmentId != null) {
            op= assignmentRepository.findById(assignmentId);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Optional<Course> nOp = courseRepository.findByNum(num); //查询是否存在num的课程
        if(nOp.isPresent()) {
            if(s == null || !s.getCourse().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("服务器错误");
            }
        }

        s.setAssignmentContent(CommonMethod.getString(form,"assignmentContent"));
        s.setSubmissionMethod(CommonMethod.getString(form,"submissionMethod"));
        s.setEndTime(CommonMethod.getString(form,"endTime"));
        s.setBeginTime(CommonMethod.getString(form,"beginTime"));
        assignmentRepository.save(s);  //修改保存教师信息
        return CommonMethod.getReturnData(s.getAssignmentId());  // 将teacherId返回前端
    }


    /**
     * teacherDeleteAll 删除教师信息Web服务 Student页面的列表里点击删除按钮则可以删除已经存在的教师信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，应为user关联person,Student关联Person 所以要先删除Student,User，再删除Person
     * @param dataRequest  前端teacherId 药删除的教师的主键 teacher_id
     * @return  正常操作
     */
    @DeleteMapping("/assignmentDeleteAll")
    public DataResponse assignmentDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return assignmentService.assignmentDeleteAll(dataRequest);
    }

    /**
     * teacherInsert 增添教师信息Web服务 Student页面的列表里点击添加按钮则可以添加教师信息
     *
     * @param dataRequest  前端教师实体信息
     * @return  正常操作
     */
    @PostMapping ("/assignmentInsert")
    public DataResponse assignmentInsert (@Valid @RequestBody Request<Map<String, AssignmentRequest>> dataRequest) {
        return assignmentService.assignmentInsert(dataRequest);
    }

    /**
     * getStudentListExcl 前端下载导出选中的学生基本信息Excl表数据
     * @param dataRequest 前端传入需要生成的学生信息列表
     * @return 生成的Excel文件流
     */
    @PostMapping("/getSelectedAssignmentListExcl")
    public ResponseEntity<StreamingResponseBody> getSelectedAssignmentListExcl(@Valid @RequestBody Request<Map<String,List<AssignmentRequest>>> dataRequest) {
        List<AssignmentRequest> selectedList =dataRequest.getData().get("selectedAssignment");
        return assignmentService.getSelectedAssignmentListExcl(selectedList);
    }

    @PostMapping("/getBee")
    public DataResponse getBee(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("userId");
        Optional<User> op = userRepository.findByUserId(CommonMethod.getUserId());
        User u = op.get();
        String s = u.getPerson().getNum();
        Optional<Teacher> ot = teacherRepository.findByPersonNum(s);
        Teacher teacher = ot.get();
        List<Course> courses = courseRepository.findByTeachers(teacher);
        List<String> Bee = new ArrayList<>();
        int i = 1;
        for (Course c: courses) {
            String Cname = c.getName();
            Bee.add(Cname);
        }

        return CommonMethod.getReturnData(Bee);  //按照测试框架规范会送Map的list
    }

}
