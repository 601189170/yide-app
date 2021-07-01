package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticePersonnelBean;
import com.yyide.chatim.model.NoticeReleaseTemplateBean;
import com.yyide.chatim.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeTemplateGeneralView extends BaseView {
    void getTemplateDetail(NoticeReleaseTemplateBean model);
    void pushTemplateSuccess(ResultBean model);
    void getTemplateDetailFail(String msg);
}
