package com.yyide.chatim_pro.model;

import java.util.List;

public class HomeAttendanceRsp {

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

    public static class DataBean {
        private int number;
        private String rate;
        private int applyNum;
        private int late;
        private int leaveEarly;
        private int absence;
        private int leave;
        private String name;
        private int section;
        private String attendanceType;
        private List<PeopleBean> people;
        private List<PeopleBean> latePeople;
        private List<PeopleBean> leavePeople;
        private List<PeopleBean> absencePeople;

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

        public String getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(String attendanceType) {
            this.attendanceType = attendanceType;
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

        public List<?> getAbsencePeople() {
            return absencePeople;
        }

        public void setAbsencePeople(List<PeopleBean> absencePeople) {
            this.absencePeople = absencePeople;
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
    }
}
