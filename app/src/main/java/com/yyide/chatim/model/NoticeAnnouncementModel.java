package com.yyide.chatim.model;

import java.io.Serializable;

public class NoticeAnnouncementModel implements Serializable {
    private int id;
    private String noticeTitle;
    private String noticeAuthor;
    private String noticeContent;
    private String noticeTime;
    private String status;//读取状态 1 已读 0未读

    public NoticeAnnouncementModel(String noticeTitle, String noticeAuthor, String noticeContent, String noticeTime) {
        this.noticeTitle = noticeTitle;
        this.noticeAuthor = noticeAuthor;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
    }

    public NoticeAnnouncementModel(int id, String noticeTitle, String noticeAuthor, String noticeContent, String noticeTime,String status) {
        this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeAuthor = noticeAuthor;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeAuthor() {
        return noticeAuthor;
    }

    public void setNoticeAuthor(String noticeAuthor) {
        this.noticeAuthor = noticeAuthor;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }
}
