package com.yyide.chatim.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

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
     * 时间戳string 转 指定格式的时间
     * @param createTime
     * @return
     */
    public static String switchCreateTime(String createTime,String dateFormat) {
        String formatStr2 = "";
        if (TextUtils.isEmpty(createTime)){
            return formatStr2;
        }
        Long time = Long.valueOf(createTime);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(new Date(time*1000L));
    }

    /**
     * //pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8"
     * @param time
     * @return
     */
    public static String switchTime(Date time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    public static String switchTime(Date time,String format){
        if (TextUtils.isEmpty(format)){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(time);
    }

    /**
     * 日期转换
     * @param time
     * @return
     */
    public static String formatTime(String time){
        if (TextUtils.isEmpty(time)){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    /**
     * 将指定格式的时间转换目标格式的时间格式
     * @param time 初始时间
     * @param source 原始格式
     * @param target 目标格式
     * @return 转换后的时间
     */
    public static String formatTime(String time, String source, String target) {
        if (TextUtils.isEmpty(time)){
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
     * 获取日期毫秒数
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowtime = simpleDateFormat.format(curDate);
        try {
            // 获得两个时间的毫秒时间差异
            diff = Math.abs(simpleDateFormat.parse(nowtime).getTime() - simpleDateFormat.parse(endTime).getTime());
            long day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            Log.e("dateDiff", "dateDiff: "+day+","+hour+","+min+","+sec);
            return new long[]{day,hour,min};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new long[]{};
    }
}
