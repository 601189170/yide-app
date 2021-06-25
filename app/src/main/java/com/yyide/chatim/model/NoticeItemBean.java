package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
            public boolean isConfirm;
            public boolean isRetract;
            public boolean isTimer;
            public String timerDate;
            public String type;
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
