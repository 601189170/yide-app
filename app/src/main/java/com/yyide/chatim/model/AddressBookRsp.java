package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/6/4 16:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/4 16:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressBookRsp {
    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBean {
        private long teacherId;
        private long userId;
        private long schoolId;
        private long deptId;
        private String teacherName;
        private String deptName;
        private int level;
        private String image;
        private boolean checked;
    }
}
