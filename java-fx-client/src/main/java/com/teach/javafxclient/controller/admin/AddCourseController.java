package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.model.CourseEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddCourseController {

    @FXML
    private MFXTextField numField;

    @FXML
    private MFXTextField nameField;

    @FXML
    private MFXTextField departmentField;

    @FXML
    private MFXTextField typeField;

    @FXML
    private MFXTextField creditField;

    @FXML
    private MFXTextField locationField;

    @FXML
    private DatePicker beginTimePick;

    @FXML
    private DatePicker endTimePick;

    @FXML
    private MFXTextField introduceField;

    DialogUtil dialogUtil = new DialogUtil();

    private Stage stage;

    private Runnable refreshMethod;

    private CourseEntity courseEntity = new CourseEntity();

    private File studentFile,teacherFile;

    private Integer courseId;

    /**
     * 初始化添加课程界面
     */
    @FXML
    public void initialize(){
        beginTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        courseEntity.setSchedule(0L);
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
            dialogUtil.openError("添加失败", "课程号为空，不能添加！");
            return;
        }

        //将需要的课程信息包装为实体类
        courseEntity.setNum(numField.getText());
        courseEntity.setName(nameField.getText());
        courseEntity.setDepartment(departmentField.getText());
        courseEntity.setType(typeField.getText());
        courseEntity.setLocation(locationField.getText());
        courseEntity.setBeginTime(beginTimePick.getEditor().getText());
        courseEntity.setEndTime(endTimePick.getEditor().getText());
        courseEntity.setIntroduction(introduceField.getText());

        DataRequest req = new DataRequest();
        //将新课程信息包装进请求
        req.putObject("course", courseEntity);
        DataResponse res = HttpRequestUtil.request("/api/course/courseEditSave",req);
        if(res.getCode() == 0) {
            courseId = CommonMethod.getIntegerFromObject(res.getData());

            if (studentFile != null){
                DataResponse ress = HttpRequestUtil.importData("/api/course/importStudentByExcel", studentFile.getPath(), "courseId=" + courseId);
                if (ress != null) {
                    if (ress.getCode() == 0) {
                    } else {
                        dialogUtil.openError("学生信息上传失败", ress.getMsg());
                        return;
                    }
                } else {
                    dialogUtil.openError("学生信息上传失败", "学生信息上传失败，服务器无响应！");
                    return;
                }
            }

            if (teacherFile != null){
                DataResponse rest = HttpRequestUtil.importData("/api/course/importTeacherByExcel", teacherFile.getPath(), "courseId=" + courseId);
                if (rest != null) {
                    if (rest.getCode() == 0) {
                    } else {
                        dialogUtil.openError("教师信息上传失败", rest.getMsg());
                        return;
                    }
                } else {
                    dialogUtil.openError("教师信息上传失败", "教师信息上传失败，服务器无响应！");
                    return;
                }
            }

            dialogUtil.openGeneric("提交成功","提交成功！点击确认关闭窗口",stage::close);
            studentFile = null;
            teacherFile = null;
        }
        else {
            dialogUtil.openError("保存失败", res.getMsg());
        }
    }

    @FXML
    public void onEditTeacherButtonClick(ActionEvent actionEvent) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("导入教师表格");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        teacherFile = fileDialog.showOpenDialog(null);
    }

    @FXML
    public void onEditStudentButtonClick(ActionEvent actionEvent) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("导入学生表格");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        studentFile = fileDialog.showOpenDialog(null);

    }

    @FXML
    public void onEditScheduleButtonClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course-schedule-editor.fxml"));
        try {
            Parent root = fxmlLoader.load();
            CourseScheduleEditorController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage editorStage = new Stage();
            editorStage.setTitle("修改课程时间");
            editorStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            editorStage.setScene(scene);
            editorStage.show();
            courseEntity.setNum(numField.getText());
            courseEntity.setName(nameField.getText());
            courseEntity.setType(typeField.getText());
            controller.init(courseEntity, editorStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
