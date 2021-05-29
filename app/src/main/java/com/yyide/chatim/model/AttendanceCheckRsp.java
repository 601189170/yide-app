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
        private List<?> schoolPeopleAllForm;

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

            public static class StudentsBean {
                private int number;
                private String rate;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private String name;
                private int section;
                private String subjectName;
                private List<?> studentIds;
                private List<PeopleBean> people;
                private List<PeopleBean> latePeople;
                private List<PeopleBean> leavePeople;
                private List<PeopleBean> absencePeople;
                private List<PeopleBean> leaveEarlyPeople;

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

                public List<?> getLeavePeople() {
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

                public static class PeopleBean {
                    private String name;
                    private String status;
                    private String time;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public String getStatusValue() {//（0正常、1缺勤、2迟到/3早退,4无效打卡）
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


                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }
                }

            }
        }
    }
}
