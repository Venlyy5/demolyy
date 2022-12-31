package com.dfds.demolyy.utils.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期转换工具
 * @Author lgc
 * @Date 2019/4/15 2:15
 **/
public class DateUtil_2 {

    /**
     * 字符串日期转换为毫秒值
     * @param ymd 2018-01-01
     * @return
     */
    public static Long dateToMillis(String ymd) {
        if(ymd != null && ymd.length() == 10) {
            try {
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdfDate.parse(ymd);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串日期转换为当日23:59:59:999的毫秒值
     * @param ymd 2018-01-01
     * @return 1514822399999
     */
    public static Long dateToMillisEnd(String ymd) {
        if(ymd != null && ymd.length() == 10) {
            try {
                ymd = ymd + " 23:59:59:999";
                SimpleDateFormat sdfDateTimeMills = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                Date date = sdfDateTimeMills.parse(ymd);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 字符串日期时间转换为毫秒值
     * @param ymdHsd 2018-01-01 15:13:00
     * @return
     */
    public static Long dateTimeToMillis(String ymdHsd) {
        if(ymdHsd != null && ymdHsd.length() == 21) {
            try {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdfDateTime.parse(ymdHsd);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串日期时间转换为毫秒值
     * @param ymdHsd 2018-01-01 15:13:00
     * @return
     */
    public static Long dateTimeToMillis2(String ymdHsd) {
        if(ymdHsd != null && ymdHsd.length() == 19) {
            try {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdfDateTime.parse(ymdHsd);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 毫秒值转换为日期字符串
     * @param mills 毫秒1515954557000
     * @return 2019/4/15
     */
    public static String millisToDate(Long mills) {
        if(mills != null && mills.longValue() > 0) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            return  sdfDate.format(mills);
        }
        return null;
    }

    //获取当天最开始一秒时刻
    public static Long startMillisToDay(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    //获取当天最后一秒时刻
    public static Long lastMillisToDay(){
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * 毫秒值转换为日期时间字符串
     * @param mills 毫秒1515954557000
     * @return 2019/4/15 2:29:17
     */
    public static String millisToDateTime(Long mills) {
        if(mills != null && mills.longValue() > 0) {
            SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return  sdfDateTime.format(mills);
        }
        return null;
    }

    /**
     * 毫秒值转换为日期时间字符串
     * @param mills 毫秒1515954557000
     * @return 2019/4/15 2:29
     */
    public static String millisToDateTimeMin(Long mills) {
        if(mills != null && mills.longValue() > 0) {
            SimpleDateFormat sdfDateTimeMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return  sdfDateTimeMin.format(mills);
        }
        return null;
    }

    /**
     * 功能描述：返回小时
     *
     * @param date
     *            日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date
     *            日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /** * 获得周几日期，上一周或下一周，依此类推
     * * @param week 指定周几
     * * @param whichWeek 那一周
     * * @return string 日期 年-月-日
     * */
    public static String StringgetDayOfWhichWeek(DayOfWeek week, int whichWeek) {
        LocalDate day = LocalDate.now().with(TemporalAdjusters.previous(week)).minusWeeks(whichWeek -1);
        return day.toString();
    }


    /**
     * 根据当前时间,获取上周一
     * @return
     */
    public static Date getLastWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday());
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }
    /**
     * 根据当前时间,获取上周日
     * @return
     */
    public static Date getLastWeekSunday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday());
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    /**
     * 根据当前时间,获取本周一
     * @return
     */
    public static Date getThisWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
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

    /**
     * 获得入参日期下周一的日期
     * @return 入参日期的下周一
     */
    public static Date getNextMonday() {
        //获得入参的日期
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());

        // 获得入参日期是一周的第几天
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        // 获得入参日期相对于下周一的偏移量（在国外，星期一是一周的第二天，所以下周一是这周的第九天）
        // 若入参日期是周日，它的下周一偏移量是1
        int nextMondayOffset = dayOfWeek == 1 ? 1 : 9 - dayOfWeek;

        // 增加到入参日期的下周一
        cd.add(Calendar.DAY_OF_MONTH, nextMondayOffset);
        return cd.getTime();
    }

    /**
     * 根据当前时间,获取本周日
     * @return
     */
    public static Date getThisWeekSunday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday());
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    /**
     * 获取当前时间是星期几
     * @return
     */
    public static String getWeek() {
        String week = "";
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = "周日";
        } else if (weekday == 2) {
            week = "周一";
        } else if (weekday == 3) {
            week = "周二";
        } else if (weekday == 4) {
            week = "周三";
        } else if (weekday == 5) {
            week = "周四";
        } else if (weekday == 6) {
            week = "周五";
        } else if (weekday == 7) {
            week = "周六";
        }
        return week;
    }
    /**
     * 比较两个时间相差的月份
     * @param one
     * @param two
     * @return
     */
    public static Integer between(LocalDate one,LocalDate two) {
        return Period.between(one,two).getMonths();
    }
    /**
     * 往前往后推多少天得出日期
     * @param day
     * @param day
     * @return
     */
    public static Date afterDays(Integer day) {
        LocalDateTime ldt = LocalDateTime.now().plus(day, ChronoUnit.DAYS); // 正数代表当前时间往后，ChronoUnit里面有年，月,日等计量，根据需求可以修改
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 指定日期往前往后推多少天得出日期
     * @param day
     * @param day
     * @return
     */
    public static Date dateAfterDays(Date date,Integer day) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDateTime ldt = localDateTime.plus(day, ChronoUnit.DAYS); // 正数代表当前时间往后，ChronoUnit里面有年，月,日等计量，根据需求可以修改
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    /**
     * 字符串转日期
     * @param dataTxt yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parseDate(String dataTxt)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date = sdf.parse(dataTxt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    /**
     * 判断时间是否在时间段内
     * @param time System.currentTimeMillis()
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd 结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(long time, String strDateBegin, String strDateEnd) {
        Calendar calendar = Calendar.getInstance();
        // 处理开始时间
        String[] startTime = strDateBegin.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
        calendar.set(Calendar.SECOND, Integer.valueOf(startTime[2]));
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeL = calendar.getTimeInMillis();
        // 处理结束时间
        String[] endTime = strDateEnd.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
        calendar.set(Calendar.SECOND, Integer.valueOf(endTime[2]));
        calendar.set(Calendar.MILLISECOND, 0);
        long endTimeL = calendar.getTimeInMillis();
        return time >= startTimeL && time <= endTimeL;
    }

    /**
     * 判断时间是否小于时间段
     * @param time System.currentTimeMillis()
     * @param strDateBegin 开始时间 00:00:00
     * @return
     */
    public static boolean isBeforeDateBegin(long time, String strDateBegin) {
        Calendar calendar = Calendar.getInstance();
        String[] startTime = strDateBegin.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
        calendar.set(Calendar.SECOND, Integer.valueOf(startTime[2]));
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeL = calendar.getTimeInMillis();
        return time < startTimeL;
    }

    /**
     * 判断时间是否大于时间段
     * @param time System.currentTimeMillis()
     * @param strDateEnd 结束时间 00:00:00
     * @return
     */
    public static boolean isAfterDateEnd(long time, String strDateEnd) {
        Calendar calendar = Calendar.getInstance();
        String[] endTime = strDateEnd.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
        calendar.set(Calendar.SECOND, Integer.valueOf(endTime[2]));
        calendar.set(Calendar.MILLISECOND, 0);
        long endTimeL = calendar.getTimeInMillis();
        return time > endTimeL;
    }

    public static void main(String[] args) {
       /* System.out.println("本周一:"+millisToDate(DateUtil.getThisWeekMonday().getTime()));
        System.out.println("本周日:"+millisToDate(DateUtil.getThisWeekSunday().getTime()));
        System.out.println("上周一:"+millisToDate(DateUtil.getLastWeekMonday().getTime()));
        System.out.println("上周日:"+millisToDate(DateUtil.getLastWeekSunday().getTime()));
        System.out.println("下周一:"+millisToDate(DateUtil.getNextMonday().getTime()));*/

    }

    /**
     * 获取指定日期是星期几
     * @return
     */
    public static String getDayOfWeek(Date date) {
        String week = "";
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = "周日";
        } else if (weekday == 2) {
            week = "周一";
        } else if (weekday == 3) {
            week = "周二";
        } else if (weekday == 4) {
            week = "周三";
        } else if (weekday == 5) {
            week = "周四";
        } else if (weekday == 6) {
            week = "周五";
        } else if (weekday == 7) {
            week = "周六";
        }
        return week;
    }


}
