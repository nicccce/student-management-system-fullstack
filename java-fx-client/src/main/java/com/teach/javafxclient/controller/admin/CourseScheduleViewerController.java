package com.teach.javafxclient.controller.admin;

import com.teach.javafxclient.model.CourseEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.Objects;

public class CourseScheduleViewerController {

    private CourseEntity course;

    private Integer courseType;

    private Stage stage;

    private Long schedule;

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

    @FXML
    public void initialize() {

    }

    public void init(CourseEntity course, Stage stage){
        courseType = mapCourseType(course.getType());
        this.course = course;
        this.schedule = 0L;
        if (course.getSchedule() != 0L){
            long schedule = course.getSchedule();
            for (int i = 1; i <= 7; i++) {
                for (int j = 1; j <= 5; j++) {
                    if ((schedule & 1) ==1){
                        setButton(i,j);
                    }
                    schedule >>= 1;
                }
            }
        }
        this.stage = stage;
    }

    private void setSchedule(Integer day, Integer period, Integer value){
        Integer index = (day-1)*5+period;
        schedule = schedule
                - (schedule & (1L << (index -1)))
                + ((long) value << (index - 1));
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent actionEvent) {
        stage.close();
    }

    public void setButton(Integer day, Integer period) {
        MFXButton clickedButton = getButtonByIndex(day,period);
            clickedButton.setId("schedule-custom"+courseType.toString());
            clickedButton.setText(course.getNum() + '\n' + course.getName());
            setSchedule(day, period, 1);

    }

    @FXML
    private void onButtonClick(Integer day, Integer period) {

    }

    public static int mapCourseType(String courseType) {
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
