package com.yyide.chatim_pro.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/12 14:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 14:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class DayStatisticsBean {
    private String time;//考勤时间
    private int rate;//考勤率
    private int due;//应到
    private int normal;//正常
    private int absence;//缺勤
    private int ask_for_leave;//请假
    private int late;//迟到

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getAbsence() {
        return absence;
    }

    public void setAbsence(int absence) {
        this.absence = absence;
    }

    public int getAsk_for_leave() {
        return ask_for_leave;
    }

    public void setAsk_for_leave(int ask_for_leave) {
        this.ask_for_leave = ask_for_leave;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public DayStatisticsBean(String time, int rate, int due, int normal, int absence, int ask_for_leave, int late) {
        this.time = time;
        this.rate = rate;
        this.due = due;
        this.normal = normal;
        this.absence = absence;
        this.ask_for_leave = ask_for_leave;
        this.late = late;
    }

    public DayStatisticsBean() {
    }
}
