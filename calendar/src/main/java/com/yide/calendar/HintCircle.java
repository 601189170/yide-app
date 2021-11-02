package com.yide.calendar;

import org.joda.time.DateTime;

/**
 * @author liu tao
 * @date 2021/9/14 15:53
 * @description 描述
 */
public class HintCircle {
    private DateTime dateTime;
    private int day;
    private int count;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HintCircle(int day, int count) {
        this.day = day;
        this.count = count;
    }

    public HintCircle() {
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public HintCircle(DateTime dateTime, int day, int count) {
        this.dateTime = dateTime;
        this.day = day;
        this.count = count;
    }
}
