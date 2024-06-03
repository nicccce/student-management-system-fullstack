package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.ActivityEntity;
import com.teach.javafxclient.model.AnnouncementEntity;
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

public class AddAnnouncementController {
    public MFXTextField announcementContentField;
    public MFXComboBox nameComboBox;
    public MFXDatePicker beginTimePick;
    public MFXDatePicker endTimePick;


    DialogUtil dialogUtil = new DialogUtil();


    private Stage stage;

    private Runnable refreshMethod;

    @FXML
    public void initialize(){

    }
    public void Age(List list) {
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

        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setAnnouncementContent(announcementContentField.getText());

        if (nameComboBox.getSelectionModel() != null && nameComboBox.getSelectionModel().getSelectedItem() != null) {
            String selectedItem = (String)nameComboBox.getSelectionModel().getSelectedItem();
            announcementEntity.setName(selectedItem);
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDate = beginTimePick.getValue();
        if (beginDate != null) {
            String formattedBeginDate = beginDate.format(formatter);
            announcementEntity.setBeginTime(formattedBeginDate);
        }
        LocalDate endDate = endTimePick.getValue();
        if (endDate != null) {
            String formattedEndDateDate = endDate.format(formatter);
            announcementEntity.setEndTime(formattedEndDateDate);
        }
        if (announcementEntity.getBeginTime()!=null&&announcementEntity.getEndTime()!=null&&!LocalDateUtil.isLaterDate(announcementEntity.getBeginTime(),announcementEntity.getEndTime())){
            dialogUtil.openError("修改失败", "截止时间早于开始时间！");
            return;
        }
        //innovationEntity.setTeamName(teamNameField.getText());
        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());
        familyEntity.setAddress(addressField.getText());*/


        DataRequest req = new DataRequest();
        req.putObject("newAnnouncement", announcementEntity);
        DataResponse res = HttpRequestUtil.request("/api/announcement/announcementInsert",req);
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
