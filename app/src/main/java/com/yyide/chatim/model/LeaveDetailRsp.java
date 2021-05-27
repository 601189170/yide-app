package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/19 18:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/19 18:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class LeaveDetailRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String name;
        private String deptOrClassName;
        private String type;
        private String startTime;
        private String endTime;
        private String reason;
        private String initiateTime;
        private String approverId;
        private String approverName;
        private String approvalTime;
        private String approvalResult;
        private int id;
        private List<ListBean> list;
        private Object leaveCourseFormList;
        private Object ccbid;

        @NoArgsConstructor
        @Data
        public static class ListBean {
            private int userId;
            private String name;
        }
    }
}
