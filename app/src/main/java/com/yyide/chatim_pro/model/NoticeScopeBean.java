package com.yyide.chatim_pro.model;

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

public class NoticeScopeBean {
    private long id;
    private String name;
    private boolean checked;
    private boolean unfold;
    private String type;//学段【中学】 表示 0学段 1年级 2班级 【大学 】0 系 1 班级
    private boolean hasNext = true;
    private List<NoticeScopeBean> list;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<NoticeScopeBean> getList() {
        return list;
    }

    public void setList(List<NoticeScopeBean> list) {
        this.list = list;
    }

    public NoticeScopeBean(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NoticeScopeBean(long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public NoticeScopeBean(long id, String name, String type, boolean hasNext) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hasNext = hasNext;
    }
}
