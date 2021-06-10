package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/10 14:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 14:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScopeRsp {

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
        private String name;
        private Object alias;
        private Object showType;
        private String showName;
        private int schoolId;
        private List<ListBeanX> list;
        private String type;
        private String isExitInd;
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
            private String name;
            private String alias;
            private String showType;
            private String showName;
            private int level;
            private int schoolId;
            private int learningSectionId;
            private List<ListBean> list;
            private String type;
            private String isExitInd;
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
                private String name;
                private int schoolId;
                private String alias;
                private String showType;
                private int gradeId;
                private String showName;
                private int headmasterId;
                private Object secondHeadmasterId;
                private String type;
                private String headmaster;
                private Object secondHeadmaster;
                private Object classesIds;
                private String isExitInd;
            }
        }
    }
}
