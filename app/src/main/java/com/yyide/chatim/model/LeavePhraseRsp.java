package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 请假短语实体
 * @Author: liu tao
 * @CreateDate: 2021/5/25 18:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/25 18:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class LeavePhraseRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String tag;
        private long id;
    }
}
