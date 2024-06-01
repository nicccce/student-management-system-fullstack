package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.TeacherEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CourseTeacherViewerController {
    @FXML
    private MFXPaginatedTableView teacherTable;

    DialogUtil dialogUtil = new DialogUtil();

    HttpRequestUtil<TeacherEntity> httpRequestUtil = new HttpRequestUtil<TeacherEntity>(TeacherEntity.class);

    Integer courseId;

    List<TeacherEntity> teacherList = new ArrayList<>(){};

    private ObservableList<TeacherEntity> observableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

    }

    public void init(Integer courseId){
        this.courseId = courseId;
        setupTeacherTable();

        teacherTable.autosizeColumnsOnInitialization();
        teacherTable.setRowsPerPage(17);

        When.onChanged(teacherTable.currentPageProperty())
                .then((oldValue, newValue) -> teacherTable.autosizeColumns())
                .listen();
    }

    private void setupTeacherTable() {
        MFXTableColumn<TeacherEntity> numColumn = new MFXTableColumn<>("学号", false, Comparator.comparing(TeacherEntity::getNum));
        MFXTableColumn<TeacherEntity> nameColumn = new MFXTableColumn<>("姓名", false, Comparator.comparing(TeacherEntity::getName));
        MFXTableColumn<TeacherEntity> deptColumn = new MFXTableColumn<>("学院", false, Comparator.comparing(TeacherEntity::getDept));
        MFXTableColumn<TeacherEntity> majorColumn = new MFXTableColumn<>("职位", false, Comparator.comparing(TeacherEntity::getPosition));
        MFXTableColumn<TeacherEntity> classNameColumn = new MFXTableColumn<>("学历", false, Comparator.comparing(TeacherEntity::getQualification));
        MFXTableColumn<TeacherEntity> genderNameColumn = new MFXTableColumn<>("性别", false, Comparator.comparing(TeacherEntity::getGenderName));
        MFXTableColumn<TeacherEntity> birthdayColumn = new MFXTableColumn<>("生日", false, Comparator.comparing(TeacherEntity::getBirthday));
        MFXTableColumn<TeacherEntity> emailColumn = new MFXTableColumn<>("邮箱", false, Comparator.comparing(TeacherEntity::getEmail));
        MFXTableColumn<TeacherEntity> phoneColumn = new MFXTableColumn<>("电话", false, Comparator.comparing(TeacherEntity::getPhone));
        numColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getNum));
        nameColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getName));
        deptColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getDept));
        majorColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getPosition));
        classNameColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getQualification));
        genderNameColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getGenderName));
        birthdayColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getBirthday));
        emailColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getEmail));
        phoneColumn.setRowCellFactory(teacherEntity -> new MFXTableRowCell<>(TeacherEntity::getPhone));

        teacherTable.getTableColumns().addAll(numColumn, nameColumn, deptColumn, majorColumn, classNameColumn, genderNameColumn, birthdayColumn, emailColumn, phoneColumn);
        teacherTable.getFilters().addAll(
                new StringFilter<>("学号", TeacherEntity::getNum),
                new StringFilter<>("姓名", TeacherEntity::getName),
                new StringFilter<>("学院", TeacherEntity::getDept),
                new StringFilter<>("专业", TeacherEntity::getPosition),
                new StringFilter<>("班级", TeacherEntity::getQualification),
                new StringFilter<>("性别", TeacherEntity::getGenderName),
                new StringFilter<>("生日", TeacherEntity::getBirthday),
                new StringFilter<>("邮箱", TeacherEntity::getEmail),
                new StringFilter<>("电话", TeacherEntity::getPhone)
        );
        DataResponse<ArrayList<TeacherEntity>> res;
        DataRequest req =new DataRequest();
        res = httpRequestUtil.requestArrayList("/api/course/getTeacher/"+courseId,req); //从后台获取所有课程信息列表集合
        if(res != null) {
            if (res.getCode()== 0) {
                teacherList = res.getData();
            }else {
                dialogUtil.openError("加载失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("加载失败", "教师数据加载失败，请重试！");
        }
        observableList.addAll(FXCollections.<TeacherEntity>observableArrayList(teacherList));
        teacherTable.setItems(observableList);
    }

    public void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = teacherList.size();
            dialogUtil.openInfo("导出教师信息", "点击确认导出选课的共 "+selectedNumber+" 条教师信息。", this::getExcel);
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedTeacher",teacherList);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/teacher/getSelectedTeacherListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("courseTeacher.xlsx");
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
