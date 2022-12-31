package com.dfds.demolyy.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 前端会传统计类型type(1,2,3)，比如近一周、近一月、近一年（12个月），我们先根据type生成日期集合，这将作为X轴
 */
public class Type2Date {

    private List<String> selectDate(String type, String startDate, String endDate){
        if ("1".equals(type)){
            //七天
            return sevenDayList();
        }else if("2".equals(type)){
            //一个月
            return thirtyDayList();
        } else if("4".equals(type)){
            //指定时间段
            return twoDatesList(startDate,endDate);
        }else{
            //默认七天
            return sevenDayList();
        }
    }

    //获取近七天的数据
    public static List<String> sevenDayList(){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        //获取一个星期前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date startDate = calendar.getTime();
        //获取当前时间时间
        calendar.setTime(new Date());
        Date endDate = calendar.getTime();
        List<String> XDate = new ArrayList<String>();
        boolean flag = true;
        while (flag){
            if(sdf1.format(startDate).equals(sdf1.format(endDate))){
                XDate.add(sdf1.format(startDate));
                flag = false;
            }else{
                XDate.add(sdf1.format(startDate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DATE,1);
                startDate =cal.getTime();
            }
        }
        return XDate;
    }

    //近三十天
    public static List<String> thirtyDayList(){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        //获取一个月前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -29);
        Date startDate = calendar.getTime();
        //获取当前时间时间
        calendar.setTime(new Date());
        Date endDate = calendar.getTime();
        List<String> XDate = new ArrayList<String>();
        boolean flag = true;
        while (flag){
            if(sdf1.format(startDate).equals(sdf1.format(endDate))){
                XDate.add(sdf1.format(startDate));
                flag = false;
            }else{
                XDate.add(sdf1.format(startDate));
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DATE,1);
                startDate =cal.getTime();
            }
        }
        return XDate;
    }

    /**
     * 获取两个时间段内的日期
     * @param sDate
     * @param eDate
     */
    public static List<String> twoDatesList(String sDate,String eDate){

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = sdf1.parse(sDate);
            Date endDate =sdf1.parse(eDate);
            List<String> XDate = new ArrayList<String>();
            boolean flag = true;
            while (flag){
                if(sdf1.format(startDate).equals(sdf1.format(endDate))){
                    XDate.add(sdf1.format(startDate));
                    flag = false;
                }else{
                    XDate.add(sdf1.format(startDate));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.add(Calendar.DATE,1);
                    startDate =cal.getTime();
                }
            }
            return XDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        List<String> strings = twoDatesList("2022-09-17 00:00:00", "2022-09-19 23:00:00");
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
