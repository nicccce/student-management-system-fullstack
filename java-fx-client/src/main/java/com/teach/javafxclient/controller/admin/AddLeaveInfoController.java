package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.LeaveInfoEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddLeaveInfoController {
    public MFXTextField studentNumField;
    public MFXComboBox backComboBox;
    public MFXTextField studentNameField;
    public MFXTextField reasonField;
    public MFXTextField phoneField;
    public MFXTextField destinationField;
    public MFXTextField opinionField;
    public DatePicker backTimePicker;
    /*public MFXTextField motherNameField;
    public MFXTextField motherOccupationField;
    public MFXTextField motherAgeField;
    public MFXTextField motherContactField;
    public MFXTextField addressField;*/

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> backList;
    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){
        backList = HttpRequestUtil.getDictionaryOptionItemList("BAC");
        backComboBox.getItems().addAll(backList);
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRefreshMethod(Runnable refreshMethod){
        this.refreshMethod = refreshMethod;
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        //弹窗错误
        if( studentNumField.getText().equals("")) {
            dialogUtil.openError("添加失败", "学号为空，不能添加！");
            return;
        }
        if( !phoneField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "电话格式不正确，不能添加！");
            return;
        }

        LeaveInfoEntity leaveInfoEntity = new LeaveInfoEntity();
        leaveInfoEntity.setNum(studentNumField.getText());
        leaveInfoEntity.setName(studentNameField.getText());
        leaveInfoEntity.setReason(reasonField.getText());
        if (backComboBox.getSelectionModel() != null && backComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedBackType = (OptionItem) backComboBox.getSelectionModel().getSelectedItem();
            leaveInfoEntity.setBack(selectedBackType.getValue());
        }
        leaveInfoEntity.setDestination(destinationField.getText());
        leaveInfoEntity.setOpinion(opinionField.getText());
        leaveInfoEntity.setPhone(phoneField.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate backTime = backTimePicker.getValue();
        if (backTime != null) {
            String formattedBackTime = backTime.format(formatter);
            leaveInfoEntity.setBackTime(formattedBackTime);
        }

        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());
        familyEntity.setAddress(addressField.getText());*/


        DataRequest req = new DataRequest();
        req.putObject("newLeaveInfo", leaveInfoEntity);
        DataResponse res = HttpRequestUtil.request("/api/leaveInfo/LeaveInfoInsert",req);
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
