package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.LeaveInfoEntity;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilterLeaveInfoController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public DatePicker backTimePick;
    public ComboBox backComboBox;
    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> backTypeList;   //性别选择列表数据


    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法


    //存储筛选的条件
    private LeaveInfoEntity filerCriteria ;
    @FXML
    public void initialize(){
        //初始化各列表
        backTypeList = HttpRequestUtil.getDictionaryOptionItemList("INO");
        backComboBox.getItems().addAll(backTypeList);
    }
    public void init(Stage stage, LeaveInfoEntity filerCriteria, Runnable refreshMethod) {
        this.stage = stage;
        this.filerCriteria = filerCriteria;
        this.refreshMethod = refreshMethod;
        //将筛选输入的初始条件改为与主界面一致
        fillForm(this.filerCriteria);
    }
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

        if (backComboBox.getValue() != null) {
            OptionItem selectedBackType = (OptionItem) backComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setBack(selectedBackType.getLabel());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate backTime = backTimePick.getValue();
        if (backTime != null) {
            String formattedBackTime = backTime.format(formatter);
            filerCriteria.setBackTime(formattedBackTime);
        }

        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }
    public void fillForm(LeaveInfoEntity leaveInfoEntity) {
        if (leaveInfoEntity != null) {
            if (leaveInfoEntity.getNum() != null) {
                numField.setText(leaveInfoEntity.getNum());
            }
            if (leaveInfoEntity.getName() != null) {
                nameField.setText(leaveInfoEntity.getName());
            }

            if (leaveInfoEntity.getBack() != null) {
                for (OptionItem optionItem:
                        backTypeList) {
                    if (optionItem.getValue()==leaveInfoEntity.getBack()){
                        backComboBox.setValue(optionItem.getLabel());
                    }
                }
            }
            if (leaveInfoEntity.getBackTime() != null) {
                backTimePick.setValue(LocalDate.parse(leaveInfoEntity.getBackTime()));
            }

        }
    }
}
