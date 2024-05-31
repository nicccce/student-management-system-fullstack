package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.FamilyEntity;
import com.teach.javafxclient.model.InnovationEntity;
import com.teach.javafxclient.model.TeacherEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
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

public class AddInnovationController {
    public MFXTextField numField;
    public MFXTextField innovationNameField;
    public MFXComboBox innovationTypeComboBox;
    public MFXTextField instructorField;
    public MFXTextField teamPositionField;
    public MFXTextField teamNameField;
    /*public MFXTextField motherNameField;
    public MFXTextField motherOccupationField;
    public MFXTextField motherAgeField;
    public MFXTextField motherContactField;
    public MFXTextField addressField;*/

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> innovationTypeList;
    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){
        innovationTypeList = HttpRequestUtil.getDictionaryOptionItemList("INO");
        innovationTypeComboBox.getItems().addAll(innovationTypeList);
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
/*        //检查数字格式等
        if( !innovationNameField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "项目名称格式不正确，不能添加！");
            return;
        }
        if( !innovationTypeField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "项目类型格式不正确，不能添加！");
            return;
        }
        if( !instructorField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "指导老师名称格式不正确，不能添加！");
            return;
        }
        if( !teamPositionField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "队伍名次格式不正确，不能添加！");
            return;
        }
        if( !teamNameField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "队伍名称格式不正确，不能添加！");
            return;
        }*/
       /* if( !motherContactField.getText().matches("\\d+")) {
            dialogUtil.openError("添加失败", "母亲联系方式格式不正确，不能添加！");
            return;
        }*/
        InnovationEntity innovationEntity = new InnovationEntity();
        innovationEntity.setNum(numField.getText());
        innovationEntity.setInnovationName(innovationNameField.getText());
        if (innovationTypeComboBox.getSelectionModel() != null && innovationTypeComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedInnovationType = (OptionItem) innovationTypeComboBox.getSelectionModel().getSelectedItem();
            innovationEntity.setInnovationType(selectedInnovationType.getValue());
        }
        innovationEntity.setInstructor(instructorField.getText());
        innovationEntity.setTeamPosition(teamPositionField.getText());
        innovationEntity.setTeamName(teamNameField.getText());
        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());
        familyEntity.setAddress(addressField.getText());*/


        DataRequest req = new DataRequest();
        req.putObject("newInnovation", innovationEntity);
        DataResponse res = HttpRequestUtil.request("/api/innovation/innovationInsert",req);
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
