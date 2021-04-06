package com.yyide.chatim.model;

import java.io.Serializable;
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
     * data : {"records":[{"id":98,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"55555","type":"0","status":"Y","content":null,"tempId":null,"messages":[{"id":101,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假1","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":102,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":100,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题888","type":"待办","status":"Y","content":"","tempId":null,"messages":[{"id":127,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":100,"name":"内容1","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":128,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":100,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":101,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题123","type":"待办","status":"Y","content":"","tempId":null,"messages":[{"id":129,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":101,"name":"内容123","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":130,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":101,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":102,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题456","type":"","status":"Y","content":"","tempId":null,"messages":[{"id":131,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":102,"name":"内容123","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":132,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":102,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":103,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":104,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:40.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:40.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":105,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:09.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:09.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1223","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":106,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:34.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:34.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试12234","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":107,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:20:26.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:20:26.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122345","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":108,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:20:26.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:20:26.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122345","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]}],"total":16,"size":10,"current":1,"searchCount":true,"pages":2}
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
         * records : [{"id":98,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"55555","type":"0","status":"Y","content":null,"tempId":null,"messages":[{"id":101,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假1","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":102,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":100,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题888","type":"待办","status":"Y","content":"","tempId":null,"messages":[{"id":127,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":100,"name":"内容1","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":128,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:55:35.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:55:35.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":100,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":101,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题123","type":"待办","status":"Y","content":"","tempId":null,"messages":[{"id":129,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":101,"name":"内容123","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":130,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:15.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":101,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":102,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"标题456","type":"","status":"Y","content":"","tempId":null,"messages":[{"id":131,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":102,"name":"内容123","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":132,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-05T02:57:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T02:57:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":102,"name":"内容2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]},{"id":103,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:33.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:33.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":104,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:12:40.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:12:40.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":105,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:09.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:09.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试1223","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":106,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:15:34.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:15:34.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试12234","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":107,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:20:26.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:20:26.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122345","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]},{"id":108,"delInd":"0","createdBy":"13577778888","createdDateTime":"2021-04-05T03:20:26.000+0000","updatedBy":null,"updatedDateTime":"2021-04-05T03:20:26.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"测试122345","type":"测试1","status":"Y","content":null,"tempId":null,"messages":[]}]
         * total : 16
         * size : 10
         * current : 1
         * searchCount : true
         * pages : 2
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

        public static class RecordsBean implements Serializable {
            /**
             * id : 98
             * delInd : 0
             * createdBy : admin
             * createdDateTime : 2021-04-03T02:20:55.000+0000
             * updatedBy : null
             * updatedDateTime : 2021-04-03T02:20:55.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * name : 55555
             * type : 0
             * status : Y
             * content : null
             * tempId : null
             * messages : [{"id":101,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假1","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null},{"id":102,"delInd":"0","createdBy":"admin","createdDateTime":"2021-04-03T02:20:55.000+0000","updatedBy":null,"updatedDateTime":"2021-04-03T02:20:55.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"messageId":98,"name":"放假2","title":null,"sort":0,"status":"Y","sendTarget":null,"signId":null}]
             */

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
            private String name;
            private String type;
            private String status;
            private Object content;
            private Object tempId;
            private List<MessagesBean> messages;

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

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }

            public Object getTempId() {
                return tempId;
            }

            public void setTempId(Object tempId) {
                this.tempId = tempId;
            }

            public List<MessagesBean> getMessages() {
                return messages;
            }

            public void setMessages(List<MessagesBean> messages) {
                this.messages = messages;
            }

            public static class MessagesBean implements Serializable {
                /**
                 * id : 101
                 * delInd : 0
                 * createdBy : admin
                 * createdDateTime : 2021-04-03T02:20:55.000+0000
                 * updatedBy : null
                 * updatedDateTime : 2021-04-03T02:20:55.000+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * messageId : 98
                 * name : 放假1
                 * title : null
                 * sort : 0
                 * status : Y
                 * sendTarget : null
                 * signId : null
                 */

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
                private int messageId;
                private String name;
                private Object title;
                private int sort;
                private String status;
                private Object sendTarget;
                private Object signId;

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

                public int getMessageId() {
                    return messageId;
                }

                public void setMessageId(int messageId) {
                    this.messageId = messageId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getSendTarget() {
                    return sendTarget;
                }

                public void setSendTarget(Object sendTarget) {
                    this.sendTarget = sendTarget;
                }

                public Object getSignId() {
                    return signId;
                }

                public void setSignId(Object signId) {
                    this.signId = signId;
                }

                @Override
                public String toString() {
                    return "MessagesBean{" +
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
                            ", messageId=" + messageId +
                            ", name='" + name + '\'' +
                            ", title=" + title +
                            ", sort=" + sort +
                            ", status='" + status + '\'' +
                            ", sendTarget=" + sendTarget +
                            ", signId=" + signId +
                            '}';
                }
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
