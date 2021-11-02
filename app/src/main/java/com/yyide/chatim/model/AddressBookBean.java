package com.yyide.chatim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/12 11:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 11:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isUnfold() {
        return unfold;
    }

    public void setUnfold(boolean unfold) {
        this.unfold = unfold;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<AddressBookBean> getList() {
        return list;
    }

    public void setList(List<AddressBookBean> list) {
        this.list = list;
    }

    public String getIsExitInd() {
        return isExitInd;
    }

    public void setIsExitInd(String isExitInd) {
        this.isExitInd = isExitInd;
    }

    public List<AddressBookRsp.DataBean> getDeptMemberList() {
        return deptMemberList;
    }

    public void setDeptMemberList(List<AddressBookRsp.DataBean> deptMemberList) {
        this.deptMemberList = deptMemberList;
    }

    public AddressBookBean(long id, String name, boolean checked, boolean unfold, int level, boolean hasNext, List<AddressBookBean> list, String isExitInd, List<AddressBookRsp.DataBean> deptMemberList) {
        this.id = id;
        this.name = name;
        this.checked = checked;
        this.unfold = unfold;
        this.level = level;
        this.hasNext = hasNext;
        this.list = list;
        this.isExitInd = isExitInd;
        this.deptMemberList = deptMemberList;

    }

    public AddressBookBean() {
    }
}
