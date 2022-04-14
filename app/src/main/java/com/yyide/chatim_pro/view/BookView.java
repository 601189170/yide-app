package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BookRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface BookView extends BaseView {

    void getBookList(BookRsp model);

    void getBookListFail(String msg);
}
