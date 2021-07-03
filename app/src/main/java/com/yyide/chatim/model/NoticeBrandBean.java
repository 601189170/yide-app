package com.yyide.chatim.model;

import java.util.ArrayList;
import java.util.List;

public class NoticeBrandBean {

    public int code;
    public Boolean success;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        public long id;
        public long siteId;
        public String name;
        public String alias;
        public Object showType;
        public String showName;
        public int level;
        public long schoolId;
        public String type;
        public String isExitInd;
        public long parentId;
        public long learningSectionId;
        public boolean check;
        public boolean unfold;
        public ArrayList<DataBean> list = new ArrayList<>();
    }
}
