package com.yyide.chatim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 14:00
 * @Description : 文件描述
 */

public class AttendanceTeacherRsp {
    private int code;
    private ArrayList<DataDTO> data;
    private String message;

    public AttendanceTeacherRsp() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArrayList<DataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<DataDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class AttendanceTeacherAdapterBean {
        public String normalRateOut;
        public String normalRateTime;
        public String normalRateCourse;
    }


    public static class DataDTO {
        public Integer getAbsenteeismNum() {
            return absenteeismNum;
        }

        public void setAbsenteeismNum(Integer absenteeismNum) {
            this.absenteeismNum = absenteeismNum;
        }

        public Integer getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(Integer attendanceType) {
            this.attendanceType = attendanceType;
        }

        public Integer getBeLateNum() {
            return beLateNum;
        }

        public void setBeLateNum(Integer beLateNum) {
            this.beLateNum = beLateNum;
        }

        public Integer getErrorNum() {
            return errorNum;
        }

        public void setErrorNum(Integer errorNum) {
            this.errorNum = errorNum;
        }

        public Integer getLeaveEarlyNum() {
            return leaveEarlyNum;
        }

        public void setLeaveEarlyNum(Integer leaveEarlyNum) {
            this.leaveEarlyNum = leaveEarlyNum;
        }

        public Integer getLeaveNum() {
            return leaveNum;
        }

        public void setLeaveNum(Integer leaveNum) {
            this.leaveNum = leaveNum;
        }

        public Integer getNormalNum() {
            return normalNum;
        }

        public void setNormalNum(Integer normalNum) {
            this.normalNum = normalNum;
        }

        public String getNormalRate() {
            return normalRate;
        }

        public void setNormalRate(String normalRate) {
            this.normalRate = normalRate;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        private int absenteeismNum;
        private int attendanceType;
        private int beLateNum;
        private int errorNum;
        private int leaveEarlyNum;
        private int leaveNum;
        private int normalNum;
        private String normalRate;
        private int total;
    }
}
