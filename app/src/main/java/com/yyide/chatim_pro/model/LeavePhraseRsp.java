package com.yyide.chatim_pro.model;

import java.util.List;

/**
 * @Description: 请假短语实体
 * @Author: liu tao
 * @CreateDate: 2021/5/25 18:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/25 18:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class LeavePhraseRsp {

    private int code;
    private boolean success;
    private String message;
    private List<DataBean> data;

    public LeavePhraseRsp() {
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
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String tag;
        private long id;
        private boolean checked;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public DataBean() {
        }
    }
}
