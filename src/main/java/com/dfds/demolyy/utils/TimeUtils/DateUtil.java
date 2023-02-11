package com.dfds.demolyy.utils.TimeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.*;
import java.util.*;


/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 */
public class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);

    public static SimpleDateFormat SIMPLE_DATE_FORMAT_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_3 = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_4 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_5 = new SimpleDateFormat("MMddyyyyHHmmss");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_6 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_7 = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat SIMPLE_DATE_FORMAT_8 = new SimpleDateFormat("yyyyMMdd");

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    // 查询时间后缀
    public final static String START_TIME = " 00:00:00"; // 查询开始时间后缀
    public final static String END_TIME = " 23:59:59"; // 查询结束时间后缀

    public static String format1 = "yyyy-MM-dd HH:mm:ss";
    public static String format2 = "yyyy-MM-dd HH:mm";
    public static String format3 = "HH:mm";
    public static String format4 = "yyyyMMddHHmmss";
    public static Calendar calendar = Calendar.getInstance();


    public static String example1 = "2020-08-05 00:00:00";
    public static String example2 = "2020-08-05 01:30:00";

    /**
     * DateStr -> DateObj
     */
    public static  Date dateStr2DateObj(String format, String strDate) throws ParseException {
        return new SimpleDateFormat(format).parse(strDate);
    }

    /**
     * DateObj -> DateStr
     */
    public static String dateObj2DateStr(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 根据format获取 当前日期偏移后的日期
     * @param format 时间的格式：YYYYMMDD或yyyyMMddHHmmss等
     * @param offsetDay 偏移天数(0不偏移)
     */
    public static String getDateTime(String format, int offsetDay) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_MONTH, offsetDay);
        return new SimpleDateFormat(format).format(rightNow.getTime());
    }

    /**
     * 获取指定日期偏移后的日期
     * @param format 时间的格式：YYYYMMDD或yyyyMMddHHmmss等
     * @param beginTime 指定的日期
     * @param offsetDay 偏移天数(0当前日期)
     */
    public static String getDateTime(String format, String beginTime, int offsetDay) throws ParseException {
        if (format == null || "".equals(format)) {
            format = "yyyy-MM-dd";
        }
        Date startTime = dateStr2DateObj("yyyy-MM-dd", beginTime);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(startTime);
        rightNow.add(Calendar.DAY_OF_MONTH, offsetDay);
        Date time = rightNow.getTime();

        return sdf.format(time);
    }

    /**
     * 判断参数DataObj是否是今天
     */
    public static boolean isToday(Date aDate) {
        return SIMPLE_DATE_FORMAT_8.format(new Date()).equals(SIMPLE_DATE_FORMAT_8.format(aDate));
    }

    /**
     * 计算两个DataObj间的分钟数
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 分钟数
     */
    public static int getMinuteMargin(Date beginDate, Date endDate) {
        int mOfMinute = 1000 * 60;
        //计算两时间毫秒差
        long divTime = (endDate.getTime() - beginDate.getTime());
        long minute = divTime % mOfMinute > 0 ? divTime / mOfMinute + 1 : divTime / mOfMinute;

        return Long.valueOf(minute).intValue();
    }

    /**
     * Timestamp -> String
     * @param timestamp
     * @return
     */
    public static String timestamp2String(Timestamp timestamp){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
        return df.format(now);
    }

    /**
     * String -> Timestamp
     * @param format
     * @param date
     * @return
     */
    public static Timestamp Str2Timestamp(String format, Date date){
        String time = new SimpleDateFormat(format).format(date);
        return Timestamp.valueOf(time);
    }

    /**
     * 计算两时间戳间隔的 天/小时/分/秒 数
     * @param timestamp1
     * @param timestamp2
     * @param type 天0/小时1/分2/秒3
     * @return 间隔数量
     */
    public static float dateDiff(Timestamp timestamp1, Timestamp timestamp2, int type) {
        float i = timestamp1.getTime() - timestamp2.getTime();
        float f = 0.0f;// i / (1000 * 60);
        switch (type) {
            case 1:// hour
                f = i / (1000 * 60 * 60);
                break;
            case 2:// min
                f = i / (1000 * 60);
                break;
            case 3:// sec
                f = i / (1000);
                break;
            case 0: // defaut is day
                f = i / (1000 * 60 * 60 * 24);
        }
        try {
            return Float.valueOf(new DecimalFormat("#,##0.0").format(f));
        } catch (Exception e) {
            //System.out.println(e);
        }
        return f;
    }

    /**
     * Timestamp偏移
     * @param timestamp
     * @param offset 偏移量
     * @param type 偏移单位 天0/小时1/分2/秒3
     * @return
     */
    public static Timestamp dateAdd(Timestamp timestamp, int offset, int type) {
        long interval = 0;
        switch (type) {
            case 0: // day
                interval = offset * 1000 * 60 * 60 * 24l;
                break;
            case 1:// hour
                interval = 1000 * 60 * 60 * offset;
                break;
            case 2:// min
                interval = 1000 * 60 * offset;
                break;
            case 3:// sec
                interval = 1000 * offset;
                break;
            default:
                break;
        }
        return new Timestamp(timestamp.getTime() + interval);
    }

    /**
     * 获取月份的开始日期与结束日期 （结束日期为下月的一号）
     * @param dateTime 日期
     * @return Date[] = [Wed Feb 01 00:00:00 CST 2023, Wed Mar 01 00:00:00 CST 2023]
     */
    public static Date[] getMonthOfStartAndEnd(Date dateTime) {
        Date[] date = new Date[2];
        try {
            // Data -> Calendar,获取年份, 月份
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);
            int yearReport = calendar.get(Calendar.YEAR);
            int monthfield = calendar.get(Calendar.MONTH) + 1;

            Calendar lastDate = Calendar.getInstance();
            lastDate.set(Calendar.YEAR, yearReport);
            lastDate.set(Calendar.MONTH, monthfield - 1);
            lastDate.set(Calendar.DATE, 1);
            int year = lastDate.get(Calendar.YEAR);
            int month = lastDate.get(Calendar.MONTH) + 1;
            int day = lastDate.get(Calendar.DATE);
            String startDate = year + "-" + month + "-" + day;
            date[0] = SIMPLE_DATE_FORMAT_yyyy_MM_dd.parse(startDate);

            lastDate.add(Calendar.MONTH, 1);
            lastDate.add(Calendar.DATE, -1);
            year = lastDate.get(Calendar.YEAR);
            month = lastDate.get(Calendar.MONTH) + 1;
            day = lastDate.get(Calendar.DATE);
            startDate = year + "-" + month + "-" + day + " 24" + ":" + "00" + ":" + "00";
            date[1] = SIMPLE_DATE_FORMAT_yyyy_MM_dd_HH_mm_ss.parse(startDate);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 获取某月的15号
     * @param date 日期
     * @return 该日期的15号
     */
    public static Date getMiddleDateOfMonth(Date date) {
        Date returnDate = new Date();
        try {
            // Data -> Calendar,获取年份月份
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            //设置为15号
            String middleDate = year + "-" + month + "-" + 15 + " 00" + ":" + "00" + ":" + "00";
            returnDate = SIMPLE_DATE_FORMAT_yyyy_MM_dd.parse(middleDate);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return returnDate;
    }

    /**
     * 根据当前时间，获取前后n个月的 1号日期
     * @param direction 往前往后n月
     * @return
     */
    public static Date getSpecialDateOfMonth(int direction) {
        Date returnDate = new Date();
        Date nowDateTime = new Date();
        try {
            int monthfield, yearReport;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDateTime);
            yearReport = calendar.get(Calendar.YEAR);
            monthfield = calendar.get(Calendar.MONTH) + 1 + direction;
            Calendar lastDate = Calendar.getInstance();
            lastDate.set(Calendar.YEAR, yearReport);
            lastDate.set(Calendar.MONTH, monthfield - 1 - direction);
            lastDate.set(Calendar.DATE, 1);
            int year = lastDate.get(Calendar.YEAR);
            int month = lastDate.get(Calendar.MONTH) + 1 + direction;
            //int day = lastDate.get(Calendar.DATE);
            String middleDate = year + "-" + month + "-01" + " 00" + ":" + "00" + ":" + "00";
            returnDate = SIMPLE_DATE_FORMAT_yyyy_MM_dd.parse(middleDate);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return returnDate;
    }

    /**
     * 获取 年 月 日 数组
     * @param date
     * @return [2023, 2, 11]
     */
    public static int[] getYearAndMonthAndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{year, month, day};
    }

    /**
     * 获取两个日期的时间差
     *
     * @param beginDate
     * @param endDate
     * @return 相隔天数
     */
    public static int getTimeDifference(Date beginDate, Date endDate) {
        long DAY = 24L * 60L * 60L * 1000L;
        long day = endDate.getTime() - beginDate.getTime();
        return Long.valueOf(day / DAY).intValue();
    }

    /**
     * 比较参数时间和当前时间：
     * 时间格式 2005-4-21 16:16:34
     * 如果date1<date2 返回>=0 否则<0
     */
    public static int compareDate(String date1, String date2) {
        try {
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.parse(date2).compareTo(df.parse(date1));
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Formats a Date into a date/time string which format like:'yyyy-MM-dd HH:mm:ss'
     *
     * @param date   java.util.Date object, that need to be format.
     * @param format the format of the data/time string, such as: "yyyy-MM-dd
     *               HH:mm:ss.SSS"
     */
    public static String format(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 计算两个时间相差的天数(以24小时间隔为一天)
     *
     * @param smdate 较小的时间
     * @param bdate   较大的时间
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static final String getSpecialDayBeginTimeStr(int direction) {
        String dayPattern = "yyyy-MM-dd";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, direction);
        String specialDayStr = new SimpleDateFormat(dayPattern).format(calendar.getTime());
        return specialDayStr + " 00:00:00";
    }

    public static final Date getSpecialDayBeginTime(int direction) {
        Date date = null;
        String specialDayStr = getSpecialDayBeginTimeStr(direction);
        SimpleDateFormat sf = new SimpleDateFormat(format1);
        try {
            date = sf.parse(specialDayStr);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);
        }
        return (date);
    }

    public static final String getSpecialDayEndTimeStr(int direction) {
        String dayPattern = "yyyy-MM-dd";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, direction);
        String specialDayStr = new SimpleDateFormat(dayPattern).format(calendar.getTime());
        return specialDayStr + " 23:59:59";
    }

    public static final Date getSpecialDayEndTime(int direction) {
        Date date = null;
        String specialDayStr = getSpecialDayEndTimeStr(direction);
        SimpleDateFormat sf = new SimpleDateFormat(format1);
        try {
            date = sf.parse(specialDayStr);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);
        }
        return (date);
    }

    /**
     * 如果当前日期是15号之前，则返还上个月的字符串，
     * 如果当前日期是15号之后，则返还当前月字符串
     * 如：2017-04-10  则返回 "201703"
     * 如 2017-04-20  则返回 "201704"
     *
     * @return 年月字符串
     */
    public static String getSumMonthStr() {
        Date nowDateTime = new Date();
        Date midMonthDateTime = getMiddleDateOfMonth(nowDateTime);
        long subTime = nowDateTime.getTime() - midMonthDateTime.getTime();
        //subTime =  midMonthDateTime.getTime() - nowDateTime.getTime()  ;
        if (subTime > 0) {
            return dateObj2DateStr("yyyyMM", nowDateTime);
        } else {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(nowDateTime);
            rightNow.add(Calendar.DAY_OF_MONTH, -16);
            return dateObj2DateStr("yyyyMM", rightNow.getTime());
        }

    }


    /**-----------------------------------------------------------------
     * 获取两个时间之间的间隔，不同的type(1/2/4)返回不同的间隔单位，
     * d-表示相隔多少天，h-表示相隔多少个小时，"m"-表示相隔多少分钟,s-表示相隔多少秒
     * @param beginTime
     * @param endTime
     * @param type
     * @return
     */
    public static long getTimeBetween(Date beginTime, Date endTime, String type) {
        long begin = beginTime.getTime();//开始时间毫秒数
        long end = endTime.getTime();//结束时间毫秒数
        long dis = end - begin;
        long ret = 0;
        //根据不同的传入类型计算间隔
        if ("d".equalsIgnoreCase(type) || type == null) {//日
            ret = Math.round(dis / (24 * 60 * 60 * 1000));
        } else if ("h".equalsIgnoreCase(type)) {
            ret = Math.round(dis / (60 * 60 * 1000));
        } else if ("m".equalsIgnoreCase(type)) {
            ret = Math.round(dis / (60 * 1000));
        } else if ("s".equalsIgnoreCase(type)) {
            ret = Math.round(dis / 1000);
        }
        return ret;
    }

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

    /**--------------------------------------
     * 获取两个时间段内的日期
     * @param sDate "2022-09-17 00:00:00"
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
                    cal.add(Calendar.HOUR,1);
                    startDate =cal.getTime();
                }
            }
            return XDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
