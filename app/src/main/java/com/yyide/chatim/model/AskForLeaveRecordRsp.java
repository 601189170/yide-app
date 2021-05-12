package com.yyide.chatim.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/11 14:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 14:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@NoArgsConstructor
@Data
public class AskForLeaveRecordRsp {
    private int id;
    private int status;//1,审批中，2，已撤销，3，审批通过 4，审批拒绝
    private String title;
    private String date;
}
