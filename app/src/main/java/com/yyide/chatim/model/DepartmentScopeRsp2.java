package com.yyide.chatim.model;

import java.util.List;

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

@NoArgsConstructor
@Data
public class DepartmentScopeRsp2 {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private long id;
        private int level;
        private String type;
        private String name;
        private String showName;
        private long parentId;
        private long schoolId;
        private String parentName;
        private long headId;
        private String headName;
        private int sort;
        private List<ListBeanXX> list;
        private int peopleNum;
        private Object schoolLogo;
        private String isExitInd;
        private int groupType;

        @NoArgsConstructor
        @Data
        public static class ListBeanXX {
            private long id;
            private int level;
            private String type;
            private String name;
            private String showName;
            private long parentId;
            private long schoolId;
            private String parentName;
            private long headId;
            private String headName;
            private int sort;
            private List<ListBeanX> list;
            private int peopleNum;
            private Object schoolLogo;
            private String isExitInd;
            private int groupType;

            @NoArgsConstructor
            @Data
            public static class ListBeanX {
                private long id;
                private int level;
                private String type;
                private String name;
                private String showName;
                private long parentId;
                private long schoolId;
                private String parentName;
                private long headId;
                private String headName;
                private int sort;
                private List<ListBean> list;
                private int peopleNum;
                private Object schoolLogo;
                private String isExitInd;
                private int groupType;

                @NoArgsConstructor
                @Data
                public static class ListBean {
                    private long id;
                    private int level;
                    private String type;
                    private String name;
                    private String showName;
                    private long parentId;
                    private long schoolId;
                    private String parentName;
                    private long headId;
                    private String headName;
                    private int sort;
                    private List<?> list;
                    private int peopleNum;
                    private Object schoolLogo;
                    private String isExitInd;
                    private int groupType;
                }
            }
        }
    }
}
