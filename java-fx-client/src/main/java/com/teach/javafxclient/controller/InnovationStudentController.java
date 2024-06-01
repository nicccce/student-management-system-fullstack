package com.teach.javafxclient.controller;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.AppStore;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.AddInnovationController;
import com.teach.javafxclient.controller.admin.AddStudentInnovationController;
import com.teach.javafxclient.controller.admin.FilterInnovationController;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.InnovationEntity;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnovationStudentController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public Label filterLabel;
    @FXML
    private TableView<InnovationEntity> dataTableView;  //教师信息表
    @FXML
    public TableColumn<InnovationEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<InnovationEntity,String> numColumn;   //教师信息表 编号列
    @FXML
    private TableColumn<InnovationEntity,String> nameColumn; //教师信息表 名称列
    //@FXML
    // private TableColumn<FamilyEntity,String> addressColumn;  //教师信息表 院系列
    @FXML
    private TableColumn<InnovationEntity,String> innovationNameColumn; //教师信息表 职位列
    @FXML
    private TableColumn<InnovationEntity,String> innovationTypeColumn; //教师信息表 学历列
    @FXML
    private TableColumn<InnovationEntity,String> instructorColumn; //教师信息表 证件号码列
    @FXML
    private TableColumn<InnovationEntity,String> teamPositionColumn; //教师信息表 性别列
    @FXML
    private TableColumn<InnovationEntity,String> teamNameColumn; //教师信息表 出生日期列
    /*@FXML
    private TableColumn<FamilyEntity,String> motherNameColumn; //教师信息表 邮箱列
    @FXML
    private TableColumn<FamilyEntity,String> motherOccupationColumn; //教师信息表 电话列
    @FXML
    private TableColumn<FamilyEntity,String> motherAgeColumn;//教师信息表 地址列
    @FXML
    private TableColumn<FamilyEntity,String> motherContactColumn;//教师信息表 地址列*/

    @FXML
    private TextField numField; //教师信息  学号输入域
    @FXML
    private TextField innovationNameField; //教师信息  院系输入域
    @FXML
    private ComboBox<OptionItem> innovationTypeComboBox;
    @FXML
    private TextField instructorField; //教师信息  学历输入域
    @FXML
    private TextField teamPositionField; //教师信息  证件号码输入域
    @FXML
    private TextField teamNameField;  //教师信息  邮箱输入域
    /*@FXML
    private TextField motherNameField;   //教师信息  电话输入域
    @FXML
    private TextField motherOccupationField;  //教师信息  地址输入域
    @FXML
    private TextField motherAgeField;  //教师信息  地址输入域
    @FXML
    private TextField motherContactField;  //教师信息  地址输入域*/
    //@FXML
    // private TextField addressField;  //教师信息  地址输入域


    private Integer innovationId = null;  //当前编辑修改的学生的主键
    private ArrayList<InnovationEntity> innovationList = new ArrayList<InnovationEntity>();  // 学生信息列表数据
    private List<OptionItem> innovationTypeList;
    private ObservableList<InnovationEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<InnovationEntity> selectedItemList = null;

    private HttpRequestUtil<InnovationEntity> httpRequestUtil = new HttpRequestUtil<>(InnovationEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    private Map Bee = new HashMap<>();

    private String Bnum;
    private String Bname;

    public String getBnum() {
        return Bnum;
    }

    public void setBnum(String bnum) {
        Bnum = bnum;
    }

    public String getBname() {
        return Bname;
    }

    public void setBname(String bname) {
        Bname = bname;
    }

    //存储筛选的条件
    private InnovationEntity filterCriteria = new InnovationEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<InnovationEntity>> res;
        DataRequest req =new DataRequest();
        req.put("userId","");
        res = httpRequestUtil.requestArrayList("/api/innovation/getInnovationListByUserId",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            innovationList = res.getData();
        }
        res =httpRequestUtil.request("/api/innovation/getBee",req);
        if(res != null && res.getCode()== 0) {
            Bee=(Map) res.getData();
            setBnum((String) Bee.get("num"));
            setBname((String) Bee.get("name"));
        }

        setupTable();
        innovationTypeList = HttpRequestUtil.getDictionaryOptionItemList("INO");
        innovationTypeComboBox.getItems().addAll(innovationTypeList);
        resetFilter();
    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<InnovationEntity>observableArrayList(innovationList));
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

        numColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("name"));
        innovationNameColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("innovationName"));
        //这这这这这这这这这
        innovationTypeColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("innovationTypeName"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("instructor"));
        teamPositionColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("teamPosition"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<InnovationEntity,String>("teamName"));
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

        TableView.TableViewSelectionModel<InnovationEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    InnovationEntity form = dataTableView.getSelectionModel().getSelectedItem();
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
        innovationId = null;
        numField.setText("");
        innovationNameField.setText("");
        innovationTypeComboBox.getSelectionModel().select(-1);
        instructorField.setText("");
        teamPositionField.setText("");
        teamNameField.setText("");
        /*motherNameField.setText("");
        motherOccupationField.setText("");
        motherAgeField.setText("");
        motherAgeField.setText("");*/
        // addressField.setText("");
    }

    protected void changeInnovationInfo() {
        InnovationEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        innovationId = form.getInnovationId();
        DataRequest req = new DataRequest();
        req.put("innovationId",innovationId);
        DataResponse<InnovationEntity> res = httpRequestUtil.requestObject("/api/innovation/getInnovationInfo",req);
        System.out.println(res.getData());
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();

        numField.setText(form.getNum());
        innovationNameField.setText(form.getInnovationName());
        innovationTypeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(innovationTypeList, form.getInnovationType()));
        instructorField.setText(form.getInstructor());
        teamPositionField.setText(form.getTeamPosition());
        teamNameField.setText(form.getTeamName());
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
        changeInnovationInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<InnovationEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/innovation/getInnovationListByFilter/" ,req);

            //因为有筛选条件，修改一下筛选按钮
            hasFilter();
        }else {
            req.put("numName","");
            res = httpRequestUtil.requestArrayList("/api/innovation/getInnovationListByUserId",req);
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            innovationList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-student-innovation.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddStudentInnovationController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加创新信息");
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
            controller.Age(Bnum);
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
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个学生的创新实践信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);
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
        ArrayList<Integer> innovationIdList = new ArrayList<Integer>();
        for (InnovationEntity item:
                selectedItemList) {
            innovationIdList.add(item.getInnovationId());
        }
        DataRequest req = new DataRequest();
        req.put("innovationId", innovationIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/innovation/innovationDeleteAll",req);
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
        InnovationEntity innovationEntity =new InnovationEntity();
        innovationEntity.setNum(numField.getText());
        innovationEntity.setInnovationName(innovationNameField.getText());
        if (innovationTypeComboBox.getSelectionModel() != null && innovationTypeComboBox.getSelectionModel().getSelectedItem() != null) {
            innovationEntity.setInnovationType(innovationTypeComboBox.getSelectionModel().getSelectedItem().getValue());
        }
        innovationEntity.setInstructor(instructorField.getText());
        innovationEntity.setTeamPosition(teamPositionField.getText());
        innovationEntity.setTeamName(teamNameField.getText());
        /*familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());*/
        //familyEntity.setAddress(addressField.getText());

/*        Map form = studentEntity.toMap();
        System.out.println(form);*/
/*        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender",genderComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("birthday",birthdayPick.getEditor().getText());
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());*/
        DataRequest req = new DataRequest();
        req.put("innovationId", innovationId);
        req.putObject("form", innovationEntity);
        DataResponse res = HttpRequestUtil.request("/api/innovation/innovationEditSave",req);
        if(res.getCode() == 0) {
            innovationId = CommonMethod.getIntegerFromObject(res.getData());
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


    private List<InnovationEntity> getSelectedItem(){
        List<InnovationEntity> selectedItems = new ArrayList<InnovationEntity>();
        for (InnovationEntity items :
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("student-filter-innovation.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterInnovationController controller = fxmlLoader.getController(); // 获取控制器对象

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
            dialogUtil.openError("导出失败", "未选中任何创新实践信息");
        }else {
            dialogUtil.openInfo("导出创新实践信息", "点击确认导出选中的 "+selectedNumber+" 条创新实践信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedInnovation",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/innovation/getSelectedInnovationListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("innovation.xlsx");
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
