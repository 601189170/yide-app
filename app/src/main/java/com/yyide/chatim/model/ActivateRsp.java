package com.yyide.chatim.model;



/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/14 9:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/14 9:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class ActivateRsp {

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
        private String activateCode;
        //激活状态（1：已启用，2：禁用）
        private String activateState;
        //绑定状态（0：未绑定，1：已绑定）
        private String bangingState;
        //开通验证方式【0：未開通，1：已開通】
        private String openState;

        public String getActivateCode() {
            return activateCode;
        }

        public void setActivateCode(String activateCode) {
            this.activateCode = activateCode;
        }

        public String getActivateState() {
            return activateState;
        }

        public void setActivateState(String activateState) {
            this.activateState = activateState;
        }

        public String getBangingState() {
            return bangingState;
        }

        public void setBangingState(String bangingState) {
            this.bangingState = bangingState;
        }

        public String getOpenState() {
            return openState;
        }

        public void setOpenState(String openState) {
            this.openState = openState;
        }
    }
}
