package com.teach.javafxclient.controller.base;


import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * HtmlEditController 登录交互控制类 对应 base/html-edit-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class HtmlEditController implements Initializable {
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private TextArea textArea;
    @FXML
    private WebView webView;
    @FXML
    private Pagination pagination;

    private PdfModel model;

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
    public void textToHtmlButtonClick(){
        htmlEditor.setHtmlText(textArea.getText());
    }
    @FXML
    public void htmlToTextButtonClick(){
        textArea.setText(htmlEditor.getHtmlText());
    }

    /**
     *  导出HTML信息到本地文件
     */
    @FXML
    public void exportButtonClick(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择要保存的文件！");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("html 文件", "*.html"));
        File file = fileDialog.showSaveDialog(null);
        try {
            String text = textArea.getText();
            String str;
            StringTokenizer sz = new StringTokenizer(text,"\n");
            PrintWriter out = new PrintWriter(new FileWriter(file));
            while(sz.hasMoreTokens()) {
                str = sz.nextToken();
                out.println(str);
            };
            out.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载本地的文件到文本编辑器
     */
    @FXML
    public void importButtonClick(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择要导入的Html文件！");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("html 文件", "*.html"));
        File file = fileDialog.showOpenDialog(null);
        try {
            String text = "";
            String str;
            BufferedReader in = new BufferedReader(new FileReader(file));
            do{
                str = in.readLine();
                if(str != null)
                    text += str + "\n";
            }while(str!= null);
            in.close();
            textArea.setText(text);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看html页面显示效果和PDF生成效果
     */
    @FXML
    public void viewButtonClick(){
        String html = textArea.getText();
        int htmlCount = HttpRequestUtil.uploadHtmlString(html);
        WebEngine webEngine = webView.getEngine();
        webEngine.load(HttpRequestUtil.serverUrl+"/api/base/getHtmlPage?htmlCount="+ htmlCount);
        DataRequest req = new DataRequest();
        req.put("htmlCount",htmlCount);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/base/getPdfData", req);
        if (bytes != null) {
            try {
                model = new PdfModel(bytes);
                pagination.setPageCount(model.numPages());
                pagination.setPageFactory(index -> new ImageView(model.getImage(index)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

}