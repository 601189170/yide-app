package com.yyide.chatim_pro.utils;

import android.graphics.Color;
import android.text.TextUtils;

import com.yyide.chatim_pro.model.schedule.LabelColor;

/**
 * @author liu tao
 * @date 2021/9/26 9:50
 * @description 描述
 */
public class ColorUtil {
    private static String formatColor(String color) {
        if(TextUtils.isEmpty(color)){
            return LabelColor.color1;
        }
        if (color.length() == 4) {
            final StringBuilder stringBuilder = new StringBuilder(color);
            if (color.charAt(1) == color.charAt(2) && (color.charAt(2) == color.charAt(3) && (color.charAt(1) == color.charAt(3)))) {
                return stringBuilder.insert(1, color.substring(1)).toString();
            } else {
                return stringBuilder.insert(1, 0).insert(3, 0).insert(5, 0).toString();
            }
        }
        if (color.length() == 7 || color.length() == 9) {
            return color;
        }
        throw new IllegalArgumentException("Unknown color");
    }

    public static int parseColor(String color) {
        color = formatColor(color);
        return Color.parseColor(color);
    }
}
