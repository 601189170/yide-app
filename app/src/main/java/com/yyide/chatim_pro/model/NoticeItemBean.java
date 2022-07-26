package com.yyide.chatim_pro.model;

import java.util.List;


public class NoticeItemBean {

    public int code;
    public boolean success;
    public String msg;
    public DataBean data;


    public static class DataBean {
        public List<RecordsBean> records;
        public int total;
        public int size;
        public int current;
        public List<?> orders;
        public boolean optimizeCountSql;
        public boolean hitCount;
        public long countId;
        public int maxLimit;
        public boolean searchCount;
        public int pages;

        public static class RecordsBean {
            public int total;
            public int size;
            public int current;
            public long id;
            public String title;
            public String content;
            public boolean isConfirm;//是否需要确认
            public boolean isRetract;
            public boolean confirmOrRead;//是否已读
            public boolean isTimer;
            public String timerDate;
            public int type;
            public String publisher;
            public String coverImgpath;
            public long messagePublishId;
            public String imgpath;
            public int height;
            public int width;
            public int totalNum;
            public int confirmOrReadNum;

        }
    }
}
