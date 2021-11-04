package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AttendanceRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface SchoolGradeView extends BaseView {

    void getGradeListSuccess(AttendanceRsp model);

    void getFail(String msg);
}
