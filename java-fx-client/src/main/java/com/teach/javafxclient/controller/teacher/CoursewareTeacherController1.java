package com.teach.javafxclient.controller.teacher;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.CoursewareEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CoursewareTeacherController1 {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public Label filterLabel;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public MFXButton getExcelButton;
    public MFXButton importExcelButton;
    public TableColumn fileNameColumn;
    public MFXTextField nameField;
    public MFXComboBox courseCombox;
    @FXML
    private TableView<CoursewareEntity> dataTableView;  //学生信息表
    @FXML
    public TableColumn<CoursewareEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<CoursewareEntity,String> courseNumColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<CoursewareEntity,String> courseNameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<CoursewareEntity,String> fileTypeColumn; //学生信息表 专业列
    @FXML
    private TableColumn<CoursewareEntity,String> importTimeColumn; //学生信息表 出生日期列
    @FXML
    private TextField courseNumField; //学生信息  学号输入域
    @FXML
    private TextField courseNameField;  //学生信息  名称输入域
    @FXML
    private TextField fileCourseNameField; //学生信息  院系输入域
    @FXML
    private TextField fileTypeField; //学生信息  专业输入域
    @FXML
    private TextField classCourseNameField; //学生信息  班级输入域
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker importTimePick;  //学生信息  出生日期选择域
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域

    @FXML
    private TextField courseNumCourseNameTextField;  //查询 姓名学号输入域

    private Integer coursewareId = null;  //当前编辑修改的学生的主键

    private ArrayList<CoursewareEntity> coursewareList = new ArrayList<CoursewareEntity>();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<CoursewareEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<CoursewareEntity> selectedItemList = null;

    private HttpRequestUtil<CoursewareEntity> httpRequestUtil = new HttpRequestUtil<>(CoursewareEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    //存储筛选的条件
    private CoursewareEntity filterCriteria = new CoursewareEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
    }


    public void onAddButtonClick(ActionEvent actionEvent) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("课件上传");
        fileDialog.setInitialDirectory(new File("C:/"));
        File file = fileDialog.showOpenDialog(null);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);


        // 原始文件名
        String originalFileName = file.getName();

        // 获取文件扩展名
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            fileExtension = originalFileName.substring(dotIndex);
        }

        // 构建新的文件名
        String newFileName = String.format("%s_%s", originalFileName, formattedDate);

        DataResponse res =HttpRequestUtil.uploadFile(file.getPath(),"courseware/" + newFileName);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void onDeleteAllButtonClick(ActionEvent actionEvent) {
    }

    public void onQueryButtonClick(ActionEvent actionEvent) {
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
    }
}
