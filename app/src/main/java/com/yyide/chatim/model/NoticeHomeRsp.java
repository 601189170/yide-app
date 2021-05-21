package com.yyide.chatim.model;

import java.util.List;

public class NoticeHomeRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String productionTarget;
        private String productionTime;
        private String content;
        private Integer id;
        private String signId;
        private String timingTime;
        private String type;
        private String status;
        private String totalNumber;
        private String readNumber;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProductionTarget() {
            return productionTarget;
        }

        public void setProductionTarget(String productionTarget) {
            this.productionTarget = productionTarget;
        }

        public String getProductionTime() {
            return productionTime;
        }

        public void setProductionTime(String productionTime) {
            this.productionTime = productionTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSignId() {
            return signId;
        }

        public void setSignId(String signId) {
            this.signId = signId;
        }

        public String getTimingTime() {
            return timingTime;
        }

        public void setTimingTime(String timingTime) {
            this.timingTime = timingTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(String totalNumber) {
            this.totalNumber = totalNumber;
        }

        public String getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(String readNumber) {
            this.readNumber = readNumber;
        }
    }
}
