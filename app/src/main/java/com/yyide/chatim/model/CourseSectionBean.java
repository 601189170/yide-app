package com.yyide.chatim.model;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/5/11 19:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 19:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class CourseSectionBean {
    private String section;
    private String name;
    private boolean checked;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public CourseSectionBean(String section, String name, boolean checked) {
        this.section = section;
        this.name = name;
        this.checked = checked;
    }

    public CourseSectionBean() {
    }
}
