package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

public class WebModel implements Serializable {

    public String enentName;
    public String subIds;
    public ParamsBean params;

    public static class ParamsBean implements Serializable {

        public List<SubsBean> subs;
        public String tempTitle;
        public String tempId;

        public List<SubsBean> getSubs() {
            return subs;
        }

        public void setSubs(List<SubsBean> subs) {
            this.subs = subs;
        }

        public String getTempTitle() {
            return tempTitle;
        }

        public void setTempTitle(String tempTitle) {
            this.tempTitle = tempTitle;
        }

        public String getTempId() {
            return tempId;
        }

        public void setTempId(String tempId) {
            this.tempId = tempId;
        }

        public static class SubsBean implements Serializable{
            public String messageTemplateDetailId;
            public Object file;
            public String imgpath;
            public String id;
            public int showType;
        }
    }
}
