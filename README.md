# student-management-system-fullstack
College of Software, Shandong University freshman java course project 山东大学软件学院大一java课设项目



## INTRODUCE

山东大学软件学院 大一下册 高级程序开发课程设计

最终成绩：A

**前端**：javafx，使用了UI组件库MaterialFX和AtlantaFX改善观感

**后端**：springboot+JPA



## DETAILS

原先的面向map的写法过于繁琐，稍微重写了一下前后端传输的方法和数据模型的存储，让他更面向对象一些。

大部分功能都是从学生信息管理、教师信息管理平行cv过来的

待改进项：

- 后面几个功能有点小bug，有bug的可以考虑删了重新cv然后改字段

- 成绩管理功能没时间了，做得很草率，需要大改动
- 学生端的某些查看的功能缺失，可以对照着管理员端写
- 个人简历导出功能没写，用了一些小伎俩骗过去了
- 选课的功能没写，太麻烦了



## AUTHOR

> 高质量神金勾八男寝



## DEPLOY

环境：java17+,javafx17+

复制`application.yml.example`为`application.yml`，根据实际情况修改。