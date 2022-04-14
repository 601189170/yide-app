package com.yyide.chatim_pro.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.UserMsgNoticeRsp;

public interface UserNoticeView extends BaseView {

    void getUserNoticePageSuccess(UserMsgNoticeRsp userNoticeRsp);

    void updateNoticeSuccess(ResultBean resultBean);

    void messageNumberSuccess(int number, String date, String msg);

    void messageNumberFail(String msg);

    void getUserNoticePageFail(String msg);

}
