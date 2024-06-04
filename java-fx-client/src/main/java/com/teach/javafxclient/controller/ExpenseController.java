package com.teach.javafxclient.controller;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.*;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.*;
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
import java.util.List;

public class ExpenseController extends ToolController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public Label filterLabel;
    @FXML
    private TableView<ExpenseEntity> dataTableView;  //教师信息表
    @FXML
    public TableColumn<ExpenseEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<ExpenseEntity,String> numColumn;   //教师信息表 编号列
    @FXML
    private TableColumn<ExpenseEntity,String> nameColumn; //教师信息表 名称列
    //@FXML
    // private TableColumn<FamilyEntity,String> addressColumn;  //教师信息表 院系列
    @FXML
    private TableColumn<ExpenseEntity,String> expenseTypeColumn; //教师信息表 学历列
    @FXML
    private TableColumn<ExpenseEntity,String> expenseContentColumn; //教师信息表 证件号码列
    @FXML
    private TableColumn<ExpenseEntity,String> expenseDateColumn; //教师信息表 性别列
    @FXML
    private TableColumn<ExpenseEntity,String> expenseNumColumn; //教师信息表 职位列


    @FXML
    private TextField numField; //教师信息  学号输入域
    @FXML
    private ComboBox<OptionItem> expenseTypeComboBox;
    @FXML
    private TextField expenseContentField; //教师信息  学历输入域
    @FXML
    private DatePicker expenseDatePick; //教师信息  证件号码输入域
    @FXML
    private TextField expenseNumField; //教师信息  院系输入域


    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer expenseId = null;  //当前编辑修改的学生的主键
    private ArrayList<ExpenseEntity> expenseList = new ArrayList<ExpenseEntity>();  // 学生信息列表数据
    private List<OptionItem> expenseTypeList;
    private ObservableList<ExpenseEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<ExpenseEntity> selectedItemList = null;

    private HttpRequestUtil<ExpenseEntity> httpRequestUtil = new HttpRequestUtil<>(ExpenseEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    //存储筛选的条件
    private ExpenseEntity filterCriteria = new ExpenseEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<ExpenseEntity>> res;
        DataRequest req =new DataRequest();
        req.put("numName","");
        res = httpRequestUtil.requestArrayList("/api/expense/getExpenseList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            expenseList = res.getData();
        }

        setupTable();
        expenseTypeList = HttpRequestUtil.getDictionaryOptionItemList("EXP");
        expenseTypeComboBox.getItems().addAll(expenseTypeList);
        expenseDatePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        resetFilter();
    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<ExpenseEntity>observableArrayList(expenseList));
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
/*        checkColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTableEntity, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<StudentTableEntity, Boolean> param) {
                StudentTableEntity studentTableEntity = param.getValue();
                Boolean value = studentTableEntity.getSelect();
                SimpleBooleanProperty observableValue = new SimpleBooleanProperty(value);
                return observableValue;
            }
        });*/
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        numColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("name"));

        expenseTypeColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("expenseTypeName"));
        expenseContentColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("expenseContent"));
        expenseDateColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("expenseDate"));
        expenseNumColumn.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,String>("expenseNum"));
        //teamNameColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("teamName"));
        /*motherNameColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherName"));
        motherOccupationColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherOccupation"));
        motherAgeColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherAge"));
        motherContactColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity, String>("motherContact"));*/
        //addressColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity, String>("address"));

        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        TableView.TableViewSelectionModel<ExpenseEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    ExpenseEntity form = dataTableView.getSelectionModel().getSelectedItem();
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
        expenseId = null;
        numField.setText("");
        expenseTypeComboBox.getSelectionModel().select(-1);
        expenseContentField.setText("");
        expenseDatePick.getEditor().setText("");
        expenseNumField.setText("");
        //teamNameField.setText("");
        /*motherNameField.setText("");
        motherOccupationField.setText("");
        motherAgeField.setText("");
        motherAgeField.setText("");*/
        // addressField.setText("");
    }

    protected void changeExpenseInfo() {
        ExpenseEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        expenseId = form.getExpenseId();
        DataRequest req = new DataRequest();
        req.put("expenseId",expenseId);
        DataResponse<ExpenseEntity> res = httpRequestUtil.requestObject("/api/expense/getExpenseInfo",req);
        System.out.println(res.getData());
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();

        numField.setText(form.getNum());
        expenseTypeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(expenseTypeList, form.getExpenseType()));
        expenseContentField.setText(form.getExpenseContent());
        expenseDatePick.getEditor().setText(form.getExpenseDate());
        expenseNumField.setText(form.getExpenseNum());
        //teamNameField.setText(form.getTeamName());
        //genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, form.getGender()));
        //birthdayPick.getEditor().setText(form.getBirthday());
        /*motherNameField.setText(form.getMotherName());
        motherOccupationField.setText(form.getMotherOccupation());
        motherAgeField.setText(form.getMotherAge());
        motherContactField.setText(form.getMotherContact());*/
        //addressField.setText("");
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
        changeExpenseInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<ExpenseEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/expense/getExpenseListByFilter/" + numName,req);

            //因为有筛选条件，修改一下筛选按钮
            hasFilter();
        }else {
            req.put("numName",numName);
            res = httpRequestUtil.requestArrayList("/api/expense/getExpenseList",req);
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            expenseList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-expense.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddExpenseController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加消费信息");
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
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个学生的消费信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);
        /*StudentTableEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        studentId = form.getStudentId();
        DataRequest req = new DataRequest();
        req.put("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/studentDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }*/
    }

    public void deleteSelectedItems(){
        ArrayList<Integer> expenseIdList = new ArrayList<Integer>();
        for (ExpenseEntity item:
                selectedItemList) {
            expenseIdList.add(item.getExpenseId());
        }
        DataRequest req = new DataRequest();
        req.put("expenseId", expenseIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/expense/expenseDeleteAll",req);
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
        if( !expenseNumField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "消费金额格式不正确，不能添加！");
            return;
        }
        ExpenseEntity expenseEntity =new ExpenseEntity();
        expenseEntity.setNum(numField.getText());

        if (expenseTypeComboBox.getSelectionModel() != null && expenseTypeComboBox.getSelectionModel().getSelectedItem() != null) {
            expenseEntity.setExpenseType(expenseTypeComboBox.getSelectionModel().getSelectedItem().getValue());
        }
        expenseEntity.setExpenseContent(expenseContentField.getText());
        expenseEntity.setExpenseDate(expenseDatePick.getEditor().getText());
        if (!LocalDateUtil.isBeforeToday(expenseEntity.getExpenseDate())){
            dialogUtil.openError("保存失败","消费日期无效！");
            return;
        }
        expenseEntity.setExpenseNum(expenseNumField.getText());

        DataRequest req = new DataRequest();
        req.put("expenseId", expenseId);
        req.putObject("form", expenseEntity);
        DataResponse res = HttpRequestUtil.request("/api/expense/expenseEditSave",req);
        if(res.getCode() == 0) {
            expenseId = CommonMethod.getIntegerFromObject(res.getData());
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
    public void doExport(){
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/expense/getExpenseListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
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

    private List<ExpenseEntity> getSelectedItem(){
        List<ExpenseEntity> selectedItems = new ArrayList<ExpenseEntity>();
        for (ExpenseEntity items :
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("filter-expense.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterExpenseController controller = fxmlLoader.getController(); // 获取控制器对象

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
            throw new RuntimeException(e);
        }
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
            dialogUtil.openError("导出失败", "未选中任何消费信息");
        }else {
            dialogUtil.openInfo("导出消费信息", "点击确认导出选中的 "+selectedNumber+" 条消费信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedExpense",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/expense/getSelectedExpenseListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("expense.xlsx");
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
