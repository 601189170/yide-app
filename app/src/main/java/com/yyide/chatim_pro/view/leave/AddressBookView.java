package com.yyide.chatim_pro.view.leave;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AddressBookRsp;
import com.yyide.chatim_pro.model.DepartmentScopeRsp;
import com.yyide.chatim_pro.model.DepartmentScopeRsp2;
import com.yyide.chatim_pro.model.StudentScopeRsp;
import com.yyide.chatim_pro.model.UniversityScopeRsp;

public interface AddressBookView extends BaseView {
    void getDepartmentListSuccess2(DepartmentScopeRsp2 departmentScopeRsp2);
    void getDepartmentListFail2(String msg);

    void getDeptMemberListSuccess(AddressBookRsp addressBookRsp);
    void getDeptMemberListFail(String msg);
}
