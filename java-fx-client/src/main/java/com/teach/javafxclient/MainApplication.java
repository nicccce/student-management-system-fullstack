package com.teach.javafxclient;

import com.teach.javafxclient.controller.base.LoginController;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.SQLiteJDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * 应用的主程序 MainApplication 按照编程规范，需继承Application 重写start 方法 主方法调用Application的launch() 启动程序
 */
public class MainApplication extends Application {
    /**
     * 加载登录对话框，设置登录Scene到Stage,显示该场景
     * @param stage
     * @throws IOException
     */
    public static Stage mainStage;
    public static Image icon = new Image(MainApplication.class.getResourceAsStream("/com/teach/javafxclient/picture/icon.png"));


    /**
     * 当应用程序启动时调用的start方法。
     * @param stage 主舞台
     * @throws IOException 如果发生I/O错误
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 474);
        //以下为优化ui，使其变得圆润
        setLoginUI(fxmlLoader, root, scene);
        mainStage.show();
        mainStage.setOnCloseRequest(event -> {
            HttpRequestUtil.close();
        });

    }

    /**
     * 给窗口设置新的Scene
     * @param name 标题
     * @param scene 新的场景对象
     */
    public static void resetStage(String name, Scene scene) {
        mainStage.setTitle(name);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }

    /**
     * 重置窗口的大小。
     * @param width 宽度
     * @param height 高度
     */
    public static void resetSize(int width, int height) {
        // 设置窗口大小
        mainStage.setWidth(width);
        mainStage.setHeight(height);

        // 获取屏幕尺寸
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // 计算居中位置
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - mainStage.getWidth()) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - mainStage.getHeight()) / 2;

        // 设置窗口位置
        mainStage.setX(centerX);
        mainStage.setY(centerY);
    }

    /**
     * 完成登录关闭登录窗口。
     */
    public static void  finishLogin(){
        mainStage.close();
        mainStage = new Stage();
        mainStage.getIcons().add(icon);
    }

    /**
     * 设置登录界面的UI。
     * @param fxmlLoader FXMLLoader对象
     * @param root 根节点
     * @param scene 场景对象
     * @throws IOException 如果发生I/O错误
     */
    public static void  setLoginUI(FXMLLoader fxmlLoader, Parent root, Scene scene) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle("登录");
        stage.setScene(scene);
        //以下为优化ui，使其变得圆润
        root.setStyle("-fx-background-radius:50;");
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);//设置窗口不可放大
        stage.initStyle(StageStyle.UNDECORATED);//隐藏标题栏
        stage.initStyle(StageStyle.TRANSPARENT);//定义stage为透明
        // 获取控制器实例
        LoginController controller = fxmlLoader.getController();
        // 将Stage引用传递给控制器
        controller.setStage(stage);
        controller.setRoot(root);
        mainStage.close();
        mainStage = stage;
    }

    /**
     * 应用程序的入口点。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch();
    }
}