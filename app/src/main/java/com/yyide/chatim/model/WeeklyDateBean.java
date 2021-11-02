package com.yyide.chatim.model;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

public class WeeklyDateBean {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    protected WeeklyDateBean(Parcel in) {
        code = in.readInt();
        success = in.readByte() != 0;
        msg = in.readString();
        data = in.readParcelable(DataBean.class.getClassLoader());
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
        private List<TimesBean> times;
        private TimesBean time;

        //添加无参的构造方法，不然报错
        public DataBean() {
            super();
        }

        public List<TimesBean> getTimes() {
            return times;
        }

        public void setTimes(List<TimesBean> times) {
            this.times = times;
        }

        public TimesBean getTime() {
            return time;
        }

        public void setTime(TimesBean time) {
            this.time = time;
        }


        public static class TimesBean implements Serializable {
            private String startTime;
            private String endTime;

            public TimesBean() {
                super();
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
    }
}
