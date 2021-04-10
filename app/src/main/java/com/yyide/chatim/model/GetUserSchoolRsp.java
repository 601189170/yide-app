package com.yyide.chatim.model;

import android.text.TextUtils;

import java.util.ArrayList;
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
        //家长：0 ,学生：1 ,校长：2 ,班主任：3,老师：4,班牌账号：5，管理员账号：6
        public static final String TYPE_PARENTS = "0";
        public static final String TYPE_STUDENT = "1";
        public static final String TYPE_PRESIDENT = "2";
        public static final String TYPE_CLASS_TEACHER = "3";
        public static final String TYPE_TEACHER = "4";
        public static final String TYPE_BRAND_AUTH = "5";
        public static final String TYPE_ADMIN = "6";

        public String getIdentity() {
            String identity = "";
            if (!TextUtils.isEmpty(status)) {
                if (TYPE_PARENTS.equals(status)) {
                    identity = "家长";
                } else if (TYPE_STUDENT.equals(status)) {
                    identity = "学生";
                } else if (TYPE_PRESIDENT.equals(status)) {
                    identity = "校长";
                } else if (TYPE_CLASS_TEACHER.equals(status)) {
                    identity = "班主任";
                } else if (TYPE_TEACHER.equals(status)) {
                    identity = "老师";
                } else if (TYPE_BRAND_AUTH.equals(status)) {
                    identity = "班牌账号";
                } else if (TYPE_ADMIN.equals(status)) {
                    identity = "管理员账号";
                }
            }
            return identity;
        }

        public String realname;
        public String username;
        public String password;
        public int userId;
        public int schoolId;
        public String classesId;
        public String classesName;
        public int parentId;
        public String schoolName;
        public String schoolType;//Y 大学 N小初高
        public int teacherId;
        public Object dataPerInd;
        public Object depIds;
        public String status;
        public Object depId;
        public String depName;
        public Object dataPerDepIds;
        public List<String> imgList;
        public String birthdayDate;
        public String sex;
        public String email;
        public Object schedule;
        public Object introduce;
        public String img;
        public List<FormBean> form;
        public boolean isCurrentUser;

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

    public void getClassInfo() {

    }

    public void getIdentity() {

    }
}
