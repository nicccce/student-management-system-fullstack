package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.request.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.util.List;

/**
 * DictionaryController 登录交互控制类 对应 base/dictionary-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class DictionaryController {
    @FXML
    private TreeTableView<MyTreeNode> treeTable;
    @FXML
    private TreeTableColumn<MyTreeNode, String> idColumn;
    @FXML
    private TreeTableColumn<MyTreeNode, String> valueColumn;
    @FXML
    private TreeTableColumn <MyTreeNode, String>labelColumn;

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        MyTreeNode root = HttpRequestUtil.requestTreeNode("/api/base/getDictionaryTreeNode",new DataRequest());
        if(root == null)
            return;
        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));
        labelColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("label"));
        valueColumn.setCellFactory(TextFieldTreeTableCell.<MyTreeNode>forTreeTableColumn());
        labelColumn.setCellFactory(TextFieldTreeTableCell.<MyTreeNode>forTreeTableColumn());
        TreeItem<MyTreeNode> rootNode = new TreeItem<>(root);
        MyTreeNode node;
        TreeItem<MyTreeNode> tNode, tNodes;
        List<MyTreeNode> sList;
        List<MyTreeNode> cList = root.getChildList();
        int i,j;
        for(i = 0;  i <cList.size();i++) {
            node = cList.get(i);
            tNode = new TreeItem<>(node);
            sList = node.getChildList();
            for(j = 0; j < sList.size();j++) {
                tNodes = new TreeItem<>(sList.get(j));
                tNode.getChildren().add(tNodes);
            }
            rootNode.getChildren().add(tNode);
        }
        rootNode.setExpanded(true);
        treeTable.setRoot(rootNode);
        treeTable.setPlaceholder(new Label("点击添加按钮增加一行"));
        treeTable.setEditable(true);
        treeTable.getSelectionModel().selectFirst();
        TreeTableView.TreeTableViewSelectionModel<MyTreeNode> tsm = treeTable.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener((ListChangeListener.Change<? extends Integer> change) -> {
            System.out.println("Row selection has changed");
        });
    }

    private void editItem(TreeItem<MyTreeNode> item) {
        int newRowIndex = treeTable.getRow(item);
        treeTable.scrollTo(newRowIndex);
        TreeTableColumn<MyTreeNode, ?> firstCol = treeTable.getColumns().get(0);
        treeTable.getSelectionModel().select(item);
        treeTable.getFocusModel().focus(newRowIndex, firstCol);
        treeTable.edit(newRowIndex, firstCol);
    }
    @FXML
    public void onAddButtonClick(){
        if (treeTable.getSelectionModel().isEmpty()) {
            System.out.println("Select a row to add.");
            MessageDialog.showDialog("选择一个要添加的的行");
            return;
        }
        TreeTableView.TreeTableViewSelectionModel<MyTreeNode> sm = treeTable.getSelectionModel();
        int rowIndex = sm.getSelectedIndex();
        TreeItem<MyTreeNode> selectedItem = sm.getModelItem(rowIndex);
        MyTreeNode node = selectedItem.getValue();
        DataRequest req = new DataRequest();
        req.put("pid",node.getId());
        req.put("label", "");
        MyTreeNode newNode = HttpRequestUtil.requestTreeNode("/api/base/newDictionaryTreeNode", req);
        if (newNode == null)
            return;
        node.getChildList().add(newNode);
        TreeItem<MyTreeNode> item = new TreeItem<>(node);
        selectedItem.getChildren().add(item);
        selectedItem.setExpanded(true);
        this.editItem(item);
    }
    @FXML
    public void onDeleteButtonClick(){
        TreeTableView.TreeTableViewSelectionModel<MyTreeNode> sm = treeTable.getSelectionModel();
        if (sm.isEmpty()) {
            MessageDialog.showDialog("没有选择，无法删除");
            return;
        }
        int rowIndex = sm.getSelectedIndex();
        TreeItem<MyTreeNode> selectedItem = sm.getModelItem(rowIndex);
        TreeItem<MyTreeNode> parent = selectedItem.getParent();
        if (parent == null) {
            MessageDialog.showDialog("不能删除根节点");
        }
            parent.getChildren().remove(selectedItem);
        MyTreeNode node = selectedItem.getValue();
        parent.getValue().getChildList().remove(node);
        DataRequest req = new DataRequest();
        req.put("id",node.getId());
        DataResponse res= HttpRequestUtil.request("/api/base/deleteDictionaryTreeNode", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    @FXML
    public void onSaveButtonClick() {
        TreeTableView.TreeTableViewSelectionModel<MyTreeNode> sm = treeTable.getSelectionModel();
        int rowIndex = sm.getSelectedIndex();
        TreeItem<MyTreeNode> selectedItem = sm.getModelItem(rowIndex);
        MyTreeNode node = selectedItem.getValue();
        DataRequest req = new DataRequest();
        req.put("id", node.getId());
        req.put("value",node.getValue());
        req.put("label",node.getLabel());
        DataResponse res = HttpRequestUtil.request("/api/base/saveDictionaryTreeNode", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("保存成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }

    }
}
