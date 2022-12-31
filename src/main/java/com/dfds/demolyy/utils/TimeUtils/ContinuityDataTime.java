package com.dfds.demolyy.utils.TimeUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dfds.demolyy.supplementDate.DataTrans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContinuityDataTime {

    public static void main(String[] args) {

        //想要查询的开始截止时间
        String startTime = "2020-08-01 00:00:00";
        String endTime = "2020-08-05 00:00:00";

        //使用Hutool工具类里面的时间工具
        DateTime dateStartTime = DateUtil.parse(startTime);
        DateTime dateEndTime = DateUtil.parse(endTime);
        //获取不间断日期
        List<String> dateList = new ArrayList<>();
        long between = DateUtil.between(dateStartTime, dateEndTime, DateUnit.HOUR);
        int num = (int) between;

        for (int i = 0; i < num; i++) {
            DateTime dateTime = DateUtil.offsetHour(dateStartTime, i);
            String time = dateTime.toString("yyyy-MM-dd HH:mm");
            dateList.add(time);
        }

        //此集合为数据库中查出的数据
        List<DataTrans> dataDTOS = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i%2==0){
                dataDTOS.add(new DataTrans("2020-08-03 " +i+ ":00:00", 64));
            }
        }

        for (DataTrans dataDTO : dataDTOS) {
            System.out.println(dataDTO);
        }

        //不间断日期补全数据
        List<DataTrans> dataList=new ArrayList<>();
        for (String time:dateList){
            List<DataTrans> collect = dataDTOS.parallelStream().filter(e -> e.getName().equals(time)).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(collect)){
                dataList.add(new DataTrans(time, 0));
            }else {
                dataList.add(collect.get(0));
            }
        }

        for (DataTrans dataTrans : dataList) {
            System.out.println(dataTrans);
        }


        // 计算时间间隔
        // LocalDateTime start = LocalDateTimeUtil.parse("2019-02-02T00:00:00");
        // LocalDateTime end = LocalDateTimeUtil.parse("2020-02-02T00:00:00");
        // Duration between1 = LocalDateTimeUtil.between(start, end);
        // long l = between1.toHours();
        // System.out.println(l);
    }
}
