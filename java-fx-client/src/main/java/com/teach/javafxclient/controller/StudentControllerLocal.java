package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.Student;
import com.teach.javafxclient.request.*;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的本地业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 *  具体每个功能的的实现 与StudentController 类似， 参考 StudentController中的说明
 */
public class StudentControllerLocal extends ToolController {
    @FXML
    private TableView<Student> dataTableView;
    @FXML
    private TableColumn<Student,String> numColumn;
    @FXML
    private TableColumn<Student,String> nameColumn;
    @FXML
    private TableColumn<Student,String> deptColumn;
    @FXML
    private TableColumn<Student,String> majorColumn;
    @FXML
    private TableColumn<Student,String> classNameColumn;
    @FXML
    private TableColumn<Student,String> cardColumn;
    @FXML
    private TableColumn<Student,String> genderColumn;
    @FXML
    private TableColumn<Student,String> birthdayColumn;
    @FXML
    private TableColumn<Student,String> emailColumn;
    @FXML
    private TableColumn<Student,String> phoneColumn;
    @FXML
    private TableColumn<Student,String> addressColumn;

    @FXML
    private TextField numField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField deptField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField classNameField;
    @FXML
    private TextField cardField;
    @FXML
    private ComboBox<OptionItem> genderComboBox;
    @FXML
    private DatePicker birthdayPick;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    @FXML
    private TextField numNameTextField;

    private Integer studentId = null;
    private Integer personId = null;

    private List<Student> studentList = new ArrayList();
    private List<OptionItem> genderList;



    private void setTableViewData() {
        dataTableView.getItems().clear();
        for (int j = 0; j < studentList.size(); j++) {
            dataTableView.getItems().add(studentList.get(j));
        }
    }

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<>("dept"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        cardColumn.setCellValueFactory(new PropertyValueFactory<>("card"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("genderName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableView.TableViewSelectionModel<Student> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        studentList = SQLiteJDBC.getInstance().getStudentList("");
        setTableViewData();
        genderList = SQLiteJDBC.getInstance().getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }
    protected void changeStudentInfo() {
        Student form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        studentId = form.getStudentId();
        form = SQLiteJDBC.getInstance().getStudentById(studentId);
        personId = form.getPersonId();
        numField.setText(form.getNum());
        nameField.setText(form.getName());
        deptField.setText(form.getDept());
        majorField.setText(form.getMajor());
        classNameField.setText(form.getClassName());
        cardField.setText(form.getCard());
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, form.getGender()));
        birthdayPick.getEditor().setText(form.getBirthday());
        emailField.setText(form.getEmail());
        phoneField.setText(form.getPhone());
        addressField.setText(form.getAddress());
    }
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }
    public void clearPanel(){
        studentId = null;
        personId = null;
        numField.setText("");
        nameField.setText("");
        deptField.setText("");
        majorField.setText("");
        classNameField.setText("");
        cardField.setText("");
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        studentList = SQLiteJDBC.getInstance().getStudentList(numName);
        setTableViewData();
    }
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }
    @FXML
    protected void onDeleteButtonClick() {
        Student form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，删除！");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_OK) {
            return;
        }
        studentId = form.getStudentId();
        personId = form.getPersonId();
        String msg =SQLiteJDBC.getInstance().studentDelete(studentId,personId);
        if(msg == null) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(msg);
        }
    }
    @FXML
    protected void onSaveButtonClick() {
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Student s = new Student();
        s.setIntroduce("");
        s.setNum(numField.getText());
        s.setName(nameField.getText());
        s.setDept(deptField.getText());
        s.setMajor(majorField.getText());
        s.setClassName(classNameField.getText());
        s.setCard(cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            s.setGender(genderComboBox.getSelectionModel().getSelectedItem().getValue());
        s.setBirthday(birthdayPick.getEditor().getText());
        s.setEmail(emailField.getText());
        s.setPhone(phoneField.getText());
        s.setAddress(addressField.getText());
        Object res = SQLiteJDBC.getInstance().studentEditSave(s,studentId,personId);
        if(res instanceof String) {
            MessageDialog.showDialog(res.toString());
        }
        else {
            Integer[] oa = (Integer [])res;
            studentId = oa[0];
            personId = oa[1];
            MessageDialog.showDialog("提交成功！");
        }
    }
}
