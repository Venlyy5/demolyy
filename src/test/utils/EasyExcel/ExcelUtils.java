package com.dfd.platform.utils.model;

import com.alibaba.excel.EasyExcel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;


/**
 * Excel工具类
 * @author LiYangYang
 * @date 2022/8/2
 */
public class ExcelUtils {

    public static List<Object> read(String filePath, final Class<?> clazz){
        File f = new File(filePath);
        try(FileInputStream fis = new FileInputStream(f)){
            return read(fis, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Object> read(InputStream inputStream, final Class<?> clazz){
        if(inputStream == null){
            throw new RuntimeException("文件不存在");
        }

        ExcelListener listener = new ExcelListener();
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();

        return listener.getDatalist();
    }


    public static void write(String outFile, List<?> list) {
        Class<?> clazz = list.get(0).getClass();
        EasyExcel.write(outFile, clazz).sheet().doWrite(list);
    }
    public static void write(String outFile, List<?> list, String sheetName) {
        Class<?> clazz = list.get(0).getClass();
        EasyExcel.write(outFile, clazz).sheet(sheetName).doWrite(list);
    }
    public static void write(OutputStream outputStream, List<?> list, String sheetName) {
        Class<?> clazz = list.get(0).getClass();
        EasyExcel.write(outputStream, clazz).sheet(sheetName).doWrite(list);
    }


    public static Workbook getWorkbook(MultipartFile multipartFile) throws Exception {
        Workbook xls = null;
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        try {
            // 创建对Excel工作簿文件的引用
            if (".xls".equals(fileType)) {
                xls = new HSSFWorkbook(multipartFile.getInputStream());
            } else if (".xlsx".equals(fileType)) {
                xls = new XSSFWorkbook(multipartFile.getInputStream());
            } else {
                throw new Exception("解析的文件格式有误！");
            }
            return xls;
        } catch (FileNotFoundException ex) {
            throw new Exception("EXCEL文件未找到！请检查文件路径是否正确。FileName ："+ fileName);
        } catch (IOException ex) {
            throw new Exception("IO异常！无法读取EXCEL文件。" + ex.getMessage());
        }
    }
}