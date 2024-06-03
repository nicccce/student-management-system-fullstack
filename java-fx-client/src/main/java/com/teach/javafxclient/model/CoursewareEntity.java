package com.teach.javafxclient.model;

import com.teach.javafxclient.request.OptionItem;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 学生数据表格类
 */
public class CoursewareEntity {

    private SimpleBooleanProperty select = new SimpleBooleanProperty(false); // 是否选中

    private String courseNum; // 学号

    private String courseName; // 姓名

    private String fileName; // 学院
    

    private String fileType; // 专业

    private String importTime; // 生日
    
    private String fullFileName;
    

    /**
     * 判断对象是否为空
     * @return 为空返回true
     */
    public boolean isEmpty() {
        return (courseNum == null || courseNum.isEmpty())
                && (courseName == null || courseName.isEmpty())
                && (fileName == null || fileName.isEmpty())
                && (fileType == null || fileType.isEmpty())
                && (importTime == null || importTime.isEmpty());
    }

    /**
     * 清空对象
     */
    public void empty() {
        courseNum = null;
        courseName = null;
        fileName = null;
        fileType = null;
        importTime = null;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    /**
     * 构造一个学生数据表格对象
     *
     * @param fileType       专业
     * @param classCourseName   班级
     * @param studentId   学生ID
     * @param personId    个人ID
     * @param courseNum         学号
     * @param courseName        姓名
     * @param fileName        部门
     * @param card        卡号
     * @param gender      性别
     * @param genderCourseName  性别名称
     * @param importTime    生日
     * @param email       邮箱
     * @param phone       电话
     * @param address     地址
     * @param introduce   介绍
     */
    public CoursewareEntity(String fileType, String classCourseName, Integer studentId, Integer personId, String courseNum, String courseName, String fileName, String card, String gender, String genderCourseName, String importTime, String email, String phone, String address, String introduce) {
        this.fileType = fileType != null ? fileType : "";

        this.courseNum = courseNum != null ? courseNum : "";
        this.courseName = courseName != null ? courseName : "";
        this.fileName = fileName != null ? fileName : "";
        this.importTime = importTime != null ? importTime : "";
    }

    public CoursewareEntity(String fileName, OptionItem optionItem) {
        this.fullFileName = fileName;
        this.courseNum = optionItem.getValue();
        this.courseName = optionItem.getLabel();
        // 提取文件名
        this.fileName = extractFileName(fileName);
        // 提取文件类型
        this.fileType = extractFileType(fileName);
        // 提取文件日期
        this.importTime = extractFileDate(fileName);
    }

    public static String extractFileName(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1) {
            return filename.substring(0, dotIndex);
        }
        return filename;
    }

    private static String extractFileType(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        int underscoreIndex = filename.lastIndexOf("_");
        if (dotIndex != -1) {
            return (filename.substring(dotIndex + 1,underscoreIndex)).toUpperCase()+"文件";
        }
        return "";
    }

    private static  String extractFileDate(String filename) {
        int underscoreIndex = filename.lastIndexOf("_");
        if (underscoreIndex != -1 && underscoreIndex < filename.length() - 1) {
            return filename.substring(underscoreIndex + 1);
        }
        return "";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    /**
     * 默认构造方法
     */
    public CoursewareEntity() {
    }


    public boolean isSelect() {
        return select.get();
    }

    public SimpleBooleanProperty selectProperty() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }

    /**
     * 获取专业
     *
     * @return 专业
     */
    public String getMajor() {
        return fileType;
    }

    /**
     * 设置专业
     *
     * @param fileType 专业
     */
    public void setMajor(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取学号
     *
     * @return 学号
     */
    public String getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    /**
     * 设置学号
     *
     * @param courseNum 学号
     */
    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    /**
     * 获取姓名
     *
     * @param courseName 姓名
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * 获取部门
     *
     * @return 部门
     */
    public String getFileCourseName() {
        return fileName;
    }

    /**
     * 设置部门
     *
     * @param fileName 部门
     */
    public void setFileCourseName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取生日
     *
     * @return 生日
     */
    public String getBirthday() {
        return importTime;
    }

    /**
     * 设置生日
     *
     * @param importTime 生日
     */
    public void setBirthday(String importTime) {
        this.importTime = importTime;
    }


    @Override
    public String toString() {
        return "studentDataTable{" +
                "selected=" + select +
                ", fileType='" + fileType + '\'' +
                ", courseNum='" + courseNum + '\'' +
                ", courseName='" + courseName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", importTime='" + importTime + '\'' +
                '}';
    }


}