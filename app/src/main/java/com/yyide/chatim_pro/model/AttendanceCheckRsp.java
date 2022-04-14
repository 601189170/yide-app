package com.yyide.chatim_pro.model;

import android.text.TextUtils;

import com.yyide.chatim_pro.widget.treeview.model.NodeId;

import java.io.Serializable;
import java.util.List;


public class AttendanceCheckRsp implements Serializable {

    public int code;
    public boolean success;
    public String msg;
    public DataBean data;

    public AttendanceCheckRsp() {
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
        public List<AttendancesFormBean> attendancesForm;
        public List<SchoolPeopleAllFormBean> schoolPeopleAllForm;

        public DataBean() {
        }

        public List<AttendancesFormBean> getAttendancesForm() {
            return attendancesForm;
        }

        public void setAttendancesForm(List<AttendancesFormBean> attendancesForm) {
            this.attendancesForm = attendancesForm;
        }

        public List<SchoolPeopleAllFormBean> getSchoolPeopleAllForm() {
            return schoolPeopleAllForm;
        }

        public void setSchoolPeopleAllForm(List<SchoolPeopleAllFormBean> schoolPeopleAllForm) {
            this.schoolPeopleAllForm = schoolPeopleAllForm;
        }

        public static class AttendancesFormBean implements Serializable {
            public int number;
            public TeachersBean teachers;
            public Students students;
            public int numberA;
            public String rateA;
            public int applyNumA;
            public int lateA;
            public int leaveEarlyA;
            public int absenceA;
            public int leaveA;
            public String thingName;
            public String peopleType;
            public String attNameA;
            public String identityType;//N 学生考勤 Y 事件考勤
            public List<Students.PeopleBean> peopleA;
            public List<Students.PeopleBean> latePeopleA;
            public List<Students.PeopleBean> leavePeopleA;
            public List<Students.PeopleBean> absencePeopleA;
            public List<Students.PeopleBean> leaveEarlyPeopleA;

            public AttendancesFormBean() {
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public TeachersBean getTeachers() {
                return teachers;
            }

            public void setTeachers(TeachersBean teachers) {
                this.teachers = teachers;
            }

            public Students getStudents() {
                return students;
            }

            public void setStudents(Students students) {
                this.students = students;
            }

            public int getNumberA() {
                return numberA;
            }

            public void setNumberA(int numberA) {
                this.numberA = numberA;
            }

            public String getRateA() {
                return rateA;
            }

            public void setRateA(String rateA) {
                this.rateA = rateA;
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

            public String getThingName() {
                return thingName;
            }

            public void setThingName(String thingName) {
                this.thingName = thingName;
            }

            public String getPeopleType() {
                return peopleType;
            }

            public void setPeopleType(String peopleType) {
                this.peopleType = peopleType;
            }

            public String getAttNameA() {
                return attNameA;
            }

            public void setAttNameA(String attNameA) {
                this.attNameA = attNameA;
            }

            public String getIdentityType() {
                return identityType;
            }

            public void setIdentityType(String identityType) {
                this.identityType = identityType;
            }

            public List<Students.PeopleBean> getPeopleA() {
                return peopleA;
            }

            public void setPeopleA(List<Students.PeopleBean> peopleA) {
                this.peopleA = peopleA;
            }

            public List<Students.PeopleBean> getLatePeopleA() {
                return latePeopleA;
            }

            public void setLatePeopleA(List<Students.PeopleBean> latePeopleA) {
                this.latePeopleA = latePeopleA;
            }

            public List<Students.PeopleBean> getLeavePeopleA() {
                return leavePeopleA;
            }

            public void setLeavePeopleA(List<Students.PeopleBean> leavePeopleA) {
                this.leavePeopleA = leavePeopleA;
            }

            public List<Students.PeopleBean> getAbsencePeopleA() {
                return absencePeopleA;
            }

            public void setAbsencePeopleA(List<Students.PeopleBean> absencePeopleA) {
                this.absencePeopleA = absencePeopleA;
            }

            public List<Students.PeopleBean> getLeaveEarlyPeopleA() {
                return leaveEarlyPeopleA;
            }

            public void setLeaveEarlyPeopleA(List<Students.PeopleBean> leaveEarlyPeopleA) {
                this.leaveEarlyPeopleA = leaveEarlyPeopleA;
            }

            public static class Students implements Serializable {
                public int number;
                public int applyNum;
                public int late;
                public int leaveEarly;
                public int absence;
                public int leave;
                public String attendanceType;//0已签到 1未签到
                public String attendanceData;//考情时间
                public String name;
                public int section;
                public String subjectName;
                public long id;
                public String peopleName;
                public String rate;
                public String applyDate;
                public String startTime;
                public String requiredTime;
                public String endTime;
                public String peopleType;//N 老师 Y 学生
                public String studentId;
                public String type;
                public String time;
                public String deviceName;
                public String thingName;
                public String goOutStatus;//0:签到，1:签退
                public String identityType;//N 学生考勤 Y 事件考勤
                public List<String> studentIds;
                public List<PeopleBean> people;
                public List<PeopleBean> latePeople;
                public List<PeopleBean> leavePeople;
                public List<PeopleBean> absencePeople;
                public List<PeopleBean> leaveEarlyPeople;
                public List<PeopleBean> applyPeople;

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
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

                public String getAttendanceType() {
                    return attendanceType;
                }

                public void setAttendanceType(String attendanceType) {
                    this.attendanceType = attendanceType;
                }

                public String getAttendanceData() {
                    return attendanceData;
                }

                public void setAttendanceData(String attendanceData) {
                    this.attendanceData = attendanceData;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getPeopleName() {
                    return peopleName;
                }

                public void setPeopleName(String peopleName) {
                    this.peopleName = peopleName;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getApplyDate() {
                    return applyDate;
                }

                public void setApplyDate(String applyDate) {
                    this.applyDate = applyDate;
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

                public String getPeopleType() {
                    return peopleType;
                }

                public void setPeopleType(String peopleType) {
                    this.peopleType = peopleType;
                }

                public String getStudentId() {
                    return studentId;
                }

                public void setStudentId(String studentId) {
                    this.studentId = studentId;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
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

                public String getThingName() {
                    return thingName;
                }

                public void setThingName(String thingName) {
                    this.thingName = thingName;
                }

                public String getGoOutStatus() {
                    return goOutStatus;
                }

                public void setGoOutStatus(String goOutStatus) {
                    this.goOutStatus = goOutStatus;
                }

                public String getIdentityType() {
                    return identityType;
                }

                public void setIdentityType(String identityType) {
                    this.identityType = identityType;
                }

                public List<String> getStudentIds() {
                    return studentIds;
                }

                public void setStudentIds(List<String> studentIds) {
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

                public Students() {
                }

                public String getStatusType() {//（0正常、1缺勤、2迟到/3早退,4无效打卡）
                    String str = "";
                    if (TextUtils.isEmpty(type)) {
                        return str;
                    }
                    switch (type) {
                        case "0":
                            str = "正常";
                            break;
                        case "1":
                            str = "缺勤";
                            break;
                        case "2":
                            str = "迟到";
                            break;
                        case "3":
                            str = "早退";
                            break;
                        case "4":
                            str = "请假";
                            break;
                    }
                    return str;
                }


                public static class PeopleBean implements Serializable, NodeId {
                    public String name;
                    public String status;
                    public String time;
                    public String startDate;
                    public String subjectName;
                    public int section;
                    public String endDate;
                    public String deviceName;
                    public String goOutStatus;//0:签到，1:签退
                    public List<PeopleBean> specialPeople;
                    public int specialCount;

                    public PeopleBean() {
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

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getStartDate() {
                        return startDate;
                    }

                    public void setStartDate(String startDate) {
                        this.startDate = startDate;
                    }

                    public String getSubjectName() {
                        return subjectName;
                    }

                    public void setSubjectName(String subjectName) {
                        this.subjectName = subjectName;
                    }

                    public int getSection() {
                        return section;
                    }

                    public void setSection(int section) {
                        this.section = section;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public String getDeviceName() {
                        return deviceName;
                    }

                    public void setDeviceName(String deviceName) {
                        this.deviceName = deviceName;
                    }

                    public String getGoOutStatus() {
                        return goOutStatus;
                    }

                    public void setGoOutStatus(String goOutStatus) {
                        this.goOutStatus = goOutStatus;
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

                    public String getStatusType() {//（0正常、1缺勤、2迟到/3早退,4无效打卡）
                        String str = "";
                        if (TextUtils.isEmpty(status)) {
                            return "";
                        }
                        switch (status) {
                            case "0":
                                str = "正常";
                                break;
                            case "1":
                                str = "缺勤";
                                break;
                            case "2":
                                str = "迟到";
                                break;
                            case "3":
                                str = "早退";
                                break;
                            case "4":
                                str = "请假";
                                break;
                        }
                        return str;
                    }

                    @Override
                    public String getId() {
                        return "1";
                    }

                    @Override
                    public String getPId() {
                        return "";
                    }
                }

            }


            public static class TeachersBean implements Serializable {
                public int number;
                public int applyNum;
                public int late;
                public int leaveEarly;
                public int absence;
                public int leave;
                public String goOutStatus;//0:签到，1:签退
                public String rate;
                public String applyDate;
                public String startTime;
                public String requiredTime;
                public String thingName;
                public String identityType;//N 学生考勤 Y 事件考勤
                public String endTime;
                public String name;
                public int section;
                public String peopleType;
                public List<String> studentIds;
                public List<Students.PeopleBean> people;
                public List<Students.PeopleBean> latePeople;
                public List<Students.PeopleBean> leavePeople;
                public List<Students.PeopleBean> absencePeople;
                public List<Students.PeopleBean> leaveEarlyPeople;
                public List<Students.PeopleBean> applyPeople;

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
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

                public String getGoOutStatus() {
                    return goOutStatus;
                }

                public void setGoOutStatus(String goOutStatus) {
                    this.goOutStatus = goOutStatus;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getApplyDate() {
                    return applyDate;
                }

                public void setApplyDate(String applyDate) {
                    this.applyDate = applyDate;
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

                public String getThingName() {
                    return thingName;
                }

                public void setThingName(String thingName) {
                    this.thingName = thingName;
                }

                public String getIdentityType() {
                    return identityType;
                }

                public void setIdentityType(String identityType) {
                    this.identityType = identityType;
                }

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getSection() {
                    return section;
                }

                public void setSection(int section) {
                    this.section = section;
                }

                public String getPeopleType() {
                    return peopleType;
                }

                public void setPeopleType(String peopleType) {
                    this.peopleType = peopleType;
                }

                public List<String> getStudentIds() {
                    return studentIds;
                }

                public void setStudentIds(List<String> studentIds) {
                    this.studentIds = studentIds;
                }

                public List<Students.PeopleBean> getPeople() {
                    return people;
                }

                public void setPeople(List<Students.PeopleBean> people) {
                    this.people = people;
                }

                public List<Students.PeopleBean> getLatePeople() {
                    return latePeople;
                }

                public void setLatePeople(List<Students.PeopleBean> latePeople) {
                    this.latePeople = latePeople;
                }

                public List<Students.PeopleBean> getLeavePeople() {
                    return leavePeople;
                }

                public void setLeavePeople(List<Students.PeopleBean> leavePeople) {
                    this.leavePeople = leavePeople;
                }

                public List<Students.PeopleBean> getAbsencePeople() {
                    return absencePeople;
                }

                public void setAbsencePeople(List<Students.PeopleBean> absencePeople) {
                    this.absencePeople = absencePeople;
                }

                public List<Students.PeopleBean> getLeaveEarlyPeople() {
                    return leaveEarlyPeople;
                }

                public void setLeaveEarlyPeople(List<Students.PeopleBean> leaveEarlyPeople) {
                    this.leaveEarlyPeople = leaveEarlyPeople;
                }

                public List<Students.PeopleBean> getApplyPeople() {
                    return applyPeople;
                }

                public void setApplyPeople(List<Students.PeopleBean> applyPeople) {
                    this.applyPeople = applyPeople;
                }

                public TeachersBean() {
                }
            }
        }


        public static class SchoolPeopleAllFormBean implements Serializable {
            public int number;
            public String rate;
            public int applyNum;
            public int late;
            public int leaveEarly;
            public int absence;
            public int leave;
            public int index;
            public String thingName;
            public String peopleType;//Y 老师 N 学生
            public String attName;
            public String identityType;//Y 课堂考勤 N 事件考勤
            public String attendanceType;
            public String requiredTime;
            public String goOutStatus;//0:签到，1:签退
            public List<?> people;
            public List<?> latePeople;
            public List<?> leavePeople;
            public List<?> absencePeople;
            public List<GradeListBean> gradeList;
            public AttendancesFormBean.Students students;
            public AttendancesFormBean.TeachersBean teachers;

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

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getThingName() {
                return thingName;
            }

            public void setThingName(String thingName) {
                this.thingName = thingName;
            }

            public String getPeopleType() {
                return peopleType;
            }

            public void setPeopleType(String peopleType) {
                this.peopleType = peopleType;
            }

            public String getAttName() {
                return attName;
            }

            public void setAttName(String attName) {
                this.attName = attName;
            }

            public String getIdentityType() {
                return identityType;
            }

            public void setIdentityType(String identityType) {
                this.identityType = identityType;
            }

            public String getAttendanceType() {
                return attendanceType;
            }

            public void setAttendanceType(String attendanceType) {
                this.attendanceType = attendanceType;
            }

            public String getRequiredTime() {
                return requiredTime;
            }

            public void setRequiredTime(String requiredTime) {
                this.requiredTime = requiredTime;
            }

            public String getGoOutStatus() {
                return goOutStatus;
            }

            public void setGoOutStatus(String goOutStatus) {
                this.goOutStatus = goOutStatus;
            }

            public List<?> getPeople() {
                return people;
            }

            public void setPeople(List<?> people) {
                this.people = people;
            }

            public List<?> getLatePeople() {
                return latePeople;
            }

            public void setLatePeople(List<?> latePeople) {
                this.latePeople = latePeople;
            }

            public List<?> getLeavePeople() {
                return leavePeople;
            }

            public void setLeavePeople(List<?> leavePeople) {
                this.leavePeople = leavePeople;
            }

            public List<?> getAbsencePeople() {
                return absencePeople;
            }

            public void setAbsencePeople(List<?> absencePeople) {
                this.absencePeople = absencePeople;
            }

            public List<GradeListBean> getGradeList() {
                return gradeList;
            }

            public void setGradeList(List<GradeListBean> gradeList) {
                this.gradeList = gradeList;
            }

            public AttendancesFormBean.Students getStudents() {
                return students;
            }

            public void setStudents(AttendancesFormBean.Students students) {
                this.students = students;
            }

            public AttendancesFormBean.TeachersBean getTeachers() {
                return teachers;
            }

            public void setTeachers(AttendancesFormBean.TeachersBean teachers) {
                this.teachers = teachers;
            }

            public SchoolPeopleAllFormBean() {
            }


            public static class GradeListBean implements Serializable {
                public String name;
                public long gradeId;
                public String className;
                public long classesId;
                //                public long studentId;
                public long id;
                public int number;
                public String rate;
                public int applyNum;
                public int late;
                public String goOutStatus;
                public int leaveEarly;
                public int absence;
                public int leave;
                public String peopleType;//Y 老师 N 学生
                public List<?> people;
                public List<?> latePeople;
                public List<?> leavePeople;
                public List<?> absencePeople;
                public List<ClassFormBean> classForm;

                public GradeListBean() {
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public long getGradeId() {
                    return gradeId;
                }

                public void setGradeId(long gradeId) {
                    this.gradeId = gradeId;
                }

                public String getClassName() {
                    return className;
                }

                public void setClassName(String className) {
                    this.className = className;
                }

                public long getClassesId() {
                    return classesId;
                }

                public void setClassesId(long classesId) {
                    this.classesId = classesId;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
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

                public String getGoOutStatus() {
                    return goOutStatus;
                }

                public void setGoOutStatus(String goOutStatus) {
                    this.goOutStatus = goOutStatus;
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

                public String getPeopleType() {
                    return peopleType;
                }

                public void setPeopleType(String peopleType) {
                    this.peopleType = peopleType;
                }

                public List<?> getPeople() {
                    return people;
                }

                public void setPeople(List<?> people) {
                    this.people = people;
                }

                public List<?> getLatePeople() {
                    return latePeople;
                }

                public void setLatePeople(List<?> latePeople) {
                    this.latePeople = latePeople;
                }

                public List<?> getLeavePeople() {
                    return leavePeople;
                }

                public void setLeavePeople(List<?> leavePeople) {
                    this.leavePeople = leavePeople;
                }

                public List<?> getAbsencePeople() {
                    return absencePeople;
                }

                public void setAbsencePeople(List<?> absencePeople) {
                    this.absencePeople = absencePeople;
                }

                public List<ClassFormBean> getClassForm() {
                    return classForm;
                }

                public void setClassForm(List<ClassFormBean> classForm) {
                    this.classForm = classForm;
                }

                public static class ClassFormBean implements Serializable {
                    //                    public long studentId;
                    public String name;
                    public long id;
                    public int number;
                    public String rate;
                    public int applyNum;
                    public int late;
                    public String goOutStatus;
                    public int leaveEarly;
                    public int absence;
                    public int leave;
                    public List<?> people;
                    public List<?> latePeople;
                    public List<?> leavePeople;
                    public List<?> absencePeople;
                    public List<?> leaveEarlyPeople;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public long getId() {
                        return id;
                    }

                    public void setId(long id) {
                        this.id = id;
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

                    public String getGoOutStatus() {
                        return goOutStatus;
                    }

                    public void setGoOutStatus(String goOutStatus) {
                        this.goOutStatus = goOutStatus;
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

                    public List<?> getPeople() {
                        return people;
                    }

                    public void setPeople(List<?> people) {
                        this.people = people;
                    }

                    public List<?> getLatePeople() {
                        return latePeople;
                    }

                    public void setLatePeople(List<?> latePeople) {
                        this.latePeople = latePeople;
                    }

                    public List<?> getLeavePeople() {
                        return leavePeople;
                    }

                    public void setLeavePeople(List<?> leavePeople) {
                        this.leavePeople = leavePeople;
                    }

                    public List<?> getAbsencePeople() {
                        return absencePeople;
                    }

                    public void setAbsencePeople(List<?> absencePeople) {
                        this.absencePeople = absencePeople;
                    }

                    public List<?> getLeaveEarlyPeople() {
                        return leaveEarlyPeople;
                    }

                    public void setLeaveEarlyPeople(List<?> leaveEarlyPeople) {
                        this.leaveEarlyPeople = leaveEarlyPeople;
                    }

                    public ClassFormBean() {
                    }
                }
            }
        }
    }
}
