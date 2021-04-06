package com.yyide.chatim.model;

import java.io.Serializable;
import java.util.List;

public class selectListByAppRsp implements Serializable{


    /**
     * code : 200
     * data : {"name":"深圳中学","list":[{"name":"初中","alias":"","schoolId":1,"grades":[{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546029000,"updatedDateTime":1610546013000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546130000,"updatedDateTime":1610546126000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":3,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546250000,"updatedDateTime":1610546246000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":4,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588259000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":4,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588258000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":5,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588434000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":5,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588408000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"showType":"0","id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610531994000,"updatedDateTime":1610588465004,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"高中","alias":"","schoolId":1,"grades":[],"showType":"0","id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610531994000,"updatedDateTime":1610588465004,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"大学","alias":"","schoolId":1,"grades":[],"showType":"0","id":3,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610532241000,"updatedDateTime":1610588465005,"updatedBy":"","versionStamp":0,"delInd":"0"}]}
     * message : Success
     */

    public int code;
    public DataBean data;
    public String message;

  
    public static class DataBean {
        /**
         * name : 深圳中学
         * list : [{"name":"初中","alias":"","schoolId":1,"grades":[{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546029000,"updatedDateTime":1610546013000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546130000,"updatedDateTime":1610546126000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":3,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546250000,"updatedDateTime":1610546246000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":4,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588259000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":4,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588258000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":5,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588434000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":5,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588408000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"showType":"0","id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610531994000,"updatedDateTime":1610588465004,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"高中","alias":"","schoolId":1,"grades":[],"showType":"0","id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610531994000,"updatedDateTime":1610588465004,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"大学","alias":"","schoolId":1,"grades":[],"showType":"0","id":3,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610532241000,"updatedDateTime":1610588465005,"updatedBy":"","versionStamp":0,"delInd":"0"}]
         */

        public String name;
        public List<ListBean> list;

        

        public static class ListBean {
            /**
             * name : 初中
             * alias : 
             * schoolId : 1
             * grades : [{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546029000,"updatedDateTime":1610546013000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546130000,"updatedDateTime":1610546126000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":3,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610546250000,"updatedDateTime":1610546246000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":4,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":1,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588259000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":4,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588258000,"updatedDateTime":1610588257000,"updatedBy":"","versionStamp":0,"delInd":"0"},{"name":"一年级","classes":[{"name":"一班","alias":"","schoolId":1,"gradeId":5,"departmentId":0,"secondHeadmasterId":0,"showType":"0","headmasterId":0,"id":2,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588434000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}],"level":1,"alias":"","learningSectionId":1,"schoolId":1,"showType":"0","id":5,"size":10,"total":0,"current":1,"createdBy":"1","createdDateTime":1610588408000,"updatedDateTime":1610588392000,"updatedBy":"","versionStamp":0,"delInd":"0"}]
             * showType : 0
             * id : 1
             * size : 10
             * total : 0
             * current : 1
             * createdBy : 1
             * createdDateTime : 1610531994000
             * updatedDateTime : 1610588465004
             * updatedBy : 
             * versionStamp : 0
             * delInd : 0
             */

            public String name;
            public String alias;
            public int schoolId;
            public String showType;
            public int id;
            public int size;
            public int total;
            public int current;
            public String createdBy;
            public long createdDateTime;
            public long updatedDateTime;
            public String updatedBy;
            public int versionStamp;
            public String delInd;
            public List<GradesBean> grades;

           

            public static class GradesBean {
                /**
                 * name : 一年级
                 * classes : []
                 * level : 1
                 * alias : 
                 * learningSectionId : 1
                 * schoolId : 1
                 * showType : 0
                 * id : 1
                 * size : 10
                 * total : 0
                 * current : 1
                 * createdBy : 1
                 * createdDateTime : 1610546029000
                 * updatedDateTime : 1610546013000
                 * updatedBy : 
                 * versionStamp : 0
                 * delInd : 0
                 */

                public String name;
                public int level;
                public String alias;
                public int learningSectionId;
                public int schoolId;
                public String showType;
                public int id;
                public int size;
                public int total;
                public int current;
                public String createdBy;
                public long createdDateTime;
                public long updatedDateTime;
                public String updatedBy;
                public int versionStamp;
                public String delInd;
                public List<?> classes;

              
            }
        }
    }
}
