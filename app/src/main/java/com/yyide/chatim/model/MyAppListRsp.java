package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyAppListRsp implements Serializable, Cloneable{
    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;


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


    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable {
        private long id;
        private String name;
        private int sort;
        private String img;//":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/88d8ec52342e4567b49f7f9714bf62ea-1585.jpg",
        private String path;//:"111"
        private String appType;
    }
}
