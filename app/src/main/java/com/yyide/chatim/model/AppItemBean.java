package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 86159 on 2021/4/1.
 */
@NoArgsConstructor
@Data
public class AppItemBean {
    /**
     * Copyright 2021 json.cn
     */
    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * records : [{"id":13,"delInd":"0","name":"应用组17","schoolId":null,"type":null,"list":[{"id":6,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-07T13:14:23.248+0000","updatedBy":null,"updatedDateTime":"2021-04-07T13:14:23.248+0000","versionStamp":0,"total":0,"size":10,"current":1,"applicationId":null,"name":"应用13","groupId":null,"schoolId":null,"schoolApplicationGroupId":null,"sort":null,"type":null,"img":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg","path":"str","owner":null,"num":null,"userId":null,"isAdd":null}],"applicationType":null,"owner":null}]
         * total : 2
         * size : 10
         * current : 1
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;
        private List<RecordsBean> records;

        @NoArgsConstructor
        @Data
        public static class RecordsBean {
            /**
             * id : 13
             * delInd : 0
             * name : 应用组17
             * schoolId : null
             * type : null
             * list : [{"id":6,"delInd":"0","createdBy":null,"createdDateTime":"2021-04-07T13:14:23.248+0000","updatedBy":null,"updatedDateTime":"2021-04-07T13:14:23.248+0000","versionStamp":0,"total":0,"size":10,"current":1,"applicationId":null,"name":"应用13","groupId":null,"schoolId":null,"schoolApplicationGroupId":null,"sort":null,"type":null,"img":"http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg","path":"str","owner":null,"num":null,"userId":null,"isAdd":null}]
             * applicationType : null
             * owner : null
             */

            private int id;
            private String delInd;
            private String name;
            private String schoolId;
            private String type;
            private String applicationType;
            private String owner;
            private List<DataBean.RecordsBean.ListBean> list;

            @NoArgsConstructor
            @Data
            public static class ListBean {
                /**
                 * id : 6
                 * delInd : 0
                 * createdBy : null
                 * createdDateTime : 2021-04-07T13:14:23.248+0000
                 * updatedBy : null
                 * updatedDateTime : 2021-04-07T13:14:23.248+0000
                 * versionStamp : 0
                 * total : 0
                 * size : 10
                 * current : 1
                 * applicationId : null
                 * name : 应用13
                 * groupId : null
                 * schoolId : null
                 * schoolApplicationGroupId : null
                 * sort : null
                 * type : null
                 * img : http://cloud-yide.oss-cn-shenzhen.aliyuncs.com/application/428f9fbdcc2a4d659297f7690fb24ef6-1615542537(1).jpg
                 * path : str
                 * owner : null
                 * num : null
                 * userId : null
                 * isAdd : null
                 */

                private int id;
                private String delInd;
                private String createdBy;
                private String createdDateTime;
                private String updatedBy;
                private String updatedDateTime;
                private int versionStamp;
                private int total;
                private int size;
                private int current;
                private String applicationId;
                private String name;
                private String groupId;
                private String schoolId;
                private String schoolApplicationGroupId;
                private String sort;
                private String type;
                private String img;
                private String path;
                private String owner;
                private String num;
                private String userId;
                private String isAdd;
            }
        }
    }
}
