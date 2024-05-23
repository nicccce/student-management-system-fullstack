package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.StudentEntity;
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

public class FilterStudentController {
    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXComboBox<String> deptCombo;
    public MFXComboBox<String> majorCombo;
    public MFXComboBox<String> classNameCombo;
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

    private List<String> deptList;
    private List<String> majorList;
    private List<String> classNameList;

    private Stage stage;

    private Runnable refreshMethod;


    //存储筛选的条件
    private StudentEntity filerCriteria ;

    @FXML
    public void initialize(){
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
        deptList = (List<String>)(HttpRequestUtil.getRequest("/api/student/dept","",null).getData());
        deptCombo.getItems().addAll(deptList);
        classNameList = (List<String>)(HttpRequestUtil.getRequest("/api/student/class","",null).getData());
        classNameCombo.getItems().addAll(classNameList);
        majorList = (List<String>)(HttpRequestUtil.getRequest("/api/student/major","",null).getData());
        majorCombo.getItems().addAll(majorList);
    }

    public void init(Stage stage, StudentEntity filerCriteria, Runnable refreshMethod) {
        this.stage = stage;
        this.filerCriteria = filerCriteria;
        this.refreshMethod = refreshMethod;
        fillForm(this.filerCriteria);
    }

    public void onFilterButtonClick(ActionEvent actionEvent) {
        filerCriteria.empty();
        if (numField.getText() != null && !numField.getText().isEmpty()) {
            filerCriteria.setNum(numField.getText());
        }
        if (nameField.getText() != null && !nameField.getText().isEmpty()) {
            filerCriteria.setName(nameField.getText());
        }
        if (deptCombo.getValue() != null) {
            filerCriteria.setDept(deptCombo.getValue());
        }
        if (majorCombo.getValue() != null) {
            filerCriteria.setMajor(majorCombo.getValue());
        }
        if (classNameCombo.getValue() != null) {
            filerCriteria.setClassName(classNameCombo.getValue());
        }
        if (cardField.getText() != null && !cardField.getText().isEmpty()) {
            filerCriteria.setCard(cardField.getText());
        }
        if (genderComboBox.getValue() != null) {
            OptionItem selectedGender = (OptionItem) genderComboBox.getSelectionModel().getSelectedItem();
            filerCriteria.setGender(selectedGender.getValue());
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
        refreshMethod.run();
        stage.close();
    }

    public void fillForm(StudentEntity student) {
        if (student != null) {
            if (student.getNum() != null) {
                numField.setText(student.getNum());
            }
            if (student.getName() != null) {
                nameField.setText(student.getName());
            }
            if (student.getDept() != null) {
                deptCombo.setValue(student.getDept());
            }
            if (student.getMajor() != null) {
                majorCombo.setValue(student.getMajor());
                System.out.println(student.getMajor());
                System.out.println(majorCombo.getValue());
            }
            if (student.getClassName() != null) {
                classNameCombo.setValue(student.getClassName());
            }
            if (student.getCard() != null) {
                cardField.setText(student.getCard());
            }
            if (student.getGender() != null) {
                genderComboBox.setValue(student.getGender());
            }
            if (student.getBirthday() != null) {
                birthdayPick.setValue(LocalDate.parse(student.getBirthday()));
            }
            if (student.getEmail() != null) {
                emailField.setText(student.getEmail());
            }
            if (student.getPhone() != null) {
                phoneField.setText(student.getPhone());
            }
            if (student.getAddress() != null) {
                addressField.setText(student.getAddress());
            }
        }
    }
}
