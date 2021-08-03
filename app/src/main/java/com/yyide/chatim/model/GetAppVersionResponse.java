package com.yyide.chatim.model;

public class GetAppVersionResponse {

    private int code;
    private Boolean success;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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
        public String id;
        public String delInd;
        public String createdBy;
        public String createdDateTime;
        public String updatedBy;
        public String updatedDateTime;
        public String versionStamp;
        public int total;
        public int size;
        public int current;
        public String versionCode;
        public String versionName;
        public String versionDesc;
        public String filePath;
        public String appName;
        public int isCompulsory;
        public String terminal;
    }
}
