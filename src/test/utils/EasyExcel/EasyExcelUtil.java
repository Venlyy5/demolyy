package com.heima.common.util;

import com.alibaba.excel.EasyExcel;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.BusinessException;
import com.heima.common.listener.EasyExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel读写工具类
 * easyexcel使用的3.0.2版本
 */
@Slf4j
public class EasyExcelUtil {

    /**
     * ------------------------------------------------------
     * 读取Excel
     */
    public static List<Object> read(String filePath, final Class<?> clazz) {
        File f = new File(filePath);
        try (FileInputStream fis = new FileInputStream(f)) {
            return read(fis, clazz);
        } catch (FileNotFoundException e) {
            log.error("文件{}不存在", filePath, e);
        } catch (IOException e) {
            log.error("文件读取出错", e);
        }
        return null;
    }

    public static List<Object> read(InputStream inputStream, final Class<?> clazz) {
        if (inputStream == null) {
            throw new BusinessException(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        // 注意: DataListener不能被Spring管理,每次读取都要new
        EasyExcelListener listener = new EasyExcelListener(
                200,
                po -> {
                    if (po != null) {  //po的各个字段验证情况
                        return true;
                    }
                    return false;
                },
                System.out::println
        );

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet, 文件流会自动关闭
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();

        return listener.getList();
    }

    /**
     * ------------------------------------------------------------------------
     * 写Excel
     * 新版本会自动关闭流，不需要自己操作
     */
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

    /**
     * -------------------------------------------------------------------------
     * 文件下载（失败了会返回一个有部分数据的Excel），用于直接把excel返回到浏览器下载
     */
    public static void download(HttpServletResponse response, List<?> list, String sheetName) throws IOException {
        Class<?> clazz = list.get(0).getClass();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
    }


}