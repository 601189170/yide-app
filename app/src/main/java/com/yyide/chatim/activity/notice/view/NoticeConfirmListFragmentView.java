package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.model.TemplateListRsp;

public interface NoticeConfirmListFragmentView extends BaseView {
    void noticeConfirmList(ConfirmDetailRsp confirmDetailRsp);
    void noticeConfirmListFail(String msg);
}
