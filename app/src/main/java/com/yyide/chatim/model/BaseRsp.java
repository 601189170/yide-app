package com.yyide.chatim.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/9 15:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/9 15:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseRsp {

    /**
     * code : 200
     * success : true
     * msg : 成功
     * data : null
     */

    private int code;
    private boolean success;
    private String msg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseRsp{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
