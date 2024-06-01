package org.fatmansoft.teach.service;

import org.apache.poi.xssf.usermodel.*;
import org.fatmansoft.teach.data.dto.*;
import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Expense;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.ExpenseRepository;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public DataResponse expenseDeleteAll(DataRequest dataRequest) {
        List<Integer> allExpenseIds = dataRequest.getList("expenseId");

        if (allExpenseIds == null || allExpenseIds.isEmpty()) {
            return CommonMethod.getReturnMessageError("无消费信息实体传入");
        }

        for (Integer expenseId : allExpenseIds) {
            Optional<Expense> op = expenseRepository.findById(expenseId);
            if (op.isPresent()) {
                Expense i = op.get();
                expenseRepository.delete(i);
            } else {
                return CommonMethod.getReturnMessageError("消费信息ID传入错误：" + expenseId);
            }
        }

        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse expenseInsert(@Valid Request<Map<String, ExpenseRequest>> expenseRequest) {
        Request<Map<String, ExpenseRequest>> request = new Request<>((expenseRequest.getData()));
        if (request == null) {
            return CommonMethod.getReturnMessageError("无有效数据传入");
        }

        //try {
        Expense expense = new Expense(request.getData().get("newExpense"));
        String num = request.getData().get("newExpense").getNum();
        Optional<Person> nOp = personRepository.findByNum(num);
        if (nOp.isPresent() || num == null || num.isEmpty()) {
           /* if(nFp.isPresent()) {
                return CommonMethod.getReturnMessageError("消费信息已存在,无法添加学生消费信息,请转到修改页面修改");
            } //ToDo：疑似不满足多对一关系，如果有时间可以修改*/
            expense.setPerson(nOp.get());
            expenseRepository.save(expense);
            return CommonMethod.getReturnMessageOK("消费信息保存成功");
        } else {
            return CommonMethod.getReturnMessageError("学号不存在,无法添加学生消费信息");
        }


        //} catch (ConstraintViolationException e) {
        //   return CommonMethod.getReturnMessageError(e.getMessage());
        // } catch (Exception e) {
        //  return CommonMethod.getReturnMessageError("数据处理异常：" + e.getMessage());
        // }
    }



    private Map<String, Object> getMapFromExpense(Expense a) {
        Map<String, Object> m = new HashMap<>();
        Person p = a.getPerson();
        if (a != null) {
            m.put("expenseId", a.getExpenseId());
            /*m.put("name", i.getName());
            m.put("num", i.getNum());*/

            m.put("expenseType", a.getExpenseType());
            m.put("expenseContent", a.getExpenseContent());
            m.put("expenseDate", a.getExpenseDate());
            m.put("expenseNum", a.getExpenseNum());
            // m.put("teamName", i.getTeamName());
            /*m.put("motherName", f.getMotherName());
            m.put("motherOccupation", f.getMotherOccupation());
            m.put("motherAge", f.getMotherAge());
            m.put("motherContact", f.getMotherContact());*/
            if (p != null) {
                m.put("personId", p.getPersonId());
                m.put("personNum", p.getNum());
                m.put("personName", p.getName());
            }
        }
        return m;
    }


    public List<ExpenseRequest> getExpenseListByFilterAndNumName(ExpenseRequest filterCriteria, String numName){
        Expense filterCriteriaExpense = new Expense(filterCriteria);
        List<Expense> matchedExpense =  expenseRepository.findByExample(filterCriteriaExpense, numName);
        List<ExpenseRequest> matchedExpenseRequest = new ArrayList<>(){};
        for (Expense expense:
                matchedExpense) {
            matchedExpenseRequest.add(new ExpenseRequest(expense));
        }
        return matchedExpenseRequest;
    }

    public ResponseEntity<StreamingResponseBody> getSelectedExpenseListExcl(List<ExpenseRequest> list){
        Integer widths[] = {8,20, 10, 25, 25, 25, 25}; //ToDo:change.
        int i, j, k;
        String titles[] = {"序号","学号", "姓名", "消费类型","消费内容","消费日期","消费金额"};
        String outPutSheetName = "expense.xlsx";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle styleTitle = CommonMethod.createCellStyle(wb, 20);
        XSSFSheet sheet = wb.createSheet(outPutSheetName);
        for(j=0;j < widths.length;j++) {
            sheet.setColumnWidth(j,widths[j]*256);
        }
        //合并第一行
        XSSFCellStyle style = CommonMethod.createCellStyle(wb, 6);
        XSSFRow row = null;
        XSSFCell cell[] = new XSSFCell[widths.length];
        row = sheet.createRow((int) 0);
        for (j = 0; j < widths.length; j++) {
            cell[j] = row.createCell(j);
            cell[j].setCellStyle(style);
            cell[j].setCellValue(titles[j]);
            cell[j].getCellStyle();
        }
        ExpenseRequest expenseRequest;
        if (list != null && list.size() > 0) {
            for (i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                for (j = 0; j < widths.length; j++) {
                    cell[j] = row.createCell(j);
                    cell[j].setCellStyle(style);
                }
                expenseRequest = list.get(i);
                cell[0].setCellValue((i + 1) + "");
                cell[1].setCellValue(expenseRequest.getNum());
                cell[2].setCellValue(expenseRequest.getName());
                cell[3].setCellValue(expenseRequest.getExpenseTypeName());
                cell[4].setCellValue(expenseRequest.getExpenseContent());
                cell[5].setCellValue(expenseRequest.getExpenseDate());
                cell[6].setCellValue(expenseRequest.getExpenseNum());
                //cell[7].setCellValue(innovationRequest.getTeamName());
                /*cell[8].setCellValue(innovationRequest.getBirthday());
                cell[9].setCellValue(innovationRequest.getEmail());
                cell[10].setCellValue(innovationRequest.getPhone());
                cell[11].setCellValue(innovationRequest.getAddress());*/
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
