package com.yide.calendar;

/**
 * @author liu tao
 * @date 2021/9/14 15:53
 * @description 描述
 */
public class HintCircle {
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
}
