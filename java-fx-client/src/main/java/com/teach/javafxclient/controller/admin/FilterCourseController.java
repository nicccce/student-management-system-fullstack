package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.model.CourseEntity;
import com.teach.javafxclient.model.StudentEntity;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilterCourseController {


    public MFXTextField numField;
    public MFXTextField nameField;
    public MFXTextField departmentField;
    public MFXTextField typeField;
    public MFXTextField creditField;
    public MFXTextField locationField;
    public MFXTextField studentField;
    public MFXTextField teacherField;
    public DatePicker beginTimePick;
    public DatePicker endTimePick;
    public MFXTextField introduceField;
    DialogUtil dialogUtil = new DialogUtil();

    private Stage stage;//本界面的对象

    private Runnable refreshMethod;//刷新主界面的方法

    //存储筛选的条件
    private CourseEntity filerCriteria;
    private CourseEntity scheduleFilter = new CourseEntity();
    private StringBuilder filterStudent;
    private StringBuilder filterTeacher;

    @FXML
    public void initialize(){
        //初始化各列表
        beginTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    /**
     * 初始化筛选界面
     * @param stage 本界面对象
     * @param filerCriteria 筛选条件，与主界面共享
     * @param refreshMethod 刷新主界面方法
     */
    public void init(Stage stage, CourseEntity filerCriteria, StringBuilder filterStudent, StringBuilder filterTeacher, Runnable refreshMethod) {
        this.stage = stage;
        this.filerCriteria = filerCriteria;
        this.refreshMethod = refreshMethod;
        this.filterStudent = filterStudent;
        this.filterTeacher = filterTeacher;
        //将筛选输入的初始条件改为与主界面一致
        fillForm(this.filerCriteria);
        studentField.setText(filterStudent.toString());
        teacherField.setText(filterTeacher.toString());

        this.scheduleFilter.setSchedule(filerCriteria.getSchedule());
    }

    /**
     * 点击筛选按钮，进行筛选
     * @param actionEvent .
     */
    public void onFilterButtonClick(ActionEvent actionEvent) {
        //清空筛选条件
        filerCriteria.empty();
        filterStudent.setLength(0);
        filterTeacher.setLength(0);
        //将填写的非空筛选条件包装
        if (numField.getText() != null && !numField.getText().isEmpty()) {
            filerCriteria.setNum(numField.getText());
        }
        if (nameField.getText() != null && !nameField.getText().isEmpty()) {
            filerCriteria.setName(nameField.getText());
        }
        if (departmentField.getText() != null && !departmentField.getText().isEmpty()) {
            filerCriteria.setDepartment(departmentField.getText());
        }
        if (typeField.getText() != null && !typeField.getText().isEmpty()) {
            filerCriteria.setType(typeField.getText());
        }
        if (creditField.getText() != null && !creditField.getText().isEmpty()) {
            filerCriteria.setCredit(Integer.parseInt(creditField.getText()));
        }
        if (locationField.getText() != null && !locationField.getText().isEmpty()) {
            filerCriteria.setLocation(locationField.getText());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginTime = beginTimePick.getValue();
        LocalDate endTime = endTimePick.getValue();
        if (beginTime != null) {
            String formattedBeginTime = beginTime.format(formatter);
            filerCriteria.setBeginTime(formattedBeginTime);
        }
        if (endTime != null) {
            String formattedEndTime = endTime.format(formatter);
            filerCriteria.setBeginTime(formattedEndTime);
        }
        if (introduceField.getText() != null && !introduceField.getText().isEmpty()) {
            filerCriteria.setIntroduction(introduceField.getText());
        }
        if (studentField.getText() != null && !studentField.getText().isEmpty()) {
            String studentNumName = studentField.getText();
            filterStudent.replace(0, studentNumName.length(), studentNumName);
        }
        if (teacherField.getText() != null && !teacherField.getText().isEmpty()) {
            String teacherNumName = teacherField.getText();
            filterTeacher.replace(0, teacherNumName.length(), teacherNumName);
        }
        filerCriteria.setSchedule(scheduleFilter.getSchedule());
        //刷新主界面
        refreshMethod.run();
        //关闭筛选窗口
        stage.close();
    }

    /**
     * 初始化筛选窗口的各各输入框元素，因为需要与主界面的筛选条件一致
     * @param course
     */
    public void fillForm(CourseEntity course) {
        if (course != null) {
            if (course.getNum() != null) {
                numField.setText(course.getNum());
            }
            if (course.getName() != null) {
                nameField.setText(course.getName());
            }
            if (course.getDepartment() != null) {
                departmentField.setText(course.getDepartment());
            }
            if (course.getType() != null) {
                typeField.setText(course.getType());
            }
            if (course.getCredit() != null) {
                creditField.setText(course.getCredit().toString());
            }
            if (course.getLocation() != null) {
                locationField.setText(course.getLocation());
            }
            if (course.getBeginTime() != null) {
                beginTimePick.setValue(LocalDate.parse(course.getBeginTime()));
            }
            if (course.getEndTime() != null) {
                endTimePick.setValue(LocalDate.parse(course.getEndTime()));
            }
            if (course.getIntroduction() != null) {
                introduceField.setText(course.getIntroduction());
            }
        }
    }

    @FXML
    private void onEditScheduleButtonClick(ActionEvent actionEvent) {
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
            filerCriteria.setNum(numField.getText());
            filerCriteria.setName(nameField.getText());
            filerCriteria.setType(typeField.getText());
            controller.init(scheduleFilter, editorStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    

}
