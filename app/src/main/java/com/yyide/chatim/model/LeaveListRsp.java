package com.yyide.chatim.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @Description: 请假列表数据
 * @Author: liu tao
 * @CreateDate: 2021/5/19 16:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/19 16:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeaveListRsp {

    private int code;
    private boolean success;
    private String message;
    private DataBean data;

    public LeaveListRsp() {
    }

    public int getCode() {
        return code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<RecordsBean> list;
        public int total;
        public int pageNo;

        public List<RecordsBean> getList() {
            return list;
        }

        public void setList(List<RecordsBean> list) {
            this.list = list;
        }

        public static class RecordsBean {
            private long id;
            private int identity;
            private String jsonData;
            private String name;
            private String procInstId;
            private String schoolId;
            private String status;
            private String title;
            private int type;
            private String userId;

            public static class LeaveDetail {
                //                {\"dept\":\"一部\",\"endTime\":\"2022-03-16 18:00\",\"hours\":\"8\",\"reason\":\"身体不适\",\"startTime\":\"2022-03-16 8:00\"}
                private String dept;
                private String hours;
                private String reason;
                private String startTime;
                private String endTime;

                public String getDept() {
                    return dept;
                }

                public void setDept(String dept) {
                    this.dept = dept;
                }

                public String getHours() {
                    return hours;
                }

                public void setHours(String hours) {
                    this.hours = hours;
                }

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }

                public String getStartTime() {
                    return startTime;
                }

                public void setStartTime(String startTime) {
                    this.startTime = startTime;
                }

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getIdentity() {
                return identity;
            }

            public void setIdentity(int identity) {
                this.identity = identity;
            }

            public String getJsonData() {
                return jsonData;
            }

            public LeaveDetail getLeaveDetail() {
                try {
                    return JSON.parseObject(jsonData, LeaveDetail.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void setJsonData(String jsonData) {
                this.jsonData = jsonData;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProcInstId() {
                return procInstId;
            }

            public void setProcInstId(String procInstId) {
                this.procInstId = procInstId;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }


        }
    }

}
