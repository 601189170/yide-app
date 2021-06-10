package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/19 17:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/19 17:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityScopeRsp {


    private int code;
    private boolean success;
    private String msg;
    private List<ListBeanXX> data;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListBeanXX {
        private long id;
        private String delInd;
        private String createdBy;
        private String createdDateTime;
        private Object updatedBy;
        private String updatedDateTime;
        private int versionStamp;
        private int total;
        private int size;
        private int current;
        private int schoolId;
        private String name;
        private String showName;
        private Object parentId;
        private String parentName;
        private String type;
        private int sort;
        private int level;
        private Object headmasterId;
        private Object secondHeadmasterId;
        private int nextLevelType;
        private List<ListBeanX> list;
        private String isExitInd;
        private Object headmaster;
        private Object secondHeadmaster;

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
            private int schoolId;
            private String name;
            private String showName;
            private int parentId;
            private String parentName;
            private String type;
            private int sort;
            private int level;
            private Object headmasterId;
            private Object secondHeadmasterId;
            private int nextLevelType;
            private List<ListBean> list;
            private String isExitInd;
            private Object headmaster;
            private Object secondHeadmaster;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ListBean {
                private long id;
                private String delInd;
                private String createdBy;
                private String createdDateTime;
                private Object updatedBy;
                private String updatedDateTime;
                private int versionStamp;
                private int total;
                private int size;
                private int current;
                private int schoolId;
                private String name;
                private String showName;
                private int parentId;
                private String parentName;
                private String type;
                private int sort;
                private int level;
                private Object headmasterId;
                private Object secondHeadmasterId;
                private int nextLevelType;
                private List<?> list;
                private String isExitInd;
                private Object headmaster;
                private Object secondHeadmaster;
            }
        }
    }
}
