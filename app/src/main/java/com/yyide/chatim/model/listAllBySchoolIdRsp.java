package com.yyide.chatim.model;

import java.util.List;

public class listAllBySchoolIdRsp {


    /**
     * code : 200
     * data : [{"id":1,"delInd":"0","createdBy":"8","createdDateTime":"2021-03-02T09:51:54.000+0000","updatedBy":"368","updatedDateTime":"2021-03-02T09:51:54.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"name":"一班","schoolId":196,"alias":"","showType":"0","gradeId":1,"headmasterId":94,"secondHeadmasterId":null,"type":"2"}]
     * message : Success
     */

    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 1
         * delInd : 0
         * createdBy : 8
         * createdDateTime : 2021-03-02T09:51:54.000+0000
         * updatedBy : 368
         * updatedDateTime : 2021-03-02T09:51:54.000+0000
         * versionStamp : 0
         * total : 0
         * size : 10
         * current : 1
         * name : 一班
         * schoolId : 196
         * alias : 
         * showType : 0
         * gradeId : 1
         * headmasterId : 94
         * secondHeadmasterId : null
         * type : 2
         */

        public long id;
        public String delInd;
        public String createdBy;
        public String createdDateTime;
        public String updatedBy;
        public String updatedDateTime;
        public int versionStamp;
        public int total;
        public int size;
        public int current;
        public String name;
        public int schoolId;
        public String alias;
        public String showType;
        public int gradeId;
        public int headmasterId;
        public Object secondHeadmasterId;
        public String type;

    }
}
