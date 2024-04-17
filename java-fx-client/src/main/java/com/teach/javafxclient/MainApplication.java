package com.teach.javafxclient;

import com.teach.javafxclient.controller.base.LoginController;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.SQLiteJDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, -1, -1);
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
        stage.show();
        stage.setOnCloseRequest(event -> {
            HttpRequestUtil.close();
        });
        mainStage = stage;
    }

    /**
     * 给舞台设置新的Scene
     * @param name 标题
     * @param scene 新的场景对象
     */
    public static void resetStage(String name, Scene scene) {
        mainStage.setTitle(name);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }

    public static void  finishLogin(){
        mainStage.close();
        mainStage = new Stage();
    }

    public static void main(String[] args) {
        launch();
    }
}