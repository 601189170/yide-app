package com.yyide.chatim.model;

import java.io.Serializable;

public class NoticeAnnouncementModel implements Serializable {
    private int id;
    private String noticeTitle;
    private String noticeAuthor;
    private String noticeContent;
    private String noticeTime;

    public NoticeAnnouncementModel(String noticeTitle, String noticeAuthor, String noticeContent, String noticeTime) {
        this.noticeTitle = noticeTitle;
        this.noticeAuthor = noticeAuthor;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
    }

    public NoticeAnnouncementModel(int id, String noticeTitle, String noticeAuthor, String noticeContent, String noticeTime) {
        this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeAuthor = noticeAuthor;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
