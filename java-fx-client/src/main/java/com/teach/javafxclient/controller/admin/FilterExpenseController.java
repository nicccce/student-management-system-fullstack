package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.ActivityEntity;
import com.teach.javafxclient.model.ExpenseEntity;
import com.teach.javafxclient.model.InnovationEntity;
import com.teach.javafxclient.model.StudentEntity;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilterExpenseController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXComboBox expenseTypeComboBox;

    public MFXTextField expenseContentField;
    public MFXDatePicker expenseDatePick;
    public MFXTextField expenseNumField;



    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> expenseTypeList;   //性别选择列表数据


    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法


    //存储筛选的条件
    private ExpenseEntity filerCriteria ;

    @FXML
    public void initialize(){
        //初始化各列表
        expenseTypeList = HttpRequestUtil.getDictionaryOptionItemList("EXP");
        expenseTypeComboBox.getItems().addAll(expenseTypeList);

    }
    public void Age(String num,String name) {
        numField.setText(num);
        nameField.setText(name);
    }

    /**
     * 初始化筛选界面
     * @param stage 本界面对象
     * @param filerCriteria 筛选条件，与主界面共享
     * @param refreshMethod 刷新主界面方法
     */
    public void init(Stage stage, ExpenseEntity filerCriteria, Runnable refreshMethod) {
        this.stage = stage;
        this.filerCriteria = filerCriteria;
        this.refreshMethod = refreshMethod;
        //将筛选输入的初始条件改为与主界面一致
        fillForm(this.filerCriteria);
    }

    /**
     * 点击筛选按钮，进行筛选
     * @param actionEvent .
     */
    public void onFilterButtonClick(ActionEvent actionEvent) {
        //清空筛选条件
        filerCriteria.empty();
        //将填写的非空筛选条件包装
        if (numField.getText() != null && !numField.getText().isEmpty()) {
            filerCriteria.setNum(numField.getText());
        }
        if (nameField.getText() != null && !nameField.getText().isEmpty()) {
            filerCriteria.setName(nameField.getText());
        }


        if (expenseTypeComboBox.getValue() != null) {
            OptionItem selectedExpenseType = (OptionItem) expenseTypeComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setExpenseType(selectedExpenseType.getValue());
            filerCriteria.setExpenseTypeName(selectedExpenseType.getLabel());
        }

        if (expenseContentField.getText() != null && !expenseContentField.getText().isEmpty()) {
            filerCriteria.setExpenseContent(expenseContentField.getText());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expenseDate = expenseDatePick.getValue();
        if (expenseDate != null) {
            String formattedExpenseDate = expenseDate.format(formatter);
            filerCriteria.setExpenseDate(formattedExpenseDate);
        }
        if (expenseNumField.getText() != null && !expenseNumField.getText().isEmpty()) {
            filerCriteria.setExpenseNum(expenseNumField.getText());
        }
        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }

    /**
     * 初始化筛选窗口的各各输入框元素，因为需要与主界面的筛选条件一致
     * @param expense
     */
    public void fillForm(ExpenseEntity expense) {
        if (expense != null) {
            if (expense.getNum() != null) {
                numField.setText(expense.getNum());
            }
            if (expense.getName() != null) {
                nameField.setText(expense.getName());
            }

            if (expense.getExpenseType() != null) {
                for (OptionItem optionItem:
                        expenseTypeList) {
                    if (optionItem.getValue()==expense.getExpenseType()){
                        expenseTypeComboBox.setValue(optionItem.getLabel());
                    }
                }

            }


            if (expense.getExpenseContent() != null) {
                expenseContentField.setText(expense.getExpenseContent());
            }
            if (expense.getExpenseDate() != null) {
                expenseDatePick.setValue(LocalDate.parse(expense.getExpenseDate()));
            }
            if (expense.getExpenseNum() != null) {
                expenseNumField.setText(expense.getExpenseNum());
            }
        }
    }
}
