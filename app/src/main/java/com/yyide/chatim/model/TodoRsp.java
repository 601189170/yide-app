package com.yyide.chatim.model;

import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/13 15:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/13 15:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TodoRsp{

    /**
     * code : 200
     * success : true
     * msg : data
     * data : {"records":[{"id":222,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-13T06:58:42.632+0000","updatedBy":null,"updatedDateTime":"2021-04-13T06:58:42.632+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"这是标题","productionTarget":"小明班主任","productionTime":null,"content":"内容1 内容2","signId":310241259811766272,"timingTime":null,"type":"2","status":"1","totalNumber":null,"readNumber":null,"sendObject":null}],"total":1,"size":20,"current":1,"searchCount":true,"pages":1}
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
         * records : [{"id":222,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-13T06:58:42.632+0000","updatedBy":null,"updatedDateTime":"2021-04-13T06:58:42.632+0000","versionStamp":0,"total":0,"size":10,"current":1,"title":"这是标题","productionTarget":"小明班主任","productionTime":null,"content":"内容1 内容2","signId":310241259811766272,"timingTime":null,"type":"2","status":"1","totalNumber":null,"readNumber":null,"sendObject":null}]
         * total : 1
         * size : 20
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

        public static class RecordsBean implements MultiItemEntity{
            /**
             * id : 222
             * delInd : 0
             * createdBy : null
             * createdDateTime : 2021-04-13T06:58:42.632+0000
             * updatedBy : null
             * updatedDateTime : 2021-04-13T06:58:42.632+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * title : 这是标题
             * productionTarget : 小明班主任
             * productionTime : null
             * content : 内容1 内容2
             * signId : 310241259811766272
             * timingTime : null
             * type : 2
             * status : 1
             * totalNumber : null
             * readNumber : null
             * sendObject : null
             */

            private int id;
            private String delInd;
            private Object createdBy;
            private String createdDateTime;
            private Object updatedBy;
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
            private long callId;
            private String timingTime;
            private String type;
            private String status;//‘读取状态 1 已读/同意 0未读 2拒绝 3已撤回
            private int totalNumber;
            private int readNumber;
            private String sendObject;
            private String firstData;
            private String attributeType;//	消息属性 1 请假 2 调课
            private String isText;//是否纯文本 1 是 2不是
            private int messageType;//消息类型 1 通知公告 2 代办 3系统通知
            private String isOperation;
            public static final String IS_TEXT_TYPE = "1";
            public static final int ITEM_TYPE_MESSAGE = 1;
            public static final int ITEM_TYPE_NOTICE = 2;
            public static final int ITEM_TYPE_TODO = 3;

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public String getIsOperation() {
                return isOperation;
            }

            public void setIsOperation(String isOperation) {
                this.isOperation = isOperation;
            }

            public String getFirstData() {
                return firstData;
            }

            public void setFirstData(String firstData) {
                this.firstData = firstData;
            }

            public int getId() {
                return id;
            }

            public long getCallId() {
                return callId;
            }

            public void setCallId(long callId) {
                this.callId = callId;
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

            public Object getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(Object createdBy) {
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

            public Object getProductionTime() {
                return productionTime;
            }

            public void setProductionTime(String productionTime) {
                this.productionTime = productionTime;
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

            public Object getTimingTime() {
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

            public Object getTotalNumber() {
                return totalNumber;
            }

            public void setTotalNumber(int totalNumber) {
                this.totalNumber = totalNumber;
            }

            public Object getReadNumber() {
                return readNumber;
            }

            public void setReadNumber(int readNumber) {
                this.readNumber = readNumber;
            }

            public Object getSendObject() {
                return sendObject;
            }

            public void setSendObject(String sendObject) {
                this.sendObject = sendObject;
            }

            @Override
            public String toString() {
                return "RecordsBean{" +
                        "id=" + id +
                        ", delInd='" + delInd + '\'' +
                        ", createdBy=" + createdBy +
                        ", createdDateTime='" + createdDateTime + '\'' +
                        ", updatedBy=" + updatedBy +
                        ", updatedDateTime='" + updatedDateTime + '\'' +
                        ", versionStamp=" + versionStamp +
                        ", total=" + total +
                        ", size=" + size +
                        ", current=" + current +
                        ", title='" + title + '\'' +
                        ", productionTarget='" + productionTarget + '\'' +
                        ", productionTime=" + productionTime +
                        ", content='" + content + '\'' +
                        ", signId=" + signId +
                        ", timingTime=" + timingTime +
                        ", type='" + type + '\'' +
                        ", status='" + status + '\'' +
                        ", totalNumber=" + totalNumber +
                        ", readNumber=" + readNumber +
                        ", sendObject=" + sendObject +
                        '}';
            }

            @Override
            public int getItemType() {
                int type = -1;
                Log.d("setTodoItem", status + "");
                switch (messageType){
                    case 1:
                        type = TodoRsp.DataBean.RecordsBean.ITEM_TYPE_MESSAGE;
                        break;
                    case 3:
                        type = TodoRsp.DataBean.RecordsBean.ITEM_TYPE_NOTICE;
                        break;
                    default:
                        type = TodoRsp.DataBean.RecordsBean.ITEM_TYPE_TODO;
                        break;
                }
                return type;
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
        return "TodoRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
