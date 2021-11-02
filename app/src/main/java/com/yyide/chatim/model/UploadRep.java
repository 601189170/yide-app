package com.yyide.chatim.model;

public class UploadRep {
    //{"code":200,"message":"操作成功","url":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/524_1984/school-user/a494574b8a4542d5bfa9daa97ada4101-fileName.jpg"}
    private int code;
    private String message;
    private String url;
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
