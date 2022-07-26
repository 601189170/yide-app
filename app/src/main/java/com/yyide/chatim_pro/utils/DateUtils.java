package com.yyide.chatim_pro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.database.ScheduleDaoUtil;

import net.sf.saxon.functions.Data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtils {
    private static final String TAG = "DateUtils";

    /**
     * 是否为今天
     */
    public static boolean isToday(Long timeStamp) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        if (calendar.get(Calendar.YEAR) == (todayCalendar.get(Calendar.YEAR))) {
            int diffDay = todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
            return diffDay == 0;
        }
        return false;
    }

    /**
     * 是否为昨天
     */
    public static boolean isYesterday(Long timeStamp) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        if (calendar.get(Calendar.YEAR) == (todayCalendar.get(Calendar.YEAR))) {
            int diffDay = todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
            return diffDay == 1;
        }
        return false;
    }

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
    public static String stampToDate(long s, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * <p>
     * s就是时间戳
     */
    public static String getDate(long dateTime) {
        String time = "";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = sDateFormat.format(new Date(dateTime + 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    public static String getDate2(long dateTime) {
        String time = "";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            time = sDateFormat.format(new Date(dateTime + 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    // 获取上周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
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
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 将时间戳转换为时间
     * <p>
     * s就是时间戳
     */
    public static String getDate(DateTime dateTime, String format) {
        String time = "";
        try {
            if (format == null) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            time = sDateFormat.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
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
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
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
    public static Date getBeginDayOfWeek2() {
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
    public static Date getEndDayOfWeek2() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek2());
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
    // 获取上月的开始时间
    public static Date getBeginDayOfMonth2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
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
    // 获取本月的结束时间
    public static Date getEndDayOfMonth2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(time);
    }

    public static long formatTime(String time, String source) {
        if (TextUtils.isEmpty(source)) {
            source = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat resourceFormat = new SimpleDateFormat(source);
        Date date = null;
        try {
            date = resourceFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        }
        return 0;
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
            return "--";
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(target);
        return simpleDateFormat2.format(date);
    }


    public static String formatTime(String time, String source, String target, boolean week) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (TextUtils.isEmpty(source)) {
            source = "yyyy-MM-dd HH:mm:ss";
        }
        if (TextUtils.isEmpty(target)) {
            target = "MM月dd日";
        }
        if (week) {
            SimpleDateFormat resourceFormat = new SimpleDateFormat(source);
            Date date = null;
            try {
                date = resourceFormat.parse(time);
                if (date != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(date.getTime()));
                    //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
                    final int i = cal.get(Calendar.DAY_OF_WEEK);
                    return formatTime(time, source, target) + " " + getWeek(i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return formatTime(time, source, target);
    }

    public static DateTime simplifiedDataTime(DateTime source) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return DateTime.parse(source.toString("yyyy-MM-dd"), dateTimeFormatter);
    }

    public static String getWeek(DateTime dateTime, boolean showToday) {
        final DateTime today = DateTime.now();
        final int dayOfWeek = dateTime.getDayOfWeek() % 7;
        if (showToday && today.toString("yyyy-MM-dd").equals(dateTime.toString("yyyy-MM-dd"))) {
            return "今天";
        }
        switch (dayOfWeek) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
        }
        return "";
    }

    public static String getWeek(int week) {
        switch (week) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            case 8:
                return "周日";
            default:
                break;

        }
        return "";
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
    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getFirstDayAndLastDayByMonthWeek(int year, int month, int week) {
        Log.e(TAG, "year=" + year + ",month=" + month + ",week=" + week);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dates = {"", ""};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate inputDate = LocalDate.parse(simpleDateFormat.format(calendar.getTime()));
            // 所在周开始时间
            LocalDate beginDayOfWeek = inputDate.with(DayOfWeek.MONDAY);
            // 所在周结束时间
            LocalDate endDayOfWeek = inputDate.with(DayOfWeek.SUNDAY);
            Log.e(TAG, "beginDayOfWeek: " + beginDayOfWeek.toString() + ",endDayOfWeek：" + endDayOfWeek.toString());
            dates[0] = beginDayOfWeek.getMonthValue() + "." + beginDayOfWeek.getDayOfMonth();
            dates[1] = endDayOfWeek.getMonthValue() + "." + endDayOfWeek.getDayOfMonth();
        } else {
            final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM.dd");
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Monday
            final String date1 = simpleDateFormat2.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Sunday
            final String date2 = simpleDateFormat2.format(calendar.getTime());
            Log.e(TAG, "getFirstDayAndLastDayByMonthWeek: date1 " + date1 + ", date2 " + date2);
            dates[0] = date1;
            dates[1] = date2;
        }
        return dates;
    }

    public static String sectionDesc(Context context, int section) {
        if (section == 0) {
            return "早读";
        }
        return String.format(context.getString(R.string.attendance_class_section), convert(section));
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

    public static String convert(int number) {
        String[] single = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};//汉字一到九
        String[] unit = {"", "十", "百", "千", "万", "亿"};//汉字单位
        StringBuilder sb = new StringBuilder();
        int[] unitInt = {100000000, 10000, 1000, 100, 10};
        int m = 0;
        int tmp = -1;
        for (int i : unitInt) {
            int n = number / i;
            number = number % i;
            if (sb.length() != 0 || n != 0) {
                if (n > single.length - 1) {
                    String cc = convert(n);
                    sb.append(cc);
                } else {
                    if (0 == tmp && 0 == n) {

                    } else {
                        sb.append(single[n]);
                    }

                    tmp = n;
                }
                if (n != 0) {
                    sb.append(unit[unit.length - 1 - m]);
                }

            }
            m++;

        }
        if (number != 0) {
            sb.append(single[number]);
        }
        String ret = sb.toString();
        if (ret.length() == 0) {
            return "零";
        }
        String last = String.valueOf(ret.charAt(ret.length() - 1));
        if ("零".equals(last)) {
            ret = ret.substring(0, ret.length() - 1);
        }

        if (sb.length() >= 2) {
            if (sb.charAt(0) == '一' && sb.charAt(1) == '十') {
                sb.deleteCharAt(0);
                ret = sb.toString();
            }
        }
        return ret;
    }

    public static boolean minMonth(String beginData, int currentMonth) {
        final String time = formatTime(beginData, null, "MM");
        try {
            final int anInt = Integer.parseInt(time);
            if (currentMonth <= anInt) {
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    public static boolean minMonth(String beginDate, DateTime currentMonth) {
        return ScheduleDaoUtil.INSTANCE.toDateTime(beginDate).compareTo(currentMonth) > 0;
    }

    public static boolean minWeek(String beginDate, DateTime currentMinWeekDate) {
        return ScheduleDaoUtil.INSTANCE.toDateTime(beginDate).compareTo(currentMinWeekDate) > 0;
    }

    /**
     * 判断beginDate是否是最小的时间限制
     *
     * @param beginDate          最小时间限制
     * @param currentMinWeekDate 当前时间最小
     * @return
     */
    public static boolean minWeek(String beginDate, String currentMinWeekDate) {
        if (TextUtils.isEmpty(beginDate)) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM.dd");
        try {
            final Date date = simpleDateFormat.parse(beginDate);
            Date date2 = simpleDateFormat2.parse(currentMinWeekDate);
            final Calendar instance = Calendar.getInstance();
            final int year = instance.get(Calendar.YEAR);
            assert date2 != null;
            instance.setTime(date2);
            instance.set(Calendar.YEAR, year);
            date2 = instance.getTime();
            Log.e(TAG, "minWeek: " + instance.toString());
            assert date != null;
            if (date.getTime() >= date2.getTime()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean dateExpired(String date) {
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            final Date parse = dateFormat.parse(date);
            if (parse != null) {
                return new Date().getTime() - parse.getTime() > 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param eq          true判断相等 false 判断大小
     * @param nowDate
     * @param compareDate
     * @return
     */
    public static boolean compareDate(String nowDate, String compareDate, boolean eq) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date now = df.parse(nowDate);
            Date compare = df.parse(compareDate);
            if (eq) {
                return now.getTime() == compare.getTime();
            }
            if (now.before(compare)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否是今天以后的
     *
     * @param compareDate 比较的日期
     * @return true 大于今天
     */
    public static boolean compareDate(String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar todayCalendar = Calendar.getInstance();
            Date today = new Date(todayCalendar.get(Calendar.YEAR) - 1900, todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH));
            Date compare = df.parse(compareDate);
            int result = today.compareTo(compare);
            return result < 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isToday(String compareStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String todayStr = simpleDateFormat.format(date);
        return todayStr.equals(compareStr);
    }

    /**
     * 判断是否同一天
     *
     * @param startDate yyyy-mm-dd hh:mm 格式的字符串日期
     * @param endDate   yyyy-mm-dd hh:mm 格式的字符串日期
     * @return true 是
     */
    public static boolean isSameDay(String startDate, String endDate) {
        String start = formatTime(startDate, "", "yyyy-MM-dd");
        String end = formatTime(endDate, "", "yyyy-MM-dd");
        return start.equals(end);
    }


    public static String judgeIsNeedAddZero(String unit) {
        if (unit == null || unit.isEmpty()) {
            return "";
        }
        if (unit.length() == 1) {
            return 0 + unit;
        }
        return unit;
    }


}
