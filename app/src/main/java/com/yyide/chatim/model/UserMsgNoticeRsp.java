package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/24 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/24 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class UserMsgNoticeRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private List<?> orders;
        private boolean optimizeCountSql;
        private boolean hitCount;
        private Object countId;
        private Object maxLimit;
        private boolean searchCount;
        private int pages;

        @NoArgsConstructor
        @Data
        public static class RecordsBean {
            private String title;
            private String content;
            private int userId;
            private long id;
            private long signId;
            private Object remakeData;
            private String firstData;
            private long callId;
            private String attributeType;
            private String isText;
            private String status;//读取状态 1 已读/同意 0未读 2拒绝 3 撤消
            private String sendTime;
        }
    }
}
