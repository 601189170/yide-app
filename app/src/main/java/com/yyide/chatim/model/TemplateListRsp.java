package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/6 16:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/6 16:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class TemplateListRsp {
    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @Data
    @NoArgsConstructor
    public static class DataBean {
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;

        @Data
        @NoArgsConstructor
        public static class RecordsBean {
            private long id;
            private String delInd;
            private Object createdBy;
            private String createdDateTime;
            private Object updatedBy;
            private String updatedDateTime;
            private int versionStamp;
            private int total;
            private int size;
            private int current;
            private String name;
            private Object type;
            private String status;
            private String content;
            private long tempId;
            private Object messages;
        }
    }
}
