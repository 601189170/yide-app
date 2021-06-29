package com.yyide.chatim.model;

import java.util.List;

public class NoticeMyReleaseDetailBean {

    public Integer code;
    public Boolean success;
    public String msg;
    public DataBean data;

    public static class DataBean {
        public long id;
        public String title;
        public String content;
        public boolean isConfirm;
        public boolean isRetract;
        public boolean isTimer;
        public String timerDate;
        public int type;
        public String coverImgpath;
        public String imgpath;
        public int height;
        public int width;
        public int notifyRange;
        public String publisher;
        public List<String> notifies;
    }
}
