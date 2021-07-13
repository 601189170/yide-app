package com.yyide.chatim.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Hao on 2017/7/18.
 */

public class TimeUtil {

    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {

        boolean result = false;

        final long aDayInMillis = 1000 * 60 * 60 * 24;

        final long currentTimeMillis = System.currentTimeMillis() + 10 * 60 * 1000;

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    /**
     * 传入的时间是否已经结束
     *
     * @param endDate 传入的时间
     * @return 返回
     */
    public static boolean isDateOver(String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = new Date();
            Date old = sdf.parse(endDate);
            return now.before(old);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isDateOver2(String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        try {
            Date now = new Date();
            Date old = sdf.parse(endDate);
            return now.before(old);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 先用SimpleDateFormat.parse() 方法将日期字符串转化为Date格式
     * 通过Date.getTime()方法，将其转化为毫秒数  String date = "2001-03-15 15：37：05";
     */
    public static long gettime(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
        long time2 = 0;
        if (str != null) {
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }

    public static long gettime10(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
        long time2 = 0;
        if (str != null) {
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }


    public static long gettime55(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");//24小时制
        //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
        long time2 = 0;
        if (str != null) {
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate2(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate3(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate4(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate6(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate7(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate5(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月-dd日");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String stampToDate8(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static List<WeekDay> getWeekDay() {

        Calendar calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        Log.e("TAG", "getWeekDay: " + firstDayOfWeek);
        List<WeekDay> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            WeekDay weekDay = new WeekDay();
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            weekDay.week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CHINA);
            weekDay.day = new SimpleDateFormat("MM/dd").format(calendar.getTime());
            weekDay.dataTime = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            list.add(weekDay);
        }

        return list;
    }


    public static String stampToDate10(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static boolean isBelong4(String start, String end) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(start);
            endTime = df.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return belongCalendar(now, beginTime, endTime);
    }

    public static boolean isBelong5(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(start);
            endTime = df.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return belongCalendar5(now, beginTime, endTime);
    }

    private static boolean belongCalendar5(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 是否当前的展示时间已经结束
     * @param endTime
     * @return true:未结束 false：已结束
     */
    public static boolean isTimeOut(String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date endDate = sdf.parse(endTime);
            Date curDate = new Date();
            if (endDate.after(curDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isTimeOut2(String endTime) {
        Log.e("TAG", "isTimeOut2: " + endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        try {
            Date endDate = sdf.parse(endTime);
            Date curDate = new Date();
            if (endDate.after(curDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static class WeekDay {
        /**
         * 星期的显示名称
         */
        public String week;
        /**
         * 对应的日期
         */
        public String day;

        /**
         * 年月日
         */
        public String dataTime;


        @Override
        public String toString() {
//			return "WeekDay{" +
//					"week='" + week + '\'' +
//					", day='" + day + '\'' +
//					'}';
            return day;


        }
    }

    public static long gettime2(String str) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");//12小时制
        long time2 = 0;
        if (str != null) {
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);

        Log.e("TAG", "gettime2: " + time2);
        return time2;

    }

    public static long gettime3(String str) {


//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
        long time2 = 0;
        if (str != null) {
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }

    /**
     * 日期格式字符串转换成时间戳
     * //     * @param date 字符串日期
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getWeek() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());
        return cd.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取系统时间，判断今天是周几
     */
    public static String getWeek(long time) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time));

        int year = cd.get(Calendar.YEAR); //获取年份
        int month = cd.get(Calendar.MONTH); //获取月份
        int day = cd.get(Calendar.DAY_OF_MONTH); //获取日期
        int week = cd.get(Calendar.DAY_OF_WEEK); //获取星期
        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "周日";
                break;
            case Calendar.MONDAY:
                weekString = "周一";
                break;
            case Calendar.TUESDAY:
                weekString = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "周三";
                break;
            case Calendar.THURSDAY:
                weekString = "周四";
                break;
            case Calendar.FRIDAY:
                weekString = "周五";
                break;
            default:
                weekString = "周六";
                break;

        }

        return weekString;
    }

    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBelong3(String start, String end) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(start);
            endTime = df.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return belongCalendar(now, beginTime, endTime);
    }

    public void ToDoTime(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String value = df2.format(date);
        Log.e("TAG", "ToDoTime: " + JSON.toJSONString(value));

    }


    private static String nums[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static String pos_units[] = {"", "十", "百", "千"};

    private static String weight_units[] = {"", "万", "亿"};

    /**
     * 数字转汉字【新】
     *
     * @param num
     * @return
     */
    public static String numberToChinese(int num) {
        if (num == 0) {
            return "零";
        }

        int weigth = 0;//节权位
        String chinese = "";
        String chinese_section = "";
        boolean setZero = false;//下一小节是否需要零，第一次没有上一小节所以为false
        while (num > 0) {
            int section = num % 10000;//得到最后面的小节
            if (setZero) {//判断上一小节的千位是否为零，是就设置零
                chinese = nums[0] + chinese;
            }
            chinese_section = sectionTrans(section);
            if (section != 0) {//判断是都加节权位
                chinese_section = chinese_section + weight_units[weigth];
            }
            chinese = chinese_section + chinese;
            chinese_section = "";
            Log.d("TAG", chinese_section);

            setZero = (section < 1000) && (section > 0);
            num = num / 10000;
            weigth++;
        }
        if ((chinese.length() == 2 || (chinese.length() == 3)) && chinese.contains("一十")) {
            chinese = chinese.substring(1, chinese.length());
        }
        if (chinese.indexOf("一十") == 0) {
            chinese = chinese.replaceFirst("一十", "十");
        }

        return chinese;
    }

    /**
     * 将每段数字转汉子
     *
     * @param section
     * @return
     */
    public static String sectionTrans(int section) {
        StringBuilder section_chinese = new StringBuilder();
        int pos = 0;//小节内部权位的计数器
        boolean zero = true;//小节内部的置零判断，每一个小节只能有一个零。
        while (section > 0) {
            int v = section % 10;//得到最后一个数
            if (v == 0) {
                if (!zero) {
                    zero = true;//需要补零的操作，确保对连续多个零只是输出一个
                    section_chinese.insert(0, nums[0]);
                }
            } else {
                zero = false;//有非零数字就把置零打开
                section_chinese.insert(0, pos_units[pos]);
                section_chinese.insert(0, nums[v]);
            }
            pos++;
            section = section / 10;
        }

        return section_chinese.toString();
    }

}
