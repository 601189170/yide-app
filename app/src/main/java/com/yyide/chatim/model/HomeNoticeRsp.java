package com.yyide.chatim.model;

/**
 * @Description: HomeNoticeRsp
 * @Author: liu tao
 * @CreateDate: 2021/4/6 19:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 19:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class HomeNoticeRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"id":222,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-08T08:35:37.647+0000","updatedBy":null,"updatedDateTime":"2021-04-08T08:35:37.647+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"这是标题","productionTarget":"小明班主任","productionTime":null,"content":"内容1 内容2","signId":310241259811766272,"timingTime":null,"type":"3","status":"0","totalNumber":1,"readNumber":0,"sendObject":"小明班主任"}
     */

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public HomeNoticeRsp(int code, boolean success, String msg, DataBean data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public HomeNoticeRsp() {
    }

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
        private long id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private String updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private String title;
        private String productionTarget;
        private String productionTime;
        private String content;
        private long signId;
        private String timingTime;
        private String type;
        private String status;
        private int totalNumber;
        private int readNumber;
        private String sendObject;
        private boolean hasNoticePermission;

        public DataBean(long id, String delInd, String createdBy, String createdDateTime, String updatedBy, String updatedDateTime, int versionStamp, int total, int size, int current, String title, String productionTarget, String productionTime, String content, long signId, String timingTime, String type, String status, int totalNumber, int readNumber, String sendObject, boolean hasNoticePermission) {
            this.id = id;
            this.delInd = delInd;
            this.createdBy = createdBy;
            this.createdDateTime = createdDateTime;
            this.updatedBy = updatedBy;
            this.updatedDateTime = updatedDateTime;
            this.versionStamp = versionStamp;
            this.total = total;
            this.size = size;
            this.current = current;
            this.title = title;
            this.productionTarget = productionTarget;
            this.productionTime = productionTime;
            this.content = content;
            this.signId = signId;
            this.timingTime = timingTime;
            this.type = type;
            this.status = status;
            this.totalNumber = totalNumber;
            this.readNumber = readNumber;
            this.sendObject = sendObject;
            this.hasNoticePermission = hasNoticePermission;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public String getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(String createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getUpdatedDateTime() {
            return updatedDateTime;
        }

        public void setUpdatedDateTime(String updatedDateTime) {
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

        public long getSignId() {
            return signId;
        }

        public void setSignId(long signId) {
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

        public boolean isHasNoticePermission() {
            return hasNoticePermission;
        }

        public void setHasNoticePermission(boolean hasNoticePermission) {
            this.hasNoticePermission = hasNoticePermission;
        }

        public DataBean() {
        }
    }
}
