package com.yyide.chatim.model;

import java.util.List;

public class AttendanceCheckRsp {

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

    public static class DataBean {
        private List<AttendancesFormBean> attendancesForm;
        private List<SchoolPeopleAllFormBean> schoolPeopleAllForm;

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

        public static class AttendancesFormBean {
            private int number;
            private TeachersBean teachers;
            private Students students;
            private int numberA;
            private String rateA;
            private int applyNumA;
            private int lateA;
            private int leaveEarlyA;
            private int absenceA;
            private int leaveA;
            private String attNameA;
            private List<?> peopleA;
            private List<?> latePeopleA;
            private List<?> leavePeopleA;
            private List<?> absencePeopleA;
            private List<?> leaveEarlyPeopleA;

            public Students getStudents() {
                return students;
            }

            public void setStudents(Students students) {
                this.students = students;
            }

            public static class Students{

                private int number;
                private String rate;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private String attendanceType;//（0正常、1缺勤、2迟到/3早退,4无效打卡）
                private String attendanceData;//考情时间
                private String name;
                private int section;
                private String subjectName;
                private String id;
                private List<?> studentIds;
                private List<PeopleBean> people;
                private List<PeopleBean> latePeople;
                private List<PeopleBean> leavePeople;
                private List<PeopleBean> absencePeople;
                private List<PeopleBean> leaveEarlyPeople;
                private List<PeopleBean> applyPeople;

                public String getAttendanceData() {
                    return attendanceData;
                }

                public void setAttendanceData(String attendanceData) {
                    this.attendanceData = attendanceData;
                }

                public String getAttendanceType() {
                    return attendanceType;
                }

                public void setAttendanceType(String attendanceType) {
                    this.attendanceType = attendanceType;
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

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public List<?> getLatePeople() {
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

                public List<?> getLeaveEarlyPeople() {
                    return leaveEarlyPeople;
                }

                public void setLeaveEarlyPeople(List<PeopleBean> leaveEarlyPeople) {
                    this.leaveEarlyPeople = leaveEarlyPeople;
                }

                public List<?> getApplyPeople() {
                    return applyPeople;
                }

                public void setApplyPeople(List<PeopleBean> applyPeople) {
                    this.applyPeople = applyPeople;
                }

                public static class PeopleBean {
                    private String name;
                    private String status;
                    private String time;

                    public String getStatusType() {//（0正常、1缺勤、2迟到/3早退,4无效打卡）
                        switch (status){
                            case "0":
                                status = "正常";
                                break;
                            case "1":
                                status = "缺勤";
                                break;
                            case "2":
                                status = "迟到";
                                break;
                            case "3":
                                status = "早退";
                                break;
                            case "4":
                                status = "无效打卡";
                                break;
                        }
                        return status;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
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
                }

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

            public String getAttNameA() {
                return attNameA;
            }

            public void setAttNameA(String attNameA) {
                this.attNameA = attNameA;
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

            public static class TeachersBean {
                private int number;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private String name;
                private int section;
                private List<?> studentIds;
                private List<PeopleBean> people;
                private List<?> latePeople;
                private List<?> leavePeople;
                private List<?> absencePeople;
                private List<?> leaveEarlyPeople;
                private List<ApplyPeopleBean> applyPeople;

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

                public List<ApplyPeopleBean> getApplyPeople() {
                    return applyPeople;
                }

                public void setApplyPeople(List<ApplyPeopleBean> applyPeople) {
                    this.applyPeople = applyPeople;
                }

                public static class PeopleBean {
                    private String name;
                    private String time;
                    private String status;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }
                }

                public static class ApplyPeopleBean {
                    private String name;
                    private String time;
                    private String status;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }
                }
            }
        }

        public static class SchoolPeopleAllFormBean {
            private int number;
            private String rate;
            private int applyNum;
            private int late;
            private int leaveEarly;
            private int absence;
            private int leave;
            private String attName;
            private List<?> people;
            private List<?> latePeople;
            private List<?> leavePeople;
            private List<?> absencePeople;
            private List<GradeListBean> gradeList;

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

            public String getAttName() {
                return attName;
            }

            public void setAttName(String attName) {
                this.attName = attName;
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

            public static class GradeListBean {
                private String name;
                private int gradeId;
                private String className;
                private int classesId;
                private String studentId;
                private int id;
                private int number;
                private String rate;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private List<?> people;
                private List<?> latePeople;
                private List<?> leavePeople;
                private List<?> absencePeople;
                private List<ClassFormBean> classForm;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getGradeId() {
                    return gradeId;
                }

                public void setGradeId(int gradeId) {
                    this.gradeId = gradeId;
                }

                public String getClassName() {
                    return className;
                }

                public void setClassName(String className) {
                    this.className = className;
                }

                public int getClassesId() {
                    return classesId;
                }

                public void setClassesId(int classesId) {
                    this.classesId = classesId;
                }

                public String getStudentId() {
                    return studentId;
                }

                public void setStudentId(String studentId) {
                    this.studentId = studentId;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
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

                public List<ClassFormBean> getClassForm() {
                    return classForm;
                }

                public void setClassForm(List<ClassFormBean> classForm) {
                    this.classForm = classForm;
                }

                public static class ClassFormBean {
                    private String studentId;
                    private String name;
                    private int id;
                    private int number;
                    private String rate;
                    private int applyNum;
                    private int late;
                    private int leaveEarly;
                    private int absence;
                    private int leave;
                    private List<?> people;
                    private List<?> latePeople;
                    private List<?> leavePeople;
                    private List<?> absencePeople;
                    private List<?> leaveEarlyPeople;

                    public String getStudentId() {
                        return studentId;
                    }

                    public void setStudentId(String studentId) {
                        this.studentId = studentId;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
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
                }
            }
        }
    }
}
