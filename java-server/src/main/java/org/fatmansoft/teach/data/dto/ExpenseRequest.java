package org.fatmansoft.teach.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.po.Activity;
import org.fatmansoft.teach.data.po.Expense;
import org.fatmansoft.teach.data.po.Innovation;
import org.fatmansoft.teach.data.po.Person;
import org.fatmansoft.teach.util.ComDataUtil;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {
    private Integer expenseId;
    private String expenseType;
    private String expenseTypeName;
    private String expenseContent;
    private String expenseDate;
    private String expenseNum;

    private String num;
    private String name;

    public ExpenseRequest(Expense expense) {
        Person person = expense.getPerson();
        setExpenseId(expense.getExpenseId());
        /*setName(innovation.getName());
        setNum(innovation.getNum());*/

        setExpenseType(expense.getExpenseType());
        setExpenseContent(expense.getExpenseContent());
        setExpenseDate(expense.getExpenseDate());
        setExpenseNum(expense.getExpenseNum());
        /*setMotherName(family.getMotherName());
        setMotherOccupation(family.getMotherOccupation());
        setMotherAge(family.getMotherAge());
        setMotherContact(family.getMotherContact());*/
        setNum(person.getNum());
        setName(person.getName());
        setExpenseTypeName(ComDataUtil.getInstance().getDictionaryLabelByValue("EXP", expenseType));

    }
}
