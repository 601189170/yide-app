package com.yyide.chatim.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import net.sf.saxon.functions.Data;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {
    private static final String TAG = "DateUtils";

    /**
     * 将时间戳转换为时间
     * <p>
     * s就是时间戳
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * <p>
     * s就是时间戳
     */
    public static String getDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 当天时间最后一分钟
     *
     * @param current
     * @return
     */
    public static String getDayEndDate(long current) {
        long zeroT = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();  //今天零点零分零秒的毫秒数
        String zero = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zeroT);
        long endT = zeroT + 24 * 60 * 60 * 1000 - 1;  //今天23点59分59秒的毫秒数
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endT);
        return end;
    }

    /**
     * 获取本周第一天
     */
    public static String getWeekStartDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.getTime();
        return dateFormater.format(cal.getTime());
    }

    // 获取当天的开始时间
    public static String getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return getDate(cal.getTime().getTime());
    }


    /**
     * 获取本周最后一天
     */
    public static String getWeekEndDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,
                cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        return dateFormater.format(cal.getTime());
    }

    // 获取本周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    // 获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
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

    // 获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
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

    /**
     * 获取本月最后一天
     */
    public static String getMonthEndDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        cal.getTime();
//        String format = dateFormater.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }

    /**
     * 获取本月第一天
     */
    public static String getMonthStartDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        return dateFormater.format(cal.getTime());
    }

    /**
     * 时间戳string 转 指定格式的时间
     *
     * @param createTime
     * @return
     */
    public static String switchCreateTime(String createTime, String dateFormat) {
        String formatStr2 = "";
        if (TextUtils.isEmpty(createTime)) {
            return formatStr2;
        }
        Long time = Long.valueOf(createTime);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(new Date(time * 1000L));
    }

    /**
     * //pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8"
     *
     * @param time
     * @return
     */
    public static String switchTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    public static String switchTime(Date time, String format) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(time);
    }

    /**
     * 日期转换
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    /**
     * 将指定格式的时间转换目标格式的时间格式
     *
     * @param time   初始时间
     * @param source 原始格式
     * @param target 目标格式
     * @return 转换后的时间
     */
    public static String formatTime(String time, String source, String target) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (TextUtils.isEmpty(source)) {
            source = "yyyy-MM-dd HH:mm:ss";
        }
        if (TextUtils.isEmpty(target)) {
            target = "MM.dd HH:mm";
        }
        SimpleDateFormat resourceFormat = new SimpleDateFormat(source);
        Date date = null;
        try {
            date = resourceFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(target);
        return simpleDateFormat2.format(date);
    }

    /**
     * 将时间字符串解析成时间戳
     *
     * @param time
     * @return
     */
    public static long parseTimestamp(String time, String format) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取日期毫秒数
     *
     * @param dateValue
     * @return
     */
    public static long getWhenPoint(String dateValue) {
        if (!TextUtils.isEmpty(dateValue)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateValue);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /***
     * 获取当前日期距离过期时间的日期差值
     * @param endTime
     * @return
     */
    public static long[] dateDiff(String endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowtime = simpleDateFormat.format(curDate);
        try {
            // 获得两个时间的毫秒时间差异
            final long timeDiff = simpleDateFormat.parse(nowtime).getTime() - simpleDateFormat.parse(endTime).getTime();
            if (timeDiff >= 0) {
                Log.e("dateDiff", "dateDiff: 倒计时过期");
                return new long[]{};
            }
            diff = Math.abs(timeDiff);
            long day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            Log.e("dateDiff", "dateDiff: " + day + "," + hour + "," + min + "," + sec);
            return new long[]{day, hour, min, sec};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new long[]{};
    }

    /**
     * 根据输入的年月周数来取该周首天
     *
     * @param year  年份(>0)
     * @param month 月份(1-12)
     * @param week  当月周数(1-5)
     * @return 该周第一天（周日）
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getFirstDayAndLastDayByMonthWeek(int year, int month, int week) {
        Log.e(TAG, "year=" + year + ",month=" + month + ",week=" + week);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dates = {"", ""};
        LocalDate inputDate = LocalDate.parse(simpleDateFormat.format(calendar.getTime()));
        // 所在周开始时间
        LocalDate beginDayOfWeek = inputDate.with(DayOfWeek.MONDAY);
        // 所在周结束时间
        LocalDate endDayOfWeek = inputDate.with(DayOfWeek.SUNDAY);
        Log.e(TAG, "beginDayOfWeek: " + beginDayOfWeek.toString() + ",endDayOfWeek：" + endDayOfWeek.toString());
        dates[0] = beginDayOfWeek.getMonthValue() + "." + beginDayOfWeek.getDayOfMonth();
        dates[1] = endDayOfWeek.getMonthValue() + "." + endDayOfWeek.getDayOfMonth();
        return dates;
    }


    public static String sectionUppercase(int section) {
        switch (section) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            case 10:
                return "十";
            default:
                break;
        }
        return "" + section;
    }
}
