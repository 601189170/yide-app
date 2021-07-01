package com.yyide.chatim.model;

import java.util.ArrayList;
import java.util.List;

public class NoticePersonnelBean {


    public int code;
    public Boolean success;
    public String msg;
    public ArrayList<ListBean> data;

    public static class ListBean {
        public String name;
        public String id;
        public int level;
        public boolean check;
        public boolean unfold;//是否展开
        public String parentId;
        public ArrayList<ListBean> list;
    }
}
