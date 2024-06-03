package com.teach.javafxclient.controller.teacher;

import com.teach.javafxclient.model.AssignmentEntity;
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

public class AddAssignmentController {
    public MFXTextField assignmentContentField;
    public MFXComboBox<OptionItem> nameComboBox;
    public MFXDatePicker beginTimePick;
    public MFXDatePicker endTimePick;
    public MFXTextField submissionMethodField;


    DialogUtil dialogUtil = new DialogUtil();


    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){

    }
    public void Age(List<OptionItem> list) {
        nameComboBox.getItems().addAll(list);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRefreshMethod(Runnable refreshMethod){
        this.refreshMethod = refreshMethod;
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        //弹窗错误
        if( nameComboBox.getText().equals("")) {
            dialogUtil.openError("添加失败", "课程为空，不能添加！");
            return;
        }

        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setAssignmentContent(assignmentContentField.getText());
        assignmentEntity.setSubmissionMethod(submissionMethodField.getText());
        if (nameComboBox.getSelectionModel() != null && nameComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedItem = nameComboBox.getSelectionModel().getSelectedItem();
            assignmentEntity.setName(selectedItem.getLabel());
            assignmentEntity.setNum(selectedItem.getValue());
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDate = beginTimePick.getValue();
        if (beginDate != null) {
            String formattedBeginDate = beginDate.format(formatter);
            assignmentEntity.setBeginTime(formattedBeginDate);
        }
        LocalDate endDate = endTimePick.getValue();
        if (endDate != null) {
            String formattedEndDateDate = endDate.format(formatter);
            assignmentEntity.setEndTime(formattedEndDateDate);
        }
        if (assignmentEntity.getBeginTime()!=null&&assignmentEntity.getEndTime()!=null&&!LocalDateUtil.isLaterDate(assignmentEntity.getBeginTime(),assignmentEntity.getEndTime())){
            dialogUtil.openError("添加失败", "截止时间早于开始时间！");
            return;
        }
        //innovationEntity.setTeamName(teamNameField.getText());
        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());
        familyEntity.setAddress(addressField.getText());*/


        DataRequest req = new DataRequest();
        req.putObject("newAssignment", assignmentEntity);
        DataResponse res = HttpRequestUtil.request("/api/assignment/assignmentInsert",req);
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
