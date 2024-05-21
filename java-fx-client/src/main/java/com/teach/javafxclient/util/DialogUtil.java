package com.teach.javafxclient.util;

import com.teach.javafxclient.controller.demo.model.Model;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * 实用工具类，用于显示各种类型的对话框。
 */
public class DialogUtil {
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;

    private Runnable confirmFunction;

    private final Runnable emptyRunnable = () -> {
        // 空操作
    };

    public DialogUtil() {

        Platform.runLater(() -> {
            this.dialogContent = MFXGenericDialogBuilder.build()
                    .setContentText(Model.ipsum)
                    .makeScrollable(true)
                    .get();
            this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder()
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(true)
                    .setTitle("Dialogs Preview")
                    .setScrimPriority(ScrimPriority.WINDOW)
                    .setScrimOwner(true)
                    .get();

            dialogContent.addActions(
                    Map.entry(new MFXButton("确认"), event -> {
                        closeDialog();
                        confirmFunction.run();
                    }),
                    Map.entry(new MFXButton("取消"), event -> {
                        closeDialog();
                    })
            );

            dialogContent.setMaxSize(400, 200);
        });
    }

    private void  closeDialog(){
        dialog.close();
    }

    /**
     * 打开信息对话框。
     *
     * @param headerText      对话框的标题文本
     * @param contentText     对话框的内容文本
     * @param confirmFunction 确认按钮点击时要执行的函数，如果为 null，则确认按钮变为关闭按钮
     */
    public void openInfo(String headerText, String contentText, Runnable confirmFunction) {
        MFXFontIcon infoIcon = new MFXFontIcon("mfx-info-circle-filled", 18);
        dialogContent.setHeaderIcon(infoIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText("\n"+contentText);
        if (confirmFunction!=null) {
            this.confirmFunction = confirmFunction;
        }else {
            this.confirmFunction = this::closeDialog;
        }
        convertDialogTo("mfx-info-dialog");
        dialog.showDialog();
    }

    /**
     * 打开警告对话框。
     *
     * @param headerText      对话框的标题文本
     * @param contentText     对话框的内容文本
     * @param confirmFunction 确认按钮点击时要执行的函数，如果为 null，则确认按钮变为关闭按钮
     */
    public void openWarning(String headerText, String contentText, Runnable confirmFunction) {
        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText("\n"+contentText);
        if (confirmFunction!=null) {
            this.confirmFunction = confirmFunction;
        }else {
            this.confirmFunction = this::closeDialog;
        }
        convertDialogTo("mfx-warn-dialog");
        dialog.showDialog();
    }

    /**
     * 打开错误对话框。
     *
     * @param headerText      对话框的标题文本
     * @param contentText     对话框的内容文本
     * @param confirmFunction 确认按钮点击时要执行的函数，如果为 null，则确认按钮变为关闭按钮
     */
    public void openError(String headerText, String contentText, Runnable confirmFunction) {
        MFXFontIcon errorIcon = new MFXFontIcon("mfx-exclamation-circle-filled", 18);
        dialogContent.setHeaderIcon(errorIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText("\n"+contentText);
        if (confirmFunction!=null) {
            this.confirmFunction = confirmFunction;
        }else {
            this.confirmFunction = this::closeDialog;
        }
        convertDialogTo("mfx-error-dialog");
        dialog.showDialog();
    }

    /**
     * 打开通用对话框。
     *
     * @param headerText      对话框的标题文本
     * @param contentText     对话框的内容文本
     * @param confirmFunction 确认按钮点击时要执行的函数，如果为 null，则确认按钮变为关闭按钮
     */
    public void openGeneric(String headerText, String contentText, Runnable confirmFunction) {
        dialogContent.setHeaderIcon(null);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText("\n"+contentText);
        if (confirmFunction!=null) {
            this.confirmFunction = confirmFunction;
        }else {
            this.confirmFunction = this::closeDialog;
        }
        convertDialogTo(null);
        dialog.showDialog();
    }

    /**
     * 打开信息对话框。
     *
     * @param headerText  对话框的标题文本
     * @param contentText 对话框的内容文本
     */
    public void openInfo(String headerText, String contentText) {
        openInfo(headerText, contentText, emptyRunnable);
    }

    /**
     * 打开警告对话框。
     *
     * @param headerText  对话框的标题文本
     * @param contentText 对话框的内容文本
     */
    public void openWarning(String headerText, String contentText) {
        openWarning(headerText, contentText, emptyRunnable);
    }

    /**
     * 打开错误对话框。
     *
     * @param headerText  对话框的标题文本
     * @param contentText 对话框的内容文本
     */
    public void openError(String headerText, String contentText) {
        openError(headerText, contentText, emptyRunnable);
    }

    /**
     * 打开通用对话框。
     *
     * @param headerText      对话框的标题文本
     * @param contentText     对话框的内容文本
     */
    public void openGeneric(String headerText, String contentText) {
        openGeneric(headerText, contentText, emptyRunnable);
    }


    private void convertDialogTo(String styleClass) {
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );

        if (styleClass != null)
            dialogContent.getStyleClass().add(styleClass);
    }
}
