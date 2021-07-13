package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/10 17:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 17:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentScopeRsp {

    /**
     * code : 200
     * success : true
     * msg : data
     * data : [{"id":430,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-22T08:21:49.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-22T08:21:49.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"深圳宝安西乡中学","parentId":427,"type":"1","headId":272,"headName":"范德萨","sort":0,"level":0,"list":[{"id":585,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-18T12:53:30.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-18T12:53:30.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"问问3地方","parentId":430,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[{"id":596,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-18T12:53:15.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-18T12:53:15.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"发第三方13","parentId":585,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[{"id":597,"delInd":"0","createdBy":"636","createdDateTime":"2021-03-29T13:42:01.000+0000","updatedBy":"13267182222","updatedDateTime":"2021-03-29T13:42:01.000+0000","versionStamp":0,"total":0,"size":10,"current":1,"schoolId":234,"name":"发发送到热1","parentId":596,"type":"0","headId":0,"headName":null,"sort":0,"level":0,"list":[]}]}]}]}]
     */

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBean {
        private long id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private String updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private long schoolId;
        private String name;
        private long parentId;
        private String type;
        private long headId;
        private String headName;
        private int sort;
        private int level;
        private List<ListBeanXX> list;
        private boolean checked;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ListBeanXX {
            private long id;
            private String delInd;
            private String createdBy;
            private String createdDateTime;
            private String updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private long schoolId;
            private String name;
            private long parentId;
            private String type;
            private long headId;
            private Object headName;
            private int sort;
            private int level;
            private List<ListBeanX> list;
            private boolean checked;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ListBeanX {
                private long id;
                private String delInd;
                private String createdBy;
                private String createdDateTime;
                private String updatedBy;
                private String updatedDateTime;
                private int versionStamp;
                private int total;
                private int size;
                private int current;
                private long schoolId;
                private String name;
                private long parentId;
                private String type;
                private long headId;
                private Object headName;
                private int sort;
                private int level;
                private List<ListBean> list;
                private boolean checked;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class ListBean {
                    private long id;
                    private String delInd;
                    private String createdBy;
                    private String createdDateTime;
                    private String updatedBy;
                    private String updatedDateTime;
                    private int versionStamp;
                    private int total;
                    private int size;
                    private int current;
                    private long schoolId;
                    private String name;
                    private long parentId;
                    private String type;
                    private long headId;
                    private Object headName;
                    private int sort;
                    private int level;
                    private List<?> list;
                    private boolean checked;
                }
            }
        }
    }
}
