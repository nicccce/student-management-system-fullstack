package com.teach.javafxclient.controller.base;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * PdfViewerController 登录交互控制类 对应 base/pdf-view-dialog.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class PdfViewerController implements Initializable {

    @FXML
    private Pagination pagination;
    private PdfModel model;

    private Stage stage;

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void showPdf(byte[] data) {
//        model = new PdfModel(Paths.get("/teach/java2/2.pdf"));
        model = new PdfModel(data);
        pagination.setPageCount(model.numPages());
        pagination.setPageFactory(index -> new ImageView(model.getImage(index)));
        this.stage.show();
    }

}