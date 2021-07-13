package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

public class SelectSchByTeaidRsp {


    /**
     * code : 200
     * success : true
     * msg : 通过老师信息找到对应的课表信息成功
     * data : [{"id":null,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-02T08:06:26.069+0000","updatedBy":null,"updatedDateTime":"2021-04-02T08:06:26.069+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"品德与生活(社会)","classesId":"630","classesName":"英语1班","classesTeacher":"丁老师","teacherId":585,"schoolId":524,"section":"2","weekTime":5,"fromDateTime":"10:30","toDateTime":"11:15"},{"id":null,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-02T08:06:26.069+0000","updatedBy":null,"updatedDateTime":"2021-04-02T08:06:26.069+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"品德与生活(社会)","classesId":"630","classesName":"英语1班","classesTeacher":"丁老师","teacherId":585,"schoolId":524,"section":"3","weekTime":5,"fromDateTime":"11:30","toDateTime":"12:15"},{"id":null,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-02T08:06:26.069+0000","updatedBy":null,"updatedDateTime":"2021-04-02T08:06:26.069+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"品德与生活(社会)","classesId":"630","classesName":"英语1班","classesTeacher":"丁老师","teacherId":585,"schoolId":524,"section":"4","weekTime":3,"fromDateTime":"12:30","toDateTime":"13:15"},{"id":null,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-02T08:06:26.069+0000","updatedBy":null,"updatedDateTime":"2021-04-02T08:06:26.069+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"品德与生活(社会)","classesId":"630","classesName":"英语1班","classesTeacher":"丁老师","teacherId":585,"schoolId":524,"section":"5","weekTime":3,"fromDateTime":"14:00","toDateTime":"14:45"},{"id":null,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-02T08:06:26.069+0000","updatedBy":null,"updatedDateTime":"2021-04-02T08:06:26.069+0000","versionStamp":0,"total":0,"size":10,"current":1,"subjectName":"品德与生活(社会)","classesId":"630","classesName":"英语1班","classesTeacher":"丁老师","teacherId":585,"schoolId":524,"section":"6","weekTime":6,"fromDateTime":"16:00","toDateTime":"16:45"}]
     */

    public int code;
    public boolean success;
    public String msg;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * id : null
         * delInd : 0
         * createdBy : null
         * createdDateTime : 2021-04-02T08:06:26.069+0000
         * updatedBy : null
         * updatedDateTime : 2021-04-02T08:06:26.069+0000
         * versionStamp : 0
         * total : 0
         * size : 10
         * current : 1
         * subjectName : 品德与生活(社会)
         * classesId : 630
         * classesName : 英语1班
         * classesTeacher : 丁老师
         * teacherId : 585
         * schoolId : 524
         * section : 2
         * weekTime : 5
         * fromDateTime : 10:30
         * toDateTime : 11:15
         */

        public long id;
        public String delInd;
        public Object createdBy;
        public String createdDateTime;
        public Object updatedBy;
        public String updatedDateTime;
        public int versionStamp;
        public int total;
        public int size;
        public int current;
        public String subjectName;
        public String classesId;
        public String classesName;
        public String classesTeacher;
        public long teacherId;
        public long schoolId;
        public String schoolType;
        public String section;
        public int weekTime;
        public String fromDateTime;
        public String toDateTime;
        public String beforeClass;
        public String afterClass;
        public String teachTool;
        public long subid;
        public long lessonsId;
        public List<String> teachToolList;
        public List<String> lessonsSubEntityList;
    }
}
