package com.yyide.chatim.model;

import java.util.List;

public class NoticeMyReleaseDetailBean {
    public Integer code;
    public boolean success;
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
        public int type;//通知公告类型 0空白模板 1非空白模板
        public String coverImgpath;
        public String imgpath;
        public int height;
        public int width;
        public String publisher;
        public int totalNum;
        public int confirmOrReadNum;
        public int notifyRange;
        public boolean confirmOrRead;//
        public List<NotifiesBean> notifies;

        public static class NotifiesBean {
            public List<ListBean> list;
            public int specifieType;
            public int nums;

            public static class ListBean {
                public long specifieId;
                public long specifieParentId;
                public int type;
                public int nums;

            }
        }
    }
}
