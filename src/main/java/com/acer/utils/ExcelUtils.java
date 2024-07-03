package com.acer.utils;

import com.acer.annotate.ExcelAnnotate;
import com.acer.annotate.ExcelSheetAnnotate;
import com.acer.model.excel.BaseExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.acer.constant.ExcelConstants.EXCEL_TYPE;
import static com.acer.utils.CopyUtils.initial;
import static com.acer.utils.CopyUtils.splicingGetAndSetName;

/**
 * Excel 上传 和 下载
 * @author acer
 */
@Slf4j
public class ExcelUtils {


    /**
     * 解析上传的Excel表
     * @author acer
     * @date 17:08 2023/5/18
     * @param file 上传要解析的Excel表
     * @param excelData 要把excel表中的数据存到那个实体类中
     * @return T
     **/
    public static <T> T  uploadExcel(MultipartFile file, Class excelData) throws NoSuchFieldException {

        //TODO： file非空校验
        if (CheckUtils.nonemptyFile(file)){
            return null;
        }


        // TODO：获取要读取第几页（ExcelSheetAnnotate自定义注解）
        ExcelSheetAnnotate excelNumber = (ExcelSheetAnnotate) excelData.getAnnotation(ExcelSheetAnnotate.class);
        int sheet = excelNumber.sheet();

        // TODO：获取excel表格对应实体类的中要存储：属性名上注解数据 和 属性名，存到hashmap中
        // 获取excel表对应的实体类中所有的属性
        Field[] declaredFields = excelData.getDeclaredFields();

        Map<String,String> arrangeMap = new HashMap<>(declaredFields.length);
        // 遍历属性，并判断是否标记了注解
        for (Field declaredField : declaredFields) {
            // 获取属性对象
            Field arrange = excelData.getDeclaredField(declaredField.getName());
            // 判断是否标记了注解
            if (arrange.getAnnotation(ExcelAnnotate.class) != null) {
                // 获取属性名上注解数据
                String arrangeName = arrange.getAnnotation(ExcelAnnotate.class).name();
                // 获取属性名
                String attributeName = declaredField.getName();
                arrangeMap.put(arrangeName,attributeName);
            }
        }

        // TODO：根据上传文件的后缀名，创建Workbook对象
        Workbook workbook = null;
        //识别Excel的版本:获取file获取后缀名
        String suffixName = file.getOriginalFilename();
        try {
            //判断Excel的版本
            if (suffixName.contains(EXCEL_TYPE)){
                //07版本后的后缀为：xlsx
                workbook = new XSSFWorkbook(file.getInputStream());
            }else{
                //03版本的后缀为:xls
                workbook = new HSSFWorkbook(file.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Excel版本识别出错：请上传 xlsx或xls文件");
        }

        //根据ExcelSheetAnnotate注解获取读取第几页
        Sheet sheets = workbook.getSheetAt(sheet);

        //创建Person类型集合用来存储从Excel表中读取的每行数据
        List<Object> result = new ArrayList<>();

        // 第0行是标题
        Row titleRow = sheets.getRow(0);

        try {
            // 遍历excel表数据：getPhysicalNumberOfRows()获取总条数，i从1开始跳过标题
            for (int i = 1; i < sheets.getPhysicalNumberOfRows(); i++) {
                // 获取当前行
                Row row = sheets.getRow(i);
                // 反射新建对象 对应一行excel
                Object eachObject = excelData.getDeclaredConstructor().newInstance();
                // 读取一格数据
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    // 获取该（列）的标题
                    Cell cell = titleRow.getCell(j);
                    //获取该（列）数据
                    Cell dataCell = row.getCell(j);

                    //设置读取类型
                    cell.setCellType(CellType.STRING);
                    dataCell.setCellType(CellType.STRING);
                    // 获取数据
                    String value = cell.getStringCellValue();
                    // 注解对应的属性名
                    String attributeName = arrangeMap.get(value);
                    if (attributeName == null) {
                        continue;
                    }
                    // 当前列的数据
                    String data = dataCell.getStringCellValue();

                    // TODO：根据eachObject属性获取类型和转换后的数据
                    // 根据属性名获取指定属性的field对象，用户获取属性的类型
                    Field declaredField = excelData.getDeclaredField(attributeName);
                    // 属性类型：例 java.lang.Integer
                    String typeName = declaredField.getGenericType().getTypeName();
                    // 拆分
                    Class typeClass = StringUtils.strToType(typeName);

                    // data数据类型转换
                    Object newData = StringUtils.strToObject(typeName, data);
                    // 拼接set方法  initial首字母转大写
                    Method method = eachObject.getClass().getMethod("set" + initial(attributeName),typeClass);
                    // 执行set方法
                    method.invoke(eachObject, newData);
                }
                //存储到集合中
                result.add(eachObject);

            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return (T) result;
    }


    /**
     * 下载Excel，数据库数据转excel表到本地
     * @author acer
     * @date 14:50 2023/5/22
     * @param path 写到本地路径 + 文件名和类型
     * @param excelData 要写入的数据
     * @return java.lang.Boolean
    **/
    public static Boolean downloadExcel(String path,List<? extends BaseExcel> excelData) {

        try {
            //TODO：步骤一 先创建excel表，反射获取实体类上的注解内容写入excel表第一行标题
            //创建一个新的工作簿
            Workbook workbook = new XSSFWorkbook();
            //在工作簿中创建一个工作表
            Sheet sheet = workbook.createSheet("Sheet1");
            //在工作表中创建一个新行
            Row row = sheet.createRow(0);

            // 获取excelData第一个对象
            Object object = excelData.get(0);
            // 获取对象中所有属性
            Field[] declaredFields = object.getClass().getDeclaredFields();
            // 创建List集合存储get属性方法
            List<String> getAttributeName = new ArrayList<>();
            // 遍历所有属性上的注解内容，写到excel表第一行标题
            for (int i = 0; i < declaredFields.length; i++) {
                // 判断属性是否有注解
                if (declaredFields[i].getAnnotation(ExcelAnnotate.class) != null) {
                    // 获取属性名上注解数据
                    String arrangeName = declaredFields[i].getAnnotation(ExcelAnnotate.class).name();
                    //在新行中创建一个单元格
                    Cell cell = row.createCell(i);
                    //将数据写入单元格
                    cell.setCellValue(arrangeName);
                    // 获取属性名
                    String name = declaredFields[i].getName();
                    // 调用splicingGetAndSetName拼接get方法
                    getAttributeName.add(splicingGetAndSetName("get",name));
                }
            }

            // TODO：步骤二 将excelData集合写入excel（从第二行开始）
            for (int j = 1;j < excelData.size();j++) {
                //在工作表中创建一个新行
                Row sheetRow = sheet.createRow(j);

                for (int i = 0; i < getAttributeName.size(); i++) {
                    // 执行get方法获取返回值
                    Method method = excelData.get(j).getClass().getMethod(getAttributeName.get(i));
                    //返回值
                    String value = (String) method.invoke(excelData.get(j));
                    //在新行中创建一个单元格
                    Cell cell = sheetRow.createCell(i);
                    //将数据写入单元格
                    cell.setCellValue(value);
                }
            }

            //创建一个输出流以将工作簿写入文件
            FileOutputStream fileOut = new FileOutputStream(path);
            //将工作簿写入输出流
            workbook.write(fileOut);
            // 关闭资源
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 下载excel表到浏览器
     * @author acer
     * @date 14:50 2023/5/22
     * @param fileName 下载文件名 + 文件类型
     * @param response 响应头
     * @param excelData 要写入的数据（必须）
     * @return java.lang.Boolean
     **/
    public static Boolean downloadExcel(String fileName,HttpServletResponse response, List<? extends BaseExcel> excelData) {

        try {
            //TODO：步骤一 先创建excel表，反射获取实体类上的注解内容写入excel表第一行标题
            //创建一个新的工作簿
            Workbook workbook = new XSSFWorkbook();
            //在工作簿中创建一个工作表
            Sheet sheet = workbook.createSheet("Sheet1");
            //在工作表中创建一个新行
            Row row = sheet.createRow(0);

            // 获取excelData第一个对象
            Object object = excelData.get(0);
            // 获取对象中所有属性
            Field[] declaredFields = object.getClass().getDeclaredFields();
            // 创建List集合存储get属性方法
            List<String> getAttributeName = new ArrayList<>();
            // 遍历所有属性上的注解内容，写到excel表第一行标题
            for (int i = 0; i < declaredFields.length; i++) {
                // 判断属性是否有注解
                if (declaredFields[i].getAnnotation(ExcelAnnotate.class) != null) {
                    // 获取属性名上注解数据
                    String arrangeName = declaredFields[i].getAnnotation(ExcelAnnotate.class).name();
                    //在新行中创建一个单元格
                    Cell cell = row.createCell(i);
                    //将数据写入单元格
                    cell.setCellValue(arrangeName);
                    // 获取属性名
                    String name = declaredFields[i].getName();
                    // 将拼接get方法
                    getAttributeName.add(splicingGetAndSetName("get",name));
                }
            }

            // TODO：步骤二 将excelData集合写入excel（从第二行开始）
            for (int j = 1; j < excelData.size(); j++) {
                //在工作表中创建一个新行
                Row sheetRow = sheet.createRow(j);

                for (int i = 0; i < getAttributeName.size(); i++) {
                    // 执行get方法获取返回值
                    Method method = excelData.get(j).getClass().getMethod(getAttributeName.get(i));
                    //返回值
                    String value = (String) method.invoke(excelData.get(j));
                    //在新行中创建一个单元格
                    Cell cell = sheetRow.createCell(i);
                    //将数据写入单元格
                    cell.setCellValue(value);
                }
            }

            //获取输出流以将工作簿写入文件
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置输出字符集
            // response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"utf8"));

            //将工作簿写入输出流
            workbook.write(outputStream);
            // 关闭资源
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }


}
