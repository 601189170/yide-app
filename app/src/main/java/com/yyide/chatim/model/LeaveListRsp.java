package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 请假列表数据
 * @Author: liu tao
 * @CreateDate: 2021/5/19 16:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/19 16:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class LeaveListRsp {

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
        private long countId;
        private Object maxLimit;
        private boolean searchCount;
        private int pages;

        @NoArgsConstructor
        @Data
        public static class RecordsBean {
            private String name;
            private long id;
            private String type;
            private String approvalResult;
            private String initiateTime;
        }
    }
}
