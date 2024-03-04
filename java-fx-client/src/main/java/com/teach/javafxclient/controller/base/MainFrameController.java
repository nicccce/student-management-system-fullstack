package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.AppStore;
import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * MainFrameController 登录交互控制类 对应 base/main-frame.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class MainFrameController {
    private Map<String,Tab> tabMap = new HashMap<String,Tab>();
    private Map<String,Scene> sceneMap = new HashMap<String,Scene>();
    private Map<String,ToolController> controlMap =new HashMap<String,ToolController>();
    @FXML
    private MenuBar menuBar;

    @FXML
    protected TabPane contentTabPane;


    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     * 系统初始时为没个角色增加了框架已经实现好了基础管理的功能，采用代码显示添加的方法加入，加入完缺省的功能菜单后，通过
     * HttpRequestUtil.request("/api/base/getMenuList",new DataRequest())加载用菜单管理功能，维护的菜单
     * 项目开发过程中，同学可以扩该方法，增肌自己设计的功能菜单，也可以通过菜单管理程序添加菜单，框架自动加载菜单管理维护的菜单，
     * 是新功能扩展
     */
    @FXML
    public void initialize() {
        Menu menu;
        MenuItem item;
        String role = AppStore.getJwt().getRoles();
        menu = new Menu("个人信息");
        item = new MenuItem();
        item.setText("修改密码");
        item.setOnAction(e->changeContent("base/password-panel","修改密码"));
        menu.getItems().add(item);
        if(role.equals("ROLE_STUDENT") || role.equals("ROLE_TEACHER")) {
            item = new MenuItem();
            item.setText("个人画像");
            if(role.equals("ROLE_STUDENT"))
                item.setOnAction(e -> changeContent("student-introduce-panel", "个人画像"));
            else
                item.setOnAction(e -> changeContent("teacher-introduce-panel", "个人画像"));
            menu.getItems().add(item);
        }
        item = new MenuItem();
        item.setText("退出");
        item.setOnAction(this::onLogoutMenuClick);
        menu.getItems().add(item);
        menuBar.getMenus().add(menu);
        if(role.equals("ROLE_ADMIN")) {
            menu = new Menu("系统管理");
            item = new MenuItem();
            item.setText("菜单管理");
            item.setOnAction(e -> changeContent("base/menu-panel", "菜单管理"));
            menu.getItems().add(item);
            item = new MenuItem();
            item.setText("字典管理");
            item.setOnAction(e -> changeContent("base/dictionary-panel", "字典管理"));
            menu.getItems().add(item);
            item = new MenuItem();
            item.setText("Html编辑");
            item.setOnAction(e -> changeContent("base/html-edit_panel", "HTML编辑"));
            menu.getItems().add(item);
            menuBar.getMenus().add(menu);
            menu = new Menu("示例程序");
            item = new MenuItem();
            item.setText("组件示例");
            item.setOnAction(e->changeContent("base/control-demo-panel","组件示例"));
            menu.getItems().add(item);
            menuBar.getMenus().add(menu);
        }
        menu = new Menu("编辑");
        item = new MenuItem();
        item.setText("新建");
        item.setOnAction(e->doNewCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("保存");
        item.setOnAction(e->doSaveCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("删除");
        item.setOnAction(e->doDeleteCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("打印");
        item.setOnAction(e->doPrintCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("导入");
        item.setOnAction(e->doImportCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("导出");
        item.setOnAction(e->doExportCommand());
        menu.getItems().add(item);
        item = new MenuItem();
        item.setText("测试");
        item.setOnAction(e->doTestCommand());
        menu.getItems().add(item);
        menuBar.getMenus().add(menu);
        DataResponse  res = HttpRequestUtil.request("/api/base/getMenuList",new DataRequest());
        List<Map> mList = (List<Map>)res.getData();
        int i,j;
        Map m;
        String title;
        List<Map> sList;
        for(i = 0; i < mList.size();i++) {
            m = mList.get(i);
            menu = new Menu((String)m.get("title"));
            sList = (List<Map>)m.get("sList");
            if(sList != null) {
                for (j = 0; j < sList.size(); j++) {
                    Map ms = sList.get(j);
                    item = new MenuItem();
                    item.setText( (String)ms.get("title"));
                    item.setOnAction(e -> changeContent((String) ms.get("name"), (String)ms.get("title")));
                    menu.getItems().add(item);
                }
            }
            menuBar.getMenus().add(menu);
        }
        contentTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    /**
     * 点击菜单栏中的“退出”菜单，执行onLogoutMenuClick方法 加载登录页面，切换回登录界面
     * @param event
     */
    protected void onLogoutMenuClick(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            MainApplication.resetStage("Login", scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击菜单栏中的菜单 执行changeContent 在主框架工作区增加和显示一个工作面板
     * @param name  菜单名 name.fxml 对应面板的配置文件
     * @param title 菜单标题 工作区中的TablePane的标题
     */
    protected  void changeContent(String name, String title) {
        Tab tab = tabMap.get(name);
        Scene scene;
        Object c;
        if(tab == null) {
            scene = sceneMap.get(name);
            if(scene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(name + ".fxml"));
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 768);
                    sceneMap.put(name, scene);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                c = fxmlLoader.getController();
                if(c instanceof ToolController) {
                    controlMap.put(name,(ToolController)c);
                }
            }
            tab = new Tab(title);
            tab.setId(name);
            tab.setOnSelectionChanged(this::tabSelectedChanged);
            tab.setOnClosed(this::tabOnClosed);
            tab.setContent(scene.getRoot());
            contentTabPane.getTabs().add(tab);
            tabMap.put(name, tab);
        }
        contentTabPane.getSelectionModel().select(tab);
    }

    public void tabSelectedChanged(Event e) {
        Tab tab = (Tab)e.getSource();
//        System.out.println("Selection changed event for " + tab.getText() +
//                " tab, selected = " + tab.isSelected());
        Node node =tab.getContent();
        Scene scene = node.getScene();
    }

    /**
     * 点击TablePane 标签页 的关闭图标 执行tabOnClosed方法
     * @param e
     */

    public void tabOnClosed(Event e) {
        Tab tab = (Tab)e.getSource();
        String name = tab.getId();
        contentTabPane.getTabs().remove(tab);
        tabMap.remove(name);
    }
    /**
     * ToolController getCurrentToolController() 获取当前显示的面板的控制对象， 如果面板响应编辑菜单中的编辑命名，交互控制需要继承 ToolController， 重写里面的方法
     * @return
     */
    public ToolController getCurrentToolController(){
        Iterator<String> iterator = controlMap.keySet().iterator();
        String name;
        Tab tab;
        while(iterator.hasNext()) {
            name = iterator.next();
            tab = tabMap.get(name);
            if(tab.isSelected()) {
                return controlMap.get(name);
            }
        }
        return null;
    }
    /**
     * 点击编辑菜单中的“新建”菜单，执行doNewCommand方法， 执行当前显示的面板对应的控制类中的doNew()方法
     */
    protected  void doNewCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doNew();
    }
    /**
     * 点击编辑菜单中的“保存”菜单，执行doSaveCommand方法， 执行当前显示的面板对应的控制类中的doSave()方法
     */
    protected  void doSaveCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doSave();
    }
    /**
     * 点击编辑菜单中的“删除”菜单，执行doDeleteCommand方法， 执行当前显示的面板对应的控制类中的doDelete()方法
     */
    protected  void doDeleteCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doDelete();
    }
    /**
     * 点击编辑菜单中的“打印”菜单，执行doPrintCommand方法， 执行当前显示的面板对应的控制类中的doPrint()方法
     */
    protected  void doPrintCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doPrint();
    }
    /**
     * 点击编辑菜单中的“导出”菜单，执行doExportCommand方法， 执行当前显示的面板对应的控制类中的doExport方法
     */
    protected  void doExportCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doExport();
    }
    /**
     * 点击编辑菜单中的“导入”菜单，执行doImportCommand方法， 执行当前显示的面板对应的控制类中的doImport()方法
     */
    protected  void doImportCommand(){
        ToolController c = getCurrentToolController();
        if(c == null)
            return;
        c.doImport();
    }
    /**
     * 点击编辑菜单中的“测试”菜单，执行doTestCommand方法， 执行当前显示的面板对应的控制类中的doImport()方法
     */
    protected  void doTestCommand(){
        ToolController c = getCurrentToolController();
        if(c == null) {
            c= new ToolController(){
            };
        }
        c.doTest();
    }
}
