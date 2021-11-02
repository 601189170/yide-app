package com.yyide.chatim.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/12 14:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 14:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeaveFlowBean {
    private String time;
    private String date;
    private String flowTitle;
    private String flowContent;
    private boolean checked;
    private boolean nopass;
    private String image;

    public LeaveFlowBean(String time, String date, String flowTitle, String flowContent, boolean checked, boolean nopass, String image) {
        this.time = time;
        this.date = date;
        this.flowTitle = flowTitle;
        this.flowContent = flowContent;
        this.checked = checked;
        this.nopass = nopass;
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFlowTitle() {
        return flowTitle;
    }

    public void setFlowTitle(String flowTitle) {
        this.flowTitle = flowTitle;
    }

    public String getFlowContent() {
        return flowContent;
    }

    public void setFlowContent(String flowContent) {
        this.flowContent = flowContent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isNopass() {
        return nopass;
    }

    public void setNopass(boolean nopass) {
        this.nopass = nopass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LeaveFlowBean() {
    }
}
