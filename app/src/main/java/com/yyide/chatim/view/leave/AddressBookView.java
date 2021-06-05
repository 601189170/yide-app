package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.model.UniversityScopeRsp;

public interface AddressBookView extends BaseView {
    void getStudentScopeSuccess(StudentScopeRsp studentScopeRsp);
    void getStudentScopeFail(String msg);

    void getDepartmentListSuccess(DepartmentScopeRsp departmentScopeRsp);
    void getDepartmentListFail(String msg);

    void getUniversityListSuccess(UniversityScopeRsp universityScopeRsp);
    void getUniversityListFail(String msg);

    void getDeptMemberListSuccess(AddressBookRsp addressBookRsp);
    void getDeptMemberListFail(String msg);
}
