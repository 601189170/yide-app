package com.yyide.chatim_pro.model;

import java.util.List;

public class SelectTableClassesRsp {

    private Integer code;
    private List<DataBean> data;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
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
        private long schoolId;
        private String alias;
        private String showType;//	0:展示班级名称、1:展示班级别名、2:如有班级别名，显示年级+班级别名，如“实验7班(初一)
        private long gradeId;
        private String showName;
        private String headmasterId;
        private String secondHeadmasterId;
        private String type;
        private List<DataBean> list;


        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

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

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public long getGradeId() {
            return gradeId;
        }

        public void setGradeId(long gradeId) {
            this.gradeId = gradeId;
        }

        public String getHeadmasterId() {
            return headmasterId;
        }

        public void setHeadmasterId(String headmasterId) {
            this.headmasterId = headmasterId;
        }

        public String getSecondHeadmasterId() {
            return secondHeadmasterId;
        }

        public void setSecondHeadmasterId(String secondHeadmasterId) {
            this.secondHeadmasterId = secondHeadmasterId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }
    }
}
