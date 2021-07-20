package com.yyide.chatim.model;

import android.text.TextUtils;

import com.yyide.chatim.widget.treeview.model.NodeId;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AttendanceCheckRsp implements Serializable {

    public int code;
    public boolean success;
    public String msg;
    public DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable {
        public List<AttendancesFormBean> attendancesForm;
        public List<SchoolPeopleAllFormBean> schoolPeopleAllForm;

        @NoArgsConstructor
        @Data
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

            @NoArgsConstructor
            @Data
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

                @NoArgsConstructor
                @Data
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

            @NoArgsConstructor
            @Data
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

            }
        }

        @NoArgsConstructor
        @Data
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

            @NoArgsConstructor
            @Data
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

                @NoArgsConstructor
                @Data
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

                }
            }
        }
    }
}
