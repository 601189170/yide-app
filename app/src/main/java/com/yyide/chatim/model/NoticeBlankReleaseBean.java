package com.yyide.chatim.model;

import java.util.List;

public class NoticeBlankReleaseBean {

    public String messageTemplateId;
    public String title;
    public String content;
    public Boolean isConfirm;
    public Boolean isTimer;
    public String timerDate;
    public int notifyRange;
    public List<String> subIds;

    public List<RecordListBean> recordList;


    public static class RecordListBean {
        public int specifieType;
        public List<ListBean> list;

        public static class ListBean {
            public int type;
            public long specifieId;
            public int nums;
        }
    }
}
