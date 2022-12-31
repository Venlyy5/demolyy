package com.dfds.demolyy.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil_6 {

    private static final String format = "yyyyMMddHH";

    public static String timestamp2format(Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timestamp));
    }

    /**
     * 格式化时间
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式化当前时间
     * @param
     * @return
     */
    public static String formatNow() {
        Date date = new Date();
        return formatTime(date);
    }


    /**
     * 秒级时间戳转换为 年月日小时
     * @param timeStrap
     * @return
     */
    public static String formatHour(Long timeStrap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        if(null != timeStrap) {
            return sdf.format(timeStrap);
        }else {
            return sdf.format(System.currentTimeMillis());
        }
    }

    /**
     * 毫秒级时间戳转换为日月年小时分钟秒
     * @param timeStamp
     * @return
     */
    public static Date timeStamp2Date(Long timeStamp) throws ParseException {
        if(timeStamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(sdf.format(timeStamp));
        }else {
            return null;
        }

    }



    /**
     * 当前时间秒级时间戳转换为 年月日小时
     * @param
     * @return
     */
    public static String formatHourNow() {
        return formatHour(System.currentTimeMillis());
    }

    /**
     * 获取今年的年份
     * @return
     */
    public static String getYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * 将yyyymmDDHH 转换为 yyyy-mm-DD HH
     * @return
     */
    public static String formatString(String s) {

        return s.substring(0, 4) + "-" +
                s.substring(4, 6) + "-" +
                s.substring(6, 8) + " " +
                s.substring(8, 10);
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static Long nowTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取今天  格式为  yyyymmDD
     * @return
     */
    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

	private static SimpleDateFormat dateFormatNum = new SimpleDateFormat("yyyyMMdd");

    /**
     * 当日0点
     * @return
     */
    public static Long todayStart() {
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.HOUR_OF_DAY, 0);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.MILLISECOND, 0);
        return cc.getTimeInMillis();
    }

    /**
     * 当日23:59:59:999
     * @return
     */
    public static Long todayEnd() {
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.HOUR_OF_DAY, 23);
        cc.set(Calendar.MINUTE, 59);
        cc.set(Calendar.SECOND, 59);
        cc.set(Calendar.MILLISECOND, 999);
        return cc.getTimeInMillis();
    }

    /**
     * 指定日期的0点
     * @return
     */
    public static Long dateStart(long mills) {
        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(mills);
        cc.set(Calendar.HOUR_OF_DAY, 0);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.MILLISECOND, 0);
        return cc.getTimeInMillis();
    }

    /**
     * 指定日期的23:59:59:999
     * @return
     */
    public static Long dateEnd(long mills) {
        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(mills);
        cc.set(Calendar.HOUR_OF_DAY, 23);
        cc.set(Calendar.MINUTE, 59);
        cc.set(Calendar.SECOND, 59);
        cc.set(Calendar.MILLISECOND, 999);
        return cc.getTimeInMillis();
    }

    /**
     * 指定日期的yyyyMMdd
     * @param mills
     * @return
     */
    public static Integer dateNum(long mills) {
        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(mills);
        return Integer.parseInt(dateFormatNum.format(cc.getTime()));
    }

    /**
     * 昨日0点
     * @return
     */
    public static Long yesterdayStart() {
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_MONTH, -1);
        cc.set(Calendar.HOUR_OF_DAY, 0);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.MILLISECOND, 0);
        return cc.getTimeInMillis();
    }

    /**
     * 昨日23:59:59:999
     * @return
     */
    public static Long yesterdayEnd() {
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_MONTH, -1);
        cc.set(Calendar.HOUR_OF_DAY, 23);
        cc.set(Calendar.MINUTE, 59);
        cc.set(Calendar.SECOND, 59);
        cc.set(Calendar.MILLISECOND, 999);
        return cc.getTimeInMillis();
    }

    /**
     * 昨日yyyyMMdd
     * @return
     */
    public static Integer yesterdayDateNum() {
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_MONTH, -1);
        return Integer.parseInt(dateFormatNum.format(cc.getTime()));
    }

    /**
     * 今日yyyyMMdd
     * @return
     */
    public static Integer todayDateNum() {
        Calendar cc = Calendar.getInstance();
        return Integer.parseInt(dateFormatNum.format(cc.getTime()));
    }

    /**
     * 判断当前时间，是否在当日start和end两个时间之间
     * @param start 0-23，整数
     * @param end 0-23，整数
     * @return
     */
    public static boolean isStartToEndDate(Integer start, Integer end) {
        Calendar cc = Calendar.getInstance();
        long nowTime = cc.getTimeInMillis();
        cc.set(Calendar.HOUR_OF_DAY, start);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.MILLISECOND, 0);
        long startTime = cc.getTimeInMillis();
        cc.add(Calendar.HOUR_OF_DAY, end);
        long endTime = cc.getTimeInMillis();
        if(nowTime >= startTime && nowTime <= endTime) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前日期是否在两个时间之间
     * @param start 毫秒
     * @param end 毫秒
     * @return
     */
    public static boolean isStartToEndDate(long start, long end) {
        Calendar cc = Calendar.getInstance();
        long nowTime = cc.getTimeInMillis();
        if(start <= nowTime && nowTime <= end) {
            return true;
        }
        return false;
    }
}
