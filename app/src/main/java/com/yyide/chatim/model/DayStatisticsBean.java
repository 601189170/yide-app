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
public class DayStatisticsBean {
    private String time;//考勤时间
    private int rate;//考勤率
    private int due;//应到
    private int normal;//正常
    private int absence;//缺勤
    private int ask_for_leave;//请假
    private int late;//迟到
}
