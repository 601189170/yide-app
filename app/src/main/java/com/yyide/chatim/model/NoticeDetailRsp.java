package com.yyide.chatim.model;

import java.util.Date;

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
     * data : {"id":3,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-09T02:55:49.327+0000","updatedBy":null,"updatedDateTime":"2021-04-09T02:55:49.327+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":274,"readNumber":12,"sendObject":null}
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

    public static class DataBean {
        /**
         * id : 3
         * delInd : 0
         * createdBy : null
         * createdDateTime : 2021-04-09T02:55:49.327+0000
         * updatedBy : null
         * updatedDateTime : 2021-04-09T02:55:49.327+0000
         * versionStamp : 0
         * total : 0
         * size : 10
         * current : 1
         * title : null
         * productionTarget : null
         * productionTime : null
         * content : 这是推送内容
         * signId : null
         * timingTime : null
         * type : 待办
         * status : 1
         * totalNumber : 274
         * readNumber : 12
         * sendObject : null
         */

        private int id;
        private String delInd;
        private String createdBy;
        private Date createdDateTime;
        private String updatedBy;
        private Date updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private String title;
        private String productionTarget;
        private String productionTime;
        private String content;
        private String signId;
        private String timingTime;
        private String type;
        private String status;
        private int totalNumber;
        private int readNumber;
        private String sendObject;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDelInd() {
            return delInd;
        }

        public void setDelInd(String delInd) {
            this.delInd = delInd;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Date getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(Date createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Date getUpdatedDateTime() {
            return updatedDateTime;
        }

        public void setUpdatedDateTime(Date updatedDateTime) {
            this.updatedDateTime = updatedDateTime;
        }

        public int getVersionStamp() {
            return versionStamp;
        }

        public void setVersionStamp(int versionStamp) {
            this.versionStamp = versionStamp;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

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

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }

        public int getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(int readNumber) {
            this.readNumber = readNumber;
        }

        public String getSendObject() {
            return sendObject;
        }

        public void setSendObject(String sendObject) {
            this.sendObject = sendObject;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", delInd='" + delInd + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", createdDateTime=" + createdDateTime +
                    ", updatedBy='" + updatedBy + '\'' +
                    ", updatedDateTime=" + updatedDateTime +
                    ", versionStamp=" + versionStamp +
                    ", total=" + total +
                    ", size=" + size +
                    ", current=" + current +
                    ", title='" + title + '\'' +
                    ", productionTarget='" + productionTarget + '\'' +
                    ", productionTime='" + productionTime + '\'' +
                    ", content='" + content + '\'' +
                    ", signId='" + signId + '\'' +
                    ", timingTime='" + timingTime + '\'' +
                    ", type='" + type + '\'' +
                    ", status='" + status + '\'' +
                    ", totalNumber=" + totalNumber +
                    ", readNumber=" + readNumber +
                    ", sendObject='" + sendObject + '\'' +
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
