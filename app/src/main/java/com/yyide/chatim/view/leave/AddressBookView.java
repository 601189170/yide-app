package com.yyide.chatim.view.leave;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.DepartmentScopeRsp2;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.model.UniversityScopeRsp;

public interface AddressBookView extends BaseView {
    void getDepartmentListSuccess2(DepartmentScopeRsp2 departmentScopeRsp2);
    void getDepartmentListFail2(String msg);

    void getDeptMemberListSuccess(AddressBookRsp addressBookRsp);
    void getDeptMemberListFail(String msg);
}
