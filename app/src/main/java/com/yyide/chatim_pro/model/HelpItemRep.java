package com.yyide.chatim_pro.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HelpItemRep {
    /**
     * Auto-generated: 2021-04-08 18:20:10
     *
     * @author json.cn (i@json.cn)
     * @website http://www.json.cn/java2pojo/
     */

    private int code;
    private boolean success;
    private String message;
    private Records data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public String getMsg() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Records getData() {
        return data;
    }

    public void setData(Records data) {
        this.data = data;
    }

    public class Records {
        private List<HelpItemBean> records = new ArrayList<>();

        public List<HelpItemBean> getRecords() {
            return records;
        }

        public void setRecords(List<HelpItemBean> records) {
            this.records = records;
        }

        public class HelpItemBean implements Serializable, MultiItemEntity {
            /**
             * Auto-generated: 2021-04-08 20:2:39
             *
             * @author json.cn (i@json.cn)
             * @website http://www.json.cn/java2pojo/
             */

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
            private String name;
            private String type;
            private String hotInd;
            private int sort;
            private String message;
            private String status;//0是富文本，1是视频
            private String video;

            public void setId(long id) {
                this.id = id;
            }

            public long getId() {
                return id;
            }

            public void setDelInd(String delInd) {
                this.delInd = delInd;
            }

            public String getDelInd() {
                return delInd;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedDateTime(String createdDateTime) {
                this.createdDateTime = createdDateTime;
            }

            public String getCreatedDateTime() {
                return createdDateTime;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedDateTime(String updatedDateTime) {
                this.updatedDateTime = updatedDateTime;
            }

            public String getUpdatedDateTime() {
                return updatedDateTime;
            }

            public void setVersionStamp(int versionStamp) {
                this.versionStamp = versionStamp;
            }

            public int getVersionStamp() {
                return versionStamp;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getTotal() {
                return total;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getSize() {
                return size;
            }

            public void setCurrent(int current) {
                this.current = current;
            }

            public int getCurrent() {
                return current;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            public void setHotInd(String hotInd) {
                this.hotInd = hotInd;
            }

            public String getHotInd() {
                return hotInd;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getSort() {
                return sort;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return status;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getVideo() {
                return video;
            }

            @Override
            public int getItemType() {
                return "0".equals(status) ? 1 : 2;
            }
        }
    }
}
