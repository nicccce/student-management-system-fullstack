package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.HonorRequest;
import org.fatmansoft.teach.data.dto.LeaveInfoRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.po.Honor;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.po.Student;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.HonorService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/honor")
public class HonorController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    HonorRepository honorRepository;
    @Autowired
    HonorService honorService;
    @PostMapping("/getHonorList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getHonorList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getHonorMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    public List getHonorMapList(String numName) {
        List dataList = new ArrayList<>();
        List<Honor> hList = new ArrayList<>();

        if(numName == null || numName.trim().isEmpty()) {
            // 查找所有的请假信息
            hList = honorRepository.findHonorListByStudentId(numName);
        } else {
            // 根据学号或姓名查找学生信息
            List<Student> slist = studentRepository.findStudentListByNumName(numName);
            for(Student s : slist) {
                Optional<Honor> h = honorRepository.findHonorByStudentStudentId(s.getStudentId());
                if(h.isPresent()) {
                    hList.add(h.get());
                }
            }
        }
        if(hList != null && !hList.isEmpty()) {
            for(Honor honor : hList) {
                dataList.add(getMapFromHonor(honor));
            }
        }
        return dataList;
    }
    public Map getMapFromHonor(Honor h) {
        Map m = new HashMap();
        Person p;
        Student s;
        if(h == null)
            return m;
        s = h.getStudent();
        p = s.getPerson();
        if(s == null)
            return m;
        m.put("HonorId",h.getHonor_Id());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("honorName", h.getTitle());
        m.put("honorTime",h.getTime());
        m.put("honorType",h.getType());
        m.put("level", h.getLevel());
        m.put("levelName",ComDataUtil.getInstance().getDictionaryLabelByValue("LEV", h.getLevel()) );
        m.put("host",h.getHost());
        return m;
    }
    @PostMapping("/getHonorInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer honorId = dataRequest.getInteger("honorId");
        Honor h= null;
        Optional<Honor> op;
        if(honorId != null) {
            op = honorRepository.findById(honorId); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                h = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromHonor(h)); //这里回传包含学生信息的Map对象
    }

    @PostMapping ("/HonorInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse honorInsert (@Valid @RequestBody Request<Map<String, HonorRequest>> dataRequest) {
        return honorService.honorInsert(dataRequest);
    }


    @PostMapping("/HonorEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse honorEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer honorId = dataRequest.getInteger("HonorId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取属性的值
        Honor h= null;
        Student s;
        Optional<Honor> op;
        Integer studentId;
        if(honorId != null) {
            op= honorRepository.findById(honorId);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                h = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if(nOp.isPresent()) {
            if(h == null || !h.getStudent().getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新学号已经存在，不能添加或修改！");
            }
        }
        s = h.getStudent();
        studentId = s.getStudentId();


        /*s.setName(CommonMethod.getString(form,"name"));
        s.setNum(CommonMethod.getString(form,"num"));*/
//        l.setStudent(CommonMethod.getString(form,"innovationName"));
        h.setTitle(CommonMethod.getString(form,"honorName"));
        h.setTime(CommonMethod.getString(form,"honorTime"));
        h.setType(CommonMethod.getString(form,"honorType"));
        h.setLevel(CommonMethod.getString(form,"level"));
        h.setHost(CommonMethod.getString(form,"host"));

        honorRepository.save(h);  //修改保存教师信息
        return CommonMethod.getReturnData(h.getHonor_Id());  // 将teacherId返回前端
    }
    @DeleteMapping("/honorDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse honorDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return honorService.honorDeleteAll(dataRequest);
    }
    @PostMapping("/getSelectedHonorListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedInnovationListExcl(@Valid @RequestBody Request<Map<String,List<HonorRequest>>> dataRequest) {
        List<HonorRequest> selectedList =dataRequest.getData().get("selectedHonor");
        return honorService.getSelectedHonorListExcl(selectedList);
    }











}
