package com.yyide.chatim.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yyide.chatim.R;

public class StringUtils {

    public static String subString(String msg, int sun) {
        if (!TextUtils.isEmpty(msg) && msg.length() > sun) {
            msg = msg.substring(msg.length() - sun, msg.length());
        }
        return msg;
    }

    public static String firstLineRetractText(Context context,String content){
        if (!TextUtils.isEmpty(content)){
            int st = 0;
            int length = content.length();
            Log.e("firstLineRetractText:","-"+content.charAt(st) );
            while (st < length && content.charAt(st) == ' ' || content.charAt(st) == '　') {
                st++;
            }
            content = content.substring(st);
            return String.format(context.getString(R.string.retract),content.trim());
        }
        return content;
    }
}
