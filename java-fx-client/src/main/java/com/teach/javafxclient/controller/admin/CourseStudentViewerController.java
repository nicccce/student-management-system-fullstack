package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.CourseEntity;
import com.teach.javafxclient.model.StudentEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.EnumFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CourseStudentViewerController {
    @FXML
    private MFXPaginatedTableView studentTable;

    DialogUtil dialogUtil = new DialogUtil();

    HttpRequestUtil<StudentEntity> httpRequestUtil = new HttpRequestUtil<StudentEntity>(StudentEntity.class);

    Integer courseId;

    List<StudentEntity> studentList = new ArrayList<>(){};

    private ObservableList<StudentEntity> observableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

    }

    public void init(Integer courseId){
        this.courseId = courseId;
        setupStudentTable();

        studentTable.autosizeColumnsOnInitialization();
        studentTable.setRowsPerPage(17);

        When.onChanged(studentTable.currentPageProperty())
                .then((oldValue, newValue) -> studentTable.autosizeColumns())
                .listen();
    }

    private void setupStudentTable() {
        MFXTableColumn<StudentEntity> numColumn = new MFXTableColumn<>("学号", false, Comparator.comparing(StudentEntity::getNum));
        MFXTableColumn<StudentEntity> nameColumn = new MFXTableColumn<>("姓名", false, Comparator.comparing(StudentEntity::getName));
        MFXTableColumn<StudentEntity> deptColumn = new MFXTableColumn<>("学院", false, Comparator.comparing(StudentEntity::getDept));
        MFXTableColumn<StudentEntity> majorColumn = new MFXTableColumn<>("专业", false, Comparator.comparing(StudentEntity::getMajor));
        MFXTableColumn<StudentEntity> classNameColumn = new MFXTableColumn<>("班级", false, Comparator.comparing(StudentEntity::getClassName));
        MFXTableColumn<StudentEntity> genderNameColumn = new MFXTableColumn<>("性别", false, Comparator.comparing(StudentEntity::getGenderName));
        MFXTableColumn<StudentEntity> birthdayColumn = new MFXTableColumn<>("生日", false, Comparator.comparing(StudentEntity::getBirthday));
        MFXTableColumn<StudentEntity> emailColumn = new MFXTableColumn<>("邮箱", false, Comparator.comparing(StudentEntity::getEmail));
        MFXTableColumn<StudentEntity> phoneColumn = new MFXTableColumn<>("电话", false, Comparator.comparing(StudentEntity::getPhone));
        numColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getNum));
        nameColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getName));
        deptColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getDept));
        majorColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getMajor));
        classNameColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getClassName));
        genderNameColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getGenderName));
        birthdayColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getBirthday));
        emailColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getEmail));
        phoneColumn.setRowCellFactory(studentEntity -> new MFXTableRowCell<>(StudentEntity::getPhone));

        studentTable.getTableColumns().addAll(numColumn, nameColumn, deptColumn, majorColumn, classNameColumn, genderNameColumn, birthdayColumn, emailColumn, phoneColumn);
        studentTable.getFilters().addAll(
                new StringFilter<>("学号", StudentEntity::getNum),
                new StringFilter<>("姓名", StudentEntity::getName),
                new StringFilter<>("学院", StudentEntity::getDept),
                new StringFilter<>("专业", StudentEntity::getMajor),
                new StringFilter<>("班级", StudentEntity::getClassName),
                new StringFilter<>("性别", StudentEntity::getGenderName),
                new StringFilter<>("生日", StudentEntity::getBirthday),
                new StringFilter<>("邮箱", StudentEntity::getEmail),
                new StringFilter<>("电话", StudentEntity::getPhone)
        );
        DataResponse<ArrayList<StudentEntity>> res;
        DataRequest req =new DataRequest();
        res = httpRequestUtil.requestArrayList("/api/course/getStudent/"+courseId,req); //从后台获取所有课程信息列表集合
        if(res != null) {
            if (res.getCode()== 0) {
                studentList = res.getData();
            }else {
                dialogUtil.openError("加载失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("加载失败", "学生数据加载失败，请重试！");
        }
        observableList.addAll(FXCollections.<StudentEntity>observableArrayList(studentList));
        studentTable.setItems(observableList);
    }

    public void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = studentList.size();
            dialogUtil.openInfo("导出学生信息", "点击确认导出选课的共 "+selectedNumber+" 条学生信息。", this::getExcel);
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedStudent",studentList);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getSelectedStudentListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("courseStudent.xlsx");
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
