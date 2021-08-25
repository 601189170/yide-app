package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BookRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface BookView extends BaseView {

    void getBookList(BookRsp model);

    void getBookListFail(String msg);
}
