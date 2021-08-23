package com.yyide.chatim.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/14 9:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/14 9:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class ActivateRsp {

    private int code;
    private boolean success;
    private String msg;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String activateCode;
        //激活状态（1：已启用，2：禁用）
        private String activateState;
        //绑定状态（0：未绑定，1：已绑定）
        private String bangingState;
    }
}
