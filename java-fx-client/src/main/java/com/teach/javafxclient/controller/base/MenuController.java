package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.request.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
/**
 * MenuController 登录交互控制类 对应 base/menu-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class MenuController {
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<OptionItem> roleChoiceBox;
    @FXML
    private TreeView<MyTreeNode> menuTreeView;
    @FXML
    private TextField nodeNameField;
    @FXML
    private TextField nodeTitleField;

    private MyTreeNode currentNode= null;

    private TreeItem<MyTreeNode> getTreeItem(MyTreeNode node) {
        TreeItem<MyTreeNode> item = new TreeItem<>(node);
        List<MyTreeNode> sList = node.getChildList();
        if (sList == null)
            return item;
        for (int i = 0; i < sList.size(); i++) {
            item.getChildren().add(getTreeItem(sList.get(i)));
        }
        return item;
    }
    private Integer getUserTypeId() {
        SingleSelectionModel<OptionItem> select = roleChoiceBox.getSelectionModel();
        if(select.isEmpty())
            return null;
        OptionItem item = select.getSelectedItem();
        return  item.getId();
    }

    public void valueChanged(TreeItem.TreeModificationEvent<MyTreeNode> e) {
        MyTreeNode nodeValue = e.getSource().getValue();
        System.out.println(nodeValue);
    }

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        List<OptionItem> optionList = HttpRequestUtil.requestOptionItemList("/api/base/getRoleOptionItemList", new DataRequest());
        roleChoiceBox.getItems().addAll(optionList);
        roleChoiceBox.getSelectionModel().select(0);
        updateTreeView(1);
    }
    public void updateTreeView(Integer userTypeId){
        DataRequest req = new DataRequest();
        req.put("userTypeId", userTypeId);
        MyTreeNode node = HttpRequestUtil.requestTreeNode("/api/base/getMenuTreeNode", req);
        if (node == null)
            return;
        TreeItem<MyTreeNode> root = getTreeItem(node);
        menuTreeView.setRoot(root);
    }
    public void updateNodePanel(MyTreeNode node){
        currentNode = node;
        if(currentNode == null) {
            nodeNameField.setText("");
            nodeTitleField.setText("");
        }else {
            nodeNameField.setText(currentNode.getValue());
            nodeTitleField.setText(currentNode.getLabel());
        }
    }
    @FXML
    protected void onQueryButtonClick() {
        updateTreeView(getUserTypeId());
    }
    @FXML
    protected void onAddButtonClick() {
        String title = titleTextField.getText();
        if (title == null || title.trim().equals("")) {
            MessageDialog.showDialog("菜单名为空，不能添加");
            return;
        }
        TreeItem<MyTreeNode> parent = menuTreeView.getSelectionModel().getSelectedItem();
        if (parent == null) {
            MessageDialog.showDialog("选择一个要添加的父节点");
            return;
        }
        MyTreeNode node = parent.getValue();
        DataRequest req = new DataRequest();
        req.put("userTypeId", getUserTypeId());
        req.put("pid",node.getId());
        req.put("title", title);
        MyTreeNode newNode = HttpRequestUtil.requestTreeNode("/api/base/newMenuTreeNode", req);
        if (newNode == null)
            return;
        node.getChildList().add(newNode);
        TreeItem<MyTreeNode> newItem = new TreeItem<>(newNode);
        parent.getChildren().add(newItem);
        if (!parent.isExpanded()) {
            parent.setExpanded(true);
        }
        titleTextField.setText("");
        updateNodePanel(newNode);
    }
    @FXML
    protected void onEditButtonClick() {
        TreeItem<MyTreeNode> item = menuTreeView.getSelectionModel().getSelectedItem();
        if (item == null) {
            MessageDialog.showDialog("没有选择，无法修改");
            return;
        }
        updateNodePanel(item.getValue());
    }
    @FXML
    protected void onDeleteButtonClick() {
        TreeItem<MyTreeNode> item = menuTreeView.getSelectionModel().getSelectedItem();
        if (item == null) {
            MessageDialog.showDialog("没有选择，无法删除");
            return;
        }
        TreeItem<MyTreeNode> parent = item.getParent();
        if (parent == null ) {
            MessageDialog.showDialog("不能删除根节点");
        } else {
            MyTreeNode node = parent.getValue();
            DataRequest req = new DataRequest();
            req.put("id",node.getId());
            DataResponse res= HttpRequestUtil.request("/api/base/deleteMenuTreeNode", req);
            if(res.getCode() == 0) {
                MessageDialog.showDialog("删除成功！");
            }else {
                MessageDialog.showDialog(res.getMsg());
            }
            parent.getChildren().remove(item);
        }
    }
    @FXML
    protected void onSubmitButtonClick() {
        currentNode.setValue(nodeNameField.getText());
        currentNode.setLabel(nodeTitleField.getText());
        DataRequest req = new DataRequest();
        req.put("id", currentNode.getId());
        req.put("name",currentNode.getValue());
        req.put("title",currentNode.getLabel());
        DataResponse res = HttpRequestUtil.request("/api/base/saveMenuTreeNode", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("保存成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
}