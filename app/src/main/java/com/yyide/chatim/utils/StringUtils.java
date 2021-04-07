package com.yyide.chatim.utils;

import android.text.TextUtils;

public class StringUtils {

    public static String subString(String msg, int sun) {
        if (!TextUtils.isEmpty(msg) && msg.length() > sun) {
            msg = msg.substring(msg.length() - sun, msg.length());
        }
        return msg;
    }
}
