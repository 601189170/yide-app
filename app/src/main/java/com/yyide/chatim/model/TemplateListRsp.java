package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 16:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 16:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TemplateListRsp {


    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : {"records":[{"id":104,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:40.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:40.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122","type":"0","status":"Y","content":null,"tempId":1,"messages":null},{"id":105,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:09.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:09.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1223","type":"0","status":"Y","content":null,"tempId":1,"messages":null}],"total":2,"size":10,"current":1,"searchCount":true,"pages":1}
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
         * records : [{"id":104,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:40.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:40.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122","type":"0","status":"Y","content":null,"tempId":1,"messages":null},{"id":105,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:09.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:09.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1223","type":"0","status":"Y","content":null,"tempId":1,"messages":null}]
         * total : 2
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
             * id : 104
             * delInd : 0
             * createdBy : 13577778888
             * createdDateTime : 2021-04-05T03:12:40.000+0000
             * updatedBy : null
             * updatedDateTime : 2021-04-05T03:12:40.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * name : 测试122
             * type : 0
             * status : Y
             * content : null
             * tempId : 1
             * messages : null
             */

            private int id;
            private String delInd;
            private String createdBy;
            private String createdDateTime;
            private String updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private String name;
            private String type;
            private String status;
            private String content;
            private int tempId;
            private Object messages;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getTempId() {
                return tempId;
            }

            public void setTempId(int tempId) {
                this.tempId = tempId;
            }

            public Object getMessages() {
                return messages;
            }

            public void setMessages(Object messages) {
                this.messages = messages;
            }

            @Override
            public String toString() {
                return "RecordsBean{" +
                        "id=" + id +
                        ", delInd='" + delInd + '\'' +
                        ", createdBy='" + createdBy + '\'' +
                        ", createdDateTime='" + createdDateTime + '\'' +
                        ", updatedBy=" + updatedBy +
                        ", updatedDateTime='" + updatedDateTime + '\'' +
                        ", versionStamp=" + versionStamp +
                        ", total=" + total +
                        ", size=" + size +
                        ", current=" + current +
                        ", name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        ", status='" + status + '\'' +
                        ", content=" + content +
                        ", tempId=" + tempId +
                        ", messages=" + messages +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "total=" + total +
                    ", size=" + size +
                    ", current=" + current +
                    ", searchCount=" + searchCount +
                    ", pages=" + pages +
                    ", records=" + records +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TemplateListRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
