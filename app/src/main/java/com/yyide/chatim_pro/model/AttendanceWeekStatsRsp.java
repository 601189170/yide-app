package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: 考勤统计数据
 * @Author: liu tao
 * @CreateDate: 2021/6/17 11:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/17 11:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class AttendanceWeekStatsRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    public AttendanceWeekStatsRsp() {
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
        private List<AttendancesFormBean> attendancesForm;
        private List<?> schoolPeopleAllForm;

        public DataBean() {
        }

        public List<AttendancesFormBean> getAttendancesForm() {
            return attendancesForm;
        }

        public void setAttendancesForm(List<AttendancesFormBean> attendancesForm) {
            this.attendancesForm = attendancesForm;
        }

        public List<?> getSchoolPeopleAllForm() {
            return schoolPeopleAllForm;
        }

        public void setSchoolPeopleAllForm(List<?> schoolPeopleAllForm) {
            this.schoolPeopleAllForm = schoolPeopleAllForm;
        }

        public static class AttendancesFormBean {
            private int number;
            private StudentsBean students;
            private int numberA;
            private int applyNumA;
            private int lateA;
            private int leaveEarlyA;
            private int absenceA;
            private int leaveA;
            private List<?> peopleA;
            private List<?> latePeopleA;
            private List<?> leavePeopleA;
            private List<?> absencePeopleA;
            private List<?> leaveEarlyPeopleA;
            private String identityType;

            public AttendancesFormBean() {
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public StudentsBean getStudents() {
                return students;
            }

            public void setStudents(StudentsBean students) {
                this.students = students;
            }

            public int getNumberA() {
                return numberA;
            }

            public void setNumberA(int numberA) {
                this.numberA = numberA;
            }

            public int getApplyNumA() {
                return applyNumA;
            }

            public void setApplyNumA(int applyNumA) {
                this.applyNumA = applyNumA;
            }

            public int getLateA() {
                return lateA;
            }

            public void setLateA(int lateA) {
                this.lateA = lateA;
            }

            public int getLeaveEarlyA() {
                return leaveEarlyA;
            }

            public void setLeaveEarlyA(int leaveEarlyA) {
                this.leaveEarlyA = leaveEarlyA;
            }

            public int getAbsenceA() {
                return absenceA;
            }

            public void setAbsenceA(int absenceA) {
                this.absenceA = absenceA;
            }

            public int getLeaveA() {
                return leaveA;
            }

            public void setLeaveA(int leaveA) {
                this.leaveA = leaveA;
            }

            public List<?> getPeopleA() {
                return peopleA;
            }

            public void setPeopleA(List<?> peopleA) {
                this.peopleA = peopleA;
            }

            public List<?> getLatePeopleA() {
                return latePeopleA;
            }

            public void setLatePeopleA(List<?> latePeopleA) {
                this.latePeopleA = latePeopleA;
            }

            public List<?> getLeavePeopleA() {
                return leavePeopleA;
            }

            public void setLeavePeopleA(List<?> leavePeopleA) {
                this.leavePeopleA = leavePeopleA;
            }

            public List<?> getAbsencePeopleA() {
                return absencePeopleA;
            }

            public void setAbsencePeopleA(List<?> absencePeopleA) {
                this.absencePeopleA = absencePeopleA;
            }

            public List<?> getLeaveEarlyPeopleA() {
                return leaveEarlyPeopleA;
            }

            public void setLeaveEarlyPeopleA(List<?> leaveEarlyPeopleA) {
                this.leaveEarlyPeopleA = leaveEarlyPeopleA;
            }

            public String getIdentityType() {
                return identityType;
            }

            public void setIdentityType(String identityType) {
                this.identityType = identityType;
            }

            public static class StudentsBean {
                private int number;
                private String rate;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private int schNum;//课程数
                private String name;
                private int type;
                private int section;
                private String subjectName;
                private String applyDate;
                private String attendanceType;
                private String peopleName;
                private String startTime;
                private String requiredTime;
                private String endTime;
                private List<?> studentIds;
                private List<PeopleBean> people;
                private List<PeopleBean> latePeople;
                private List<PeopleBean> leavePeople;
                private List<PeopleBean> absencePeople;
                private List<PeopleBean> leaveEarlyPeople;
                private List<PeopleBean> applyPeople;
                private String peopleType;
                private String deviceName;
                private String path;
                private String classesName;
                private String attId;
                private String time;
                private String thingName;
                private int dateType;
                private String identityType;
                private String beginDate;
                private int goOutStatus;

                public StudentsBean() {
                }

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public int getApplyNum() {
                    return applyNum;
                }

                public void setApplyNum(int applyNum) {
                    this.applyNum = applyNum;
                }

                public int getLate() {
                    return late;
                }

                public void setLate(int late) {
                    this.late = late;
                }

                public int getLeaveEarly() {
                    return leaveEarly;
                }

                public void setLeaveEarly(int leaveEarly) {
                    this.leaveEarly = leaveEarly;
                }

                public int getAbsence() {
                    return absence;
                }

                public void setAbsence(int absence) {
                    this.absence = absence;
                }

                public int getLeave() {
                    return leave;
                }

                public void setLeave(int leave) {
                    this.leave = leave;
                }

                public int getSchNum() {
                    return schNum;
                }

                public void setSchNum(int schNum) {
                    this.schNum = schNum;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getSection() {
                    return section;
                }

                public void setSection(int section) {
                    this.section = section;
                }

                public String getSubjectName() {
                    return subjectName;
                }

                public void setSubjectName(String subjectName) {
                    this.subjectName = subjectName;
                }

                public String getApplyDate() {
                    return applyDate;
                }

                public void setApplyDate(String applyDate) {
                    this.applyDate = applyDate;
                }

                public String getAttendanceType() {
                    return attendanceType;
                }

                public void setAttendanceType(String attendanceType) {
                    this.attendanceType = attendanceType;
                }

                public String getPeopleName() {
                    return peopleName;
                }

                public void setPeopleName(String peopleName) {
                    this.peopleName = peopleName;
                }

                public String getStartTime() {
                    return startTime;
                }

                public void setStartTime(String startTime) {
                    this.startTime = startTime;
                }

                public String getRequiredTime() {
                    return requiredTime;
                }

                public void setRequiredTime(String requiredTime) {
                    this.requiredTime = requiredTime;
                }

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public List<?> getStudentIds() {
                    return studentIds;
                }

                public void setStudentIds(List<?> studentIds) {
                    this.studentIds = studentIds;
                }

                public List<PeopleBean> getPeople() {
                    return people;
                }

                public void setPeople(List<PeopleBean> people) {
                    this.people = people;
                }

                public List<PeopleBean> getLatePeople() {
                    return latePeople;
                }

                public void setLatePeople(List<PeopleBean> latePeople) {
                    this.latePeople = latePeople;
                }

                public List<PeopleBean> getLeavePeople() {
                    return leavePeople;
                }

                public void setLeavePeople(List<PeopleBean> leavePeople) {
                    this.leavePeople = leavePeople;
                }

                public List<PeopleBean> getAbsencePeople() {
                    return absencePeople;
                }

                public void setAbsencePeople(List<PeopleBean> absencePeople) {
                    this.absencePeople = absencePeople;
                }

                public List<PeopleBean> getLeaveEarlyPeople() {
                    return leaveEarlyPeople;
                }

                public void setLeaveEarlyPeople(List<PeopleBean> leaveEarlyPeople) {
                    this.leaveEarlyPeople = leaveEarlyPeople;
                }

                public List<PeopleBean> getApplyPeople() {
                    return applyPeople;
                }

                public void setApplyPeople(List<PeopleBean> applyPeople) {
                    this.applyPeople = applyPeople;
                }

                public String getPeopleType() {
                    return peopleType;
                }

                public void setPeopleType(String peopleType) {
                    this.peopleType = peopleType;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public String getClassesName() {
                    return classesName;
                }

                public void setClassesName(String classesName) {
                    this.classesName = classesName;
                }

                public String getAttId() {
                    return attId;
                }

                public void setAttId(String attId) {
                    this.attId = attId;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getThingName() {
                    return thingName;
                }

                public void setThingName(String thingName) {
                    this.thingName = thingName;
                }

                public int getDateType() {
                    return dateType;
                }

                public void setDateType(int dateType) {
                    this.dateType = dateType;
                }

                public String getIdentityType() {
                    return identityType;
                }

                public void setIdentityType(String identityType) {
                    this.identityType = identityType;
                }

                public String getBeginDate() {
                    return beginDate;
                }

                public void setBeginDate(String beginDate) {
                    this.beginDate = beginDate;
                }

                public int getGoOutStatus() {
                    return goOutStatus;
                }

                public void setGoOutStatus(int goOutStatus) {
                    this.goOutStatus = goOutStatus;
                }

                public static class PeopleBean {
                    private String name;
                    private String status;
                    private int section;

                    private String time;
                    private String deviceName;
                    private String subjectName;

                    private String startDate;
                    private String endDate;
                    private String thingName;
                    private String path;
                    private String identityType;
                    //分组
                    private List<PeopleBean> specialPeople;
                    private int specialCount;
                    private boolean checked;

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

                    public int getSection() {
                        return section;
                    }

                    public void setSection(int section) {
                        this.section = section;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getDeviceName() {
                        return deviceName;
                    }

                    public void setDeviceName(String deviceName) {
                        this.deviceName = deviceName;
                    }

                    public String getSubjectName() {
                        return subjectName;
                    }

                    public void setSubjectName(String subjectName) {
                        this.subjectName = subjectName;
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

                    public String getThingName() {
                        return thingName;
                    }

                    public void setThingName(String thingName) {
                        this.thingName = thingName;
                    }

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public String getIdentityType() {
                        return identityType;
                    }

                    public void setIdentityType(String identityType) {
                        this.identityType = identityType;
                    }

                    public List<PeopleBean> getSpecialPeople() {
                        return specialPeople;
                    }

                    public void setSpecialPeople(List<PeopleBean> specialPeople) {
                        this.specialPeople = specialPeople;
                    }

                    public int getSpecialCount() {
                        return specialCount;
                    }

                    public void setSpecialCount(int specialCount) {
                        this.specialCount = specialCount;
                    }

                    public boolean isChecked() {
                        return checked;
                    }

                    public void setChecked(boolean checked) {
                        this.checked = checked;
                    }

                    public PeopleBean() {
                    }
                }
            }
        }
    }
}
