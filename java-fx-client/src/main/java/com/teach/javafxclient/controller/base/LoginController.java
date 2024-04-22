package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.request.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 登录交互控制类，用于处理登录界面的交互逻辑。
 * 该类与FXML文件base/login-view.fxml相关联，用于控制登录界面的各个组件。
 */
public class LoginController {
    @FXML
    public Button loginButton;

    /**
     * 用户名输入框的引用，对应FXML文件中的fx:id="usernameField"。
     */
    @FXML
    private TextField usernameField;

    /**
     * 密码输入框的引用，对应FXML文件中的fx:id="passwordField"。
     */
    @FXML
    private TextField passwordField;

    /**
     * 舞台(Stage)的引用，用于管理当前窗口的显示和关闭等操作。
     */
    private Stage stage;

    /**
     * 根节点(Root)的引用，代表整个场景的根元素。
     */
    private Parent root;

    /**
     * 鼠标相对于窗口左上角的X轴偏移量。
     */
    private double offsetX;

    /**
     * 鼠标相对于窗口左上角的Y轴偏移量。
     */
    private double offsetY;

    /**
     * 设置舞台(Stage)的引用。
     *
     * @param stage 要设置的舞台对象
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * 设置根节点(Root)的引用，并使其可拖动。
     *
     * @param root 要设置的根节点对象
     */
    public void setRoot(Parent root) {
        this.root = root;
        this.makeRootDraggeble();
    }

    /**
     * 页面加载对象创建完成后的初始化方法。
     * 在此方法中设置用户名和密码输入框的初始值等。
     */
    @FXML
    public void initialize() {
        usernameField.setText("admin");
        // usernameField.setText("2022030001"); // 注释掉的代码示例
        passwordField.setText("123456");
    }

    /**
     * 登录按钮点击事件处理方法。
     * 获取用户名和密码，调用后台登录服务，根据登录结果执行相应的操作。
     */
    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LoginRequest loginRequest = new LoginRequest(username, password);
        String msg = HttpRequestUtil.login(loginRequest);
        if (msg != null) {
            MessageDialog.showDialog(msg);
            loginButton.setText("大香蕉");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/main-frame.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            MainApplication.finishLogin();
            MainApplication.resetStage("教学管理系统", scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 链接按钮点击事件处理方法。
     * 当前仅为示例，显示一个包含错误信息的对话框。
     *
     * @throws IOException 如果在对话框显示过程中发生I/O错误，则抛出此异常
     */
    @FXML
    protected void linkButtonClick() throws IOException {
        MessageDialog.showDialog("你的钢门比较松弛");//TODO: edit it!!!!
        return;
    }

    /**
     * 关闭按钮点击事件处理方法。
     * 用于关闭当前Stage（窗口）。
     */
    @FXML
    protected void  closeButtonClick() {
        stage.close(); // 关闭Stage（即窗口）
    }

    /**
     * 最小化按钮点击事件处理方法。
     * 将当前Stage（窗口）最小化到任务栏。
     */
    @FXML
    protected void minimizeButtonClick() {
        stage.setIconified(true);
    }

    /**
     * 使根节点可拖动的方法。
     * 为根节点添加鼠标事件监听器，实现窗口的拖动功能。
     * 当鼠标按下时记录偏移量，鼠标拖动时根据偏移量更新窗口位置，
     * 鼠标释放时恢复窗口透明度。
     */
    protected void makeRootDraggeble()
    {
        root.setOnMousePressed(e -> {
            offsetX = e.getSceneX();
            offsetY = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - offsetX);
            stage.setY(e.getScreenY() - offsetY);
            //拖动时增加透明度
            stage.setOpacity(0.9);
        });

        root.setOnMouseReleased(e -> {
            stage.setOpacity(1.0);
        });
    }
}