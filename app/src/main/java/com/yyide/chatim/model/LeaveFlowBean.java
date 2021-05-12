package com.yyide.chatim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/12 14:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 14:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveFlowBean {
    private String time;
    private String date;
    private String flowTitle;
    private String flowContent;
    private boolean checked;
}
