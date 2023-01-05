package com.heima.media.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dto.ResponseResult;
import com.heima.common.util.EasyExcelUtil;
import com.heima.media.entity.WmSensitive;
import com.heima.media.service.IWmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * EasyExcel 读写
 */
@Controller
@RequestMapping("/easyexcel")
public class EasyExcelController {

    @Autowired
    private IWmSensitiveService wmSensitiveService;

    /**-----------------------------------------
     * 读取 敏感词Excel文件, 保存到数据库
     * 参数: excel文件绝对路径
     */
    @PostMapping("/read")
    public ResponseResult readExcel(String filename) {
        EasyExcel.read(filename, WmSensitive.class, new PageReadListener<WmSensitive>(dataList -> {
            for (WmSensitive sensitive : dataList) {
                //查询敏感词是否存在, 不存在则新增, 存在则更新时间
                int count = wmSensitiveService.count(new LambdaQueryWrapper<WmSensitive>()
                        .eq(WmSensitive::getSensitives, sensitive.getSensitives()));

                if (count == 0) {
                    sensitive.setCreatedTime(new Date());
                    wmSensitiveService.save(sensitive);
                } else {
                    sensitive.setCreatedTime(new Date());
                    wmSensitiveService.updateById(sensitive);
                }
            }
        })).sheet().doRead();

        //------------- 方式2: 读监听器+工具类
        // List<Object> list = EasyExcelUtil.read(filename, WmSensitive.class);
        // for (int i = 0; i < list.size(); i++) {
        //     System.out.println("读取到第"+i+"条数据: " + list.get(i) );
        // }

        return ResponseResult.okResult();
    }

    /**---------------------------------------
     * 将敏感词数据写入到excel文件
     * 参数: excel文件生成位置
     */
    @PostMapping("/write")
    public ResponseResult writeExcel(String fileName){
        // 从数据库查询敏感词列表
        List<WmSensitive> list = wmSensitiveService.list();
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, WmSensitive.class)
                .sheet("敏感词")
                .doWrite(() -> {
                    // 分页查询数据
                    return list;
                });

        return ResponseResult.okResult("生成Excel: "+fileName);
    }


}
