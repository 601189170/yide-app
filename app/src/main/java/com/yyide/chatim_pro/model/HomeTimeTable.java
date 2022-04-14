package com.yyide.chatim_pro.model;

import com.google.gson.annotations.SerializedName;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 18:54
 * @Description : 文件描述
 */

public class HomeTimeTable {

    public Integer code;
    public DataDTO data;
    public String message;

    public static class DataDTO {
        public String name;
        public String startTime;
        public String endTime;
        public int hour;
        public int min;
        public int sec;
        public String subjectName;
    }
}
