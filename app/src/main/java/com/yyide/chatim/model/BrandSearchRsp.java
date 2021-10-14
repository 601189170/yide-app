package com.yyide.chatim.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/6 15:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/6 15:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class BrandSearchRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    public BrandSearchRsp() {
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private Object id;
        private Object delInd;
        private Object createdBy;
        private Object createdDateTime;
        private Object updatedBy;
        private Object updatedDateTime;
        private Object versionStamp;
        private int total;
        private int size;
        private int current;
        private Object schoolId;
        private String signName;//班牌名称
        private String loginName;//登录名
        private String password;
        private String classesId;
        private String siteId;
        private Object sysId;
        private String status;//登录状态 0：已登录、1：未登录
        private Object isMessage;
        private Object isSecond;
        private Object remarks;
        private Object name;
        private String isOnLine;
        private Object equipmentNo;
        private Object buildingId;
        private Object siteList;
        private Object siteSet;
        private Object classesList;
        private Object page;
        private Object classesSignSwitchEntitys;
        private Object layoutClassSignDetailsEntities;
        private Object layoutClassSignDetailsEntitieList;
        private Object dataPermission;
        private Object registrationCode;
        private Object saveList;
        private Object userId;
        private boolean checked;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
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

        public Object getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(Object schoolId) {
            this.schoolId = schoolId;
        }

        public String getSignName() {
            return signName;
        }

        public void setSignName(String signName) {
            this.signName = signName;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getClassesId() {
            return classesId;
        }

        public void setClassesId(String classesId) {
            this.classesId = classesId;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public Object getSysId() {
            return sysId;
        }

        public void setSysId(Object sysId) {
            this.sysId = sysId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getIsMessage() {
            return isMessage;
        }

        public void setIsMessage(Object isMessage) {
            this.isMessage = isMessage;
        }

        public Object getIsSecond() {
            return isSecond;
        }

        public void setIsSecond(Object isSecond) {
            this.isSecond = isSecond;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public String getIsOnLine() {
            return isOnLine;
        }

        public void setIsOnLine(String isOnLine) {
            this.isOnLine = isOnLine;
        }

        public Object getEquipmentNo() {
            return equipmentNo;
        }

        public void setEquipmentNo(Object equipmentNo) {
            this.equipmentNo = equipmentNo;
        }

        public Object getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(Object buildingId) {
            this.buildingId = buildingId;
        }

        public Object getSiteList() {
            return siteList;
        }

        public void setSiteList(Object siteList) {
            this.siteList = siteList;
        }

        public Object getSiteSet() {
            return siteSet;
        }

        public void setSiteSet(Object siteSet) {
            this.siteSet = siteSet;
        }

        public Object getClassesList() {
            return classesList;
        }

        public void setClassesList(Object classesList) {
            this.classesList = classesList;
        }

        public Object getPage() {
            return page;
        }

        public void setPage(Object page) {
            this.page = page;
        }

        public Object getClassesSignSwitchEntitys() {
            return classesSignSwitchEntitys;
        }

        public void setClassesSignSwitchEntitys(Object classesSignSwitchEntitys) {
            this.classesSignSwitchEntitys = classesSignSwitchEntitys;
        }

        public Object getLayoutClassSignDetailsEntities() {
            return layoutClassSignDetailsEntities;
        }

        public void setLayoutClassSignDetailsEntities(Object layoutClassSignDetailsEntities) {
            this.layoutClassSignDetailsEntities = layoutClassSignDetailsEntities;
        }

        public Object getLayoutClassSignDetailsEntitieList() {
            return layoutClassSignDetailsEntitieList;
        }

        public void setLayoutClassSignDetailsEntitieList(Object layoutClassSignDetailsEntitieList) {
            this.layoutClassSignDetailsEntitieList = layoutClassSignDetailsEntitieList;
        }

        public Object getDataPermission() {
            return dataPermission;
        }

        public void setDataPermission(Object dataPermission) {
            this.dataPermission = dataPermission;
        }

        public Object getRegistrationCode() {
            return registrationCode;
        }

        public void setRegistrationCode(Object registrationCode) {
            this.registrationCode = registrationCode;
        }

        public Object getSaveList() {
            return saveList;
        }

        public void setSaveList(Object saveList) {
            this.saveList = saveList;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public DataBean() {
        }
    }
}
