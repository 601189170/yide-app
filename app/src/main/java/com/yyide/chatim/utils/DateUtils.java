package com.yyide.chatim.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

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
}
