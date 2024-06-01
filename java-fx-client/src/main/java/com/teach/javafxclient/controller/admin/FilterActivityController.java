package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.ActivityEntity;
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

public class FilterActivityController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField activityNameField;
    public MFXComboBox activityTypeComboBox;

    public MFXTextField activityContentField;
    public MFXDatePicker activityDatePick;



    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> activityTypeList;   //性别选择列表数据


    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法


    //存储筛选的条件
    private ActivityEntity filerCriteria ;

    @FXML
    public void initialize(){
        //初始化各列表
        activityTypeList = HttpRequestUtil.getDictionaryOptionItemList("ACT");
        activityTypeComboBox.getItems().addAll(activityTypeList);

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
    public void init(Stage stage, ActivityEntity filerCriteria, Runnable refreshMethod) {
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

        if (activityNameField.getText() != null && !activityNameField.getText().isEmpty()) {
            filerCriteria.setActivityName(activityNameField.getText());
        }
        if (activityTypeComboBox.getValue() != null) {
            OptionItem selectedActivityType = (OptionItem) activityTypeComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setActivityType(selectedActivityType.getValue());
            filerCriteria.setActivityTypeName(selectedActivityType.getLabel());
        }

        if (activityContentField.getText() != null && !activityContentField.getText().isEmpty()) {
            filerCriteria.setActivityContent(activityContentField.getText());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate activityDate = activityDatePick.getValue();
        if (activityDate != null) {
            String formattedActivityDate = activityDate.format(formatter);
            filerCriteria.setActivityDate(formattedActivityDate);
        }

        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }

    /**
     * 初始化筛选窗口的各各输入框元素，因为需要与主界面的筛选条件一致
     * @param activity
     */
    public void fillForm(ActivityEntity activity) {
        if (activity != null) {
            if (activity.getNum() != null) {
                numField.setText(activity.getNum());
            }
            if (activity.getName() != null) {
                nameField.setText(activity.getName());
            }

            if (activity.getActivityType() != null) {
                for (OptionItem optionItem:
                        activityTypeList) {
                    if (optionItem.getValue()==activity.getActivityType()){
                        activityTypeComboBox.setValue(optionItem.getLabel());
                    }
                }

            }

            if (activity.getActivityName() != null) {
                activityNameField.setText(activity.getActivityName());
            }
            if (activity.getActivityContent() != null) {
                activityContentField.setText(activity.getActivityContent());
            }
            if (activity.getActivityDate() != null) {
                activityDatePick.setValue(LocalDate.parse(activity.getActivityDate()));
            }

        }
    }
}
