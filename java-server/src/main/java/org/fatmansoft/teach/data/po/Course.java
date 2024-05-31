package org.fatmansoft.teach.data.po;

import org.fatmansoft.teach.data.dto.CourseRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Course 课程表实体类  保存课程的的基本信息信息，
 * Integer courseId 人员表 course 主键 course_id
 * String num 课程编号
 * String name 课程名称
 * Integer credit 学分
 * Course preCourse 前序课程 pre_course_id 关联前序课程的主键 course_id
 */
@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    @NotBlank
    @Size(max = 20)
    private String num;

    @Size(max = 50)
    private String name;

    /**
     * 课程学生
     */
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "student_id")
    private List<Student> students;

    /**
     * 授课老师
     */
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "teacher_id")
    private List<Teacher> teachers;

    /**
     * 通过二进制存储课程日程安排
     */
    private long schedule;

    /**
     * 课程开始日期
     */
    private String beginTime;

    /**
     * 课程结束日期
     */
    private String endTime;

    /**
     * 课程类型
     */
    private String  type;

    /**
     * 课程学分
     */
    private Integer credit;

    /**
     * 上课地点
     */
    private String location;

    /**
     * 课程介绍
     */
    private String introduction;

    private String department; // 开课单位
/*
    @ManyToOne
    @JoinColumn(name="pre_course_id")
    private Course preCourse;
*/

    private @Version Long version;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public long getSchedule() {
        return schedule;
    }

    public void setSchedule(long schedule) {
        this.schedule = schedule;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Course() {
    }

    public Course(Integer courseId, String num, String name, List<Student> students, List<Teacher> teachers, long schedule, String beginTime, String endTime, String type, Integer credit, String location, String introduction, Long version) {
        this.courseId = courseId;
        this.num = num;
        this.name = name;
        this.students = students;
        this.teachers = teachers;
        this.schedule = schedule;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.type = type;
        this.credit = credit;
        this.location = location;
        this.introduction = introduction;
        this.version = version;
    }

    public Course(CourseRequest request) {
        this.courseId = request.getCourseId();
        this.num = request.getNum();
        this.name = request.getName();
        this.schedule = request.getSchedule();
        this.beginTime = request.getBeginTime();
        this.endTime = request.getEndTime();
        this.type = request.getType();
        this.credit = request.getCredit();
        this.location = request.getLocation();
        this.introduction = request.getIntroduction();
        this.department = request.getDepartment();
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", students=" + students +
                ", teachers=" + teachers +
                ", schedule=" + schedule +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type='" + type + '\'' +
                ", credit=" + credit +
                ", location='" + location + '\'' +
                ", introduction='" + introduction + '\'' +
                ", department='" + department + '\'' +
                ", version=" + version +
                '}';
    }

    public void merge(Course updatedCourse) {
            this.num = updatedCourse.getNum();
            this.name = updatedCourse.getName();
            this.schedule = updatedCourse.getSchedule();
            this.beginTime = updatedCourse.getBeginTime();
            this.endTime = updatedCourse.getEndTime();
            this.type = updatedCourse.getType();
            this.credit = updatedCourse.getCredit();
            this.location = updatedCourse.getLocation();
            this.introduction = updatedCourse.getIntroduction();
            this.department = updatedCourse.getDepartment();
        }

}
