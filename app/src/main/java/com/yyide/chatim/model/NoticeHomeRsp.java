package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

public class NoticeHomeRsp {

    /**
     * code : 200
     * success : true
     * msg : data
     * data : [{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":3,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":5,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null}]
     */

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

    public static class DataBean implements Serializable {
        /**
         * title : null
         * productionTarget : null
         * productionTime : null
         * content : 这是推送内容
         * id : 3
         * signId : null
         * timingTime : null
         * type : 待办
         * status : 0
         * totalNumber : null
         * readNumber : null
         */

        private Object title;
        private Object productionTarget;
        private Object productionTime;
        private String content;
        private int id;
        private Object signId;
        private Object timingTime;
        private String type;
        private String status;
        private Object totalNumber;
        private Object readNumber;

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public Object getProductionTarget() {
            return productionTarget;
        }

        public void setProductionTarget(Object productionTarget) {
            this.productionTarget = productionTarget;
        }

        public Object getProductionTime() {
            return productionTime;
        }

        public void setProductionTime(Object productionTime) {
            this.productionTime = productionTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getSignId() {
            return signId;
        }

        public void setSignId(Object signId) {
            this.signId = signId;
        }

        public Object getTimingTime() {
            return timingTime;
        }

        public void setTimingTime(Object timingTime) {
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

        public Object getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(Object totalNumber) {
            this.totalNumber = totalNumber;
        }

        public Object getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(Object readNumber) {
            this.readNumber = readNumber;
        }
    }
}
