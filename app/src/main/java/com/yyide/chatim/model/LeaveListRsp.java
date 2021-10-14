package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: 请假列表数据
 * @Author: liu tao
 * @CreateDate: 2021/5/19 16:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/19 16:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeaveListRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public LeaveListRsp() {
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
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private List<?> orders;
        private boolean optimizeCountSql;
        private boolean hitCount;
        private long countId;
        private Object maxLimit;
        private boolean searchCount;
        private int pages;

        public DataBean() {
        }

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

        public List<?> getOrders() {
            return orders;
        }

        public void setOrders(List<?> orders) {
            this.orders = orders;
        }

        public boolean isOptimizeCountSql() {
            return optimizeCountSql;
        }

        public void setOptimizeCountSql(boolean optimizeCountSql) {
            this.optimizeCountSql = optimizeCountSql;
        }

        public boolean isHitCount() {
            return hitCount;
        }

        public void setHitCount(boolean hitCount) {
            this.hitCount = hitCount;
        }

        public long getCountId() {
            return countId;
        }

        public void setCountId(long countId) {
            this.countId = countId;
        }

        public Object getMaxLimit() {
            return maxLimit;
        }

        public void setMaxLimit(Object maxLimit) {
            this.maxLimit = maxLimit;
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
            private String name;
            private long id;
            private String type;
            private String approvalResult;
            private String initiateTime;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getApprovalResult() {
                return approvalResult;
            }

            public void setApprovalResult(String approvalResult) {
                this.approvalResult = approvalResult;
            }

            public String getInitiateTime() {
                return initiateTime;
            }

            public void setInitiateTime(String initiateTime) {
                this.initiateTime = initiateTime;
            }

            public RecordsBean() {
            }
        }
    }
}
