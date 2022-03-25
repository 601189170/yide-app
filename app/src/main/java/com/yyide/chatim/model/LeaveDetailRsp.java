package com.yyide.chatim.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/19 18:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/19 18:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeaveDetailRsp {

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataDTO data;
    @SerializedName("message")
    private String message;

    public static class DataDTO {
        @SerializedName("apprJson")
        private ApprJsonDTO apprJson;
        @SerializedName("hiApprNodeList")
        private List<HiApprNodeListDTO> hiApprNodeList;
        @SerializedName("ccList")
        private List<Cc> ccList;
        @SerializedName("id")
        private String id;
        @SerializedName("procInstId")
        private String procInstId;
        @SerializedName("name")
        private String name;
        @SerializedName("status")
        private String status;
        @SerializedName("title")
        private String title;
        @SerializedName("taskId")
        private String taskId;
        @SerializedName("identity")
        private String identity;

        public static class ApprJsonDTO {
            @SerializedName("dept")
            private String dept;
            @SerializedName("endTime")
            private String endTime;
            @SerializedName("hours")
            private String hours;
            @SerializedName("reason")
            private String reason;
            @SerializedName("startTime")
            private String startTime;

            public String getDept() {
                return dept;
            }

            public void setDept(String dept) {
                this.dept = dept;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }
        }

        public static class Cc {
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("name")
            private String name;
            @SerializedName("userId")
            private String userId;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public List<Cc> getCcList() {
            return ccList;
        }

        public void setCcList(List<Cc> ccList) {
            this.ccList = ccList;
        }

        public static class HiApprNodeListDTO {
            @SerializedName("apprTime")
            private String apprTime;
            @SerializedName("id")
            private String id;
            @SerializedName("nodeName")
            private String nodeName;
            @SerializedName("status")
            private String status;
            @SerializedName("userId")
            private String userId;
            @SerializedName("usreName")
            private String usreName;
            @SerializedName("avatar")
            private String avatar;
            private boolean isCc;
            private List<Cc> ccList;

            public List<Cc> getCcList() {
                return ccList;
            }

            public void setCcList(List<Cc> ccList) {
                this.ccList = ccList;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public boolean isCc() {
                return isCc;
            }

            public void setCc(boolean cc) {
                isCc = cc;
            }

            public String getApprTime() {
                return apprTime;
            }

            public void setApprTime(String apprTime) {
                this.apprTime = apprTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNodeName() {
                return nodeName;
            }

            public void setNodeName(String nodeName) {
                this.nodeName = nodeName;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUsreName() {
                return usreName;
            }

            public void setUsreName(String usreName) {
                this.usreName = usreName;
            }
        }

        public String getProcInstId() {
            return procInstId;
        }

        public void setProcInstId(String procInstId) {
            this.procInstId = procInstId;
        }

        public ApprJsonDTO getApprJson() {
            return apprJson;
        }

        public void setApprJson(ApprJsonDTO apprJson) {
            this.apprJson = apprJson;
        }

        public List<HiApprNodeListDTO> getHiApprNodeList() {
            return hiApprNodeList;
        }

        public void setHiApprNodeList(List<HiApprNodeListDTO> hiApprNodeList) {
            this.hiApprNodeList = hiApprNodeList;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
