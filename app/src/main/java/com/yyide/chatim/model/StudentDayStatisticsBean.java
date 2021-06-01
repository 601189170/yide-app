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
public class StudentDayStatisticsBean {
    private String time;//考勤时间
    private String eventName;
    //考勤状态 1 正常，2迟到，3早退，4，未签到，5请假
    private int eventStatus;
    //打卡时间
    private String eventTime;
}
