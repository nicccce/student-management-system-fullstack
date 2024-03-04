package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
/**
 * StudentIntroduceController 登录交互控制类 对应 student-introduce-panel..fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 *  该功能是为学生提供生成个人简历的是示例成程序，继承了多种布局容器和多种组件，集成PDF生成转换技术。学生可以在此程序的基础上进行修改，扩展，
 *  在简单熟悉HTML的基础上，构建比较美观丰富的个人简历，并生成和下载PDF文件
 */
public class StudentIntroduceController extends ToolController {
    private ImageView photoImageView;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();

    @FXML
    private HTMLEditor introduceHtml; //个人简历HTML编辑器
    @FXML
    private Button photoButton;  //照片显示和上传按钮
    @FXML
    private Label num;  //学号标签
    @FXML
    private Label name;//姓名标签
    @FXML
    private Label dept; //学院标签
    @FXML
    private Label major; //专业标签
    @FXML
    private Label className; //班级标签
    @FXML
    private Label card;  //证件号码标签
    @FXML
    private Label gender; //性别标签
    @FXML
    private Label birthday; //出生日期标签
    @FXML
    private Label email; //邮箱标签
    @FXML
    private Label phone; //电话标签
    @FXML
    private Label address; //地址标签
    @FXML
    private TableView<Map> scoreTable;  //成绩表TableView
    @FXML
    private TableColumn<Map,String> courseNumColumn;  //课程号列
    @FXML
    private TableColumn<Map,String> courseNameColumn; //课程名列
    @FXML
    private TableColumn<Map,String> creditColumn; //学分列
    @FXML
    private TableColumn<Map,String> markColumn; //成绩列
    @FXML
    private TableColumn<Map,String> rankingColumn; //排名列

    @FXML
    private BarChart<String,Number> barChart;  //消费直方图控件
    @FXML
    private PieChart pieChart;   //成绩分布饼图控件
    private Integer studentId = null;  //学生主键
    private Integer personId = null;  //学生关联人员主键

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        photoImageView = new ImageView();
        photoImageView.setFitHeight(100);
        photoImageView.setFitWidth(100);
        photoButton.setGraphic(photoImageView);
        courseNumColumn.setCellValueFactory(new MapValueFactory("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        rankingColumn.setCellValueFactory(new MapValueFactory<>("ranking"));

        getIntroduceData();
    }

    /**
     * getIntroduceData 从后天获取当前学生的所有信息，不传送的面板各个组件中
     */
    public void getIntroduceData(){
        DataRequest req = new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStudentIntroduceData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        studentId = CommonMethod.getInteger(info,"studentId");
        personId = CommonMethod.getInteger(info,"personId");
        num.setText(CommonMethod.getString(info,"num"));
        name.setText(CommonMethod.getString(info,"name"));
        dept.setText(CommonMethod.getString(info,"dept"));
        major.setText(CommonMethod.getString(info,"major"));
        className.setText(CommonMethod.getString(info,"className"));
        card.setText(CommonMethod.getString(info,"card"));
        gender.setText(CommonMethod.getString(info,"genderName"));
        birthday.setText(CommonMethod.getString(info,"birthday"));
        email.setText(CommonMethod.getString(info,"email"));
        phone.setText(CommonMethod.getString(info,"phone"));
        address.setText(CommonMethod.getString(info,"address"));
        introduceHtml.setHtmlText(CommonMethod.getString(info,"introduce"));
        List<Map> scoreList= (List)data.get("scoreList");
        List<Map>markList = (List)data.get("markList");
        List<Map>feeList = (List)data.get("feeList");
        for (Map m: scoreList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        scoreTable.setItems(observableList);  // 成绩表数据显示

        req.put("fileName", "photo/" + personId + ".jpg");  //个人照片显示
        byte[] bytes = HttpRequestUtil.requestByteData("/api/base/getImageByteData", req);
        if (bytes != null) {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            Image img = new Image(in);
            photoImageView.setImage(img);
        }

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for(Map m:markList) {
            chartData.add(new PieChart.Data(m.get("title").toString(),Double.parseDouble(m.get("value").toString())));
        }
        pieChart.setData(chartData);  //成绩分类表显示

        XYChart.Series<String, Number> seriesFee = new XYChart.Series<>();
        seriesFee.setName("日常消费");
        for(Map m:feeList)
            seriesFee.getData().add(new XYChart.Data<>(m.get("title").toString(),Double.parseDouble(m.get("value").toString())));
        ObservableList<XYChart.Series<String, Number>> barData =
                FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        barData.add(seriesFee);
        barChart.setData(barData); //消费数据直方图展示

    }

    /**
     * 点击保存按钮 执行onSubmitButtonClick 调用doSave 实现个人简历保存
     */
    @FXML
    public void onSubmitButtonClick(){
        doSave();
    }

    /**
     * 显示生成的个人简历的PDF， 可以直接将PDF数据存入本地文件参见StudentController 中的doExpert 方法中的本地文件保存
     * 后台修改完善扩展PDF内容的生成方法，可以按照HTML语法生成PDF要展示的数据内容
     */
    @FXML
    public void onIntroduceDownloadClick(){
        DataRequest req = new DataRequest();
        req.put("studentId",studentId);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentIntroducePdf", req);
        if (bytes != null) {
            try {
                MessageDialog.pdfViewerDialog(bytes);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *  点击图片位置，可以重现上传图片，可在本地目录选择要上传的张片进行上传
     */
    @FXML
    public void onPhotoButtonClick(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("图片上传");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG 文件", "*.jpg"));
        File file = fileDialog.showOpenDialog(null);
        DataResponse res =HttpRequestUtil.uploadFile(file.getPath(),"photo/" + personId + ".jpg");
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 保存个人简介数据到数据库里
     */

    public void doSave(){
        String introduce = introduceHtml.getHtmlText();
        DataRequest req = new DataRequest();
        req.put("studentId",studentId);
        req.put("introduce",introduce);
        DataResponse res = HttpRequestUtil.request("/api/student/saveStudentIntroduce", req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("提交成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * 数据导入示例，点击编辑菜单中的导入菜单执行该方法， doImport 重写了 ToolController 中的doImport
     * 该方法从本地选择Excl文件，数据上传到后台，后台从Excl格式的数据流中解析出日期和金额添加更新学生的消费记录
     */
    public void doImport(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择消费数据表");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "studentId="+studentId;
        DataResponse res =HttpRequestUtil.importData("/api/student/importFeeData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}