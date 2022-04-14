package com.yyide.chatim_pro.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/6 13:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/6 13:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class ClassBrandInfoRsp {


    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public ClassBrandInfoRsp() {
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
        private String id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private String updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private String registrationCodeId;
        private String registrationCode;
        private String officeName;
        private String officeId;
        private Object classId;
        private Object equipmentSerialNumber;
        private String status;
        private String remarks;
        private int num;
        private Object registrationCodeEntityList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getRegistrationCodeId() {
            return registrationCodeId;
        }

        public void setRegistrationCodeId(String registrationCodeId) {
            this.registrationCodeId = registrationCodeId;
        }

        public String getRegistrationCode() {
            return registrationCode;
        }

        public void setRegistrationCode(String registrationCode) {
            this.registrationCode = registrationCode;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getOfficeId() {
            return officeId;
        }

        public void setOfficeId(String officeId) {
            this.officeId = officeId;
        }

        public Object getClassId() {
            return classId;
        }

        public void setClassId(Object classId) {
            this.classId = classId;
        }

        public Object getEquipmentSerialNumber() {
            return equipmentSerialNumber;
        }

        public void setEquipmentSerialNumber(Object equipmentSerialNumber) {
            this.equipmentSerialNumber = equipmentSerialNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public Object getRegistrationCodeEntityList() {
            return registrationCodeEntityList;
        }

        public void setRegistrationCodeEntityList(Object registrationCodeEntityList) {
            this.registrationCodeEntityList = registrationCodeEntityList;
        }

        public DataBean() {
        }
    }
}
