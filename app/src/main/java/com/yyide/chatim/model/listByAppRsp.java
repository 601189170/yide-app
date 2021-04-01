package com.yyide.chatim.model;

import java.util.List;

public class listByAppRsp {


    /**
     * code : 200
     * success : true
     * msg : 查询成功
     * data : [{"id":173,"delInd":"0","createdBy":"1","createdDateTime":"2021-02-20T05:36:08.000+0000","updatedBy":null,"updatedDateTime":"2021-02-20T05:36:08.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":158,"name":"发的发生","parentId":null,"parentName":"发的发生","type":"0","headId":null,"headName":null,"sort":0,"level":0,"list":[],"peopleNum":0,"isExitInd":"Y"}]
     */

    public int code;
    public boolean success;
    public String msg;
    public List<DataBean> data;


    public static class DataBean {
        /**
         * id : 173
         * delInd : 0
         * createdBy : 1
         * createdDateTime : 2021-02-20T05:36:08.000+0000
         * updatedBy : null
         * updatedDateTime : 2021-02-20T05:36:08.000+0000
         * versionStamp : 0
         * total : 0
         * size : 10
         * current : 1
         * schoolId : 158
         * name : 发的发生
         * parentId : null
         * parentName : 发的发生
         * type : 0
         * headId : null
         * headName : null
         * sort : 0
         * level : 0
         * list : []
         * peopleNum : 0
         * isExitInd : Y
         */

        public int id;
        public String delInd;
        public String createdBy;
        public String createdDateTime;
        public Object updatedBy;
        public String updatedDateTime;
        public int versionStamp;
        public int total;
        public int size;
        public int current;
        public int schoolId;
        public String name;
        public Object parentId;
        public String parentName;
        public String type;
        public Object headId;
        public Object headName;
        public int sort;
        public int level;
        public int peopleNum;
        public String isExitInd;
        public List<?> list;

     
    }
}
