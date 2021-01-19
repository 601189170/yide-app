package com.yyide.chatim.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by 86159 on 2021/1/14.
 */

public class OtherUtils {
    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    public static void startcall(String phone, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
