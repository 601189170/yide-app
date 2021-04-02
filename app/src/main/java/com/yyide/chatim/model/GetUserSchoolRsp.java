package com.yyide.chatim.model;

import java.util.List;

public class GetUserSchoolRsp {


    /**
     * code : 200
     * success : true
     * msg : 得到用户学校成功
     * data : [{"realname":"胡老师","username":"14712341234","password":"$2a$10$XeLCw.KRU76QCXRtXtHeSOOsse.ZY.VG6arccScds1.CEXAn5Ngwu","userId":1946,"schoolId":480,"classesId":null,"classesName":null,"parentId":674,"schoolName":"野鸡中学","schoolType":"N","teacherId":574,"dataPerInd":null,"depIds":null,"status":"4","depId":null,"depName":null,"dataPerDepIds":null,"imgList":null,"birthdayDate":null,"sex":null,"email":null,"schedule":null,"form":[{"classesId":"1985","classesName":"小一2021级3班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1984","classesName":"小一2021级2班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1983","classesName":"小一2021级1班","classesTeacher":"胡老师","teacherId":574}],"introduce":null,"img":null}]
     */

    public int code;
    public boolean success;
    public String msg;
    public List<DataBean> data;

   

    public static class DataBean {
        /**
         * realname : 胡老师
         * username : 14712341234
         * password : $2a$10$XeLCw.KRU76QCXRtXtHeSOOsse.ZY.VG6arccScds1.CEXAn5Ngwu
         * userId : 1946
         * schoolId : 480
         * classesId : null
         * classesName : null
         * parentId : 674
         * schoolName : 野鸡中学
         * schoolType : N
         * teacherId : 574
         * dataPerInd : null
         * depIds : null
         * status : 4
         * depId : null
         * depName : null
         * dataPerDepIds : null
         * imgList : null
         * birthdayDate : null
         * sex : null
         * email : null
         * schedule : null
         * form : [{"classesId":"1985","classesName":"小一2021级3班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1984","classesName":"小一2021级2班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1983","classesName":"小一2021级1班","classesTeacher":"胡老师","teacherId":574}]
         * introduce : null
         * img : null
         */

        public String realname;
        public String username;
        public String password;
        public int userId;
        public int schoolId;
        public Object classesId;
        public Object classesName;
        public int parentId;
        public String schoolName;
        public String schoolType;
        public int teacherId;
        public Object dataPerInd;
        public Object depIds;
        public String status;
        public Object depId;
        public Object depName;
        public Object dataPerDepIds;
        public List<String> imgList;
        public Object birthdayDate;
        public Object sex;
        public Object email;
        public Object schedule;
        public Object introduce;
        public Object img;
        public List<FormBean> form;

        public static class FormBean {
            /**
             * classesId : 1985
             * classesName : 小一2021级3班
             * classesTeacher : 胡老师
             * teacherId : 574
             */

            public String classesId;
            public String classesName;
            public String classesTeacher;
            public int teacherId;

            
        }
    }
}
