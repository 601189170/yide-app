package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.StudentScopeRsp;

public interface NoticeScopeView extends BaseView {
    void getStudentScopeSuccess(StudentScopeRsp studentScopeRsp);
    void getStudentScopeFail(String msg);

    void getDepartmentListSuccess(DepartmentScopeRsp departmentScopeRsp);
    void getDepartmentListFail(String msg);
}
