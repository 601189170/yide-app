package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ConfirmDetailRsp;
import com.yyide.chatim_pro.model.TemplateListRsp;

public interface NoticeConfirmListFragmentView extends BaseView {
    void noticeConfirmList(ConfirmDetailRsp confirmDetailRsp);
    void noticeConfirmListFail(String msg);
}
