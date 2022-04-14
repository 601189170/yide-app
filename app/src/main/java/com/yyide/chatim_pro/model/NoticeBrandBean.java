package com.yyide.chatim_pro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticeBrandBean implements Serializable {

    public int code;
    public Boolean success;
    public String msg;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
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
        public long classesId;
        public long parendId;
        public long learningSectionId;
        public boolean check;
        public boolean unfold;
        public DataBean parentBean;
        public ArrayList<DataBean> list;
        public ArrayList<DataBean> siteEntityFormList;
    }
}
