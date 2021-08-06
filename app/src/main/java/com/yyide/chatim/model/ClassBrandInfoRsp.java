package com.yyide.chatim.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/6 13:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/6 13:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class ClassBrandInfoRsp {


    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
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
    }
}
