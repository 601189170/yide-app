package com.yyide.chatim.model;

import java.io.Serializable;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 15:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 15:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NoticeDetailRsp {

    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"title":null,"productionTarget":null,"productionTime":null,"content":"测试1，测试2。","id":16,"signId":null,"timingTime":null,"type":"测试1","status":"0","totalNumber":null,"readNumber":null}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * title : null
         * productionTarget : null
         * productionTime : null
         * content : 测试1，测试2。
         * id : 16
         * signId : null
         * timingTime : null
         * type : 测试1
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "title=" + title +
                    ", productionTarget=" + productionTarget +
                    ", productionTime=" + productionTime +
                    ", content='" + content + '\'' +
                    ", id=" + id +
                    ", signId=" + signId +
                    ", timingTime=" + timingTime +
                    ", type='" + type + '\'' +
                    ", status='" + status + '\'' +
                    ", totalNumber=" + totalNumber +
                    ", readNumber=" + readNumber +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NoticeDetailRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
