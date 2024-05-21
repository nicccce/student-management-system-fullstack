package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStudent {

    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField deptField;
    public MFXTextField majorField;
    public MFXTextField classNameField;
    public MFXComboBox genderComboBox;
    public MFXTextField cardField;
    public MFXTextField emailField;
    public MFXDatePicker birthdayPick;
    public MFXTextField phoneField;
    public MFXTextField addressField;

    DialogUtil dialogUtil;

    private List<OptionItem> genderList;   //性别选择列表数据

    Stage stage;

    @FXML
    public void initialize(){
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        //弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("添加失败", "学号为空，不能添加！");
            return;
        }
        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender",((OptionItem)(genderComboBox.getSelectionModel().getSelectedItem())).getValue());

        // 定义日期格式为 "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        form.put("birthday",birthdayPick.getValue().format(formatter));
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());
        DataRequest req = new DataRequest();
        req.put("newStudent", form);
        DataResponse res = HttpRequestUtil.request("/api/student/studentEditSave",req);
        if (res != null) {
            if (res.getCode() == 0) {
                dialogUtil.openGeneric("添加成功", "添加成功!点击确认继续添加。", this::continueAdding);
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
