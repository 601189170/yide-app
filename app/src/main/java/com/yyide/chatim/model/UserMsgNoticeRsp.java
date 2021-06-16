package com.yyide.chatim.model;

import android.net.sip.SipErrorCode;

import java.io.Serializable;
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
public class UserMsgNoticeRsp implements Serializable{

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable {
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;
        private List<?> orders;
        private boolean optimizeCountSql;
        private boolean hitCount;
        private long countId;
        private Object maxLimit;
        private boolean searchCount;
        private int pages;

        @NoArgsConstructor
        @Data
        public static class RecordsBean implements Serializable{
            private String title;
            private String content;
            private long userId;
            private long id;
            private long signId;
            private Object remakeData;
            private String firstData;
            private long callId;
            private String attributeType;
            private String isText;//是否纯文本 1 是 2不是
            private String status;//读取状态 1 已读/同意 0未读 2拒绝 3 撤消
            private String sendTime;
        }
    }
}
