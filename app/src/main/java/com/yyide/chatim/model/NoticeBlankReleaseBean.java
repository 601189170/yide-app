package com.yyide.chatim.model;

import java.util.ArrayList;
import java.util.List;

public class NoticeBlankReleaseBean {

    public String messageTemplateId;
    public String title;
    public String content;
    public boolean isConfirm;
    public boolean isTimer;
    public String timerDate;
    public int notifyRange;
    public List<String> subIds;

    public List<RecordListBean> recordList;

    public static class RecordListBean {
        public String specifieType;
        public int nums;
        public List<ListBean> list = new ArrayList<>();

        public static class ListBean {
            public String type;
            public long specifieId;
            public long specifieParentId;
            public int nums;
        }
    }
}
