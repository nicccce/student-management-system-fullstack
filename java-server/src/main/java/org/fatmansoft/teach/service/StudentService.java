package org.fatmansoft.teach.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.CourseRequest;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.fatmansoft.teach.util.ExcelUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;



@Service
public class StudentService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private  FamilyRepository familyRepository;
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private LeaveInfoRepository leaveInfoRepository;

    public DataResponse studentDeleteAll(DataRequest dataRequest) {
        List<Integer> allStudentIds = dataRequest.getList("studentId");  // 获取studentId值

        if (allStudentIds == null || allStudentIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无学生实体传入");
        }

        for (Integer studentId : allStudentIds) {
            Optional<Student> op = studentRepository.findById(studentId);   // 查询获得实体对象
            if (op.isPresent()) {
                Student s = op.get();
                Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); // 查询对应该学生的账户
                if (uOp.isPresent()) {
                    userRepository.delete(uOp.get()); // 删除对应该学生的账户
                }
                List<Course> courseList = courseRepository.findByStudents(s);
                for (Course course :
                        courseList) {
                    course.getStudents().remove(s);
                }
                Person p = s.getPerson();
                //删除家庭信息
                Optional<Family> uFp = familyRepository.findByPersonNum(p.getNum());
                if (uFp.isPresent()) {
                    familyRepository.delete(uFp.get());
                }

                //删除创新实践信息
                List<Innovation> innovationList = innovationRepository.findInnovationListByNumName(p.getNum());
                innovationRepository.deleteAll(innovationList);

                //删除日常活动
                List<Activity> activityList = activityRepository.findActivityListByNumName(p.getNum());
                activityRepository.deleteAll(activityList);

                //删除消费信息
                List<Expense> expenseList = expenseRepository.findExpenseListByNumName(p.getNum());
                expenseRepository.deleteAll(expenseList);


                studentRepository.delete(s);    // 首先数据库永久删除学生信息
                //personRepository.delete(p);     // 然后数据库永久删除人员信息(不需要加，因为数据库中设计过了)
            } else {
                return CommonMethod.getReturnMessageError("学生ID传入错误：" + studentId);
            }
        }

        return CommonMethod.getReturnMessageOK();  // 通知前端操作正常
    }
    public DataResponse studentInsert(Request<Map<String, StudentRequest>> dataRequest) {
        Request<Map<String,StudentRequest>> request = new Request<>(dataRequest.getData());
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        try {
            // 创建或更新学生实体
            Student student = new Student(request.getData().get("newStudent"));
            String num = student.getPerson().getNum();
            Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
            if(nOp.isPresent()||student.getPerson().getNum()==""||student.getPerson().getNum()==null) {
                return CommonMethod.getReturnMessageError("新学号已经存在或无效，不能添加或修改！");
            }if (student.getPerson().getEmail()!=""&&!EmailValidator.isValidEmail(student.getPerson().getEmail())){
                return CommonMethod.getReturnMessageError("不是合法的邮箱地址！");
            }
            studentRepository.save(student);
            String password = encoder.encode("123456");
            User u= new User();
            u.setUserId(student.getPerson().getPersonId());
            u.setPerson(student.getPerson());
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            userRepository.saveAndFlush(u); //插入新的User记录
            return CommonMethod.getReturnMessageOK("学生信息保存成功");

        }catch (ConstraintViolationException e){
            return CommonMethod.getReturnMessageError(e.getMessage());
        } catch (Exception e) {
            return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        }
    }

    /**
     * 获取所有学生的部门列表，去重后返回。
     *
     * @return 学生的部门列表
     */
    public List<String> getStudentDeptList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueDepts = new HashSet<>();

        for (Student student : studentList) {
            String dept = student.getPerson().getDept();
            uniqueDepts.add(dept);
        }

        List<String> deptList = new ArrayList<>(uniqueDepts);
        return deptList;
    }

    /**
     * 获取所有学生的专业列表，去重后返回。
     *
     * @return 学生的专业列表
     */
    public List<String> getStudentMajorList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueMajors = new HashSet<>();

        for (Student student : studentList) {
            String major = student.getMajor();
            uniqueMajors.add(major);
        }

        List<String> majorList = new ArrayList<>(uniqueMajors);
        return majorList;
    }

    /**
     * 获取所有学生的班级列表，去重后返回。
     *
     * @return 学生的班级列表
     */
    public List<String> getStudentClassNameList() {
        List<Student> studentList = studentRepository.findAll();
        Set<String> uniqueClassNames = new HashSet<>();

        for (Student student : studentList) {
            String className = student.getClassName();
            uniqueClassNames.add(className);
        }

        List<String> classNameList = new ArrayList<>(uniqueClassNames);
        return classNameList;
    }

    public List<StudentRequest> getStudentListByFilterAndNumName(StudentRequest filterCriteria, String numName){
        Student filterCriteriaStudent = new Student(filterCriteria);
        List<Student> matchedStudent =  studentRepository.findByExample(filterCriteriaStudent, numName);
        List<StudentRequest> matchedStudentRequest = new ArrayList<>(){};
        for (Student student:
             matchedStudent) {
            matchedStudentRequest.add(new StudentRequest(student));
        }
        return matchedStudentRequest;
    }

    public ResponseEntity<StreamingResponseBody> getSelectedStudentListExcl(List<StudentRequest> list){
        Integer widths[] = {8, 20, 10, 15, 15, 15, 25, 10, 15, 30, 20, 30};
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "学院", "专业", "班级", "证件号码", "性别","出生日期","邮箱","电话","地址"};
        String outPutSheetName = "student.xlsx";
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
        StudentRequest studentRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                studentRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(studentRequest.getNum());
                cell[2].setCellValue(studentRequest.getName());
                cell[3].setCellValue(studentRequest.getDept());
                cell[4].setCellValue(studentRequest.getMajor());
                cell[5].setCellValue(studentRequest.getClassName());
                cell[6].setCellValue(studentRequest.getCard());
                cell[7].setCellValue(studentRequest.getGenderName());
                cell[8].setCellValue(studentRequest.getBirthday());
                cell[9].setCellValue(studentRequest.getEmail());
                cell[10].setCellValue(studentRequest.getPhone());
                cell[11].setCellValue(studentRequest.getAddress());
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

    public List<StudentRequest> changeExcelToStudent(MultipartFile file) throws IOException {
        Map<String, String> headerMap = new HashMap<String, String>() {{
            put("学号", "num");
            put("姓名", "name");
            put("学院", "dept");
            put("专业", "major");
            put("班级", "className");
            put("证件号码", "card");
            put("性别", "genderName");
            put("出生日期", "birthday");
            put("邮箱", "email");
            put("电话", "phone");
            put("地址", "address");
        }};
        List<StudentRequest> studentRequestList = ExcelUtil.<StudentRequest>readExcelOfList(file,StudentRequest.class,headerMap);
        return studentRequestList;
    }

    public DataResponse importStudentByExcel(MultipartFile file) throws IOException {
        try {
            List<StudentRequest> studentRequestList = changeExcelToStudent(file);
            for (StudentRequest studentRequest :
                    studentRequestList) {
                Student student = new Student(studentRequest);
                String num = student.getPerson().getNum();
                Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
                if(nOp.isPresent()||student.getPerson().getNum()==""||student.getPerson().getNum()==null) {
                    return CommonMethod.getReturnMessageError("学号 " + num + " 已经存在或无效，不能添加或修改！");
                }if (student.getPerson().getEmail()!=""&&!EmailValidator.isValidEmail(student.getPerson().getEmail())){
                    return CommonMethod.getReturnMessageError(student.getPerson().getEmail()+"不是合法的邮箱地址！");
                }
                studentRepository.save(student);
                String password = encoder.encode("123456");
                User u= new User();
                u.setUserId(student.getPerson().getPersonId());
                u.setPerson(student.getPerson());
                u.setUserName(num);
                u.setPassword(password);
                u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
                userRepository.saveAndFlush(u); //插入新的User记录
            }
        }catch (Exception e) {
            return CommonMethod.getReturnMessageError("传入数据异常，请重试！！\n" + e.getMessage());
        }
        return CommonMethod.getReturnMessageOK("学生信息保存成功");
    }

}

