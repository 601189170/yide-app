package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/6 15:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/6 15:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class BrandSearchRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @NoArgsConstructor
    @Data
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
    }
}
