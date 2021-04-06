package com.yyide.chatim.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }
}
