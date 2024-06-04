package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.StudentEntity;
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

public class AddStudentController {

    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField deptField;
    public MFXTextField majorField;
    public MFXTextField classNameField;
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

    /**
     * 初始化添加学生界面
     */
    @FXML
    public void initialize(){
        //初始化性别栏
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
    }

    /**
     * 获取自己的页面对象
     * @param stage 自己的界面对象
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * 点击添加按钮后执行操作
     * @param actionEvent .
     */
    public void onAddButtonClick(ActionEvent actionEvent) {
        //若无学号则弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("添加失败", "学号为空，不能添加！");
            return;
        }
        if( !phoneField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "电话格式不正确，不能添加！");
            return;
        }

        //将需要的学生信息包装为实体类
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setNum(numField.getText());
        studentEntity.setName(nameField.getText());
        studentEntity.setDept(deptField.getText());
        studentEntity.setMajor(majorField.getText());
        studentEntity.setClassName(classNameField.getText());
        studentEntity.setCard(cardField.getText());


        if (genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null) {
            OptionItem selectedGender = (OptionItem) genderComboBox.getSelectionModel().getSelectedItem();
            studentEntity.setGender(selectedGender.getValue());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = birthdayPick.getValue();
        if (birthday != null) {
            String formattedBirthday = birthday.format(formatter);
            studentEntity.setBirthday(formattedBirthday);
        }
        if (!LocalDateUtil.isBeforeToday(studentEntity.getBirthday())){
            dialogUtil.openError("保存失败","出生日期无效！");
            return;
        }
        studentEntity.setEmail(emailField.getText());
        studentEntity.setPhone(phoneField.getText());
        studentEntity.setAddress(addressField.getText());

        /*Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender",((OptionItem)(genderComboBox.getSelectionModel().getSelectedItem())).getValue());

        // 定义日期格式为 "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        form.put("birthday",birthdayPick.getValue().format(formatter));
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());*/
        DataRequest req = new DataRequest();
        req.putObject("newStudent", studentEntity);
        DataResponse res = HttpRequestUtil.request("/api/student/studentInsert",req);
        if (res != null) {
            if (res.getCode() == 0) {
                //弹窗询问是否继续，若继续则设置isContinue为真
                dialogUtil.openGeneric("添加成功", "添加成功!点击确认继续添加。", this::continueAdding);
                //refreshMethod.run();
                if (isContinue) {//
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

    /**
     * 设置是否继续添加
     */
    private void continueAdding(){
        isContinue = true;
    }
}
