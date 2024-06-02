package com.teach.javafxclient.controller.student;

import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.model.CourseEntity;
import com.teach.javafxclient.model.TeacherEntity;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.DialogUtil;
import com.teach.javafxclient.util.LocalDateUtil;
import com.teach.javafxclient.util.ScheduleUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class curriculumController {
    public Label contentLabel;
    public Label notificationCircle;
    public WebView announcementWebView;
    public Label numLabel;
    public Label nameLabel;
    public Label departmentLabel;
    public Label typeLabel;
    public Label creditLabel;
    public Label locationLabel;
    public Label teacherLabel;
    public Label beginTimeLabel;
    public Label endTimeLabel;
    public Label ScheduleLabel;
    public Label introductionLabel;
    public DatePicker beginTimePick;
    public DatePicker endTimePick;

    @FXML
    private MFXButton button11;

    @FXML
    private MFXButton button12;

    @FXML
    private MFXButton button13;

    @FXML
    private MFXButton button14;

    @FXML
    private MFXButton button15;

    @FXML
    private MFXButton button21;

    @FXML
    private MFXButton button22;

    @FXML
    private MFXButton button23;

    @FXML
    private MFXButton button24;

    @FXML
    private MFXButton button25;

    @FXML
    private MFXButton button31;

    @FXML
    private MFXButton button32;

    @FXML
    private MFXButton button33;

    @FXML
    private MFXButton button34;

    @FXML
    private MFXButton button35;

    @FXML
    private MFXButton button41;

    @FXML
    private MFXButton button42;

    @FXML
    private MFXButton button43;

    @FXML
    private MFXButton button44;

    @FXML
    private MFXButton button45;

    @FXML
    private MFXButton button51;

    @FXML
    private MFXButton button52;

    @FXML
    private MFXButton button53;

    @FXML
    private MFXButton button54;

    @FXML
    private MFXButton button55;

    @FXML
    private MFXButton button61;

    @FXML
    private MFXButton button62;

    @FXML
    private MFXButton button63;

    @FXML
    private MFXButton button64;

    @FXML
    private MFXButton button65;

    @FXML
    private MFXButton button71;

    @FXML
    private MFXButton button72;

    @FXML
    private MFXButton button73;

    @FXML
    private MFXButton button74;

    @FXML
    private MFXButton button75;
    private List<CourseEntity> courseList;

    private Long schedule;

    private Integer courseType;

    private List<String> announcementList = new ArrayList<>(){};

    private DialogUtil dialogUtil = new DialogUtil();

    String beginDate = "0000-01-01";
    String endDate = "9999-01-01";

    HttpRequestUtil<CourseEntity> httpRequestUtil = new HttpRequestUtil<>(CourseEntity.class);

    @FXML
    public void initialize() {
        DataResponse<ArrayList<CourseEntity>> res;
        DataRequest req =new DataRequest();
        res = httpRequestUtil.requestArrayList("/api/course/getStudentCourse",req); //从后台获取所有课程信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = res.getData();
        }
        beginTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        endTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

        setUpCurriculum();
        setUpAnnouncement();
    }

    public void onQueryButtonClick(ActionEvent actionEvent) {
        DataResponse<ArrayList<CourseEntity>> res;
        DataRequest req =new DataRequest();
        res = httpRequestUtil.requestArrayList("/api/course/getStudentCourse",req); //从后台获取所有课程信息列表集合
        if(res != null && res.getCode()== 0) {
            courseList = res.getData();
        }
        beginDate = beginTimePick.getEditor().getText();
        endDate = endTimePick.getEditor().getText();
        if (!LocalDateUtil.isLaterDate(beginDate,endDate)){
            dialogUtil.openError("日期错误","请选择合理的日期区间");
            return;
        }
        reSetCurriculum();
        setUpCurriculum();
        setUpAnnouncement();
    }

    private void reSetCurriculum(){
        announcementList.clear();
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                MFXButton button = getButtonByIndex(i, j);
                button.setId("unselected-custom");
                button.setText("");
            }
        }
    }

    private void setUpCurriculum(){
        if (courseList==null) return;
        for (CourseEntity course :
                courseList) {
            if (LocalDateUtil.isIntervalContained(course.getBeginTime(),course.getEndTime(),beginDate,endDate)){
                courseType = mapCourseType(course.getType());
                if (course.getSchedule() != 0L) {
                    long schedule = course.getSchedule();
                    for (int i = 1; i <= 7; i++) {
                        for (int j = 1; j <= 5; j++) {
                            if ((schedule & 1) == 1) {
                                MFXButton button = getButtonByIndex(i, j);
                                if (Objects.equals(button.getId(), "unselected-custom")) {
                                    setButton(i, j, course);
                                } else {
                                    System.out.println(extractNum(button.getText()));
                                    addAnnouncement(course,
                                            getCourseByNum(extractNum(button.getText())), i, j);
                                }
                            }
                            schedule >>= 1;
                        }
                    }
                }
            }
        }
    }

    public void setButton(Integer day, Integer period, CourseEntity course) {
        MFXButton clickedButton = getButtonByIndex(day,period);
        clickedButton.setId("schedule-custom"+courseType.toString());
        clickedButton.setText(course.getNum() + '\n' + course.getName());
    }

    private void setSchedule(Integer day, Integer period, Integer value){
        Integer index = (day-1)*5+period;
        schedule = schedule
                - (schedule & (1L << (index -1)))
                + ((long) value << (index - 1));
    }

    private CourseEntity getCourseByNum(String courseNum){
        CourseEntity course = null;
        DataResponse<CourseEntity> res;
        DataRequest req =new DataRequest();
        res = httpRequestUtil.requestObject("/api/course/getCourseByNum/" + courseNum,req); //从后台获取所有课程信息列表集合
        if(res != null) {
            if (res.getCode()== 0) {
                course = res.getData();
            }else {
                dialogUtil.openError("加载失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("加载失败","服务器无响应，无法加载课程信息！");
        }
        return course;
    }

    private void setUpInfo(String courseNum){
        CourseEntity course = getCourseByNum(courseNum);
        if (course.getNum() != null) {
            numLabel.setText(course.getNum());
        }

        if (course.getName() != null) {
            nameLabel.setText(course.getName());
        }

        if (course.getDepartment() != null) {
            departmentLabel.setText(course.getDepartment());
        }

        if (course.getType() != null) {
            typeLabel.setText(course.getType());
        }

        if (course.getCredit() != null) {
            creditLabel.setText(course.getCredit().toString());
        }

        if (course.getLocation() != null) {
            locationLabel.setText(course.getLocation());
        }
        HttpRequestUtil<TeacherEntity> teacherEntityHttpRequestUtil= new HttpRequestUtil<TeacherEntity>(TeacherEntity.class);
        DataResponse<ArrayList<TeacherEntity>> rest;
        DataRequest reqt =new DataRequest();
        rest = teacherEntityHttpRequestUtil.requestArrayList("/api/course/getTeacher/"+course.getCourseId(),reqt); //从后台获取所有课程信息列表集合
        if(rest != null || rest.getCode()== 0) {
            List<TeacherEntity> teacherList = rest.getData();
            StringBuilder teacherStringBuilder = new StringBuilder();
            for (int i = 0; i < teacherList.size(); i++) {
                teacherStringBuilder.append(teacherList.get(i).getName());
                if (i < teacherList.size() - 1) {
                    teacherStringBuilder.append(",");
                }
            }
            teacherLabel.setText(teacherStringBuilder.toString());
        }

        if (course.getBeginTime() != null) {
            beginTimeLabel.setText(course.getBeginTime());
        }

        if (course.getEndTime() != null) {
            endTimeLabel.setText(course.getEndTime());
        }

        if (course.getSchedule() != 0L) {
            ScheduleLabel.setText(ScheduleUtil.getScheduleString(course.getSchedule()));
        }

        if (course.getIntroduction() != null) {
            introductionLabel.setText(course.getIntroduction());
        }
    }

    private String extractNum(String courseString) {
        int newLineIndex = courseString.indexOf('\n');
        if (newLineIndex != -1) {
            return courseString.substring(0, newLineIndex);
        }
        return "";
    }

    @FXML
    private void onButtonClick(Integer day, Integer period) {
        MFXButton clickedButton = getButtonByIndex(day,period);
        if (!Objects.equals(clickedButton.getId(), "unselected-custom")){
            setUpInfo(extractNum(clickedButton.getText()));
        }
    }

    public static int mapCourseType(String courseType) {
        if (courseType==null){
            courseType="";
        }
        if (courseType.contains("必修")) {
            return 1;
        } else if (courseType.contains("限选")) {
            return 2;
        } else if (courseType.contains("任选")) {
            return 3;
        } else if (courseType.contains("选修")) {
            return 4;
        } else if (courseType.contains("交流")) {
            return 5;
        } else {
            return 6;
        }
    }

    public MFXButton getButtonByIndex(int day, int period) {
        int buttonNumber = (day * 10) + period;
        String buttonID = "button" + buttonNumber;

        try {
            Field field = getClass().getDeclaredField(buttonID);
            return (MFXButton) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addAnnouncement(CourseEntity course1, CourseEntity course2, Integer day, Integer period){
        String content = "<span style=\"color: orange;\">【注意】</span><span>课程 " + "<a href=\"#\" onclick=\"handleLinkClick('" + course1.getNum()+ "')\">" + course1.getNum() + " " + course1.getName() + "</a>"+" 和课程 " + "<a href=\"#\" onclick=\"handleLinkClick('" + course1.getNum()+ "')\">" + course1.getNum() + " " + course1.getName() + "</a>" + " 在星期 " + day +  " 第 " + period + "节课发生冲突，请合理安排时间或联系教务老师退选！</span>";
        announcementList.add(content);
    }

    private void setUpAnnouncement(){
        notificationCircle.setText(String.valueOf((announcementList.size())));
        String htmlContentHead = "<html><body>";
        String HtmlContentTail = "<script>\n" +
                "    function  handleLinkClick(courseNum){\n" +
                "        window.controller.handleLinkClick(courseNum);\n" +
                "    }\n" +
                "</script>" +
                "</body></html>";
        String br = "<br>";
        StringBuilder htmlContent = new StringBuilder(htmlContentHead);
        for (String content :
                announcementList) {
            htmlContent.append(content).append(br);
        }
        htmlContent.append(HtmlContentTail);
        announcementWebView.getEngine().setJavaScriptEnabled(true);
        announcementWebView.getEngine().loadContent(htmlContent.toString());
        announcementWebView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State t1) {

                if (t1 == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) announcementWebView.getEngine().executeScript("window");
                    window.setMember("controller", this);
                }

            }
        });
    }

    public void handleLinkClick(String courseNum){
        setUpInfo(courseNum);
    }

    @FXML
    private void onButton11Click(ActionEvent actionEvent) {
        onButtonClick(1,1);
    }

    @FXML
    private void onButton12Click(ActionEvent actionEvent) {
        onButtonClick(1, 2);
    }

    @FXML
    private void onButton13Click(ActionEvent actionEvent) {
        onButtonClick(1, 3);
    }

    @FXML
    private void onButton14Click(ActionEvent actionEvent) {
        onButtonClick(1, 4);
    }

    @FXML
    private void onButton15Click(ActionEvent actionEvent) {
        onButtonClick(1, 5);
    }

    @FXML
    private void onButton21Click(ActionEvent actionEvent) {
        onButtonClick(2, 1);
    }

    @FXML
    private void onButton22Click(ActionEvent actionEvent) {
        onButtonClick(2, 2);
    }

    @FXML
    private void onButton23Click(ActionEvent actionEvent) {
        onButtonClick(2, 3);
    }

    @FXML
    private void onButton24Click(ActionEvent actionEvent) {
        onButtonClick(2, 4);
    }

    @FXML
    private void onButton25Click(ActionEvent actionEvent) {
        onButtonClick(2, 5);
    }

    @FXML
    private void onButton31Click(ActionEvent actionEvent) {
        onButtonClick(3, 1);
    }

    @FXML
    private void onButton32Click(ActionEvent actionEvent) {
        onButtonClick(3, 2);
    }

    @FXML
    private void onButton33Click(ActionEvent actionEvent) {
        onButtonClick(3, 3);
    }

    @FXML
    private void onButton34Click(ActionEvent actionEvent) {
        onButtonClick(3, 4);
    }

    @FXML
    private void onButton35Click(ActionEvent actionEvent) {
        onButtonClick(3, 5);
    }

    @FXML
    private void onButton41Click(ActionEvent actionEvent) {
        onButtonClick(4, 1);
    }

    @FXML
    private void onButton42Click(ActionEvent actionEvent) {
        onButtonClick(4, 2);
    }

    @FXML
    private void onButton43Click(ActionEvent actionEvent) {
        onButtonClick(4, 3);
    }

    @FXML
    private void onButton44Click(ActionEvent actionEvent) {
        onButtonClick(4, 4);
    }

    @FXML
    private void onButton45Click(ActionEvent actionEvent) {
        onButtonClick(4, 5);
    }

    @FXML
    private void onButton51Click(ActionEvent actionEvent) {
        onButtonClick(5, 1);
    }

    @FXML
    private void onButton52Click(ActionEvent actionEvent) {
        onButtonClick(5, 2);
    }

    @FXML
    private void onButton53Click(ActionEvent actionEvent) {
        onButtonClick(5, 3);
    }

    @FXML
    private void onButton54Click(ActionEvent actionEvent) {
        onButtonClick(5, 4);
    }

    @FXML
    private void onButton55Click(ActionEvent actionEvent) {
        onButtonClick(5, 5);
    }

    @FXML
    private void onButton61Click(ActionEvent actionEvent) {
        onButtonClick(6, 1);
    }

    @FXML
    private void onButton62Click(ActionEvent actionEvent) {
        onButtonClick(6, 2);
    }

    @FXML
    private void onButton63Click(ActionEvent actionEvent) {
        onButtonClick(6, 3);
    }

    @FXML
    private void onButton64Click(ActionEvent actionEvent) {
        onButtonClick(6, 4);
    }

    @FXML
    private void onButton65Click(ActionEvent actionEvent) {
        onButtonClick(6, 5);
    }

    @FXML
    private void onButton71Click(ActionEvent actionEvent) {
        onButtonClick(7, 1);
    }

    @FXML
    private void onButton72Click(ActionEvent actionEvent) {
        onButtonClick(7, 2);
    }

    @FXML
    private void onButton73Click(ActionEvent actionEvent) {
        onButtonClick(7, 3);
    }

    @FXML
    private void onButton74Click(ActionEvent actionEvent) {
        onButtonClick(7, 4);
    }

    @FXML
    private void onButton75Click(ActionEvent actionEvent) {
        onButtonClick(7, 5);
    }
}
