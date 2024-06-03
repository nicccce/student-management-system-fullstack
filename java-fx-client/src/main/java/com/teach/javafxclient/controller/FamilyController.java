package com.teach.javafxclient.controller;

import atlantafx.base.theme.Styles;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.AddFamilyController;
import com.teach.javafxclient.controller.admin.AddTeacherController;
import com.teach.javafxclient.controller.admin.FilterTeacherController;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.FamilyEntity;
import com.teach.javafxclient.model.TeacherEntity;
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
import java.util.List;

public class FamilyController extends ToolController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public Label filterLabel;
    @FXML
    private TableView<FamilyEntity> dataTableView;  //教师信息表
    @FXML
    public TableColumn<FamilyEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<FamilyEntity,String> numColumn;   //教师信息表 编号列
    @FXML
    private TableColumn<FamilyEntity,String> nameColumn; //教师信息表 名称列
    //@FXML
   // private TableColumn<FamilyEntity,String> addressColumn;  //教师信息表 院系列
    @FXML
    private TableColumn<FamilyEntity,String> familySizeColumn; //教师信息表 职位列
    @FXML
    private TableColumn<FamilyEntity,String> fatherNameColumn; //教师信息表 学历列
    @FXML
    private TableColumn<FamilyEntity,String> fatherOccupationColumn; //教师信息表 证件号码列
    @FXML
    private TableColumn<FamilyEntity,String> fatherAgeColumn; //教师信息表 性别列
    @FXML
    private TableColumn<FamilyEntity,String> fatherContactColumn; //教师信息表 出生日期列
    @FXML
    private TableColumn<FamilyEntity,String> motherNameColumn; //教师信息表 邮箱列
    @FXML
    private TableColumn<FamilyEntity,String> motherOccupationColumn; //教师信息表 电话列
    @FXML
    private TableColumn<FamilyEntity,String> motherAgeColumn;//教师信息表 地址列
    @FXML
    private TableColumn<FamilyEntity,String> motherContactColumn;//教师信息表 地址列

    @FXML
    private TextField numField; //教师信息  学号输入域
    @FXML
    private TextField familySizeField; //教师信息  院系输入域
    @FXML
    private TextField fatherNameField; //教师信息  职位输入域
    @FXML
    private TextField fatherOccupationField; //教师信息  学历输入域
    @FXML
    private TextField fatherAgeField; //教师信息  证件号码输入域

    @FXML
    private TextField fatherContactField;  //教师信息  邮箱输入域
    @FXML
    private TextField motherNameField;   //教师信息  电话输入域
    @FXML
    private TextField motherOccupationField;  //教师信息  地址输入域
    @FXML
    private TextField motherAgeField;  //教师信息  地址输入域
     @FXML
    private TextField motherContactField;  //教师信息  地址输入域
    //@FXML
   // private TextField addressField;  //教师信息  地址输入域

    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer familyId = null;  //当前编辑修改的学生的主键

    private ArrayList<FamilyEntity> familyList = new ArrayList<FamilyEntity>();  // 学生信息列表数据
    private ObservableList<FamilyEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<FamilyEntity> selectedItemList = null;

    private HttpRequestUtil<FamilyEntity> httpRequestUtil = new HttpRequestUtil<>(FamilyEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    //存储筛选的条件
    private FamilyEntity filterCriteria = new FamilyEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<FamilyEntity>> res;
        DataRequest req =new DataRequest();
        req.put("numName","");
        res = httpRequestUtil.requestArrayList("/api/family/getFamilyList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            familyList = res.getData();
        }

        setupTable();

    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<FamilyEntity>observableArrayList(familyList));
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

        numColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("name"));
        familySizeColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("familySize"));
        fatherNameColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("fatherName"));
        fatherOccupationColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("fatherOccupation"));
        fatherAgeColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("fatherAge"));
        fatherContactColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("fatherContact"));
        motherNameColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherName"));
        motherOccupationColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherOccupation"));
        motherAgeColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity,String>("motherAge"));
        motherContactColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity, String>("motherContact"));
        //addressColumn.setCellValueFactory(new PropertyValueFactory<FamilyEntity, String>("address"));

        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        TableView.TableViewSelectionModel<FamilyEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    FamilyEntity form = dataTableView.getSelectionModel().getSelectedItem();
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
        familyId = null;
        numField.setText("");
        familySizeField.setText("");
        fatherNameField.setText("");
        fatherOccupationField.setText("");
        fatherAgeField.setText("");
        fatherContactField.setText("");
        motherNameField.setText("");
        motherOccupationField.setText("");
        motherAgeField.setText("");
        motherContactField.setText("");
       // addressField.setText("");
    }

    protected void changeFamilyInfo() {
        FamilyEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        familyId = form.getFamilyId();
        DataRequest req = new DataRequest();
        req.put("familyId",familyId);
        DataResponse<FamilyEntity> res = httpRequestUtil.requestObject("/api/family/getFamilyInfo",req);
        System.out.println(res.getData());
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();

        numField.setText(form.getNum());
        familySizeField.setText(form.getFamilySize());
        fatherNameField.setText(form.getFatherName());
        fatherOccupationField.setText(form.getFatherOccupation());
        fatherAgeField.setText(form.getFatherAge());
        fatherContactField.setText(form.getFatherContact());
        //genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, form.getGender()));
        //birthdayPick.getEditor().setText(form.getBirthday());
        motherNameField.setText(form.getMotherName());
        motherOccupationField.setText(form.getMotherOccupation());
        motherAgeField.setText(form.getMotherAge());
        motherContactField.setText(form.getMotherContact());
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
        changeFamilyInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<FamilyEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/family/getFamilyListByFilter/" + numName,req);

            //因为有筛选条件，修改一下筛选按钮

        }else {
            req.put("numName",numName);
            res = httpRequestUtil.requestArrayList("/api/family/getFamilyList",req);

        }
        if(res != null && res.getCode()== 0) {
            familyList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-family.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddFamilyController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加教师信息");
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
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个学生的家庭信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);

    }

    public void deleteSelectedItems(){
        ArrayList<Integer> familyIdList = new ArrayList<Integer>();
        for (FamilyEntity item:
                selectedItemList) {
            familyIdList.add(item.getFamilyId());
        }
        DataRequest req = new DataRequest();
        req.put("familyId", familyIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/family/familyDeleteAll",req);
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
        if( !fatherAgeField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "父亲年龄格式不正确，不能添加！");
            return;
        }
        if( !familySizeField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "家庭人口格式不正确，不能添加！");
            return;
        }
        if( !fatherContactField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "父亲联系方式格式不正确，不能添加！");
            return;
        }
        if( !motherAgeField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "母亲年龄格式不正确，不能添加！");
            return;
        }
        if( !motherContactField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "母亲联系方式格式不正确，不能添加！");
            return;
        }
        FamilyEntity familyEntity =new FamilyEntity();
        familyEntity.setNum(numField.getText());
        familyEntity.setFamilySize(familySizeField.getText());
        familyEntity.setFatherName(fatherNameField.getText());
        familyEntity.setFatherOccupation(fatherOccupationField.getText());
        familyEntity.setFatherAge(fatherAgeField.getText());
        familyEntity.setFatherContact(fatherContactField.getText());
        familyEntity.setMotherName(motherNameField.getText());
        familyEntity.setMotherOccupation(motherOccupationField.getText());
        familyEntity.setMotherAge(motherAgeField.getText());
        familyEntity.setMotherContact(motherContactField.getText());

        DataRequest req = new DataRequest();
        req.put("familyId", familyId);
        req.putObject("form", familyEntity);
        DataResponse res = HttpRequestUtil.request("/api/family/familyEditSave",req);
        if(res.getCode() == 0) {
            familyId = CommonMethod.getIntegerFromObject(res.getData());
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
        byte[] bytes = HttpRequestUtil.requestByteData("/api/family/getFamilyListExcl", req);
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

    private List<FamilyEntity> getSelectedItem(){
        List<FamilyEntity> selectedItems = new ArrayList<FamilyEntity>();
        for (FamilyEntity items :
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

    /*private void resetFilter() {
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
*/
/*    private void setFilter(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("filter-family.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterTeacherController controller = fxmlLoader.getController(); // 获取控制器对象

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
    }*/

/*    public void onAddFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    public void onChangeFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    public void onResetFilterButtonClicked(ActionEvent actionEvent) {
        resetFilter();
        onQueryButtonClick();
    }*/

    public void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = getSelectedItem().size();
        if (selectedNumber == 0){
            dialogUtil.openError("导出失败", "未选中任何教师信息");
        }else {
            dialogUtil.openInfo("导出教师信息", "点击确认导出选中的 "+selectedNumber+" 条教师信息。", this::getExcel);
        }
    }
    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedTeacher",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/teacher/getSelectedTeacherListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("teacher.xlsx");
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
