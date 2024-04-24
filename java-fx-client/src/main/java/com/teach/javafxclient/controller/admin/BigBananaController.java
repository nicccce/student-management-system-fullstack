package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.MainApplication;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

import static java.lang.Math.min;

public class BigBananaController {
    @FXML
    public MediaView youAreTreated;
    public AnchorPane anchorPane;


    @FXML
    public void initialize(){
        youAreTreated.setFitHeight(min(anchorPane.getWidth()*1080/1440,anchorPane.getHeight()));
        youAreTreated.setFitWidth(min(anchorPane.getHeight()*1440/1080,anchorPane.getWidth()));

        // 监听窗口面板宽度变化
        MainApplication.mainStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 更新GUI组件
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        anchorPane.setMaxHeight(newValue.doubleValue());
                        youAreTreated.setFitHeight(min(anchorPane.getWidth()*1080/1440,anchorPane.getHeight()));
                        youAreTreated.setFitWidth(min(anchorPane.getHeight()*1440/1080,anchorPane.getWidth()));
                        System.out.println(anchorPane.getWidth());
                    }
                });
            }
        });
        MainApplication.mainStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 更新GUI组件
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        anchorPane.setMaxHeight(anchorPane.getHeight()+newValue.doubleValue()-oldValue.doubleValue());
                        youAreTreated.setFitHeight(min(anchorPane.getWidth()*1080/1440,anchorPane.getHeight()));
                        youAreTreated.setFitWidth(min(anchorPane.getHeight()*1440/1080,anchorPane.getWidth()));
                    }
                });
            }
        });

    }
}
