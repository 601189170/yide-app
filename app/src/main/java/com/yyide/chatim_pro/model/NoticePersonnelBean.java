package com.yyide.chatim_pro.model;

import java.util.ArrayList;

public class NoticePersonnelBean {


    public int code;
    public Boolean success;
    public String msg;
    public ArrayList<ListBean> data;

    public static class ListBean {
        public String name;
        public long id;
        public int level;
        public boolean check;
        public int nums;
        public boolean unfold;//是否展开
        public long parentId;
        public String type;
        public NoticePersonnelBean.ListBean parentBean;
        public ArrayList<ListBean> list = new ArrayList<>();
    }
}
