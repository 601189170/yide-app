package com.yyide.chatim_pro.model.schedule;

import java.util.List;

/**
 * @author liu tao
 * @date 2021/9/27 15:14
 * @description 描述
 */
public class Settings {

    private Integer code;
    private Boolean success;
    private String msg;
    private List<DataBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private Object delInd;
        private Object createdBy;
        private Object createdDateTime;
        private Object updatedBy;
        private Object updatedDateTime;
        private Object versionStamp;
        private Integer total;
        private Integer size;
        private Integer current;
        private Object userId;
        private Integer remindType;
        private Integer isOpen;
        private List<SubListBean> subList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getDelInd() {
            return delInd;
        }

        public void setDelInd(Object delInd) {
            this.delInd = delInd;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
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

        public Object getVersionStamp() {
            return versionStamp;
        }

        public void setVersionStamp(Object versionStamp) {
            this.versionStamp = versionStamp;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Integer getRemindType() {
            return remindType;
        }

        public void setRemindType(Integer remindType) {
            this.remindType = remindType;
        }

        public Integer getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(Integer isOpen) {
            this.isOpen = isOpen;
        }

        public List<SubListBean> getSubList() {
            return subList;
        }

        public void setSubList(List<SubListBean> subList) {
            this.subList = subList;
        }

        public static class SubListBean {
            private String id;
            private Object delInd;
            private Object createdBy;
            private Object createdDateTime;
            private Object updatedBy;
            private Object updatedDateTime;
            private Object versionStamp;
            private Integer total;
            private Integer size;
            private Integer current;
            private Object scheduleSettingId;
            private String remindTime;
            private Integer remindType;
            private Integer isOpen;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Object getDelInd() {
                return delInd;
            }

            public void setDelInd(Object delInd) {
                this.delInd = delInd;
            }

            public Object getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(Object createdBy) {
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

            public Object getVersionStamp() {
                return versionStamp;
            }

            public void setVersionStamp(Object versionStamp) {
                this.versionStamp = versionStamp;
            }

            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getCurrent() {
                return current;
            }

            public void setCurrent(Integer current) {
                this.current = current;
            }

            public Object getScheduleSettingId() {
                return scheduleSettingId;
            }

            public void setScheduleSettingId(Object scheduleSettingId) {
                this.scheduleSettingId = scheduleSettingId;
            }

            public String getRemindTime() {
                return remindTime;
            }

            public void setRemindTime(String remindTime) {
                this.remindTime = remindTime;
            }

            public Integer getRemindType() {
                return remindType;
            }

            public void setRemindType(Integer remindType) {
                this.remindType = remindType;
            }

            public Integer getIsOpen() {
                return isOpen;
            }

            public void setIsOpen(Integer isOpen) {
                this.isOpen = isOpen;
            }
        }
    }
}
