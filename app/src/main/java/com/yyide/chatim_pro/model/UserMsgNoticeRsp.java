package com.yyide.chatim_pro.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/24 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/24 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class UserMsgNoticeRsp implements Serializable {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public UserMsgNoticeRsp() {
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

    public static class DataBean implements Serializable {
        private List<DataItemBean> list;
        private int total;

        public List<DataItemBean> getList() {
            return list;
        }

        public void setList(List<DataItemBean> list) {
            this.list = list;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public static class DataItemBean implements Serializable {
            private String callId;
            private String content;
            private String firstData;
            private String identityUserId;
            private int moduleId;
            private int moduleType;
            private String remakeData;
            private String schoolId;
            private String sendTime;
            private String title;
            private String userName;
            private String userType;

            public String getCallId() {
                return callId;
            }

            public void setCallId(String callId) {
                this.callId = callId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getFirstData() {
                return firstData;
            }

            public void setFirstData(String firstData) {
                this.firstData = firstData;
            }

            public String getIdentityUserId() {
                return identityUserId;
            }

            public void setIdentityUserId(String identityUserId) {
                this.identityUserId = identityUserId;
            }

            public int getModuleId() {
                return moduleId;
            }

            public void setModuleId(int moduleId) {
                this.moduleId = moduleId;
            }

            public int getModuleType() {
                return moduleType;
            }

            public void setModuleType(int moduleType) {
                this.moduleType = moduleType;
            }

            public String getRemakeData() {
                return remakeData;
            }

            public void setRemakeData(String remakeData) {
                this.remakeData = remakeData;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }
        }
    }
}
