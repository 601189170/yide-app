package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/6/4 16:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/4 16:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class AddressBookRsp {
    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

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

    public AddressBookRsp(int code, boolean success, String msg, List<DataBean> data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public AddressBookRsp() {
    }


    public static class DataBean {
        private long teacherId;
        private long userId;
        private long schoolId;
        private long deptId;
        private String teacherName;
        private String deptName;
        private int level;
        private String image;
        private boolean checked;

        public DataBean(long teacherId, long userId, long schoolId, long deptId, String teacherName, String deptName, int level, String image, boolean checked) {
            this.teacherId = teacherId;
            this.userId = userId;
            this.schoolId = schoolId;
            this.deptId = deptId;
            this.teacherName = teacherName;
            this.deptName = deptName;
            this.level = level;
            this.image = image;
            this.checked = checked;
        }

        public DataBean() {
        }

        public long getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(long teacherId) {
            this.teacherId = teacherId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public long getDeptId() {
            return deptId;
        }

        public void setDeptId(long deptId) {
            this.deptId = deptId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
