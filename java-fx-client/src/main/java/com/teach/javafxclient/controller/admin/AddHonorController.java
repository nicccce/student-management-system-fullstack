package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.HonorEntity;
import com.teach.javafxclient.model.LeaveInfoEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import com.teach.javafxclient.util.LocalDateUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddHonorController {
    public MFXTextField studentNumField;
    public MFXComboBox levelComboBox;
    public MFXTextField honorNameField;
    public MFXTextField honorTypeField;
    public MFXTextField hostField;
    public DatePicker honorTimePicker;
    /*public MFXTextField motherNameField;
    public MFXTextField motherOccupationField;
    public MFXTextField motherAgeField;
    public MFXTextField motherContactField;
    public MFXTextField addressField;*/

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> levelList;
    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){
        levelList = HttpRequestUtil.getDictionaryOptionItemList("LEV");
        levelComboBox.getItems().addAll(levelList);
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

        HonorEntity honorEntity = new HonorEntity();
        honorEntity.setNum(studentNumField.getText());
        honorEntity.setHonorName(honorNameField.getText());

        if (levelComboBox.getSelectionModel() != null && levelComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedBackType = (OptionItem) levelComboBox.getSelectionModel().getSelectedItem();
            honorEntity.setLevelName(selectedBackType.getValue());
        }
        honorEntity.setHonorType(honorTypeField.getText());
        honorEntity.setHost(hostField.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate honorTime = honorTimePicker.getValue();
        if (honorTime != null) {
            String formattedHonorTime = honorTime.format(formatter);
            honorEntity.setHonorTime(formattedHonorTime);
        }
        if (!LocalDateUtil.isBeforeToday(honorEntity.getHonorTime())){
            dialogUtil.openError("保存失败","荣誉日期无效！");
            return;
        }


        DataRequest req = new DataRequest();
        req.putObject("newHonor", honorEntity);
        DataResponse res = HttpRequestUtil.request("/api/honor/HonorInsert",req);
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
