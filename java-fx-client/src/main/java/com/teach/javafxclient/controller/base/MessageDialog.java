package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MessageDialog 消息对话框工具类 可以显示提示信息，用户选择确认信息和PDF显示
 */
public class MessageDialog {
    public final static int CHOICE_OK = 1;
    public final static int CHOICE_CANCEL = 2;
    public final static int CHOICE_YES = 3;
    public final static int CHOICE_NO = 4;

    private  MessageController messageController= null;
    private  ChoiceController choiceController= null;
    private  PdfViewerController pdfViewerController = null;
    private static MessageDialog instance = new MessageDialog();

    /**
     * 初始加载三个页面
     * base/message-dialog.fxml  用于显示提示信息
     * base/choice-dialog.fxml 用于选择 是，否 取消 消息对话框
     * base/pdf-viewer-dialog.fxml 查看PDF效果对话框
     */
    private MessageDialog() {
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        Stage stage;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/message-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 300, 260);
            stage = new Stage();
            stage.initOwner(null);
            stage.setScene(scene);
            stage.setTitle("信息显示对话框");
            messageController = (MessageController) fxmlLoader.getController();
            messageController.setStage(stage);

            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/choice-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 300, 260);
            stage = new Stage();
            stage.initOwner(null);
            stage.setScene(scene);
            stage.setTitle("信息显示对话框");
            choiceController = (ChoiceController) fxmlLoader.getController();
            choiceController.setStage(stage);

            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/pdf-viewer-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 820);
            stage = new Stage();
            stage.initOwner(null);
            stage.setScene(scene);
            stage.setTitle("PDF显示对话框");
            pdfViewerController = (PdfViewerController) fxmlLoader.getController();
            pdfViewerController.setStage(stage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * showDialog 信息提示
     * @param msg  提示的信息
     */
    public static void showDialog(String msg) {
        if(instance == null)
            return;
        if(instance.messageController == null)
            return;
        instance.messageController.showDialog(msg);
    }
    /**
     * choiceDialog 显示提示信息和是 否 取消按钮， 用户可选择
     * 点击 是 返回 CHOICE_YES = 3;
     * 点击 否 返回 CHOICE_NO = 4;
     * 点击 取消 返回 CHOICE_CANCEL = 2;
     * @param msg  提示的信息
     */
    public static int choiceDialog(String msg) {
        if(instance == null)
            return 0;
        if(instance.choiceController == null)
            return 0;
        return instance.choiceController.choiceDialog(msg);
    }

    /**
     * pdfViewerDialog 显示PDF
     * @param data  PDF二进制数据
     */
    public static void pdfViewerDialog(byte []data) {
        if(instance == null)
            return ;
        if(instance.pdfViewerController == null)
            return ;
        instance.pdfViewerController.showPdf(data);
    }

}
