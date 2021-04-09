package com.yyide.chatim.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 将时间戳转换为时间
     * <p>
     * s就是时间戳
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 接收到后台传来的时间格式2019-04-30T15:59:59.000+0000，转换指定格式的时间
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
