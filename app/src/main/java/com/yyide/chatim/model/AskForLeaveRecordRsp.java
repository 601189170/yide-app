package com.yyide.chatim.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/11 14:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 14:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class AskForLeaveRecordRsp {
    private long id;
    private int status;//1,审批中，2，已撤销，3，审批通过 4，审批拒绝
    private String title;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AskForLeaveRecordRsp() {
    }
}
