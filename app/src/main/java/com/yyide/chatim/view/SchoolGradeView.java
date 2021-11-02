package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceSchoolGradeRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface SchoolGradeView extends BaseView {

    void getGradeListSuccess(AttendanceCheckRsp model);

    void getFail(String msg);
}
