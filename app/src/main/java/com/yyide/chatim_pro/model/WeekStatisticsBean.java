package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/12 14:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 14:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class WeekStatisticsBean {
    private int time;//次数
    private String name;//人员姓名
    private boolean checked;
    private List<DataBean> dataBeanList;

    public WeekStatisticsBean(int time, String name, boolean checked, List<DataBean> dataBeanList) {
        this.time = time;
        this.name = name;
        this.checked = checked;
        this.dataBeanList = dataBeanList;
    }

    public WeekStatisticsBean() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }

    public static class DataBean{
        private String title;
        private String time;
        private String status;

        public DataBean(String title, String time, String status) {
            this.title = title;
            this.time = time;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public DataBean() {
        }
    }
}
