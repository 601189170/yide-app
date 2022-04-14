package com.yyide.chatim_pro.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/8 19:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/8 19:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AddUserAnnouncementResponse {

    /**
     * code : 200
     * success : true
     * msg : data
     * data : {"success":"添加成功!"}
     */

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
        /**
         * success : 添加成功!
         */

        private String success;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }
    }
}
