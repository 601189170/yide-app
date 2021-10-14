package com.yyide.chatim.model;

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

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public LeaveDetailRsp() {
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
        private String name;
        private String initName;
        private String initImage;
        private String deptOrClassName;
        private String studentName;
        private String type;
        private String startTime;
        private String endTime;
        private String reason;
        private String initiateTime;
        private String approverId;
        private String approverName;
        private String approvalTime;
        private String approvalResult;
        private String approverImage;
        private long id;
        private List<ListBean> list;
        private Object leaveCourseFormList;
        private Object ccbid;
        /**
         * 请假类型: 1 家长请假， 2 教职工请假
         */
        private String leaveType;

        /**
         * 撤销时间
         */
        private String undoTime;

        public DataBean() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInitName() {
            return initName;
        }

        public void setInitName(String initName) {
            this.initName = initName;
        }

        public String getInitImage() {
            return initImage;
        }

        public void setInitImage(String initImage) {
            this.initImage = initImage;
        }

        public String getDeptOrClassName() {
            return deptOrClassName;
        }

        public void setDeptOrClassName(String deptOrClassName) {
            this.deptOrClassName = deptOrClassName;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getInitiateTime() {
            return initiateTime;
        }

        public void setInitiateTime(String initiateTime) {
            this.initiateTime = initiateTime;
        }

        public String getApproverId() {
            return approverId;
        }

        public void setApproverId(String approverId) {
            this.approverId = approverId;
        }

        public String getApproverName() {
            return approverName;
        }

        public void setApproverName(String approverName) {
            this.approverName = approverName;
        }

        public String getApprovalTime() {
            return approvalTime;
        }

        public void setApprovalTime(String approvalTime) {
            this.approvalTime = approvalTime;
        }

        public String getApprovalResult() {
            return approvalResult;
        }

        public void setApprovalResult(String approvalResult) {
            this.approvalResult = approvalResult;
        }

        public String getApproverImage() {
            return approverImage;
        }

        public void setApproverImage(String approverImage) {
            this.approverImage = approverImage;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public Object getLeaveCourseFormList() {
            return leaveCourseFormList;
        }

        public void setLeaveCourseFormList(Object leaveCourseFormList) {
            this.leaveCourseFormList = leaveCourseFormList;
        }

        public Object getCcbid() {
            return ccbid;
        }

        public void setCcbid(Object ccbid) {
            this.ccbid = ccbid;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public String getUndoTime() {
            return undoTime;
        }

        public void setUndoTime(String undoTime) {
            this.undoTime = undoTime;
        }

        public static class ListBean {
            private long userId;
            private String name;
            private String image;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public ListBean() {
            }
        }
    }
}
