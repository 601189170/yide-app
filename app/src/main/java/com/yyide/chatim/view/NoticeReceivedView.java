package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeReceivedView extends BaseView {
    void getMyReceivedList(NoticeItemBean model);

    void getMyReceivedFail(String msg);
}
