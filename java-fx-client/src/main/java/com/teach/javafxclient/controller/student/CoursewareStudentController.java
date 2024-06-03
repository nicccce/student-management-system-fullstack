package com.teach.javafxclient.controller.student;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.controller.AnnouncementController;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.model.AnnouncementEntity;
import com.teach.javafxclient.model.CoursewareEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.DialogUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CoursewareStudentController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public Label filterLabel;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public MFXButton getExcelButton;
    public MFXButton importExcelButton;
    public TableColumn<CoursewareEntity,String> fileNameColumn;
    public MFXTextField nameField;

    public MFXComboBox<OptionItem> courseComboBox;
    public Label courseNameLabel;
    public Label courseNumLabel;
    public Label courseTypeLabel;
    public MFXTextField numNameTextField;
    public Label fileTypeLabel;
    public MFXButton DownloadButton;
    public MFXButton deleteAllButton;
    public MFXButton saveButton;
    @FXML
    private TableView<CoursewareEntity> dataTableView;  //课件信息表
    @FXML
    public TableColumn<CoursewareEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<CoursewareEntity,String> courseNumColumn;   //课件信息表 编号列
    @FXML
    private TableColumn<CoursewareEntity,String> courseNameColumn; //课件信息表 名称列
    @FXML
    private TableColumn<CoursewareEntity,String> fileTypeColumn; //课件信息表 专业列
    @FXML
    private TableColumn<CoursewareEntity,String> importTimeColumn; //课件信息表 出生日期列
    @FXML
    private TextField fileNameField; //课件信息  院系输入域
    @FXML
    private DatePicker importTimePick;  //课件信息  出生日期选择域

    private Integer coursewareId = null;  //当前编辑修改的课件的主键

    private ArrayList<CoursewareEntity> coursewareList = new ArrayList<CoursewareEntity>();  // 课件信息列表数据
    private ObservableList<CoursewareEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<CoursewareEntity> selectedItemList = null;

    private HttpRequestUtil<CoursewareEntity> httpRequestUtil = new HttpRequestUtil<>(CoursewareEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        //设置表的列属性和表属性
        setupTable();

        setupCourseComboBox();

        importTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        importTimePick.setDisable(true);

        addButton.setManaged(false);
        deleteButton.setManaged(false);
        deleteAllButton.setManaged(false);
        saveButton.setManaged(false);
        fileNameField.setDisable(true);

    }

    private void setupCourseComboBox(){
        HttpRequestUtil<OptionItem> optionItemHttpRequestUtil = new HttpRequestUtil<>(OptionItem.class);
        DataRequest req = new DataRequest();
        req.put("coursewareId",coursewareId);
        DataResponse<ArrayList<OptionItem>> res = optionItemHttpRequestUtil.requestArrayList("/api/course/getStudentOptionItem",req);
        if (res!=null) {
            courseComboBox.getItems().addAll(res.getData());
        }else {
            dialogUtil.openError("加载失败", "页面加载失败，服务器无响应！");
        }
        courseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (courseComboBox.getSelectedItem()!=null){
                changeCoursewareList();
                setTableViewData();
            }
        });
    }

    /**
     * 将课件数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<CoursewareEntity>observableArrayList(coursewareList));
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
        //设置列元素为课件对象的checkColumn成员
        checkColumn.setCellValueFactory(c -> c.getValue().selectProperty());
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        //初始化表中的其他数据列
        courseNumColumn.setCellValueFactory(new PropertyValueFactory<CoursewareEntity,String>("courseNum"));  //设置列值工程属性
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<CoursewareEntity,String>("courseName"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<CoursewareEntity,String>("fileName"));
        fileTypeColumn.setCellValueFactory(new PropertyValueFactory<CoursewareEntity,String>("fileType"));
        importTimeColumn.setCellValueFactory(new PropertyValueFactory<CoursewareEntity,String>("importTime"));
        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        //添加选择行的监听器，用于更新右边栏
        TableView.TableViewSelectionModel<CoursewareEntity> tsm = dataTableView.getSelectionModel();
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
                    CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
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
     * 清除课件表单中输入信息
     */
    public void clearPanel(){
        coursewareId = null;
        courseNumLabel.setText("");
        courseNameLabel.setText("");
        fileNameField.setText("");
        fileTypeLabel.setText("");
        importTimePick.getEditor().setText("");
    }

    /**
     * 用于点击表格数据时，更新右边栏的内容
     */
    protected void changeCoursewareInfo() {
        CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        courseNumLabel.setText(form.getCourseNum());
        courseNameLabel.setText(form.getCourseName());
        fileNameField.setText(form.getFileName());
        fileTypeLabel.setText(form.getFileType());
        importTimePick.getEditor().setText(form.getImportTime());
    }

    private void changeCoursewareList(){
        coursewareList.clear();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<String>> res;
        OptionItem course = courseComboBox.getSelectedItem();
        res = HttpRequestUtil.request("/api/courseware/getFileNames/" + course.getValue(), req);
        if (res!=null){
            if (res.getCode()==0){
                for (String courseware :
                        res.getData()) {
                    if (courseware.contains(numNameTextField.getText())) {
                        coursewareList.add(new CoursewareEntity(courseware, course));
                    }
                }
            }else {
                dialogUtil.openError("加载失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("加载失败", "服务器无响应，课件数据获取失败！");
        }
    }

    /**
     * 点击课件列表的某一行，根据coursewareId ,从后台查询课件的基本信息，切换课件的编辑信息
     */
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeCoursewareInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的课件在课件列表中显示
     * 也是刷新界面的方法
     */
    @FXML
    protected void onQueryButtonClick() {
        if (courseComboBox.getSelectedItem()==null){
            dialogUtil.openError("无法加载", "请选择需要查询的课程");
            return;
        }
        changeCoursewareList();
        setTableViewData();

    }

    /**
     *  添加新课件， 清空输入信息， 输入相关信息，点击保存即可添加新的课件
     */
    @FXML
    protected void onAddButtonClick() {
        if (courseComboBox.getSelectedItem()==null){
            dialogUtil.openError("无法上传", "请选择需要上传课件的课程");
            return;
        }
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("课件上传");
        fileDialog.setInitialDirectory(new File("C:/"));
        File file = fileDialog.showOpenDialog(null);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);


        // 原始文件名
        String originalFileName = file.getName();

        for (CoursewareEntity courseware :
                coursewareList) {
            if (courseware.getFileName().equals(CoursewareEntity.extractFileName(originalFileName))) {
                dialogUtil.openError("上传失败", "课程资料文件名重复，请修改文件名后上传！");
                return;
            }


        }
        // 获取文件扩展名
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            fileExtension = originalFileName.substring(dotIndex);
        }

        // 构建新的文件名
        String newFileName = String.format("%s_%s", originalFileName, formattedDate);

        DataResponse res =HttpRequestUtil.uploadFile(file.getPath(),"courseware/" + courseComboBox.getSelectedItem().getValue() + "/" + newFileName);
        if(res.getCode() == 0) {
            dialogUtil.openGeneric("上传成功", "课件上传成功！");
        }
        else {
            dialogUtil.openError("上传失败！", res.getMsg());
        }

        changeCoursewareList();
        setTableViewData();
    }


    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对课件的增，删，改操作
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
    private List<CoursewareEntity> getSelectedItem(){
        List<CoursewareEntity> selectedItems = new ArrayList<CoursewareEntity>();
        for (CoursewareEntity items :
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

    private void download(File selectedDirectory , CoursewareEntity courseware){
        if (selectedDirectory != null) {
            try {
                String fileName = courseware.getFileName() + "." + courseware.getFileType().toLowerCase().substring(0,courseware.getFileType().length()-2);
                File file = new File(selectedDirectory, fileName);

                // 发起下载请求
                DataRequest req = new DataRequest();
                Map<String, Object> param = new HashMap<>();
                param.put("fileName", courseware.getFullFileName());
                byte[] bytes = HttpRequestUtil.getByteData("/api/courseware/getCourseware", courseware.getCourseNum(), param);

                if (bytes != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            } catch (Exception e) {
                dialogUtil.openError("文件下载失败","课件下载失败！\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDownloadAllButtonClick(ActionEvent actionEvent) {
        getSelectedItem();
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("下载失败", "请选择需要下载的课件！");
        }
        dialogUtil.openInfo("下载课件", "点击确认下载选中的 " + selectedItemList.size() + " 个课件。", this::downloadAllCourseware);
    }

    private void downloadAllCourseware(){
        // 选择保存的文件夹
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择保存的文件夹");
        File selectedDirectory = directoryChooser.showDialog(null);
        for (CoursewareEntity courseware :
                selectedItemList) {
            download(selectedDirectory, courseware);
        }
        if (selectedDirectory!=null) {
            dialogUtil.openGeneric("下载成功", "下载成功！");
        }
    }

    @FXML
    private void onSaveButtonClick() {
        CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            dialogUtil.openError("保存失败", "请先选择需要修改的文件");
            return;
        }
        String newFileName = fileNameField.getText();
        if (Objects.equals(form.getFileName(), newFileName)){
            dialogUtil.openError("保存失败", "文件信息未发生任何更改！");
            return;
        }
        for (CoursewareEntity courseware :
                coursewareList) {
            if (courseware.getFileName().equals(newFileName)) {
                dialogUtil.openError("保存失败", "课程资料文件名重复，请修改文件名后上传！");
                return;
            }

        }
        // 发起保存请求
        DataRequest req = new DataRequest();
        Map<String, Object> param = new HashMap<>();
        param.put("fileName", form.getFullFileName());
        param.put("newFileName", newFileName);
        DataResponse res = HttpRequestUtil.getRequest("/api/courseware/changeCoursewareName", form.getCourseNum(), param);
        if (res!=null){
            if (res.getCode() == 0){
                dialogUtil.openGeneric("保存成功", "保存成功！");
            }else {
                dialogUtil.openError("保存失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("保存失败", "服务器无响应");
        }
        onQueryButtonClick();
    }

    @FXML
    private void onDeleteAllButtonClick(ActionEvent actionEvent) {
        getSelectedItem();
        getSelectedItem();
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("删除失败", "请选择需要删除的课件！");
        }
        dialogUtil.openWarning("删除课件", "将永久删除框选的 " + selectedItemList.size() + " 个课件，并且无法还原，确认要删除吗?",this::deleteAllCourseware);
    }

    private void deleteAllCourseware(){
        for (CoursewareEntity courseware :
                selectedItemList) {
            if(!deleteCourseware(courseware)){
                return;
            }
        }
        dialogUtil.openInfo("删除成功", "删除成功！");
        onQueryButtonClick();
    }

    @FXML
    private void onDownloadButtonClick(ActionEvent actionEvent) {
        CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            dialogUtil.openError("下载失败", "请先选择需要下载的文件");
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择保存的文件夹");
        File selectedDirectory = directoryChooser.showDialog(null);
        download(selectedDirectory,form);
        if (selectedDirectory!=null) {
            dialogUtil.openGeneric("下载成功", "下载成功！");
        }
    }

    /**
     * 点击删除按钮 删除当前编辑的课件的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            dialogUtil.openError("删除失败", "请先选择需要删除的文件");
            return;
        }
        //弹窗警告，若点击警告的“确定”，再继续进行删除操作
        dialogUtil.openWarning("警告", "将永久删除课件的所有信息，并且无法还原，确认要删除吗?", this::deleteSelectedCourseware);
    }

    private void deleteSelectedCourseware(){
        CoursewareEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(deleteCourseware(form)) {
            dialogUtil.openInfo("删除成功", "删除成功！");
        }
        onQueryButtonClick();
    }

    private boolean deleteCourseware(CoursewareEntity courseware){
        if (courseware != null){
            DataRequest req = new DataRequest();
            DataResponse res = HttpRequestUtil.deleteRequest("/api/courseware/deleteCourseware/" + courseware.getCourseNum() + "?fileName=" + courseware.getFullFileName(), req);
            if (res != null) {
                if (res.getCode() == 0) {
                    return true;
                } else {
                    dialogUtil.openError("删除失败", res.getMsg());
                }
            } else {
                dialogUtil.openError("删除失败", "后台无响应，请稍后再尝试。");
            }
        }
        return false;
    }
}

