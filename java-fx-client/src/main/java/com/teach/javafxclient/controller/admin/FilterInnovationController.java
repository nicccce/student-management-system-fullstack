package com.teach.javafxclient.controller.admin;

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

public class FilterInnovationController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField innovationNameField;
    public MFXComboBox innovationTypeComboBox;

    public MFXTextField instructorField;
    public MFXTextField teamPositionField;
    public MFXTextField teamNameField;


    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> innovationTypeList;   //性别选择列表数据


    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法


    //存储筛选的条件
    private InnovationEntity filerCriteria ;

    @FXML
    public void initialize(){
        //初始化各列表
        innovationTypeList = HttpRequestUtil.getDictionaryOptionItemList("INO");
        innovationTypeComboBox.getItems().addAll(innovationTypeList);

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
    public void init(Stage stage, InnovationEntity filerCriteria, Runnable refreshMethod) {
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

        if (innovationNameField.getText() != null && !innovationNameField.getText().isEmpty()) {
            filerCriteria.setInnovationName(innovationNameField.getText());
        }
        if (innovationTypeComboBox.getValue() != null) {
            OptionItem selectedInnovationType = (OptionItem) innovationTypeComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setInnovationType(selectedInnovationType.getValue());
            filerCriteria.setInnovationTypeName(selectedInnovationType.getLabel());
        }

        if (instructorField.getText() != null && !instructorField.getText().isEmpty()) {
            filerCriteria.setInstructor(instructorField.getText());
        }
        if (teamPositionField.getText() != null && !teamPositionField.getText().isEmpty()) {
            filerCriteria.setTeamPosition(teamPositionField.getText());
        }
        if (teamNameField.getText() != null && !teamNameField.getText().isEmpty()) {
            filerCriteria.setTeamName(teamNameField.getText());
        }
        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }

    /**
     * 初始化筛选窗口的各各输入框元素，因为需要与主界面的筛选条件一致
     * @param innovation
     */
    public void fillForm(InnovationEntity innovation) {
        if (innovation != null) {
            if (innovation.getNum() != null) {
                numField.setText(innovation.getNum());
            }
            if (innovation.getName() != null) {
                nameField.setText(innovation.getName());
            }

            if (innovation.getInnovationType() != null) {
                for (OptionItem optionItem:
                        innovationTypeList) {
                    if (optionItem.getValue()==innovation.getInnovationType()){
                        innovationTypeComboBox.setValue(optionItem.getLabel());
                    }
                }

            }

            if (innovation.getInnovationName() != null) {
                innovationNameField.setText(innovation.getInnovationName());
            }
            if (innovation.getInstructor() != null) {
                instructorField.setText(innovation.getInstructor());
            }
            if (innovation.getTeamPosition() != null) {
                teamPositionField.setText(innovation.getTeamPosition());
            }
            if (innovation.getTeamName() != null) {
                teamNameField.setText(innovation.getTeamName());
            }
        }
    }
}
