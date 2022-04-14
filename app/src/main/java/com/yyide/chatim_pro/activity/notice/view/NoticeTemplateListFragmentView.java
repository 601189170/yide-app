package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeListRsp;
import com.yyide.chatim_pro.model.TemplateListRsp;

public interface NoticeTemplateListFragmentView extends BaseView {
    void noticeTemplateList(TemplateListRsp templateListRsp);
    void noticeTemplateListFail(String msg);
}
