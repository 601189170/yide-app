package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.NoticeDetailRsp;
import com.yyide.chatim_pro.model.NoticeListRsp;

public interface NoticeDetailView extends BaseView {

    void noticeDetail(NoticeDetailRsp noticeDetailRsp);
    void noticeDetailFail(String msg);

    void updateMyNotice(BaseRsp baseRsp);
    void updateFail(String msg);
}
