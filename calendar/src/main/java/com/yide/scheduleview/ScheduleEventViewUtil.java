package com.yide.scheduleview;

import java.util.Calendar;

/**
 * Created by jesse on 6/02/2016.
 */
public class ScheduleEventViewUtil {


    /////////////////////////////////////////////////////////////////
    //
    //      Helper methods.
    //
    /////////////////////////////////////////////////////////////////

    /**
     * Checks if two times are on the same day.
     * @param dayOne The first day.
     * @param dayTwo The second day.
     * @return Whether the times are on the same day.
     */
    public static boolean isSameDay(Calendar dayOne, Calendar dayTwo) {
        return dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR) && dayOne.get(Calendar.DAY_OF_YEAR) == dayTwo.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Returns a calendar instance at the start of this day
     * @return the calendar instance
     */
    public static Calendar today(){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    /**
     * 获取有透明度的颜色值
     * @param color #8F81FE原始颜色
     * @param alpha 透明度对应的十六进制值
     * @return
     */
    public static String getColorWithAlpha(String color,String alpha){
        if (color.length() == 7){
            final StringBuilder stringBuilder = new StringBuilder(color);
            return stringBuilder.insert(1,alpha).toString();
        }
        return color;
    }
}
