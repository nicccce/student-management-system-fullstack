package com.teach.javafxclient.controller.admin;


import com.teach.javafxclient.model.TeacherEntity;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilterTeacherController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXComboBox<String> deptCombo;
    public MFXComboBox<String> positionCombo;
    public MFXComboBox<String> qualificationCombo;
    public MFXTextField cardField;
    public MFXComboBox genderComboBox;
    public MFXDatePicker birthdayPick;
    public MFXTextField emailField;
    public MFXTextField phoneField;
    public MFXTextField addressField;
    public MFXDatePicker birthdayBegin;
    public MFXDatePicker birthdayEnd;

    DialogUtil dialogUtil = new DialogUtil();

    private List<OptionItem> genderList;   //性别选择列表数据

    private List<String> deptList;//部门列表数据
    private List<String> positionList;//专业列表数据
    private List<String> qualificationList;//班级列表数据

    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法


    //存储筛选的条件
    private TeacherEntity filerCriteria ;

    @FXML
    public void initialize(){
        //初始化各列表
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
        deptList = (List<String>)(HttpRequestUtil.getRequest("/api/teacher/dept","",null).getData());
        deptCombo.getItems().addAll(deptList);
        qualificationList = (List<String>)(HttpRequestUtil.getRequest("/api/teacher/qualification","",null).getData());
        qualificationCombo.getItems().addAll(qualificationList);
        positionList = (List<String>)(HttpRequestUtil.getRequest("/api/teacher/position","",null).getData());
        positionCombo.getItems().addAll(positionList);
    }

    /**
     * 初始化筛选界面
     * @param stage 本界面对象
     * @param filerCriteria 筛选条件，与主界面共享
     * @param refreshMethod 刷新主界面方法
     */
    public void init(Stage stage, TeacherEntity filerCriteria, Runnable refreshMethod) {
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
        if (deptCombo.getValue() != null) {
            filerCriteria.setDept(deptCombo.getValue());
        }
        if (positionCombo.getValue() != null) {
            filerCriteria.setPosition(positionCombo.getValue());
        }
        if (qualificationCombo.getValue() != null) {
            filerCriteria.setQualification(qualificationCombo.getValue());
        }
        if (cardField.getText() != null && !cardField.getText().isEmpty()) {
            filerCriteria.setCard(cardField.getText());
        }
        if (genderComboBox.getValue() != null) {
            OptionItem selectedGender = (OptionItem) genderComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setGender(selectedGender.getValue());
            filerCriteria.setGenderName(selectedGender.getLabel());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = birthdayPick.getValue();
        if (birthday != null) {
            String formattedBirthday = birthday.format(formatter);
            filerCriteria.setBirthday(formattedBirthday);
        }
        if (emailField.getText() != null && !emailField.getText().isEmpty()) {
            filerCriteria.setEmail(emailField.getText());
        }
        if (phoneField.getText() != null && !phoneField.getText().isEmpty()) {
            filerCriteria.setPhone(phoneField.getText());
        }
        if (addressField.getText() != null && !addressField.getText().isEmpty()) {
            filerCriteria.setAddress(addressField.getText());
        }
        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }

    /**
     * 初始化筛选窗口的各各输入框元素，因为需要与主界面的筛选条件一致
     * @param teacher
     */
    public void fillForm(TeacherEntity teacher) {
        if (teacher != null) {
            if (teacher.getNum() != null) {
                numField.setText(teacher.getNum());
            }
            if (teacher.getName() != null) {
                nameField.setText(teacher.getName());
            }
            if (teacher.getDept() != null) {
                deptCombo.setValue(teacher.getDept());
            }
            if (teacher.getPosition() != null) {
                positionCombo.setValue(teacher.getPosition());
                System.out.println(teacher.getPosition());
                System.out.println(positionCombo.getValue());
            }
            if (teacher.getQualification() != null) {
                qualificationCombo.setValue(teacher.getQualification());
            }
            if (teacher.getCard() != null) {
                cardField.setText(teacher.getCard());
            }
            if (teacher.getGender() != null) {
                for (OptionItem optionItem:
                        genderList) {
                    if (optionItem.getValue()==teacher.getGender()){
                        genderComboBox.setValue(optionItem.getLabel());
                    }
                }

            }
            if (teacher.getBirthday() != null) {
                birthdayPick.setValue(LocalDate.parse(teacher.getBirthday()));
            }
            if (teacher.getEmail() != null) {
                emailField.setText(teacher.getEmail());
            }
            if (teacher.getPhone() != null) {
                phoneField.setText(teacher.getPhone());
            }
            if (teacher.getAddress() != null) {
                addressField.setText(teacher.getAddress());
            }
        }
    }
}
