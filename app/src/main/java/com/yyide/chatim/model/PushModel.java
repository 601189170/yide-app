package com.yyide.chatim.model;

import android.text.TextUtils;

public class PushModel {

    private String pushType;//消息类型 1 通知公告 2 代办 3系统通知
    private String signId;
    private String title;
    private String id;

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
