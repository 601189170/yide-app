package com.yyide.chatim.model;

import java.util.List;


/**
 * @Description: 通知公告列表数据
 * @Author: liu tao
 * @CreateDate: 2021/4/6 14:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 14:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class NoticeListRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"records":[{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":3,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":4,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":1617349545000,"content":"这是推送内容","id":2,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null}],"total":3,"size":10,"current":1,"searchCount":true,"pages":1}
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
         * records : [{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":3,"signId":null,"timingTime":null,"type":"待办","status":"0","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":null,"content":"这是推送内容","id":4,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null},{"title":null,"productionTarget":null,"productionTime":1617349545000,"content":"这是推送内容","id":2,"signId":null,"timingTime":null,"type":"待办","status":"1","totalNumber":null,"readNumber":null}]
         * total : 3
         * size : 10
         * current : 1
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;
        private List<RecordsBean> records;

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

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean {
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
             * sendObject
             */

            private String title;
            private String productionTarget;
            private String productionTime;
            private String content;
            private long id;
            private long signId;
            private String timingTime;
            private String type;
            private String status;
            private int totalNumber;
            private int readNumber;
            private String sendObject;

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
        }}
}
