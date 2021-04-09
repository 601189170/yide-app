package com.yyide.chatim.model;

import java.util.List;

public class HomeBannerRsp {

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
        private Integer classifyId;
        private String name;
        private String url;

        public Integer getClassifyId() {
            return classifyId;
        }

        public void setClassifyId(Integer classifyId) {
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
    }
}
