package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.AttendanceRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface SchoolGradeView extends BaseView {

    void getGradeListSuccess(AttendanceRsp model);

    void getFail(String msg);
}
