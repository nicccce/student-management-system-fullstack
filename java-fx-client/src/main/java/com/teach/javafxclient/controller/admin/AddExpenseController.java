package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.*;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import com.teach.javafxclient.util.LocalDateUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddExpenseController {
    public MFXTextField numField;
    public MFXComboBox expenseTypeComboBox;
    public MFXTextField expenseContentField;
    public MFXDatePicker expenseDatePick;
    public MFXTextField expenseNumField;
    //public MFXTextField teamNameField;
    /*public MFXTextField motherNameField;
    public MFXTextField motherOccupationField;
    public MFXTextField motherAgeField;
    public MFXTextField motherContactField;
    public MFXTextField addressField;*/

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> expenseTypeList;
    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){
        expenseTypeList = HttpRequestUtil.getDictionaryOptionItemList("EXP");
        expenseTypeComboBox.getItems().addAll(expenseTypeList);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRefreshMethod(Runnable refreshMethod){
        this.refreshMethod = refreshMethod;
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        //弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("添加失败", "学号为空，不能添加！");
            return;
        }
        if( !expenseNumField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "消费金额格式不正确，不能添加！");
            return;
        }

        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setNum(numField.getText());

        if (expenseTypeComboBox.getSelectionModel() != null && expenseTypeComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedExpenseType = (OptionItem) expenseTypeComboBox.getSelectionModel().getSelectedItem();
            expenseEntity.setExpenseType(selectedExpenseType.getValue());
        }

        expenseEntity.setExpenseContent(expenseContentField.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expenseDate = expenseDatePick.getValue();
        if (expenseDate != null) {
            String formattedExpenseDate = expenseDate.format(formatter);
            expenseEntity.setExpenseDate(formattedExpenseDate);
        }
        if (!LocalDateUtil.isBeforeToday(expenseEntity.getExpenseDate())){
            dialogUtil.openError("保存失败","消费日期无效！");
            return;
        }

        expenseEntity.setExpenseNum(expenseNumField.getText());
        //innovationEntity.setTeamName(teamNameField.getText());
        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());
        familyEntity.setAddress(addressField.getText());*/


        DataRequest req = new DataRequest();
        req.putObject("newExpense", expenseEntity);
        DataResponse res = HttpRequestUtil.request("/api/expense/expenseInsert",req);
        if (res != null) {
            if (res.getCode() == 0) {
                dialogUtil.openGeneric("添加成功", "添加成功!点击确认继续添加。", this::continueAdding);
                //refreshMethod.run();
                if (isContinue) {
                    isContinue = false;
                } else {
                    stage.close();
                }
            } else {
                dialogUtil.openError("添加失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("添加失败", "服务器无响应，请稍后重试。");
        }
    }

    boolean isContinue = false;
    private void continueAdding(){
        isContinue = true;
    }

}
