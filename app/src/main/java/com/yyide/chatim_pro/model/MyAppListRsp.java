package com.yyide.chatim_pro.model;

import java.io.Serializable;
import java.util.List;


public class MyAppListRsp implements Serializable, Cloneable{
    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public Object clone(){
        MyAppListRsp bean = null;
        try{
            bean = (MyAppListRsp) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return bean;
    }


    public static class DataBean implements Serializable {
        private long id;
        private String name;
        private int sort;
        private String img;//":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/88d8ec52342e4567b49f7f9714bf62ea-1585.jpg",
        private String path;//:"111"
        private String appType;//是否为 第三方

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }
    }
}
