package com.teach.javafxclient;

import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.SQLiteJDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("登录");
        stage.setScene(scene);
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


    public static void main(String[] args) {
        launch();
    }
}