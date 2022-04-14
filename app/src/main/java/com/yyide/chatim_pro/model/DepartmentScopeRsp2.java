package com.yyide.chatim_pro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/4/10 17:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 17:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */


public class DepartmentScopeRsp2 {

    private int code;
    private boolean success;
    private String msg;
    private List<DataBean> data;

    public DepartmentScopeRsp2() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private long id;
        private int level;
        private String type;
        private String name;
        private String showName;
        private long parentId;
        private long schoolId;
        private String parentName;
        private long headId;
        private String headName;
        private int sort;
        private List<DataBean> list;
        private int peopleNum;
        private Object schoolLogo;
        private String isExitInd;
        private int groupType;

        private boolean checked;
        private boolean unfold;
        //部门成员列表
        private List<AddressBookRsp.DataBean> deptMemberList = new ArrayList<>();

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public long getParentId() {
            return parentId;
        }

        public void setParentId(long parentId) {
            this.parentId = parentId;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public long getHeadId() {
            return headId;
        }

        public void setHeadId(long headId) {
            this.headId = headId;
        }

        public String getHeadName() {
            return headName;
        }

        public void setHeadName(String headName) {
            this.headName = headName;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        public Object getSchoolLogo() {
            return schoolLogo;
        }

        public void setSchoolLogo(Object schoolLogo) {
            this.schoolLogo = schoolLogo;
        }

        public String getIsExitInd() {
            return isExitInd;
        }

        public void setIsExitInd(String isExitInd) {
            this.isExitInd = isExitInd;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
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

        public List<AddressBookRsp.DataBean> getDeptMemberList() {
            return deptMemberList;
        }

        public void setDeptMemberList(List<AddressBookRsp.DataBean> deptMemberList) {
            this.deptMemberList = deptMemberList;
        }

        public DataBean() {
        }
    }
}
