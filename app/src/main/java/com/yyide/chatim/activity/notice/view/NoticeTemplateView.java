package com.yyide.chatim.activity.notice.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.TemplateTypeRsp;

public interface NoticeTemplateView extends BaseView {

    void templateType(TemplateTypeRsp templateTypeRsp);
    void templateTypeFail(String msg);
}
