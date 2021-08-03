package com.yyide.chatim.model;

import java.io.Serializable;

public class AttendanceSchoolGradeRsp implements Serializable {
    public int code;
    public boolean success;
    public String msg;
    public AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean data;
}
