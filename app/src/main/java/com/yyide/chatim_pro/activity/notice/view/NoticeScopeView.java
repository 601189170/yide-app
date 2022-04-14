package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.DepartmentScopeRsp;
import com.yyide.chatim_pro.model.StudentScopeRsp;
import com.yyide.chatim_pro.model.UniversityScopeRsp;

public interface NoticeScopeView extends BaseView {
    void getStudentScopeSuccess(StudentScopeRsp studentScopeRsp);
    void getStudentScopeFail(String msg);

    void getDepartmentListSuccess(DepartmentScopeRsp departmentScopeRsp);
    void getDepartmentListFail(String msg);

    void getUniversityListSuccess(UniversityScopeRsp universityScopeRsp);
    void getUniversityListFail(String msg);
}
