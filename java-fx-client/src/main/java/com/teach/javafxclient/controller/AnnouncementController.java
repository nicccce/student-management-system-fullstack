package com.teach.javafxclient.controller;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.AddAnnouncementController;
import com.teach.javafxclient.controller.admin.AddStudentActivityController;
import com.teach.javafxclient.controller.admin.FilterActivityController;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.ActivityEntity;
import com.teach.javafxclient.model.AnnouncementEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
import com.teach.javafxclient.util.LocalDateUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnouncementController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public Label filterLabel;
    @FXML
    private TableView<AnnouncementEntity> dataTableView;  //教师信息表
    @FXML
    public TableColumn<AnnouncementEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<AnnouncementEntity,String> numColumn;   //课程编号信息表 编号列
    @FXML
    private TableColumn<AnnouncementEntity,String> nameColumn; //课程名称信息表 名称列

    @FXML
    private TableColumn<AnnouncementEntity,String> announcementContentColumn; //教师信息表 职位列
    @FXML
    private TableColumn<AnnouncementEntity,String> beginTimeColumn; //教师信息表 学历列
    @FXML
    private TableColumn<AnnouncementEntity,String> endTimeColumn; //教师信息表 证件号码列


    @FXML
    private TextField numField; //教师信息  学号输入域
    @FXML
    private TextField nameField;
    @FXML
    private TextField announcementContentField; //教师信息  院系输入域
    @FXML
    private DatePicker beginTimePick; //教师信息  证件号码输入域
    @FXML
    private DatePicker endTimePick;
    @FXML
    private TextField numNameTextField;



    private Integer announcementId = null;  //当前编辑修改的学生的主键
    private ArrayList<AnnouncementEntity> announcementList = new ArrayList<AnnouncementEntity>();  // 学生信息列表数据
    private ObservableList<AnnouncementEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<AnnouncementEntity> selectedItemList = null;

    private HttpRequestUtil<AnnouncementEntity> httpRequestUtil = new HttpRequestUtil<>(AnnouncementEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();
    private List Bee = new ArrayList();




    //存储筛选的条件
    private AnnouncementEntity filterCriteria = new AnnouncementEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<AnnouncementEntity>> res;
        DataRequest req =new DataRequest();
        req.put("userId","");
        res = httpRequestUtil.requestArrayList("/api/announcement/getAnnouncementListByUserId",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            announcementList = res.getData();
        }
        res =httpRequestUtil.request("/api/announcement/getBee",req);
        if(res != null && res.getCode()== 0) {
            Bee= res.getData();
        }

        setupTable();

        beginTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        resetFilter();
    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<AnnouncementEntity>observableArrayList(announcementList));
        dataTableView.setItems(observableList);
    }

    /**
     * 初始化表格
     */
    private void setupTable(){

        var selectAll = new CheckBox();

        checkColumn.setGraphic(selectAll);
        checkColumn.setSortable(false);
        checkColumn.setCellValueFactory(c -> c.getValue().selectProperty());

        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        numColumn.setCellValueFactory(new PropertyValueFactory<AnnouncementEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<AnnouncementEntity,String>("name"));
        announcementContentColumn.setCellValueFactory(new PropertyValueFactory<AnnouncementEntity,String>("announcementContent"));
        beginTimeColumn.setCellValueFactory(new PropertyValueFactory<AnnouncementEntity,String>("beginTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<AnnouncementEntity,String>("endTime"));


        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        TableView.TableViewSelectionModel<AnnouncementEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    AnnouncementEntity form = dataTableView.getSelectionModel().getSelectedItem();
                    if(form != null) {
                        form.setSelect(!form.isSelect());
                    }
                }
            }
        });

        dataTableView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        Styles.toggleStyleClass(dataTableView, Styles.BORDERED);
        Styles.toggleStyleClass(dataTableView, Styles.STRIPED);

    }

    // 辅助方法：检查节点是否是有效行或单元格
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
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        announcementId = null;
        numField.setText("");
        nameField.setText("");
        announcementContentField.setText("");
        beginTimePick.getEditor().setText("");
        endTimePick.getEditor().setText("");


    }

    protected void changeAnnouncementInfo() {
        AnnouncementEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        announcementId = form.getAnnouncementId();
        DataRequest req = new DataRequest();
        req.put("announcementId",announcementId);
        DataResponse<AnnouncementEntity> res = httpRequestUtil.requestObject("/api/announcement/getAnnouncementInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();

        numField.setText(form.getNum());
        nameField.setText(form.getName());
        announcementContentField.setText(form.getAnnouncementContent());
        beginTimePick.getEditor().setText(form.getBeginTime());
        endTimePick.getEditor().setText(form.getEndTime());

    }
    private void setTextField(TextField textField, String value) {
        if (value != null) {
            textField.setText(value);
        } else {
            textField.setText("");
        }
    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeAnnouncementInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<AnnouncementEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/announcement/getAnnouncementListByFilter/" ,req);

            //因为有筛选条件，修改一下筛选按钮
            hasFilter();
        }else {
            req.put("numName",numName);
            res = httpRequestUtil.requestArrayList("/api/announcement/getAnnouncementList",req);
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            announcementList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-announcement.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddAnnouncementController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加公告信息");
            addStage.getIcons().add(MainApplication.icon);

            // 通过控制器的 setStage 方法传递 Stage 对象
            controller.setStage(addStage);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            // 添加关闭事件处理程序
            addStage.setOnHiding(event -> {
                onQueryButtonClick(); // 在关闭事件中调用 onQueryButtonClick() 方法
            });

            addStage.show();
            controller.Age(Bee);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        getSelectedItem();
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("删除失败", "当前未选择任何元素，无法删除！");
            return;
        }
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个公告信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);

    }

    public void deleteSelectedItems(){
        ArrayList<Integer> announcementIdList = new ArrayList<Integer>();
        for (AnnouncementEntity item:
                selectedItemList) {
            announcementIdList.add(item.getAnnouncementId());
        }
        DataRequest req = new DataRequest();
        req.put("announcementId", announcementIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/announcement/announcementDeleteAll",req);
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
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        //弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("修改失败", "课程编号为空，不能修改！");
            return;
        }
        AnnouncementEntity announcementEntity =new AnnouncementEntity();
        announcementEntity.setNum(numField.getText());
        announcementEntity.setName(nameField.getText());
        announcementEntity.setAnnouncementContent(announcementContentField.getText());
        if (!LocalDateUtil.isLaterDate(beginTimePick.getEditor().getText(),endTimePick.getEditor().getText())){
            dialogUtil.openError("修改失败", "截止时间早于开始时间！");
            return;
        }
        announcementEntity.setBeginTime(beginTimePick.getEditor().getText());
        announcementEntity.setEndTime(endTimePick.getEditor().getText());

        DataRequest req = new DataRequest();
        req.put("announcementId", announcementId);
        req.putObject("form", announcementEntity);
        DataResponse res = HttpRequestUtil.request("/api/announcement/announcementEditSave",req);
        if(res.getCode() == 0) {
            announcementId = CommonMethod.getIntegerFromObject(res.getData());
            dialogUtil.openGeneric("提交成功","提交成功！",null);
            onQueryButtonClick();
            // MessageDialog.showDialog("提交成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
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

    private List<AnnouncementEntity> getSelectedItem(){
        List<AnnouncementEntity> selectedItems = new ArrayList<AnnouncementEntity>();
        for (AnnouncementEntity items :
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

    private void resetFilter() {
        addFilterButton.setVisible(true);
        addFilterButton.setManaged(true);
        changeFilterButton.setManaged(false);
        changeFilterButton.setVisible(false);
        resetFilterButton.setVisible(false);
        resetFilterButton.setManaged(false); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前无筛选条件");
        filterCriteria.empty();//清空筛选条件
    }

    private void hasFilter(){
        addFilterButton.setVisible(false);
        addFilterButton.setManaged(false);
        changeFilterButton.setManaged(true);
        changeFilterButton.setVisible(true);
        resetFilterButton.setVisible(true);
        resetFilterButton.setManaged(true); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前已设置筛选条件");
    }

    private void setFilter(){
/*        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("student-filter-activity.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterActivityController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage filterStage = new Stage();
            filterStage.setTitle("筛选条件");
            filterStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            filterStage.setScene(scene);
            filterStage.show();
            controller.Age(Bnum,Bname);
            // 初始化筛选控制器所需要的值，并把筛选条件的指针传进去，使在弹出页面更改的会自动同步到这个页面
            controller.init(filterStage, filterCriteria, this::onQueryButtonClick);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
    /**
     * 点击添加筛选按钮
     * @param actionEvent .
     */
    public void onAddFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }
    /**
     * 点击修改筛选按钮
     * @param actionEvent .
     */
    public void onChangeFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }
    /**
     * 点击清除筛选按钮
     * @param actionEvent .
     */
    public void onResetFilterButtonClicked(ActionEvent actionEvent) {
        resetFilter();
        onQueryButtonClick();
    }

    public void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = getSelectedItem().size();
        if (selectedNumber == 0){
            dialogUtil.openError("导出失败", "未选中任何日常活动信息");
        }else {
            dialogUtil.openInfo("导出日常活动信息", "点击确认导出选中的 "+selectedNumber+" 条日常活动信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedAnnouncement",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/announcement/getSelectedAnnouncementListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("announcement.xlsx");
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
