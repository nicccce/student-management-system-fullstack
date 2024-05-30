package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.dto.DataRequest;
import org.fatmansoft.teach.data.dto.FamilyRequest;
import org.fatmansoft.teach.data.dto.Request;
import org.fatmansoft.teach.data.dto.TeacherRequest;
import org.fatmansoft.teach.data.po.Family;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.FamilyRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;

@Service
public class FamilyService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FamilyRepository familyRepository;

    public DataResponse familyDeleteAll(DataRequest dataRequest) {
        List<Integer> allFamilyIds = dataRequest.getList("familyId");

        if (allFamilyIds == null || allFamilyIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无家庭实体传入");
        }

        for (Integer familyId : allFamilyIds) {
            Optional<Family> op = familyRepository.findById(familyId);
            if (op.isPresent()) {
                Family f = op.get();
                familyRepository.delete(f);
            } else {
                return CommonMethod.getReturnMessageError("家庭ID传入错误：" + familyId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse familyInsert(@Valid Request<Map<String, FamilyRequest>> familyRequest) {
        Request<Map<String, FamilyRequest>> request = new Request<>((familyRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
            Family family = new Family(request.getData().get("newFamily"));
            String num = request.getData().get("newFamily").getNum();
            Optional<Person> nOp = personRepository.findByNum(num);
            Optional<Family> nFp = familyRepository.findByPersonNum(num);
            if (nOp.isPresent() || num == null || num.isEmpty()) {
                if(nFp.isPresent()) {
                    return CommonMethod.getReturnMessageError("家庭信息已存在,无法添加学生家庭信息,请转到修改页面修改");
                }
                family.setPerson(nOp.get());
                familyRepository.save(family);
                return CommonMethod.getReturnMessageOK("家庭信息保存成功");
            } else {
                return CommonMethod.getReturnMessageError("学号不存在,无法添加学生家庭信息");
            }


        //} catch (ConstraintViolationException e) {
         //   return CommonMethod.getReturnMessageError(e.getMessage());
       // } catch (Exception e) {
          //  return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
       // }
    }



    private Map<String, Object> getMapFromFamily(Family f) {
        Map<String, Object> m = new HashMap<>();
        Person p = f.getPerson();
        if (f != null) {
            m.put("familyId", f.getFamilyId());
            m.put("address", f.getAddress());
            m.put("familySize", f.getFamilySize());
            m.put("fatherName", f.getFatherName());
            m.put("fatherOccupation", f.getFatherOccupation());
            m.put("fatherAge", f.getFatherAge());
            m.put("fatherContact", f.getFatherContact());
            m.put("motherName", f.getMotherName());
            m.put("motherOccupation", f.getMotherOccupation());
            m.put("motherAge", f.getMotherAge());
            m.put("motherContact", f.getMotherContact());
            if (p != null) {
                m.put("personId", p.getPersonId());
                m.put("personNum", p.getNum());
                m.put("personName", p.getName());
            }
        }
        return m;
    }
}


