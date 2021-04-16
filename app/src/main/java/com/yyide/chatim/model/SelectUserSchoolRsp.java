package com.yyide.chatim.model;

import java.util.List;

public class SelectUserSchoolRsp {

    /**
     * code : 200
     * data : {"username":"13659896596","password":"$2a$10$YgBanTvXwFVqsNKi/0V3MO5oSnpWKsvrKBV.yLJHGWcxEgZr9VH12","userId":null,"schoolId":158,"schoolName":"发的发生","teacherId":null,"dataPerInd":null,"depIds":null,"status":null,"depId":null,"depName":null,"dataPerDepIds":null,"imgList":[],"birthdayDate":null,"sex":null,"email":null}
     * message : 操作成功
     */

    public int code;
    public DataBean data;
    public String message;

    public static class DataBean {
        /**
         * username : 13659896596
         * password : $2a$10$YgBanTvXwFVqsNKi/0V3MO5oSnpWKsvrKBV.yLJHGWcxEgZr9VH12
         * userId : null
         * schoolId : 158
         * schoolName : 发的发生
         * teacherId : null
         * dataPerInd : null
         * depIds : null
         * status : null
         * depId : null
         * depName : null
         * dataPerDepIds : null
         * imgList : []
         * birthdayDate : null
         * sex : null
         * email : null
         */

        public String username;
        public String password;
        public Object userId;
        public int schoolId;
        public String schoolName;
        public Object teacherId;
        public Object dataPerInd;
        public Object depIds;
        public Object status;
        public Object depId;
        public Object depName;
        public Object dataPerDepIds;
        public Object birthdayDate;
        public Object sex;
        public Object email;
        public List<?> imgList;


    }
}
