package com.yyide.chatim_pro.activity.notice.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.TemplateTypeRsp;

public interface NoticeTemplateView extends BaseView {

    void templateType(TemplateTypeRsp templateTypeRsp);
    void templateTypeFail(String msg);
}
