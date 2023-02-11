package com.dfds.demolyy.utils.TimeUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dfds.demolyy.supplementDate.DataTrans;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContinuityDataTime {

    public static void main(String[] args) {
        //此集合为数据库中查出的零散数据
        List<DataTrans> dataDTOS = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i%2==0){
                //小时格式补0
                String hour = i<10? "0"+i: String.valueOf(i);
                dataDTOS.add(new DataTrans("2020-08-03 " + hour + ":00:00", 3.1415926));
            }
        }
        for (DataTrans dataDTO : dataDTOS) {
            System.out.println(dataDTO);
        }
        System.out.println("==========================================================");

        //想要查询的开始截止时间, 以小时间隔, 获取startTime与endTime中不间断日期
        String startTime = "2020-08-01 00:00:00";
        String endTime = "2020-08-05 00:00:00";
        DateTime dateStartTime = DateUtil.parse(startTime);
        DateTime dateEndTime = DateUtil.parse(endTime);
        long between = DateUtil.between(dateStartTime, dateEndTime, DateUnit.HOUR);

        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < between; i++) {
            DateTime dateTime = DateUtil.offsetHour(dateStartTime, i);
            String time = dateTime.toString("yyyy-MM-dd HH:mm:ss");
            dateList.add(time);
        }

        //不间断日期补全数据:循环不间断日期集合, 如果未匹配上零散数据的时间则补充1.1, 匹配上则使用原本数据
        List<DataTrans> resultList = new ArrayList<>();
        for (String time:dateList){
            List<DataTrans> collect = dataDTOS.parallelStream().filter(e -> e.getName().equals(time)).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(collect)){
                resultList.add(new DataTrans(time, 1.1));
            }else {
                resultList.add(collect.get(0));
            }
        }
        for (DataTrans dataTrans : resultList) {
            System.out.println(dataTrans);
        }

        // 计算时间间隔
        //LocalDateTime start = LocalDateTimeUtil.parse("2020-02-02T00:00:00");
        //LocalDateTime end = LocalDateTimeUtil.parse("2020-02-03T00:00:00");
        //Duration between1 = LocalDateTimeUtil.between(start, end);
        //long l = between1.toHours();
        //System.out.println(l);
    }
}
