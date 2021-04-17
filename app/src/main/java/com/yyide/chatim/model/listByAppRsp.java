package com.yyide.chatim.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class listByAppRsp {


    /**
     * code : 200
     * data : [{"createdBy":"admin","createdDateTime":"2021-04-02T03:23:36.000+0000","current":1,"delInd":"0","id":1193,"isExitInd":"Y","level":0,"list":[{"id":1194,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T03:24:28.000+0000","updatedDateTime":"2021-04-02T03:24:28.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"安卓","parentId":1193,"parentName":"APP测试学校","type":"0","sort":0,"level":1,"list":[],"peopleNum":3,"isExitInd":"Y"},{"id":1195,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T03:24:28.000+0000","updatedDateTime":"2021-04-02T03:24:28.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"iOS","parentId":1193,"parentName":"APP测试学校","type":"0","sort":0,"level":1,"list":[],"peopleNum":2,"isExitInd":"Y"},{"id":1199,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T05:41:05.000+0000","updatedDateTime":"2021-04-02T05:41:05.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"学生会","parentId":1193,"parentName":"APP测试学校","type":"1","sort":0,"level":1,"list":[],"peopleNum":4,"isExitInd":"Y"}],"name":"APP测试学校","parentName":"APP测试学校","peopleNum":0,"schoolId":524,"size":10,"sort":0,"total":0,"type":"0","updatedDateTime":"2021-04-02T03:23:36.000+0000","versionStamp":0}]
     * msg : 查询成功
     * success : true
     */

    public int code;
    public String msg;
    public boolean success;
    public List<DataBean> data;


    public static class DataBean {
        /**
         * createdBy : admin
         * createdDateTime : 2021-04-02T03:23:36.000+0000
         * current : 1
         * delInd : 0
         * id : 1193
         * isExitInd : Y
         * level : 0
         * list : [{"id":1194,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T03:24:28.000+0000","updatedDateTime":"2021-04-02T03:24:28.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"安卓","parentId":1193,"parentName":"APP测试学校","type":"0","sort":0,"level":1,"list":[],"peopleNum":3,"isExitInd":"Y"},{"id":1195,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T03:24:28.000+0000","updatedDateTime":"2021-04-02T03:24:28.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"iOS","parentId":1193,"parentName":"APP测试学校","type":"0","sort":0,"level":1,"list":[],"peopleNum":2,"isExitInd":"Y"},{"id":1199,"delInd":"0","createdBy":"18166666666","createdDateTime":"2021-04-02T05:41:05.000+0000","updatedDateTime":"2021-04-02T05:41:05.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":524,"name":"学生会","parentId":1193,"parentName":"APP测试学校","type":"1","sort":0,"level":1,"list":[],"peopleNum":4,"isExitInd":"Y"}]
         * name : APP测试学校
         * parentName : APP测试学校
         * peopleNum : 0
         * schoolId : 524
         * size : 10
         * sort : 0
         * total : 0
         * type : 0
         * updatedDateTime : 2021-04-02T03:23:36.000+0000
         * versionStamp : 0
         */

        public String createdBy;
        public String createdDateTime;
        public int current;
        public String delInd;
        public int id;
        public String isExitInd;
        public int level;
        public String name;
        public String parentName;
        public int peopleNum;
        public int schoolId;
        public int size;
        public int sort;
        public int total;
        public String type;
        public String updatedDateTime;
        public int versionStamp;
        public List<ListBean> list;

        public static class ListBean implements Serializable, MultiItemEntity {
            /**
             * id : 1194.0
             * delInd : 0
             * createdBy : 18166666666
             * createdDateTime : 2021-04-02T03:24:28.000+0000
             * updatedDateTime : 2021-04-02T03:24:28.000+0000
             * versionStamp : 0.0
             * total : 0.0
             * size : 10.0
             * current : 1.0
             * schoolId : 524.0
             * name : 安卓
             * parentId : 1193.0
             * parentName : APP测试学校
             * type : 0
             * sort : 0.0
             * level : 1.0
             * list : []
             * peopleNum : 3.0
             * isExitInd : Y
             */

            public int id;
            public String delInd;
            public String createdBy;
            public String createdDateTime;
            public String updatedDateTime;
            public double versionStamp;
            public double total;
            public double size;
            public double current;
            public double schoolId;
            public String name;
            public double parentId;
            public String parentName;
            public String type;
            public double sort;
            public double level;
            public double peopleNum;
            public String isExitInd;
            public List<ListBean> list;
            public int itemType;//1部门 2学生

            @Override
            public int getItemType() {
                return itemType;
            }
        }
    }
}
