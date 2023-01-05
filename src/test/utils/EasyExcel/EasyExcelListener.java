package com.heima.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * EasyExcel解析监听器
 * 每解析一行会回调invoke()方法
 * 整个Excel解析结束会执行doAfterAllAnalysed()方法
 */
@Slf4j
@Data
public class
EasyExcelListener<T> extends AnalysisEventListener<T> {

    private final Predicate<T> predicate;
    private final Consumer<List<T>> consumer;

    // 用于存储解析的数据返回
    private final List<T> list;
    // 默认行数
    private static int BATCH_COUNT = 3000;

    /**--------------------------------------------------------
     * 读取每一条数据进行predicate操作
     * 读取到count行数进行consumer操作
     *
     * @param count     读取的行数
     * @param predicate 读取一条数据执行的方法。例：校验数据规则
     * @param consumer  读取规定行数后执行的方法
     */
    public EasyExcelListener(int count, Predicate<T> predicate, Consumer<List<T>> consumer) {
        BATCH_COUNT = count;
        this.consumer = consumer == null ? ts -> {
        } : consumer;
        this.predicate = predicate == null ? t -> true : predicate;
        list = new ArrayList<>(BATCH_COUNT);
    }

    /**------------------------------------------------------
     * 每一条数据解析都会来调用
     * 参数1: 当前行的数据
     */
    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        // log.info("解析到一条数据: {}", JSON.toJSONString(data));
        // 如果不符合校验规则，就不进行操作开始解析下一条
        if( !predicate.test(data)){
            return ;
        }
        // 符合校验规则,添加数据到集合
        list.add(data);

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据在内存溢出
        if (list.size() >= BATCH_COUNT) {
            consumer.accept(list);
            list.clear();
        }
    }

    /**---------------------------------------------------------
     * 所有数据解析完成调用,用来关闭不用的资源
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (list.size() > 0) {
            consumer.accept(list);
        }
        log.info("Excel文件数据解析完成");
    }

    /**-------------------------------------------------------------------
     * 数据转换异常
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号, 如果要获取头的信息 配合doAfterAllAnalysedHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;

            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex() + 1,
                    excelDataConvertException.getColumnIndex() + 1);
        }
    }
}