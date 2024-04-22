package com.teach.javafxclient.controller.admin;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

import static java.lang.Math.min;

public class BigBananaController {
    @FXML
    public MediaView youAreTreated;
    @FXML
    public VBox vBox;
    @FXML
    public void initialize(){
        youAreTreated.setFitHeight(min(vBox.getWidth()*1080/1440,vBox.getHeight()));
        youAreTreated.setFitWidth(min(vBox.getHeight()*1440/1080,vBox.getWidth()));
        System.out.println(vBox.getWidth());
        System.out.println(vBox.getHeight());
        // 监听窗口面板宽度变化
        vBox.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 更新GUI组件
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        youAreTreated.setFitHeight(min(vBox.getWidth()*1080/1440,vBox.getHeight()));
                        youAreTreated.setFitWidth(min(vBox.getHeight()*1440/1080,vBox.getWidth()));
                        System.out.println(vBox.getWidth());
                        System.out.println(vBox.getHeight());
                    }
                });
            }
        });
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 更新GUI组件
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        youAreTreated.setFitHeight(min(vBox.getWidth()*1080/1440,vBox.getHeight()));
                        youAreTreated.setFitWidth(min(vBox.getHeight()*1440/1080,vBox.getWidth()));
                        System.out.println(vBox.getWidth());
                        System.out.println(vBox.getHeight());
                    }
                });
            }
        });

    }
}
