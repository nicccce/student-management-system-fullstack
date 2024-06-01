package org.fatmansoft.teach.data.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fatmansoft.teach.data.dto.ActivityRequest;
import org.fatmansoft.teach.data.dto.ExpenseRequest;
import org.fatmansoft.teach.data.dto.InnovationRequest;

import javax.persistence.*;

@Entity
@Table(name = "expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expenseId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    //消费类型 消费内容 消费日期 消费金额
    private String expenseType;
    private String expenseContent;
    private String expenseDate;
    private String expenseNum;

    public Expense(ExpenseRequest expenseRequest) {
        setExpenseId(expenseRequest.getExpenseId());
        /*setName(innovationRequest.getName());
        setNum(innovationRequest.getNum());*/
        setExpenseType(expenseRequest.getExpenseType());
        setExpenseContent(expenseRequest.getExpenseContent());
        setExpenseDate(expenseRequest.getExpenseDate());
        setExpenseNum(expenseRequest.getExpenseNum());

    }
}
