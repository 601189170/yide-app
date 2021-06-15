package com.yyide.chatim.model;

import java.util.List;

public class ClassesPhotoBannerRsp {

    private String msg;
    private Integer code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private long classifyId;
        private String name;
        private String url;
        private String type;

        public long getClassifyId() {
            return classifyId;
        }

        public void setClassifyId(long classifyId) {
            this.classifyId = classifyId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
