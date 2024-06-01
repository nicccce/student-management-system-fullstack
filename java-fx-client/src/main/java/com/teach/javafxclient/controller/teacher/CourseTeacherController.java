package com.teach.javafxclient.controller.teacher;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.*;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.CourseEntity;
import com.teach.javafxclient.model.TeacherEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CourseTeacherController {

    public MFXProgressBar determinateBar;
    public Label progressLable;
    @FXML
    private MFXButton addButton;

    @FXML
    private MFXButton deleteButton;

    @FXML
    private MFXButton getExcelButton;

    @FXML
    private FlowPane filterPane;

    @FXML
    private Label filterLabel;

    @FXML
    private MFXButton changeFilterButton;

    @FXML
    private MFXButton resetFilterButton;

    @FXML
    private MFXButton addFilterButton;

    @FXML
    private MFXTextField numNameTextField;

    @FXML
    private TableView<CourseEntity> dataTableView;

    @FXML
    private TableColumn<CourseEntity,Boolean> checkColumn;

    @FXML
    private TableColumn<CourseEntity,String> numColumn;

    @FXML
    private TableColumn<CourseEntity,String> nameColumn;

    @FXML
    private TableColumn<CourseEntity,String> departmentColumn;

    @FXML
    private TableColumn<CourseEntity,String> teacherColumn;

    @FXML
    private TableColumn<CourseEntity,String> studentColumn;

    @FXML
    private TableColumn<CourseEntity,String> typeColumn;

    @FXML
    private TableColumn<CourseEntity,Integer> creditColumn;

    @FXML
    private TableColumn<CourseEntity,String> scheduleColumn;

    @FXML
    private TableColumn<CourseEntity,String> beginTimeColumn;

    @FXML
    private TableColumn<CourseEntity,String> endTimeColumn;

    @FXML
    private TableColumn<CourseEntity,String> locationColumn;

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

    private Integer courseId = null;

    private HttpRequestUtil<CourseEntity> httpRequestUtil = new HttpRequestUtil<>(CourseEntity.class);

    private List<CourseEntity> courseList = new ArrayList<CourseEntity>(){};

    private ObservableList<CourseEntity> observableList = FXCollections.observableArrayList();

    private List<CourseEntity> selectedItemList = null;

    private final DialogUtil dialogUtil = new DialogUtil();

    private CourseEntity filterCriteria = new CourseEntity();
    private StringBuilder filterStudent = new StringBuilder("");
    private StringBuilder filterTeacher = new StringBuilder("");


    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<CourseEntity>> res;
        DataRequest req =new DataRequest();
        req.putObject("filterCriteria",filterCriteria);
        res = httpRequestUtil.requestArrayList("/api/course/getTeacherCourseListByFilter/?filterStudent="+filterStudent+"&filterTeacher="+filterTeacher,req); //从后台获取所有课程信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = res.getData();
        }

        setUpCourseList();

        //设置表的列属性和表属性
        setupTable();

        beginTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

        // 创建一个TextFormatter，使用IntegerStringConverter将文本值转换为整数
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());

        // 添加一个过滤器，仅允许输入数字字符
        creditField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                creditField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // 将TextFormatter应用于creditField
        creditField.setTextFormatter(formatter);

        //初始化筛选器及其按钮
        resetFilter();
    }

    private  void setUpCourseList(){
        HttpRequestUtil<TeacherEntity> teacherEntityHttpRequestUtil= new HttpRequestUtil<TeacherEntity>(TeacherEntity.class);

        for (int c = 0;c < courseList.size(); c++) {
            CourseEntity course = courseList.get(c);
            if (course.getSchedule()!=0L){
                course.setScheduleString(getScheduleString(course.getSchedule()));
            }
            DataResponse<ArrayList<TeacherEntity>> rest;
            DataRequest reqt =new DataRequest();
            rest = teacherEntityHttpRequestUtil.requestArrayList("/api/course/getTeacher/"+course.getCourseId(),reqt); //从后台获取所有课程信息列表集合
            if(rest == null || rest.getCode()== 1) {
                continue;
            }
            List<TeacherEntity> teacherList = rest.getData();
            StringBuilder teacherStringBuilder = new StringBuilder();
            for (int i = 0; i < teacherList.size(); i++) {
                teacherStringBuilder.append(teacherList.get(i).getName());
                if (i < teacherList.size() - 1) {
                    teacherStringBuilder.append(",");
                }
            }
            course.setTeachers(teacherStringBuilder.toString());
        }
    }

    private String getScheduleString(Long schedule){
        StringBuilder scheduleString = new StringBuilder();
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                if ((schedule & 1) ==1){
                    scheduleString.append(convertNumberToWeekday(i)+ " 第" + (2*j-1) + "-" + (2*j) + "节，");
                }
                schedule >>= 1;
            }
        }
        if (!scheduleString.isEmpty()){
            scheduleString.deleteCharAt(scheduleString.length() - 1);
        }
        return scheduleString.toString();
    }

    public String convertNumberToWeekday(int number) {
        String weekday = "";
        switch (number) {
            case 1:
                weekday = "星期一";
                break;
            case 2:
                weekday = "星期二";
                break;
            case 3:
                weekday = "星期三";
                break;
            case 4:
                weekday = "星期四";
                break;
            case 5:
                weekday = "星期五";
                break;
            case 6:
                weekday = "星期六";
                break;
            case 7:
                weekday = "星期日";
                break;
            default:
                weekday = "UNDEFINED";
                break;
        }
        return weekday;
    }

    /**
     * 将课程数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<CourseEntity>observableArrayList(courseList));
        dataTableView.setItems(observableList);
    }

    /**
     * 初始化表格列元素属性
     */
    private void setupTable(){

        var selectAll = new CheckBox();

        //创建复选框列的全选按钮并放入column中
        checkColumn.setGraphic(selectAll);
        checkColumn.setSortable(false);//复选框列不可点击列顶排序
        //设置列元素为课程对象的checkColumn成员
        checkColumn.setCellValueFactory(c -> c.getValue().selectProperty());
/*        checkColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CourseTableEntity, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<CourseTableEntity, Boolean> param) {
                CourseTableEntity courseTableEntity = param.getValue();
                Boolean value = courseTableEntity.getSelect();
                SimpleBooleanProperty observableValue = new SimpleBooleanProperty(value);
                return observableValue;
            }
        });*/
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        //初始化表中的其他数据列
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teachers"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleString"));
        beginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("beginTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        //添加选择行的监听器，用于更新右边栏
        TableView.TableViewSelectionModel<CourseEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        //检查鼠标点击行元素时增加监听器选择或取消该行的复选框
        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    CourseEntity form = dataTableView.getSelectionModel().getSelectedItem();
                    if(form != null) {
                        form.setSelect(!form.isSelect());
                    }
                }
            }
        });

        //设置表格的属性
        dataTableView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
        Styles.toggleStyleClass(dataTableView, Styles.BORDERED);//显示表格轮廓
        Styles.toggleStyleClass(dataTableView, Styles.STRIPED);//每行呈斑马交错颜色

    }

    /**
     * 辅助方法：检查节点是否是有效行或单元格
     */
    private boolean isRowOrCell(Node node) {
        if (node instanceof TableRow || node instanceof TableCell || node instanceof Text) {
            return true;
        }
        if (node.getParent() != null) {
            return isRowOrCell(node.getParent());
        }
        return false;
    }

    /**
     * 清除课程表单中输入信息
     */
    private void clearPanel() {
        courseId = null;
        numField.clear();
        nameField.clear();
        departmentField.clear();
        typeField.clear();
        creditField.clear();
        locationField.clear();
        beginTimePick.setValue(null);
        endTimePick.setValue(null);
    }

    /**
     * 用于点击表格数据时，更新右边栏的内容
     */
    protected void changeCourseInfo() {
        CourseEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        courseId = form.getCourseId();
        DataRequest req = new DataRequest();
        req.put("courseId",courseId);
        DataResponse<CourseEntity> res = httpRequestUtil.requestObject("/api/course/getCourseInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();
        numField.setText(form.getNum());
        nameField.setText(form.getName());
        departmentField.setText(form.getDepartment());
        typeField.setText(form.getType());
        creditField.setText(form.getCredit().toString());
        locationField.setText(form.getLocation());
        beginTimePick.getEditor().setText(form.getBeginTime());
        endTimePick.getEditor().setText(form.getEndTime());
        introduceField.setText(form.getIntroduction());
    }

    /**
     * 点击课程列表的某一行，根据courseId ,从后台查询课程的基本信息，切换课程的编辑信息
     */
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeCourseInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的课程在课程列表中显示
     * 也是刷新界面的方法
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<CourseEntity>> res;
        //将筛选对象包装进请求
        req.putObject("filterCriteria",filterCriteria);
        res = httpRequestUtil.requestArrayList("/api/course/getTeacherCourseListByFilter/" + numName+"?filterStudent="+filterStudent+"&filterTeacher="+filterTeacher,req);
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()||!filterStudent.isEmpty()||filterTeacher.isEmpty()){
            //因为有筛选条件，修改一下筛选按钮
            hasFilter();
        }else {
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            courseList = res.getData();
            setUpCourseList();
            setTableViewData();
        }

    }

    /**
     *  添加新课程， 清空输入信息， 输入相关信息，点击保存即可添加新的课程
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-course.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddCourseController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加课程信息");
            addStage.getIcons().add(MainApplication.icon);

            // 通过控制器的 setStage 方法传递 Stage 对象
            controller.setStage(addStage);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            // 添加关闭事件处理程序，用来关闭时自动刷新
            addStage.setOnHiding(event -> {
                onQueryButtonClick(); // 在关闭事件中调用 onQueryButtonClick() 方法
            });

            addStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击删除按钮 删除当前编辑的课程的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        getSelectedItem();//更新复选框选中的课程数据
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("删除失败", "当前未选择任何元素，无法删除！");
            return;
        }
        //弹窗警告，若点击警告的“确定”，再继续进行删除操作
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个课程的所有信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);
    }

    /**
     * 删除复选框选中的课程对象
     */
    public void deleteSelectedItems(){
        ArrayList<Integer> courseIdList = new ArrayList<Integer>();
        //提取复选框选中的课程的id，包装成列表传后端
        for (CourseEntity item:
                selectedItemList) {
            courseIdList.add(item.getCourseId());
        }
        DataRequest req = new DataRequest();
        req.put("courseId", courseIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/course/courseDeleteAll",req);
        if (res != null){
            if (res.getCode() == 0) {
                dialogUtil.openGeneric("删除成功", "删除成功！");
                onQueryButtonClick();
            } else {
                dialogUtil.openError("删除失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("删除失败", "后台无响应，请稍后再尝试。");
        }
    }

    /**
     * 点击保存按钮，保存当前编辑的课程信息，如果是新添加的课程，后台添加课程
     */
    @FXML
    protected void onSaveButtonClick() {
        //弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("修改失败", "学号为空，不能修改！");
            return;
        }
        //将右边栏的输入内容包装为课程信息对象

        CourseEntity courseEntity = dataTableView.getSelectionModel().getSelectedItem();
        try {
            Integer credit = Integer.parseInt(creditField.getText());
            if (credit < 0 ){
                dialogUtil.openError("保存错误", "保存错误，输入的不是有效的学分！");
                return;
            }
            courseEntity.setCredit(credit);
        } catch (NumberFormatException e) {
            dialogUtil.openError("保存错误", "保存错误，输入的不是有效的学分！");
            return;
        }
        courseEntity.setNum(numField.getText());
        courseEntity.setName(nameField.getText());
        courseEntity.setDepartment(departmentField.getText());
        courseEntity.setType(typeField.getText());
        courseEntity.setLocation(locationField.getText());
        courseEntity.setBeginTime(beginTimePick.getEditor().getText());
        courseEntity.setEndTime(endTimePick.getEditor().getText());
        courseEntity.setIntroduction(introduceField.getText());

        courseEntity.setCourseId(courseId);

        DataRequest req = new DataRequest();
        //将新课程信息包装进请求
        req.putObject("course", courseEntity);
        DataResponse res = HttpRequestUtil.request("/api/course/courseEditSave",req);
        if(res.getCode() == 0) {
            courseId = CommonMethod.getIntegerFromObject(res.getData());
            dialogUtil.openGeneric("提交成功","提交成功！",null);
            onQueryButtonClick();
            // MessageDialog.showDialog("提交成功！");
        }
        else {
            dialogUtil.openError("保存失败", res.getMsg());
        }
    }

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对课程的增，删，改操作
     */
    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }

    /**
     * 获取复选框选中的列表对象
     * @return 复选框选中的对象列表
     */
    private List<CourseEntity> getSelectedItem(){
        List<CourseEntity> selectedItems = new ArrayList<CourseEntity>();
        for (CourseEntity items :
                observableList) {
            if (items.isSelect()){
                if (selectedItems != null) {
                    selectedItems.add(items);
                }
            }
        }
        selectedItemList = selectedItems;
        return  selectedItems;
    }

    /**
     * 重置筛选条件并修改按钮
     */
    private void resetFilter() {
        addFilterButton.setVisible(true);
        addFilterButton.setManaged(true);
        changeFilterButton.setManaged(false);
        changeFilterButton.setVisible(false);
        resetFilterButton.setVisible(false);
        resetFilterButton.setManaged(false); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前无筛选条件");
        filterCriteria.empty();//清空筛选条件
        filterStudent = new StringBuilder("");
        filterTeacher = new StringBuilder("");
    }

    /**
     * 检测到存在筛选条件时，修改筛选按钮
     */
    private void hasFilter(){
        addFilterButton.setVisible(false);
        addFilterButton.setManaged(false);
        changeFilterButton.setManaged(true);
        changeFilterButton.setVisible(true);
        resetFilterButton.setVisible(true);
        resetFilterButton.setManaged(true); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前已设置筛选条件");
    }

    /**
     * 添加或修改筛选
     */
    private void setFilter(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("filter-course.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterCourseController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage filterStage = new Stage();
            filterStage.setTitle("筛选条件");
            filterStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            filterStage.setScene(scene);
            filterStage.show();
            // 初始化筛选控制器所需要的值，并把筛选条件的指针传进去，使在弹出页面更改的会自动同步到这个页面
            controller.init(filterStage, filterCriteria, filterStudent, filterTeacher,this::onQueryButtonClick);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击添加筛选按钮
     * @param actionEvent .
     */
    @FXML
    private void onAddFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    /**
     * 点击修改筛选按钮
     * @param actionEvent .
     */
    @FXML
    private void onChangeFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    /**
     * 点击清除筛选按钮
     * @param actionEvent .
     */
    @FXML
    private void onResetFilterButtonClicked(ActionEvent actionEvent) {
        resetFilter();
        onQueryButtonClick();
    }

    @FXML
    private void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = getSelectedItem().size();
        if (selectedNumber == 0){
            dialogUtil.openError("导出失败", "未选中任何课程信息");
        }else {
            dialogUtil.openInfo("导出课程信息", "点击确认导出选中的 "+selectedNumber+" 条课程信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedCourse",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/course/getSelectedCourseListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("course.xlsx");
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

    @FXML
    private void onAddButtonClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course-add.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddCourseController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加课程信息");
            addStage.getIcons().add(MainApplication.icon);

            // 通过控制器的 setStage 方法传递 Stage 对象
            controller.setStage(addStage);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            // 添加关闭事件处理程序，用来关闭时自动刷新
            addStage.setOnHiding(event -> {
                onQueryButtonClick(); // 在关闭事件中调用 onQueryButtonClick() 方法
            });

            addStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onEditTeacherButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法导入", "请先选择需要导入授课教师的课程！");
            return;
        }
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("导入教师表格");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        if (file != null){
            DataResponse res = HttpRequestUtil.importData("/api/course/importTeacherByExcel", file.getPath(), "courseId=" + course.getCourseId());
            if (res != null) {
                if (res.getCode() == 0) {
                    dialogUtil.openGeneric("上传成功", "上传成功！");
                } else {
                    dialogUtil.openError("上传失败", res.getMsg());
                }
            } else {
                dialogUtil.openError("上传失败", "上传失败");
            }
            onQueryButtonClick();
        }
    }

    @FXML
    private void onCheckTeacherButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法查看", "请先选择需要查看的课程！");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course-teacher-viewer.fxml"));
        try {
            Parent root = fxmlLoader.load();
            CourseTeacherViewerController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("查看授课教师信息");
            addStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            addStage.show();

            controller.init(dataTableView.getSelectionModel().getSelectedItem().getCourseId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onEditStudentButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法导入", "请先选择需要导入学生的课程！");
            return;
        }
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("导入学生表格");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        if (file != null){
            DataResponse res = HttpRequestUtil.importData("/api/course/importStudentByExcel", file.getPath(), "courseId=" + course.getCourseId());
            if (res != null) {
                if (res.getCode() == 0) {
                    dialogUtil.openGeneric("上传成功", "上传成功！");
                } else {
                    dialogUtil.openError("上传失败", res.getMsg());
                }
            } else {
                dialogUtil.openError("上传失败", "上传失败");
            }
            onQueryButtonClick();
        }
    }


    @FXML
    private void onCheckStudentButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法查看", "请先选择需要查看的课程！");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course-student-viewer.fxml"));
        try {
            Parent root = fxmlLoader.load();
            CourseStudentViewerController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("查看学生信息");
            addStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            addStage.show();

            controller.init(dataTableView.getSelectionModel().getSelectedItem().getCourseId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void onEditScheduleButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法修改", "请先选择需要修改的课程！");
            return;
        }

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
            controller.init(course, editorStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCheckScheduleButtonClick(ActionEvent actionEvent) {
        CourseEntity course = dataTableView.getSelectionModel().getSelectedItem();
        if (course == null){
            dialogUtil.openError("无法查看", "请先选择需要查看的课程！");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("course-schedule-viewer.fxml"));
        try {
            Parent root = fxmlLoader.load();
            CourseScheduleViewerController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage editorStage = new Stage();
            editorStage.setTitle("查看课程时间");
            editorStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            editorStage.setScene(scene);
            editorStage.show();
            controller.init(course, editorStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long checkSchedule (List<Long> scheduleList){
        long a = 0L,result = 0L;
        for (long schedule :
                scheduleList) {
            a |= schedule;
        }
        for (long schedule :
                scheduleList) {
            long ans = solve(a,schedule);
            result|=ans;
            a-=schedule-ans;
        }
        return result;
    }

    private long solve (long a, long b){
        Long ans = 0L;
        for (int i = 0; i<35 ; i++){
            if ((a&1)==0&&(b&1)==1){
                ans += 1L <<i;
            }
            a>>=1;b>>=1;
            if (b==0){
                return ans;
            }
        }
        return ans;
    }

    public void onCalculateScheduleButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = getSelectedItem().size();
        if (selectedNumber <= 1){
            dialogUtil.openError("检查失败", "请选择数量不少于2门的课程");
        }else {
            List<Long> scheduleList = new ArrayList<>(){};
            for (CourseEntity course :
                    selectedItemList) {
                scheduleList.add(course.getSchedule());
            }
            String scheduleString = getScheduleString(checkSchedule(scheduleList));
            if (!scheduleString.isEmpty()) {
                dialogUtil.openInfo("检查课程时间", "您选中的 " + selectedNumber + " 条课程信息在" + scheduleString + "发生了时间冲突！");
            }else {
                dialogUtil.openInfo("检查课程时间", "您确认选中的 " + selectedNumber + " 条课程信息没有时间冲突。");
            }
        }
    }
}
