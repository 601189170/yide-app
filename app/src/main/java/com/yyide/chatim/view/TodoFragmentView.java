package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.TodoRsp;

public interface TodoFragmentView extends BaseView {
    void getMyNoticePageSuccess(TodoRsp noticeHomeRsp);
    void getMyNoticePageFail(String msg);
}
