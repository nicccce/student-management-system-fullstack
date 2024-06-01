package org.fatmansoft.teach.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

public class ExcelUtil {

    /**
     * 根据文件路径将Excel文件解析成List
     *
     * @param pathName 文件路径（如 D:\test\xzh.xlsx）
     * @param head     表头
     * @param <T>      泛型
     * @return 转换的List
     */
    public static <T> List<T> upload(String pathName, Class<T> head) {
        List<T> list = new ArrayList<>();
        AnalysisEventListener<T> analysisEventListener = new AnalysisEventListener<T>() {
            // 这个每一条数据解析都会来调用
            @Override
            public void invoke(T data, AnalysisContext context) {
                list.add(data);
                // TODO 这里也可以对数据进行操作
            }

            // 所有数据解析完成了都会来调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // TODO 这里也可以对数据进行操作
            }
        };
        EasyExcel.read(pathName, head, analysisEventListener).sheet().doRead();
        return list;
    }

    /**
     * 根据文件流将Excel文件解析成List
     *
     * @param file 文件
     * @param head 表头
     * @param <T>  泛型
     * @return 转换的List
     */
    public static <T> List<T> upload(MultipartFile file, Class<T> head) {
        List<T> list = new ArrayList<>();
        AnalysisEventListener<T> analysisEventListener = new AnalysisEventListener<T>() {
            // 这个每一条数据解析都会来调用
            @Override
            public void invoke(T data, AnalysisContext context) {
//                log.info("解析到一条数据:{}", JSON.toJSONString(data));
                list.add(data);
                // TODO 这里也可以对数据进行操作
            }

            // 所有数据解析完成了都会来调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
//                log.info("所有数据解析完成！");
                // TODO 这里也可以对数据进行操作
            }
        };
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
//            log.error("upload-InputStream-Exception：", e);
            return null;
        }
        EasyExcel.read(inputStream, head, analysisEventListener).sheet().doRead();
        return list;
    }


    /**
     * Excel文件下载
     *
     * @param pathName  文件路径
     * @param sheetName 工作表名称
     * @param data      数据
     * @param head      表头
     * @param <T>       泛型
     */
    public static <T> void download(String pathName, String sheetName, List<T> data, Class<T> head) {
        EasyExcel.write(pathName, head).sheet(sheetName).doWrite(data);
    }

    /**
     * Excel文件下载
     *
     * @param response  响应体
     * @param excelName 文件名
     * @param sheetName 工作表名称
     * @param data      数据
     * @param head      表头
     * @param <T>       泛型
     */
    public static <T> void download(HttpServletResponse response, String excelName, String sheetName, List<T> data, Class<T> head) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        try {
            String fileName = URLEncoder.encode(excelName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), head).sheet(sheetName).doWrite(data);
        } catch (IOException e) {
//            log.error("download-Exception：", e);
        }
    }

    public static List<String> getColumnData(String filePath, String sheetName, String columnHeader) throws IOException {
        List<String> columnData = new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheet(sheetName);

        Row headerRow = sheet.getRow(0);
        int columnIndex = -1;

        // 寻找指定表头的列索引
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        while (headerCellIterator.hasNext()) {
            Cell cell = headerCellIterator.next();
            if (cell.getStringCellValue().equals(columnHeader)) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex >= 0) {
            // 遍历数据行，获取指定列的数据
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // 跳过表头行

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(columnIndex);
                String cellValue = "";

                if (cell != null) {
                    if (cell.getCellType() == CellType.STRING) {
                        cellValue = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    // 添加其他数据类型的处理逻辑

                    columnData.add(cellValue);
                }
            }
        }

        workbook.close();
        fileInputStream.close();

        return columnData;
    }

    public static void updateColumnHeaders(MultipartFile file, String sheetName, Map<String, String> headerMap) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheet(sheetName);

        Row headerRow = sheet.getRow(0);

        // 遍历当前表头行，更新指定的列名
        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue();
            if (headerMap.containsKey(cellValue)) {
                String newHeaderValue = headerMap.get(cellValue);
                cell.setCellValue(newHeaderValue);
            }
        }

        // 保存更新后的 Excel 文件
        FileOutputStream fileOutputStream = new FileOutputStream(file.getName());
        workbook.write(fileOutputStream);

        workbook.close();
        fileOutputStream.close();
    }

    private static String headerMapper(String header, Map<String, String> headerMap){
        if (headerMap.containsKey(header)) {
            String newHeaderValue = headerMap.get(header);
            return newHeaderValue;
        }return header;
    }

    /**
     * 获取Excel，将数据转换成 List<T> 的形式
     * Excel 数据要求第一行为对象的属性名称
     *
     * @param file  文件
     * @param tClass    要转换成的实体类
     * @param <T>
     * @return List对象数组
     * @throws IOException
     */
    public static <T> List<T> readExcelOfList(MultipartFile file, Class<T> tClass, Map<String,String> headerMap) throws IOException {
        List<T> resultMapList = new ArrayList<>();
        // 使用工厂模式 根据文件扩展名 创建对应的Workbook
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet(file.getName());
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        JSONObject jsonObject;
        Map<String, Object> resultMap;
        for (int i = 1; i < rowCount + 1; i++) {
            Row row = sheet.getRow(i);
            resultMap = new HashMap<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                if(Objects.equals(row.getCell(j).getCellType(), CellType.STRING)) {
                    resultMap.put(headerMapper(sheet.getRow(0).getCell(j).toString(), headerMap), row.getCell(j).getStringCellValue());
                } else if(Objects.equals(row.getCell(j).getCellType(), CellType.NUMERIC)) {
                    resultMap.put(headerMapper(sheet.getRow(0).getCell(j).toString(), headerMap), row.getCell(j).getNumericCellValue());
                }else {
                    resultMap.put(headerMapper(sheet.getRow(0).getCell(j).toString(), headerMap), row.getCell(j).getStringCellValue());
                }
            }
            jsonObject = new JSONObject(resultMap);
            resultMapList.add(JSON.parseObject(jsonObject.toJSONString(),tClass));
        }
        return resultMapList;
    }

}
