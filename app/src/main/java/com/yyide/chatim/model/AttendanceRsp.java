package com.yyide.chatim.model;

import com.google.gson.annotations.SerializedName;
import com.yyide.chatim.widget.treeview.model.NodeId;

import java.io.Serializable;
import java.util.List;

import javax.sql.DataSource;

public class AttendanceRsp implements Serializable {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;
    private String userName;
    private String userId;
    private int sectionNumber;
    private List<CourseInfoFormListBean> courseInfoFormList;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public List<CourseInfoFormListBean> getCourseInfoFormList() {
        return courseInfoFormList;
    }

    public void setCourseInfoFormList(List<CourseInfoFormListBean> courseInfoFormList) {
        this.courseInfoFormList = courseInfoFormList;
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

    public static class TeacherCourseFormBean implements Serializable {
        @SerializedName(value = "headmasterAttendanceList1", alternate = {"headmasterAttendanceList", "classroomTeacherAttendanceList"})
        private List<DataBean.AttendanceListBean> headmasterAttendanceList;
        @SerializedName(value = "baseInfo1", alternate = {"classCourseBasicForm", "baseInfo", "eventBasicForm"})
        private DataBean.AttendanceListBean baseInfo;
        private List<TeacherItemBean> absenteeismList;
        private List<TeacherItemBean> leaveList;
        private List<TeacherItemBean> lateList;
        private List<TeacherItemBean> allRollList;
        private List<TeacherItemBean> normalList;
        private List<TeacherItemBean> earlyList;
        private List<DataBean.AttendanceListBean> classInfoForms;

        public List<TeacherItemBean> getAllRollList() {
            return allRollList;
        }

        public void setAllRollList(List<TeacherItemBean> allRollList) {
            this.allRollList = allRollList;
        }

        public List<TeacherItemBean> getNormalList() {
            return normalList;
        }

        public void setNormalList(List<TeacherItemBean> normalList) {
            this.normalList = normalList;
        }

        public List<TeacherItemBean> getEarlyList() {
            return earlyList;
        }

        public void setEarlyList(List<TeacherItemBean> earlyList) {
            this.earlyList = earlyList;
        }

        public List<DataBean.AttendanceListBean> getClassInfoForms() {
            return classInfoForms;
        }

        public void setClassInfoForms(List<DataBean.AttendanceListBean> classInfoForms) {
            this.classInfoForms = classInfoForms;
        }

        public List<DataBean.AttendanceListBean> getHeadmasterAttendanceList() {
            return headmasterAttendanceList;
        }

        public void setHeadmasterAttendanceList(List<DataBean.AttendanceListBean> headmasterAttendanceList) {
            this.headmasterAttendanceList = headmasterAttendanceList;
        }

        public DataBean.AttendanceListBean getBaseInfo() {
            return baseInfo;
        }

        public void setBaseInfo(DataBean.AttendanceListBean baseInfo) {
            this.baseInfo = baseInfo;
        }

        public List<TeacherItemBean> getAbsenteeismList() {
            return absenteeismList;
        }

        public void setAbsenteeismList(List<TeacherItemBean> absenteeismList) {
            this.absenteeismList = absenteeismList;
        }

        public List<TeacherItemBean> getLeaveList() {
            return leaveList;
        }

        public void setLeaveList(List<TeacherItemBean> leaveList) {
            this.leaveList = leaveList;
        }

        public List<TeacherItemBean> getLateList() {
            return lateList;
        }

        public void setLateList(List<TeacherItemBean> lateList) {
            this.lateList = lateList;
        }
    }

    public static class TeacherItemBean implements Serializable {
        private String userName;
        private String userId;
        private int sectionNumber;
        private String signInTime;
        private String clockName;
        private String startTime;
        private String leaveStartTime;
        private String leaveEndTime;
        private String endTime;
        private String attendanceType;
        private String attendanceSignInOut;
        private List<CourseInfoFormListBean> courseInfoFormList;

        public String getStartTime() {
            return startTime;
        }

        public String getLeaveStartTime() {
            return leaveStartTime;
        }

        public void setLeaveStartTime(String leaveStartTime) {
            this.leaveStartTime = leaveStartTime;
        }

        public String getLeaveEndTime() {
            return leaveEndTime;
        }

        public void setLeaveEndTime(String leaveEndTime) {
            this.leaveEndTime = leaveEndTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getAttendanceSignInOut() {
            return attendanceSignInOut;
        }

        public void setAttendanceSignInOut(String attendanceSignInOut) {
            this.attendanceSignInOut = attendanceSignInOut;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSignInTime() {
            return signInTime;
        }

        public void setSignInTime(String signInTime) {
            this.signInTime = signInTime;
        }

        public String getClockName() {
            return clockName;
        }

        public void setClockName(String clockName) {
            this.clockName = clockName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getSectionNumber() {
            return sectionNumber;
        }

        public void setSectionNumber(int sectionNumber) {
            this.sectionNumber = sectionNumber;
        }

        public List<CourseInfoFormListBean> getCourseInfoFormList() {
            return courseInfoFormList;
        }

        public void setCourseInfoFormList(List<CourseInfoFormListBean> courseInfoFormList) {
            this.courseInfoFormList = courseInfoFormList;
        }

        public String getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(String attendanceType) {
            this.attendanceType = attendanceType;
        }

        public static class CourseInfoFormListBean implements NodeId {
            private String section;
            private String timeFrame;
            private String attendanceType;
            private String courseInfo;
            private String currentTime;
            private String courseName;
            private String courseEndTime;
            private String courseStartTime;
            private String clockName;
            private String attendanceSignInOut;
            /**
             * 签到时间
             */
            private String signInTime;
            /**
             * 请假开始时间
             */
            private String leaveStartTime;
            /**
             * 请假结束时间
             */
            private String leaveEndTime;

            public String getAttendanceSignInOut() {
                return attendanceSignInOut;
            }

            public void setAttendanceSignInOut(String attendanceSignInOut) {
                this.attendanceSignInOut = attendanceSignInOut;
            }

            public String getClockName() {
                return clockName;
            }

            public void setClockName(String clockName) {
                this.clockName = clockName;
            }

            public String getLeaveStartTime() {
                return leaveStartTime;
            }

            public String getSignInTime() {
                return signInTime;
            }

            public void setSignInTime(String signInTime) {
                this.signInTime = signInTime;
            }

            public void setLeaveStartTime(String leaveStartTime) {
                this.leaveStartTime = leaveStartTime;
            }

            public String getLeaveEndTime() {
                return leaveEndTime;
            }

            public void setLeaveEndTime(String leaveEndTime) {
                this.leaveEndTime = leaveEndTime;
            }

            public String getSection() {
                return section;
            }

            public void setSection(String section) {
                this.section = section;
            }

            public String getTimeFrame() {
                return timeFrame;
            }

            public void setTimeFrame(String timeFrame) {
                this.timeFrame = timeFrame;
            }

            public String getAttendanceType() {
                return attendanceType;
            }

            public void setAttendanceType(String attendanceType) {
                this.attendanceType = attendanceType;
            }

            public String getCourseInfo() {
                return courseInfo;
            }

            public void setCourseInfo(String courseInfo) {
                this.courseInfo = courseInfo;
            }

            public String getCurrentTime() {
                return currentTime;
            }

            public void setCurrentTime(String currentTime) {
                this.currentTime = currentTime;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getCourseEndTime() {
                return courseEndTime;
            }

            public void setCourseEndTime(String courseEndTime) {
                this.courseEndTime = courseEndTime;
            }

            public String getCourseStartTime() {
                return courseStartTime;
            }

            public void setCourseStartTime(String courseStartTime) {
                this.courseStartTime = courseStartTime;
            }

            @Override
            public String getId() {
                return "";
            }

            @Override
            public String getPId() {
                return "";
            }
        }
    }

    public static class DataBean implements Serializable {
        private List<AttendanceListBean> classroomTeacherAttendanceList;
        private List<AttendanceListBean> studentAttendanceList;
        private List<AttendanceListBean> headmasterAttendanceList;
        private AttendanceListBean attendanceAppNumberForm;
        private List<AttendanceListBean> gradeInfoList;
        @SerializedName(value = "courseAttendanceList1", alternate = {"eventAttendanceList", "courseAttendanceList"})
        private List<AttendanceListBean> courseAttendanceList;
        @SerializedName(value = "studentCourseFormBean1", alternate = {"studentEventForm", "studentCourseForm"})
        private StudentCourseFormBean studentCourseFormBean;
        @SerializedName(value = "teacherCourseForm1", alternate = {"teacherEventForm", "teacherCourseForm", "attendanceCourseForm", "attendanceEventForm"})
        private TeacherCourseFormBean teacherCourseForm;

        public TeacherCourseFormBean getTeacherCourseForm() {
            return teacherCourseForm;
        }

        public AttendanceListBean getAttendanceAppNumberForm() {
            return attendanceAppNumberForm;
        }

        public void setAttendanceAppNumberForm(AttendanceListBean attendanceAppNumberForm) {
            this.attendanceAppNumberForm = attendanceAppNumberForm;
        }

        public List<AttendanceListBean> getGradeInfoList() {
            return gradeInfoList;
        }

        public void setGradeInfoList(List<AttendanceListBean> gradeInfoList) {
            this.gradeInfoList = gradeInfoList;
        }

        public List<AttendanceListBean> getCourseAttendanceList() {
            return courseAttendanceList;
        }

        public void setCourseAttendanceList(List<AttendanceListBean> courseAttendanceList) {
            this.courseAttendanceList = courseAttendanceList;
        }

        public void setTeacherCourseForm(TeacherCourseFormBean teacherCourseForm) {
            this.teacherCourseForm = teacherCourseForm;
        }

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
            private String attendanceSignInOut;//0 签到 1签退
            private String signInOutRate;
            private String serverId;
            private String peopleType;
            private String type;//	事件类型 1 事件考勤 2 课程考勤
            private String attendanceType;
            private int normal;
            private int late;
            private int early;
            private int leave;
            private int absenteeism;
            private int totalNumber;
            private String attendanceTimeId;
            private String signInTime;
            private String gradeName;
            private long gradeId;
            private String className;
            private String classId;
            private String courseTime;
            private String eventTime;

            public String getEventTime() {
                return eventTime;
            }

            public void setEventTime(String eventTime) {
                this.eventTime = eventTime;
            }

            public String getAttendanceType() {
                return attendanceType;
            }

            public void setAttendanceType(String attendanceType) {
                this.attendanceType = attendanceType;
            }

            public String getClassId() {
                return classId;
            }

            public void setClassId(String classId) {
                this.classId = classId;
            }

            public String getCourseTime() {
                return courseTime;
            }

            public void setCourseTime(String courseTime) {
                this.courseTime = courseTime;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getGradeName() {
                return gradeName;
            }

            public long getGradeId() {
                return gradeId;
            }

            public void setGradeId(long gradeId) {
                this.gradeId = gradeId;
            }

            public void setGradeName(String gradeName) {
                this.gradeName = gradeName;
            }

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

    public static class CourseInfoFormListBean {
        private String section;
        private String timeFrame;
        private String attendanceType;
        private String courseInfo;
        private String currentTime;
        private String courseName;
        private String courseEndTime;
        private String courseStartTime;

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getTimeFrame() {
            return timeFrame;
        }

        public void setTimeFrame(String timeFrame) {
            this.timeFrame = timeFrame;
        }

        public String getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(String attendanceType) {
            this.attendanceType = attendanceType;
        }

        public String getCourseInfo() {
            return courseInfo;
        }

        public void setCourseInfo(String courseInfo) {
            this.courseInfo = courseInfo;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseEndTime() {
            return courseEndTime;
        }

        public void setCourseEndTime(String courseEndTime) {
            this.courseEndTime = courseEndTime;
        }

        public String getCourseStartTime() {
            return courseStartTime;
        }

        public void setCourseStartTime(String courseStartTime) {
            this.courseStartTime = courseStartTime;
        }
    }
}
