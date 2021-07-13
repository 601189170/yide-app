package com.yyide.chatim.model;

import java.util.ArrayList;
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
public class AddressBookBean {
    private long id;
    private String name;
    private boolean checked;
    private boolean unfold;
    private int level;//学段【中学】 表示 0学段 1年级 2班级 【大学 】0 系 1 班级
    private boolean hasNext = true;
    private List<AddressBookBean> list;
    private String isExitInd;
    //部门成员列表
    private List<AddressBookRsp.DataBean> deptMemberList = new ArrayList<>();

    public AddressBookBean(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AddressBookBean(long id, String name, int level,String isExitInd) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.isExitInd = isExitInd;
    }

    public AddressBookBean(long id, String name, int level, boolean hasNext) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.hasNext = hasNext;
    }
}
