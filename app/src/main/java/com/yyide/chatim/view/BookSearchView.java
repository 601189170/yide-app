package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BookRsp;
import com.yyide.chatim.model.BookSearchRsp;
import com.yyide.chatim.model.BookSearchRsp2;
import com.yyide.chatim.model.UserInfoRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface BookSearchView extends BaseView {
    void selectUserListSuccess(BookSearchRsp userInfoRsp);
    void selectUserListFail(String msg);
}
