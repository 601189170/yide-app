package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.model.NoticeListRsp;

public interface NoticeDetailView extends BaseView {

    void noticeDetail(NoticeDetailRsp noticeDetailRsp);
    void noticeDetailFail(String msg);
}
