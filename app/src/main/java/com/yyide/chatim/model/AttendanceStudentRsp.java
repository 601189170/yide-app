package com.yyide.chatim.model;

import java.util.List;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 13:54
 * @Description : 文件描述
 */

public class AttendanceStudentRsp {

    public AttendanceStudentRsp() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int code;
    public List<DataDTO> data;
    public String message;

    public static class AttendanceAdapterBean {
        public String id;
        public String name;
        public String attendanceType; // 0:出入校、1:事件考勤，2课堂考勤）
        public int errorNumOut;
        public int normalNumOut;
        public int errorNumEvent;
        public int normalNumEvent;
        public int errorNumCourse;
        public int normalNumCourse;

    }


    public static class DataDTO {
        private List<CountListDTO> countList;
        private String id;
        private String name;

        public DataDTO() {
        }

        public List<CountListDTO> getCountList() {
            return countList;
        }

        public void setCountList(List<CountListDTO> countList) {
            this.countList = countList;
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

        public static class CountListDTO {
            public CountListDTO() {
            }

            public int getAbsenteeismNum() {
                return absenteeismNum;
            }

            public void setAbsenteeismNum(int absenteeismNum) {
                this.absenteeismNum = absenteeismNum;
            }

            public int getAttendanceType() {
                return attendanceType;
            }

            public void setAttendanceType(int attendanceType) {
                this.attendanceType = attendanceType;
            }

            public int getBeLateNum() {
                return beLateNum;
            }

            public void setBeLateNum(int beLateNum) {
                this.beLateNum = beLateNum;
            }

            public int getErrorNum() {
                return errorNum;
            }

            public void setErrorNum(int errorNum) {
                this.errorNum = errorNum;
            }

            public int getLeaveEarlyNum() {
                return leaveEarlyNum;
            }

            public void setLeaveEarlyNum(int leaveEarlyNum) {
                this.leaveEarlyNum = leaveEarlyNum;
            }

            public int getLeaveNum() {
                return leaveNum;
            }

            public void setLeaveNum(int leaveNum) {
                this.leaveNum = leaveNum;
            }

            public int getNormalNum() {
                return normalNum;
            }

            public void setNormalNum(int normalNum) {
                this.normalNum = normalNum;
            }

            public String getStudentId() {
                return studentId;
            }

            public void setStudentId(String studentId) {
                this.studentId = studentId;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int absenteeismNum;
            public int attendanceType;
            public int beLateNum;
            public int errorNum;
            public int leaveEarlyNum;
            public int leaveNum;
            public int normalNum;
            public String studentId;
            public int total;
        }
    }
}
