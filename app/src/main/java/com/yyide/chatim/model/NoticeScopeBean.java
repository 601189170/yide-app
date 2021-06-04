package com.yyide.chatim.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/12 11:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 11:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeScopeBean {
    private int id;
    private String name;
    private boolean checked;
    private boolean unfold;
    private String type;//学段【中学】 表示 0学段 1年级 2班级 【大学 】0 系 1 班级
    private boolean hasNext = true;
    private List<NoticeScopeBean> list;

    public NoticeScopeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public NoticeScopeBean(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public NoticeScopeBean(int id, String name, String type, boolean hasNext) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hasNext = hasNext;
    }
}
