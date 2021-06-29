package com.yyide.chatim.model;

import java.util.List;

public class NoticeReleaseTemplateBean {

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
        public Boolean optimizeCountSql;
        public Boolean hitCount;
        public long countId;
        public int maxLimit;
        public Boolean searchCount;
        public int pages;

        public static class RecordsBean {
            public long id;
            public String delInd;
            public String createdBy;
            public String createdDateTime;
            public Object updatedBy;
            public String updatedDateTime;
            public int versionStamp;
            public int total;
            public int size;
            public int current;
            public String coverImg;

            public String messageTemplateTypeId;
            public String name;
            public String title;
            public int titleLength;
            public String content;
            public int contentLength;
            public int sort;
            public String createdUserId;
            public Object updatedUserId;
            public int type;//0空白模板 1非空白模板
            public List<DetailsBean> details;

            public static class DetailsBean {
                public long id;
                public String delInd;
                public String createdBy;
                public String createdDateTime;
                public Object updatedBy;
                public Object updatedDateTime;
                public int versionStamp;
                public int total;
                public int size;
                public int current;
                public Long messageTemplateId;
                public String coverImg;
                public String contentImg;
                public int width;
                public int height;
                public String parseJson;
                public Object coverImgFile;
                public Object contentImgFile;

            }
        }
    }
}
