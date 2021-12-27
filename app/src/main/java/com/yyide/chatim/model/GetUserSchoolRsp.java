package com.yyide.chatim.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class GetUserSchoolRsp implements Serializable {

    /**
     * code : 200
     * success : true
     * msg : 得到用户学校成功
     * data : [{"realname":"胡老师","username":"14712341234","password":"$2a$10$XeLCw.KRU76QCXRtXtHeSOOsse.ZY.VG6arccScds1.CEXAn5Ngwu","userId":1946,"schoolId":480,"classesId":null,"classesName":null,"parentId":674,"schoolName":"野鸡中学","schoolType":"N","teacherId":574,"dataPerInd":null,"depIds":null,"status":"4","depId":null,"depName":null,"dataPerDepIds":null,"imgList":null,"birthdayDate":null,"sex":null,"email":null,"schedule":null,"form":[{"classesId":"1985","classesName":"小一2021级3班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1984","classesName":"小一2021级2班","classesTeacher":"胡老师","teacherId":574},{"classesId":"1983","classesName":"小一2021级1班","classesTeacher":"胡老师","teacherId":574}],"introduce":null,"img":null}]
     */

    public int code;
    public boolean success;
    public String msg;
    public String message;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
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
        //0 家长,1 学生,2 校长,3 班主任,4 老师,5 班牌账号，6 管理员账号
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
                    identity = "监护人";
                } else if (TYPE_STUDENT.equals(status)) {
                    identity = "学生";
                } else if (TYPE_PRESIDENT.equals(status)) {
                    identity = "校长";
                } else if (TYPE_CLASS_TEACHER.equals(status)) {
                    identity = "班主任";
                } else if (TYPE_TEACHER.equals(status)) {
                    identity = "教师";
                } else if (TYPE_BRAND_AUTH.equals(status)) {
                    identity = "班牌账号";
                } else if (TYPE_ADMIN.equals(status)) {
                    identity = "管理员账号";
                }
            }
            return identity;
        }

        /**
         * 返回身份描述
         *
         * @return
         */
        public String getIdentityResult() {
            String identity = "";
            if (!TextUtils.isEmpty(status)) {
                if (TYPE_PARENTS.equals(status)) {
                    identity = "家长";
                } else if (TYPE_STUDENT.equals(status)) {
                    identity = "学生";
                } else if (TYPE_PRESIDENT.equals(status)) {
                    identity = "教师";
                } else if (TYPE_CLASS_TEACHER.equals(status)) {
                    identity = "教师";
                } else if (TYPE_TEACHER.equals(status)) {
                    identity = "教师";
                } else if (TYPE_BRAND_AUTH.equals(status)) {
                    identity = "班牌账号";
                } else if (TYPE_ADMIN.equals(status)) {
                    identity = "教师";
                }
            }
            return identity;
        }

        public boolean isSchool() {
            return TYPE_PRESIDENT.equals(status);
        }

        public boolean isAdmin() {
            return TYPE_ADMIN.equals(status);
        }

        public boolean isParent() {
            return TYPE_PARENTS.equals(status);
        }

        public boolean isTeacherOrCharge() {
            return TYPE_CLASS_TEACHER.equals(status) || TYPE_TEACHER.equals(status);
        }

        /**
         * 判断当前是否是教职工
         *
         * @return 默认是教职工
         */
        public boolean staffIdentity() {
            return TextUtils.isEmpty(status) || (!TYPE_PARENTS.equals(status) && !TYPE_STUDENT.equals(status));
        }

        public String realname;
        public String username;
        public String password;
        public String userId;
        public long schoolId;
        public long classesId;
        public String classesName;
        public long parentId;
        public String schoolName;
        public String schoolType;//Y 大学 N小初高
        public String teacherId;
        public Object dataPerInd;
        public Object depIds;
        public String status;
        public Object depId;
        public String depName;
        public Object dataPerDepIds;
        public List<String> imgList;
        public String birthdayDate;
        public String sex;//1 男 0女
        public String email;
        public int teacherDepId;
        public Object schedule;
        public Object introduce;
        public String img;
        public List<FormBean> form;
        public boolean isCurrentUser;
        public int weekNum;
        public String studentName;

        public static class FormBean implements Serializable {
            /**
             * classesId : 1985
             * classesName : 小一2021级3班
             * classesTeacher : 胡老师
             * teacherId : 574
             */
            public String studentBirthdayDate;
            public String studentSex;//1 男 0女
            public String classesId;
            public String classesName;
            public String teacherInd;
            public String studentEmail;
            public String studentPhone;
            public String classesTeacher;
            public String teacherId;
            public String studentPic;
            public String classesStudentName;
            public String studentName;
            public String studentId;
            public String studentUserId;
            public String userId;

        }
    }

    public void getClassInfo() {

    }

    public void getIdentity() {

    }
}
