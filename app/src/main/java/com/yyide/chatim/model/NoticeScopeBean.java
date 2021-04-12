package com.yyide.chatim.model;

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
    private int id;
    private String name;
    private boolean checked;
    private List<NoticeScopeBean> list;

    public NoticeScopeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public NoticeScopeBean(int id, String name, List<NoticeScopeBean> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<NoticeScopeBean> getList() {
        return list;
    }

    public void setList(List<NoticeScopeBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NoticeScopeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", checked=" + checked +
                ", list=" + list +
                '}';
    }
}
