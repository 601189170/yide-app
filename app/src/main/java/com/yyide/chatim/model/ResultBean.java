package com.yyide.chatim.model;

public class ResultBean {

    /**
     * code : -1
     * success : false
     * msg : 手机号码不是平台账号!
     */

    private int code;
    private boolean success;
    private String msg;

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
}
