package com.yyide.chatim.utils;

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
