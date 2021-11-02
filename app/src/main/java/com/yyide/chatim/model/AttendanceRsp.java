package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

public class AttendanceRsp implements Serializable {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

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

    public static class StudentCourseFormBean implements Serializable {
        private List<DataBean.AttendanceListBean> attendanceAppGradeInfoFormList;
        private DataBean.AttendanceListBean baseInfo;

        public List<DataBean.AttendanceListBean> getAttendanceAppGradeInfoFormList() {
            return attendanceAppGradeInfoFormList;
        }

        public void setAttendanceAppGradeInfoFormList(List<DataBean.AttendanceListBean> attendanceAppGradeInfoFormList) {
            this.attendanceAppGradeInfoFormList = attendanceAppGradeInfoFormList;
        }

        public DataBean.AttendanceListBean getBaseInfo() {
            return baseInfo;
        }

        public void setBaseInfo(DataBean.AttendanceListBean baseInfo) {
            this.baseInfo = baseInfo;
        }
    }

    public static class DataBean implements Serializable {
        private List<AttendanceListBean> classroomTeacherAttendanceList;
        private List<AttendanceListBean> studentAttendanceList;
        private List<AttendanceListBean> headmasterAttendanceList;
        private StudentCourseFormBean studentCourseFormBean;

        public List<AttendanceListBean> getHeadmasterAttendanceList() {
            return headmasterAttendanceList;
        }

        public void setHeadmasterAttendanceList(List<AttendanceListBean> headmasterAttendanceList) {
            this.headmasterAttendanceList = headmasterAttendanceList;
        }

        public List<AttendanceListBean> getClassroomTeacherAttendanceList() {
            return classroomTeacherAttendanceList;
        }

        public void setClassroomTeacherAttendanceList(List<AttendanceListBean> classroomTeacherAttendanceList) {
            this.classroomTeacherAttendanceList = classroomTeacherAttendanceList;
        }

        public List<AttendanceListBean> getStudentAttendanceList() {
            return studentAttendanceList;
        }

        public void setStudentAttendanceList(List<AttendanceListBean> studentAttendanceList) {
            this.studentAttendanceList = studentAttendanceList;
        }

        public StudentCourseFormBean getStudentCourseFormBean() {
            return studentCourseFormBean;
        }

        public void setStudentCourseFormBean(StudentCourseFormBean studentCourseFormBean) {
            this.studentCourseFormBean = studentCourseFormBean;
        }

        public static class AttendanceListBean implements Serializable {
            private String theme;
            private String eventName;
            private String attendanceSignInOut;
            private String signInOutRate;
            private String serverId;
            private String peopleType;
            private String type;
            private int normal;
            private int late;
            private int early;
            private int leave;
            private int absenteeism;
            private int totalNumber;
            private String attendanceTimeId;
            private String signInTime;

            public String getSignInTime() {
                return signInTime;
            }

            public void setSignInTime(String signInTime) {
                this.signInTime = signInTime;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public String getEventName() {
                return eventName;
            }

            public void setEventName(String eventName) {
                this.eventName = eventName;
            }

            public String getAttendanceSignInOut() {
                return attendanceSignInOut;
            }

            public void setAttendanceSignInOut(String attendanceSignInOut) {
                this.attendanceSignInOut = attendanceSignInOut;
            }

            public String getSignInOutRate() {
                return signInOutRate;
            }

            public void setSignInOutRate(String signInOutRate) {
                this.signInOutRate = signInOutRate;
            }

            public String getServerId() {
                return serverId;
            }

            public void setServerId(String serverId) {
                this.serverId = serverId;
            }

            public String getPeopleType() {
                return peopleType;
            }

            public void setPeopleType(String peopleType) {
                this.peopleType = peopleType;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNormal() {
                return normal;
            }

            public void setNormal(int normal) {
                this.normal = normal;
            }

            public int getLate() {
                return late;
            }

            public void setLate(int late) {
                this.late = late;
            }

            public int getEarly() {
                return early;
            }

            public void setEarly(int early) {
                this.early = early;
            }

            public int getLeave() {
                return leave;
            }

            public void setLeave(int leave) {
                this.leave = leave;
            }

            public int getAbsenteeism() {
                return absenteeism;
            }

            public void setAbsenteeism(int absenteeism) {
                this.absenteeism = absenteeism;
            }

            public int getTotalNumber() {
                return totalNumber;
            }

            public void setTotalNumber(int totalNumber) {
                this.totalNumber = totalNumber;
            }

            public String getAttendanceTimeId() {
                return attendanceTimeId;
            }

            public void setAttendanceTimeId(String attendanceTimeId) {
                this.attendanceTimeId = attendanceTimeId;
            }
        }
    }
}
