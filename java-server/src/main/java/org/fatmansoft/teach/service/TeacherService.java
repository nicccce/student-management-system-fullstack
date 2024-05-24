package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.StudentRequest;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.*;

@Service
public class TeacherService {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    public DataResponse teacherDeleteAll(DataRequest dataRequest) {
        List<Integer> allTeacherIds = dataRequest.getList("teacherId");  // 获取teacherId值

        if (allTeacherIds == null || allTeacherIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无教师实体传入");
        }

        for (Integer teacherId : allTeacherIds) {
            Optional<Teacher> op = teacherRepository.findById(teacherId);   // 查询获得实体对象
            if (op.isPresent()) {
                Teacher s = op.get();
                Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); // 查询对应该学生的账户
                if (uOp.isPresent()) {
                    userRepository.delete(uOp.get()); // 删除对应该学生的账户
                }
                Person p = s.getPerson();
                teacherRepository.delete(s);    // 首先数据库永久删除教师信息
                personRepository.delete(p);     // 然后数据库永久删除人员信息
            } else {
                return CommonMethod.getReturnMessageError("教师ID传入错误：" + teacherId);
            }
        }

        return CommonMethod.getReturnMessageOK();  // 通知前端操作正常
    }
    public DataResponse teacherInsert(Request<Map<String, TeacherRequest>> dataRequest) {
        Request<Map<String,TeacherRequest>> request = new Request<>(dataRequest.getData());
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        try {
            // 创建或更新学生实体
            Teacher teacher = new Teacher(request.getData().get("newTeacher"));
            String num = teacher.getPerson().getNum();
            Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
            if(nOp.isPresent()||teacher.getPerson().getNum()==""||teacher.getPerson().getNum()==null) {
                return CommonMethod.getReturnMessageError("新学号已经存在或无效，不能添加或修改！");
            }if (teacher.getPerson().getEmail()!=""&&!EmailValidator.isValidEmail(teacher.getPerson().getEmail())){
                return CommonMethod.getReturnMessageError("不是合法的邮箱地址！");
            }
            personRepository.save(teacher.getPerson());
            teacherRepository.save(teacher);
            String password = encoder.encode("123456");
            User u= new User();
            u.setUserId(teacher.getPerson().getPersonId());
            u.setPerson(teacher.getPerson());
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            userRepository.saveAndFlush(u); //插入新的User记录
            return CommonMethod.getReturnMessageOK("教师信息保存成功");

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
    public List<String> getTeacherDeptList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        Set<String> uniqueDepts = new HashSet<>();

        for (Teacher teacher : teacherList) {
            String dept = teacher.getPerson().getDept();
            uniqueDepts.add(dept);
        }

        List<String> deptList = new ArrayList<>(uniqueDepts);
        return deptList;
    }

    /**
     * 获取所有教师的职位列表，去重后返回。
     *
     * @return 教师的职位列表
     */
    public List<String> getTeacherPositionList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        Set<String> uniquePosition = new HashSet<>();

        for (Teacher teacher : teacherList) {
            String position = teacher.getPosition();
            uniquePosition.add(position);
        }

        List<String> positionList = new ArrayList<>(uniquePosition);
        return positionList;
    }

    /**
     * 获取所有教师的班级列表，去重后返回。
     *
     * @return 教师的班级列表
     */
    public List<String> getTeacherQualificationList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        Set<String> uniqueQualification = new HashSet<>();

        for (Teacher teacher : teacherList) {
            String qualification = teacher.getQualification();
            uniqueQualification.add(qualification);
        }

        List<String> qualificationList = new ArrayList<>(uniqueQualification);
        return qualificationList;
    }

    public List<TeacherRequest> getTeacherListByFilterAndNumName(TeacherRequest filterCriteria, String numName){
        Teacher filterCriteriaTeacher = new Teacher(filterCriteria);
        List<Teacher> matchedTeacher =  teacherRepository.findByExample(filterCriteriaTeacher, numName);
        List<TeacherRequest> matchedTeacherRequest = new ArrayList<>(){};
        for (Teacher teacher:
                matchedTeacher) {
            matchedTeacherRequest.add(new TeacherRequest(teacher));
        }
        return matchedTeacherRequest;
    }

    public ResponseEntity<StreamingResponseBody> getSelectedTeacherListExcl(List<TeacherRequest> list){
        Integer widths[] = {8, 20, 10, 15, 15, 15, 25, 10, 15, 30, 20, 30};
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "学院", "职位", "学历", "证件号码", "性别","出生日期","邮箱","电话","地址"};
        String outPutSheetName = "teacher.xlsx";
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
        TeacherRequest teacherRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                teacherRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(teacherRequest.getNum());
                cell[2].setCellValue(teacherRequest.getName());
                cell[3].setCellValue(teacherRequest.getDept());
                cell[4].setCellValue(teacherRequest.getPosition());
                cell[5].setCellValue(teacherRequest.getQualification());
                cell[6].setCellValue(teacherRequest.getCard());
                cell[7].setCellValue(teacherRequest.getGenderName());
                cell[8].setCellValue(teacherRequest.getBirthday());
                cell[9].setCellValue(teacherRequest.getEmail());
                cell[10].setCellValue(teacherRequest.getPhone());
                cell[11].setCellValue(teacherRequest.getAddress());
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
