package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 考勤统计数据
 * @Author: liu tao
 * @CreateDate: 2021/6/17 11:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/17 11:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class AttendanceDayStatsRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<AttendancesFormBean> attendancesForm;
        private List<?> schoolPeopleAllForm;
        private int page;

        @NoArgsConstructor
        @Data
        public static class AttendancesFormBean {
            private int number;
            private int numberA;
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
            private List<?> applyPeopleA;
            private String identityType;
            private String dayType;
            private List<StudentListsBean> studentLists;

            @NoArgsConstructor
            @Data
            public static class StudentListsBean {
                private int number;
                private String rate;
                private int applyNum;
                private int late;
                private int leaveEarly;
                private int absence;
                private int leave;
                private String name;
                private int type;
                private int section;
                private String subjectName;
                private String applyDate;
                private String startTime;
                private String endTime;
                private List<?> studentIds;
                private List<PeopleBean> people;
                private List<PeopleBean> latePeople;
                private List<PeopleBean> leavePeople;
                private List<PeopleBean> absencePeople;
                private List<PeopleBean> leaveEarlyPeople;
                private List<PeopleBean> applyPeople;
                private String peopleType;
                private String path;
                private String startDate;
                private String endDate;
                private String classesName;
                private String attId;
                private String thingName;
                private int dateType;
                private String time;
                private int page;

                @NoArgsConstructor
                @Data
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
                }
            }
        }
    }
}
