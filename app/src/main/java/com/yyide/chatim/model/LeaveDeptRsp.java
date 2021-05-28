package com.yyide.chatim.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/20 10:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/20 10:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class LeaveDeptRsp {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String deptName;
        private long deptId;
        private int deptHeadId;
        private int schoolId;
        private String type;
        private int isDefault;
    }
}
