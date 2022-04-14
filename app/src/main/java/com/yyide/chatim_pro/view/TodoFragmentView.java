package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.NoticeHomeRsp;
import com.yyide.chatim_pro.model.TodoRsp;

public interface TodoFragmentView extends BaseView {
    void getMyNoticePageSuccess(TodoRsp noticeHomeRsp);
    void getMyNoticePageFail(String msg);
}
