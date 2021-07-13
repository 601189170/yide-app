package com.yyide.chatim.model;

import java.util.List;

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
public class WeekStatisticsBean {
    private int time;//次数
    private String name;//人员姓名
    private boolean checked;
    private List<DataBean> dataBeanList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DataBean{
        private String title;
        private String time;
        private String status;
    }
}
