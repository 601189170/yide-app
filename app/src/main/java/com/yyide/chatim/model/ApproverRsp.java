package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 获取流程审批人和抄送人实体
 * @Author: liu tao
 * @CreateDate: 2021/5/20 10:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/20 10:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class ApproverRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private PeopleFormBean peopleForm;
        private List<ListBean> list;

        @NoArgsConstructor
        @Data
        public static class PeopleFormBean {
            private int userId;
            private String name;
            private String image;
        }

        @NoArgsConstructor
        @Data
        public static class ListBean {
            private long userId;
            private String name;
            private String image;
        }
    }
}
