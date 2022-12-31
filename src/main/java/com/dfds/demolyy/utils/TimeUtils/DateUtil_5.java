package com.dfds.demolyy.utils.TimeUtils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理
 */
public class DateUtil_5 {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN_INT = "yyyyMMdd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_PATTERN_TIME = "yyyyMMddHHmmss";
    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static final SimpleDateFormat DATE_FMT = new SimpleDateFormat(DATE_PATTERN);
    public static final SimpleDateFormat DATE_TIME_FMT = new SimpleDateFormat(DATE_TIME_PATTERN);

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (null==strDate) {
            return null;
        }

        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            return fmt.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据当前时间往前推多少天
     *
     * @param days 多少天
     * @return 2019-01-02
     */
    public static String getForwardDateByDays(Integer days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        return DATE_FMT.format(c.getTime());
    }

    /**
     * 根据当前时间往后推多少天
     *
     * @param days 多少天
     * @return 2019-01-02
     */
    public static String getNextDateByDays(String startDate, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(startDate, DATE_PATTERN));
        c.add(Calendar.DATE, days);
        return DATE_FMT.format(c.getTime());
    }

    /**
     * 根据当前时间往前推多少天
     *
     * @param days 多少天
     * @return 2019-03-18 11:59:59
     */
    public static String getForwardTimesDateByDays(Integer days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        return DATE_TIME_FMT.format(c.getTime());
    }


    /**
     * 根据某个日期获取上周的时间
     *
     * @param date
     * @return
     */
    public static Date getUwDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -7);
        return c.getTime();
    }

    /**
     * 根据某个日期获取上月的时间
     *
     * @param date
     * @return
     */
    public static Date getUmDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 根据某个日期获取当前天数
     *
     * @param date
     * @return
     */
    public static int getDayNumByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据某个日期数值获取当前天数
     *
     * @param datenum
     * @return
     */
    public static int getDayNumByDatenum(int datenum) {
        Date date = getMonthEndDayDateByDate(datenum);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据某个日期获取当月天数
     *
     * @param date
     * @return
     */
    public static int getMonthDayNumByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据某个日期获取当月天数
     *
     * @param datenum
     * @return
     */
    public static int getMonthDayNumByDate(int datenum) {
        Date date = getMonthEndDayDateByDate(datenum);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据某个日期获取月份
     *
     * @param date
     * @return
     */
    public static int getMonthNumByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 根据某个日期获取年份
     *
     * @param date
     * @return
     */
    public static int getYearNumByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 根据某个日期获取当月最后一天
     *
     * @param date
     * @return
     */
    public static Date getMonthEndDayByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 根据某个日期获取当月最后一天
     *
     * @param date
     * @return
     */
    public static int getMonthEndDayNumByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String num = format(c.getTime(), DATE_PATTERN_INT);
        return Integer.valueOf(num);
    }

    /**
     * 根据某个日期获取当月最后一天
     *
     * @param datenum
     * @return
     */
    public static Date getMonthEndDayDateByDate(int datenum) {
        Date date = stringToDate(datenum + "", DATE_PATTERN_INT);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 根据某个日期获取当月最后一天
     *
     * @param datenum
     * @return
     */
    public static int getMonthEndDayNumByDate(int datenum) {
        Date date = getMonthEndDayDateByDate(datenum);
        String num = format(date, DATE_PATTERN_INT);
        return Integer.valueOf(num);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentDaysByMillisecond(String startTime, String endTime) {
        int days = 0;
        try {
            Date startDate = DATE_TIME_FMT.parse(startTime);
            Date endDate = DATE_TIME_FMT.parse(endTime);
            days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentDays(String startTime, String endTime) {
        int days = 0;
        try {
            Date startDate = DATE_FMT.parse(startTime);
            Date endDate = DATE_FMT.parse(endTime);
            days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        System.out.println(c.getTime());
        return DATE_FMT.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return DATE_FMT.format(c.getTime());
    }

    // 获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String getToday() {
        return format(new Date());
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static Date getThisMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return cal.getTime();

    }

    //public static String getCurrenDateOfFormat(){
    //    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN));
    //}


}
