package com.yyide.chatim.model;

import java.util.List;

public class GetUserSchoolRsp {


    /**
     * code : 200
     * data : [{"realname":"第一个老师信777","username":"13714199999","password":"$2a$10$tWhh5AqMReX3YNQlpyRC6uHvwskV6P5TPXXxvAnEGa42/7RRz.j/e","userId":875,"schoolId":230,"parentId":418,"schoolName":"苏仙完小1","schoolType":null,"teacherId":258,"dataPerInd":null,"depIds":null,"status":"4","depId":null,"depName":null,"dataPerDepIds":null,"imgList":["cloud-yide.oss-cn-shenzhen.aliyuncs.com/418/school/225397ab28e94618a5573021b5bac2ca-418.jpg"],"birthdayDate":null,"sex":null,"email":null,"schedule":[{"id":36,"delInd":"0","createdBy":"18118777469","createdDateTime":"2021-03-18T08:52:50.000+0000","updatedBy":"18118777469","updatedDateTime":"2021-03-18T08:52:50.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"语文","curriculumType":"课程","classesId":"1693","classIds":null,"classesName":"小二2020级1班","classesTeacher":"第一个老师信777","teacherId":258,"schoolId":230,"status":null,"teacherAttInd":"N","studentAttInd":"N"}],"introduce":"<p>个人各位二哥我二哥我二哥我仍2021.3.18 11:32<\/p>"}]
     * message : 操作成功
     */

    public int code;
    public String message;
    public List<DataBean> data;

  
    public static class DataBean {
        /**
         * realname : 第一个老师信777
         * username : 13714199999
         * password : $2a$10$tWhh5AqMReX3YNQlpyRC6uHvwskV6P5TPXXxvAnEGa42/7RRz.j/e
         * userId : 875
         * schoolId : 230
         * parentId : 418
         * schoolName : 苏仙完小1
         * schoolType : null
         * teacherId : 258
         * dataPerInd : null
         * depIds : null
         * status : 4
         * depId : null
         * depName : null
         * dataPerDepIds : null
         * imgList : ["cloud-yide.oss-cn-shenzhen.aliyuncs.com/418/school/225397ab28e94618a5573021b5bac2ca-418.jpg"]
         * birthdayDate : null
         * sex : null
         * email : null
         * schedule : [{"id":36,"delInd":"0","createdBy":"18118777469","createdDateTime":"2021-03-18T08:52:50.000+0000","updatedBy":"18118777469","updatedDateTime":"2021-03-18T08:52:50.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"语文","curriculumType":"课程","classesId":"1693","classIds":null,"classesName":"小二2020级1班","classesTeacher":"第一个老师信777","teacherId":258,"schoolId":230,"status":null,"teacherAttInd":"N","studentAttInd":"N"}]
         * introduce : <p>个人各位二哥我二哥我二哥我仍2021.3.18 11:32</p>
         */

        public String realname;
        public String username;
        public String password;
        public int userId;
        public int schoolId;
        public int parentId;
        public String schoolName;
        public Object schoolType;
        public int teacherId;
        public Object dataPerInd;
        public Object depIds;
        public String status;
        public Object depId;
        public Object depName;
        public Object dataPerDepIds;
        public Object birthdayDate;
        public Object sex;
        public Object email;
        public String introduce;
        public List<String> imgList;
        public List<ScheduleBean> schedule;

        
        public static class ScheduleBean {
            /**
             * id : 36
             * delInd : 0
             * createdBy : 18118777469
             * createdDateTime : 2021-03-18T08:52:50.000+0000
             * updatedBy : 18118777469
             * updatedDateTime : 2021-03-18T08:52:50.000+0000
             * versionStamp : 0
             * total : 0
             * size : 10
             * current : 1
             * subjectName : 语文
             * curriculumType : 课程
             * classesId : 1693
             * classIds : null
             * classesName : 小二2020级1班
             * classesTeacher : 第一个老师信777
             * teacherId : 258
             * schoolId : 230
             * status : null
             * teacherAttInd : N
             * studentAttInd : N
             */

            public int id;
            public String delInd;
            public String createdBy;
            public String createdDateTime;
            public String updatedBy;
            public String updatedDateTime;
            public int versionStamp;
            public int total;
            public int size;
            public int current;
            public String subjectName;
            public String curriculumType;
            public String classesId;
            public Object classIds;
            public String classesName;
            public String classesTeacher;
            public int teacherId;
            public int schoolId;
            public Object status;
            public String teacherAttInd;
            public String studentAttInd;

         
        }
    }
}
