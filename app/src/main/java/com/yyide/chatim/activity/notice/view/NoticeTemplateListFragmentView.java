package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.TemplateListRsp;

public interface NoticeTemplateListFragmentView extends BaseView {
    void noticeTemplateList(TemplateListRsp templateListRsp);
    void noticeTemplateListFail(String msg);
}
