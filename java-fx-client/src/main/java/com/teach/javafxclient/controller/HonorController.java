package com.teach.javafxclient.controller;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.AddHonorController;
import com.teach.javafxclient.controller.admin.AddLeaveInfoController;
import com.teach.javafxclient.controller.admin.FilterLeaveInfoController;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.HonorEntity;
import com.teach.javafxclient.model.LeaveInfoEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.util.DialogUtil;
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
import java.util.ArrayList;
import java.util.List;

public class HonorController extends ToolController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public Label filterLabel;
    public MFXButton addButton;
    public MFXButton deleteButton;
    @FXML
    private TableView<HonorEntity> dataTableView;  //学生信息表
    @FXML
    public TableColumn<HonorEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<HonorEntity,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<HonorEntity,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<HonorEntity,String> honorNameColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<HonorEntity,String> honorTimeColumn; //学生信息表 专业列
    @FXML
    private TableColumn<HonorEntity,String> levelColumn; //学生信息表 班级列
    @FXML
    private TableColumn<HonorEntity,String> honorTypeColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<HonorEntity,String> hostColumn; //学生信息表 性别列



    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField honorNameField; //学生信息  院系输入域
    @FXML
    private TextField honorTypeField; //学生信息  专业输入域
    @FXML
    private TextField hostField; //学生信息  班级输入域

    @FXML
    private ComboBox<OptionItem> levelComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker honorTimePick;  //学生信息  出生日期选择域



    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer honorId = null;  //当前编辑修改的学生的主键

    private ArrayList<HonorEntity> hList = new ArrayList<HonorEntity>();  // 学生信息列表数据
    private List<OptionItem> levelList;   //性别选择列表数据
    private ObservableList<HonorEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<HonorEntity> selectedItemList = null;

    private HttpRequestUtil<HonorEntity> httpRequestUtil = new HttpRequestUtil<>(HonorEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    //存储筛选的条件
    private HonorEntity filterCriteria = new HonorEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws Exception, IllegalAccessException {
        DataResponse<ArrayList<HonorEntity>> res;
        DataRequest req =new DataRequest();
        req.put("numName","");
        res = httpRequestUtil.requestArrayList("/api/honor/getHonorList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            hList = res.getData();
        }

        //设置表的列属性和表属性
        setupTable();

        //初始化右边栏元素
        levelList = HttpRequestUtil.getDictionaryOptionItemList("LEV");
        levelComboBox.getItems().addAll(levelList);
        honorTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        resetFilter();
        //初始化筛选器及其按钮
        resetFilter();
    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<HonorEntity>observableArrayList(hList));
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
        //设置列元素为学生对象的checkColumn成员
        checkColumn.setCellValueFactory(c -> c.getValue().selectProperty());

        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        //初始化表中的其他数据列
        numColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("name"));
        honorNameColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("honorName"));
        honorTimeColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("honorTime"));
        honorTypeColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("honorType"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("levelName"));
        hostColumn.setCellValueFactory(new PropertyValueFactory<HonorEntity,String>("host"));
        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        //添加选择行的监听器，用于更新右边栏
        TableView.TableViewSelectionModel<HonorEntity> tsm = dataTableView.getSelectionModel();
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
                    HonorEntity form = dataTableView.getSelectionModel().getSelectedItem();
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
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        numField.setText("");
        nameField.setText("");
        honorNameField.setText("");
        honorTypeField.setText("");
        hostField.setText("");
        levelComboBox.getSelectionModel().select(-1);
        honorTimePick.getEditor().setText("");

    }

    /**
     * 用于点击表格数据时，更新右边栏的内容
     */
    protected void changeHonorInfo() {
        HonorEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        honorId = form.getHonorId();
        DataRequest req = new DataRequest();
        req.put("honorId",honorId);
        DataResponse<HonorEntity> res = httpRequestUtil.requestObject("/api/honor/getHonorInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();
        numField.setText(form.getNum());
        nameField.setText(form.getName());
        honorNameField.setText(form.getHonorName());
        honorTypeField.setText(form.getHonorType());
        hostField.setText(form.getHost());
        levelComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(levelList, form.getLevelName()));
        honorTimePick.getEditor().setText(form.getHonorTime());

    }

    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){changeHonorInfo();}

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     * 也是刷新界面的方法
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<HonorEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/honor/getHonorListByFilter/" + numName,req);

            //因为有筛选条件，修改一下筛选按钮
            hasFilter();

        }else {
            req.put("numName",numName);
            res = httpRequestUtil.requestArrayList("/api/honor/getHonorList",req);
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            hList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-honor.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddHonorController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加信息");
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
     * 点击删除按钮 删除当前编辑的学生的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        getSelectedItem();//更新复选框选中的学生数据
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("删除失败", "当前未选择任何元素，无法删除！");
            return;
        }
        //弹窗警告，若点击警告的“确定”，再继续进行删除操作
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个学生的荣誉信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);

    }

    /**
     * 删除复选框选中的学生对象
     */
    public void deleteSelectedItems(){
        ArrayList<Integer> HonorIdList = new ArrayList<Integer>();
        //提取复选框选中的学生的id，包装成列表传后端
        for (HonorEntity item:
                selectedItemList) {
            HonorIdList.add(item.getHonorId());
        }
        DataRequest req = new DataRequest();
        req.put("HonorId", HonorIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/honor/honorDeleteAll",req);
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
            dialogUtil.openError("修改失败", "学号为空，不能修改！");
            return;
        }
        //将右边栏的输入内容包装为学生信息对象
        HonorEntity honorEntity =new HonorEntity();
        honorEntity.setNum(numField.getText());
        honorEntity.setName(nameField.getText());
        honorEntity.setHonorName(honorNameField.getText());
        honorEntity.setHonorType(honorTypeField.getText());
        honorEntity.setHost(hostField.getText());

        if (levelComboBox.getSelectionModel() != null && levelComboBox.getSelectionModel().getSelectedItem() != null) {
            honorEntity.setLevelName(levelComboBox.getSelectionModel().getSelectedItem().getValue());
        }
        honorEntity.setHonorTime(honorTimePick.getEditor().getText());

        DataRequest req = new DataRequest();
        //将学生id单独包装方便后端
        req.put("HonorId", honorId);
        //将新学生信息包装进请求
        req.putObject("form", honorEntity);
        DataResponse res = HttpRequestUtil.request("/api/honor/HonorEditSave",req);
        if(res.getCode() == 0) {
            honorId = CommonMethod.getIntegerFromObject(res.getData());
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

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */


    /**
     * 获取复选框选中的列表对象
     * @return 复选框选中的对象列表
     */
    private List<HonorEntity> getSelectedItem(){
        List<HonorEntity> selectedItems = new ArrayList<HonorEntity>();
        for (HonorEntity items :
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
        /*FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("filter-honor.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterLeaveInfoController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage filterStage = new Stage();
            filterStage.setTitle("筛选条件");
            filterStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            filterStage.setScene(scene);
            filterStage.show();
            // 初始化筛选控制器所需要的值，并把筛选条件的指针传进去，使在弹出页面更改的会自动同步到这个页面
            controller.init(filterStage, filterCriteria, this::onQueryButtonClick);
        } catch (IOException e) {
            throw new RuntimeException(e);*/
//        }
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
            dialogUtil.openError("导出失败", "未选中任何学生荣誉信息");
        }else {
            dialogUtil.openInfo("导出学生荣誉信息", "点击确认导出选中的 "+selectedNumber+" 条学生荣誉信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedHonor",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/honor/getSelectedHonorListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("honor.xlsx");
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



