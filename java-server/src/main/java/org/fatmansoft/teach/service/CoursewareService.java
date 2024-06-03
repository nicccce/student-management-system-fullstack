package org.fatmansoft.teach.service;

import org.fatmansoft.teach.data.vo.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoursewareService {
    @Value("${attach.folder}")    //环境配置变量获取
    private String attachFolder;  //服务器端数据存储

    /**
     * 根据课程编号获取文件名列表
     * @param courseNum 课程编号
     * @return 文件名列表
     */
    public List<String> getFileNames(String courseNum) {
        String directoryPath = attachFolder + "courseware/" + courseNum;
        File directory = new File(directoryPath);

        List<String> fileNames = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }

        return fileNames;
    }

    /**
     * 根据课程和文件名获取课程
     * @param courseNum
     * @param fileName
     * @return
     */
    public ResponseEntity<StreamingResponseBody> getFileByCourseNumAndFileName(String courseNum, String fileName) {
        String filePath = attachFolder + "courseware/" + courseNum + "/" + fileName;
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            try {
                InputStream inputStream = new FileInputStream(file);

                StreamingResponseBody responseBody = outputStream -> {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                };

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);

                return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public DataResponse changeCoursewareNameByCourseNumAndFileName(String courseNum, String fileName, String newFileName) {
        // 构建原始文件路径
        String filePath = attachFolder + "courseware/" + courseNum + "/" + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            // 获取文件后缀名
            int dotIndex = fileName.lastIndexOf(".");
            String extension = "";
            if (dotIndex >= 0) {
                extension = fileName.substring(dotIndex);
            }

            // 构建新的文件名
            String newFilePath = attachFolder + "courseware/" + courseNum + "/" + newFileName + extension;
            File newFile = new File(newFilePath);

            // 重命名文件
            if (file.renameTo(newFile)) {
                return CommonMethod.getReturnMessageOK();
            } else {
                return CommonMethod.getReturnMessageError("重命名失败，请稍后重试！");
            }
        } else {
            return CommonMethod.getReturnMessageError("重命名失败，文件不存在！");
        }
    }

    public DataResponse deleteCourseware(String courseNum, String fileName) {
        // 构建文件路径
        String filePath = attachFolder + "courseware/" + courseNum + "/" + fileName;
        File file = new File(filePath);
        System.out.println(filePath);
        if (file.exists()) {

            boolean deleted = file.delete();
            if (deleted) {
                return CommonMethod.getReturnMessageOK();
            } else {
                return CommonMethod.getReturnMessageError("删除失败，请稍后重试！");
            }
        } else {
            return CommonMethod.getReturnMessageError("删除失败，文件不存在！");
        }
    }
}
