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

            private String title;
            private String content;
            private long userId;
            private long id;
            private long signId;
            private String remakeData;
            private String firstData;
            private long callId;
            private String attributeType;
            private String isText;
            private String status;
            private String sendTime;
            private String createdDateTime;
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getSignId() {
                return signId;
            }

            public void setSignId(long signId) {
                this.signId = signId;
            }

            public String getRemakeData() {
                return remakeData;
            }

            public void setRemakeData(String remakeData) {
                this.remakeData = remakeData;
            }

            public String getFirstData() {
                return firstData;
            }

            public void setFirstData(String firstData) {
                this.firstData = firstData;
            }

            public long getCallId() {
                return callId;
            }

            public void setCallId(long callId) {
                this.callId = callId;
            }

            public String getAttributeType() {
                return attributeType;
            }

            public void setAttributeType(String attributeType) {
                this.attributeType = attributeType;
            }

            public String getIsText() {
                return isText;
            }

            public void setIsText(String isText) {
                this.isText = isText;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public String getCreatedDateTime() {
                return createdDateTime;
            }

            public void setCreatedDateTime(String createdDateTime) {
                this.createdDateTime = createdDateTime;
            }
        }
    }
}
