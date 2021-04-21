package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/21 16:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/21 16:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserNoticeRsp {
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
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
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

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public static class RecordsBean {
            private int id;
            private String delInd;
            private String createdBy;
            private String createdDateTime;
            private Object updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private int userId;
            private String content;
            private Object registrationId;
            private String alias;
            private String type;
            private String equipmentType;
            private String status;
            private String title;
            private Object productionTarget;
            private String productionTime;
            private int productionTargetId;
            private String timingTime;
            private long signId;
            private Object msgId;
            private String timingRead;

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

            public String getCreatedDateTime() {
                return createdDateTime;
            }

            public void setCreatedDateTime(String createdDateTime) {
                this.createdDateTime = createdDateTime;
            }

            public Object getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(Object updatedBy) {
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

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Object getRegistrationId() {
                return registrationId;
            }

            public void setRegistrationId(Object registrationId) {
                this.registrationId = registrationId;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getEquipmentType() {
                return equipmentType;
            }

            public void setEquipmentType(String equipmentType) {
                this.equipmentType = equipmentType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getProductionTarget() {
                return productionTarget;
            }

            public void setProductionTarget(Object productionTarget) {
                this.productionTarget = productionTarget;
            }

            public String getProductionTime() {
                return productionTime;
            }

            public void setProductionTime(String productionTime) {
                this.productionTime = productionTime;
            }

            public int getProductionTargetId() {
                return productionTargetId;
            }

            public void setProductionTargetId(int productionTargetId) {
                this.productionTargetId = productionTargetId;
            }

            public String getTimingTime() {
                return timingTime;
            }

            public void setTimingTime(String timingTime) {
                this.timingTime = timingTime;
            }

            public long getSignId() {
                return signId;
            }

            public void setSignId(long signId) {
                this.signId = signId;
            }

            public Object getMsgId() {
                return msgId;
            }

            public void setMsgId(Object msgId) {
                this.msgId = msgId;
            }

            public String getTimingRead() {
                return timingRead;
            }

            public void setTimingRead(String timingRead) {
                this.timingRead = timingRead;
            }
        }
    }
}
