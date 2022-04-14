package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BookRsp;
import com.yyide.chatim_pro.model.BookSearchRsp;
import com.yyide.chatim_pro.model.BookSearchRsp2;
import com.yyide.chatim_pro.model.UserInfoRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface BookSearchView extends BaseView {
    void selectUserListSuccess(BookSearchRsp userInfoRsp);
    void selectUserListFail(String msg);
}
