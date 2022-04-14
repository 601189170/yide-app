package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/20 10:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/20 10:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeaveDeptRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    public LeaveDeptRsp() {
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
        private String deptName;
        private String deptId;
        private String deptHeadId;
        private String schoolId;
        private String type;
        private int isDefault;
        private String classId;
        private String studentUserId;
        private String startDate;
        private String endDate;
        private boolean curWeek;
        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getDeptHeadId() {
            return deptHeadId;
        }

        public void setDeptHeadId(String deptHeadId) {
            this.deptHeadId = deptHeadId;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getStudentUserId() {
            return studentUserId;
        }

        public void setStudentUserId(String studentUserId) {
            this.studentUserId = studentUserId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public boolean isCurWeek() {
            return curWeek;
        }

        public void setCurWeek(boolean curWeek) {
            this.curWeek = curWeek;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "deptName='" + deptName + '\'' +
                    ", deptId='" + deptId + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    '}';
        }

        public DataBean() {
        }
    }
}
