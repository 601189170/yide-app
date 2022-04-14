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

public class StudentDayStatisticsBean {
    private String time;//考勤时间
    private String eventName;
    //考勤状态 1 正常，2迟到，3早退，4，未签到，5请假
    private int eventStatus;
    //打卡时间
    private String eventTime;

    public StudentDayStatisticsBean(String time, String eventName, int eventStatus, String eventTime) {
        this.time = time;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.eventTime = eventTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public StudentDayStatisticsBean() {
    }
}
