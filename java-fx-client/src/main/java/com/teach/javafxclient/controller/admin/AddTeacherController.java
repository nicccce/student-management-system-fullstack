package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.StudentEntity;
import com.teach.javafxclient.model.TeacherEntity;
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

public class AddTeacherController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField deptField;
    public MFXTextField qualificationField;
    public MFXTextField positionField;
    public MFXComboBox genderComboBox;
    public MFXTextField cardField;
    public MFXTextField emailField;
    public MFXDatePicker birthdayPick;
    public MFXTextField phoneField;
    public MFXTextField addressField;

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> genderList;   //性别选择列表数据

    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
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
            dialogUtil.openError("添加失败", "教师编号为空，不能添加！");
            return;
        }
        if( !phoneField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "电话格式不正确，不能添加！");
            return;
        }

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setNum(numField.getText());
        teacherEntity.setName(nameField.getText());
        teacherEntity.setDept(deptField.getText());
        teacherEntity.setPosition(positionField.getText());
        teacherEntity.setQualification(qualificationField.getText());
        teacherEntity.setCard(cardField.getText());

        if (genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedGender = (OptionItem) genderComboBox.getSelectionModel().getSelectedItem();
            teacherEntity.setGender(selectedGender.getValue());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = birthdayPick.getValue();
        if (birthday != null) {
            String formattedBirthday = birthday.format(formatter);
            teacherEntity.setBirthday(formattedBirthday);
        }
        if (!LocalDateUtil.isBeforeToday(teacherEntity.getBirthday())){
            dialogUtil.openError("保存失败","出生日期无效！");
            return;
        }
        teacherEntity.setEmail(emailField.getText());
        teacherEntity.setPhone(phoneField.getText());
        teacherEntity.setAddress(addressField.getText());


        DataRequest req = new DataRequest();
        req.putObject("newTeacher", teacherEntity);
        DataResponse res = HttpRequestUtil.request("/api/teacher/teacherInsert",req);
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
