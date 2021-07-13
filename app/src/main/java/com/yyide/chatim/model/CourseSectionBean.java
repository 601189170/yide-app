package com.yyide.chatim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/11 19:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 19:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseSectionBean {
    private String section;
    private String name;
    private boolean checked;
}
