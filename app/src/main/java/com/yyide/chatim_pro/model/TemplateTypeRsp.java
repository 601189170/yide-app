package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 16:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 16:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TemplateTypeRsp {

    /**
     * code : 200
     * success : true
     * msg : 查询成功
     * data : {"records":[{"id":1,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"安全"},{"id":2,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"假期"},{"id":3,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"全部"}],"total":3,"size":10,"current":1,"searchCount":true,"pages":1}
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
         * records : [{"id":1,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"安全"},{"id":2,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"假期"},{"id":3,"delInd":"0","createdBy":"","createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"versionStamp":0,"total":0,"size":10,"current":1,"tempName":"全部"}]
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
             * id : 1
             * delInd : 0
             * createdBy :
             * createdDateTime : null
             * updatedBy : null
             * updatedDateTime : null
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * tempName : 安全
             */

            private long id;
            private String delInd;
            private String createdBy;
            private Object createdDateTime;
            private Object updatedBy;
            private Object updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private String tempName;

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

            public Object getCreatedDateTime() {
                return createdDateTime;
            }

            public void setCreatedDateTime(Object createdDateTime) {
                this.createdDateTime = createdDateTime;
            }

            public Object getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(Object updatedBy) {
                this.updatedBy = updatedBy;
            }

            public Object getUpdatedDateTime() {
                return updatedDateTime;
            }

            public void setUpdatedDateTime(Object updatedDateTime) {
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

            public String getTempName() {
                return tempName;
            }

            public void setTempName(String tempName) {
                this.tempName = tempName;
            }
        }
    }
}
