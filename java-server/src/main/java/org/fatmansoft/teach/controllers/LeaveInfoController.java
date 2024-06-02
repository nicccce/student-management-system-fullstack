package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.data.dto.*;
import org.fatmansoft.teach.data.po.*;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.data.vo.OptionItem;
import org.fatmansoft.teach.data.vo.OptionItemList;
import org.fatmansoft.teach.repository.LeaveInfoRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.LeaveInfoService;
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
@RequestMapping("/api/leaveInfo")
public class LeaveInfoController {
    @Autowired
    LeaveInfoRepository leaveInfoRepository;
    @Autowired
    LeaveInfoService leaveInfoService;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    StudentRepository studentRepository;
    @PostMapping("/getLeaveInfoList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getLeaveInfoList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getLeaveInfoMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

public List getLeaveInfoMapList(String numName) {
    List dataList = new ArrayList<>();
    List<LeaveInfo> lList = new ArrayList<>();

    if(numName == null || numName.trim().isEmpty()) {
        // 查找所有的请假信息
        lList = leaveInfoRepository.findLeaveInfoListByStudentId(numName);
    } else {
        // 根据学号或姓名查找学生信息
        List<Student> slist = studentRepository.findStudentListByNumName(numName);
        for(Student s : slist) {
            Optional<LeaveInfo> l = leaveInfoRepository.findLeaveInfoByStudentStudentId(s.getStudentId());
            if(l.isPresent()) {
                lList.add(l.get());
            }
        }
    }

    if(lList != null && !lList.isEmpty()) {
        for(LeaveInfo leaveInfo : lList) {
            dataList.add(getMapFromLeaveInfo(leaveInfo));
        }
    }

    return dataList;
}
    public Map getMapFromLeaveInfo(LeaveInfo l) {
        Map m = new HashMap();
        Person p;
        Student s;
        if(l == null)
            return m;
        s = l.getStudent();
        p = s.getPerson();


        if(s == null)
            return m;
        m.put("LeaveInfoId",l.getLeaveInfoId());
        m.put("num",p.getNum());
        m.put("name",p.getName());


        m.put("reason", l.getReason());
        m.put("opinion", l.getOpinion());
        m.put("backTime",l.getBackTime());
        m.put("back",l.getBack());
        m.put("backName",ComDataUtil.getInstance().getDictionaryLabelByValue("BAC", l.getBack()));
        m.put("phone",l.getPhone());
        m.put("destination",l.getDestination());
//        String gender = p.getGender();
//        m.put("gender",gender);
//        m.put("backName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", back)); //性别类型的值转换成数据类型名
//        m.put("birthday", p.getBirthday());  //时间格式转换字符串
//        m.put("email",p.getEmail());
//        m.put("phone",p.getPhone());
//        m.put("address",p.getAddress());
//        m.put("introduce",p.getIntroduce());
        return m;
    }
    @PostMapping("/getLeaveInfoInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer leaveInfoId = dataRequest.getInteger("leaveInfoId");
        LeaveInfo l= null;
        Optional<LeaveInfo> op;
        if(leaveInfoId != null) {
            op = leaveInfoRepository.findById(leaveInfoId); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                l = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromLeaveInfo(l)); //这里回传包含学生信息的Map对象
    }
// //   @PostMapping("/getLeaveInfoListByFilter/{numName}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DataResponse getLeaveInfoListByFilterAndNumName(@Valid @RequestBody Request<Map<String, LeaveInfoRequest>> dataRequest, @PathVariable String numName) {
//        if (numName == null){
//            numName="";
//        }
//        LeaveInfoRequest filterCriteria = dataRequest.getData().get("filterCriteria");
//        List dataList = leaveInfoService.getLeaveInfoListByFilterAndNumName(filterCriteria,numName);
//        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
//    }
    @PostMapping ("/LeaveInfoInsert")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationInsert (@Valid @RequestBody Request<Map<String, LeaveInfoRequest>> dataRequest) {
        return leaveInfoService.leaveInfoInsert(dataRequest);
    }


    @PostMapping("/leaveInfoEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse leaveInfoEditSave(@Valid @RequestBody DataRequest dataRequest) {
        System.out.println(dataRequest.getData());
        Integer leaveInfoId = dataRequest.getInteger("LeaveInfoId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");  //Map 获取属性的值
        LeaveInfo l= null;
        Student s;
        Optional<LeaveInfo> op;
        Integer studentId;
        if(leaveInfoId != null) {
            op= leaveInfoRepository.findById(leaveInfoId);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                l = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if(nOp.isPresent()) {
            if(l == null || !l.getStudent().getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新学号已经存在，不能添加或修改！");
            }
        }
        s = l.getStudent();
        studentId = s.getStudentId();


        /*s.setName(CommonMethod.getString(form,"name"));
        s.setNum(CommonMethod.getString(form,"num"));*/
//        l.setStudent(CommonMethod.getString(form,"innovationName"));
        l.setDestination(CommonMethod.getString(form,"destination"));
        l.setOpinion(CommonMethod.getString(form,"opinion"));
        l.setPhone(CommonMethod.getString(form,"phone"));
        l.setBackTime(CommonMethod.getString(form,"backTime"));
        l.setReason(CommonMethod.getString(form,"reason"));
        l.setBack(CommonMethod.getString(form,"back"));
        /*s.setMotherName(CommonMethod.getString(form,"motherName"));
        s.setMotherOccupation(CommonMethod.getString(form,"motherOccupation"));
        s.setMotherAge(CommonMethod.getString(form,"motherAge"));
        s.setMotherContact(CommonMethod.getString(form,"motherContact"));*/
        leaveInfoRepository.save(l);  //修改保存教师信息
        return CommonMethod.getReturnData(l.getLeaveInfoId());  // 将teacherId返回前端
    }
    @DeleteMapping("/leaveInfoDeleteAll")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse leaveInfoDeleteAll (@Valid @RequestBody DataRequest dataRequest) {
        return leaveInfoService.leaveInfoDeleteAll(dataRequest);
    }

    @PostMapping("/getSelectedLeaveInfoListExcl")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StreamingResponseBody> getSelectedInnovationListExcl(@Valid @RequestBody Request<Map<String,List<LeaveInfoRequest>>> dataRequest) {
        List<LeaveInfoRequest> selectedList =dataRequest.getData().get("selectedLeaveInfo");
        return leaveInfoService.getSelectedLeaveInfoListExcl(selectedList);
    }
}
