package com.yyide.chatim.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.UserMsgNoticeRsp;

public interface UserNoticeView extends BaseView {

    void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp);

    void updateNoticeSuccess(ResultBean resultBean);

    void messageNumberSuccess(int number, String date, String msg);

    void messageNumberFail(String msg);

    void getUserNoticePageFail(String msg);

}
