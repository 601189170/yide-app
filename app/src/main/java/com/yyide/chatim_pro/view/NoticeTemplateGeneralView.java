package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticePersonnelBean;
import com.yyide.chatim_pro.model.NoticeReleaseTemplateBean;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeTemplateGeneralView extends BaseView {
    void getTemplateDetail(NoticeReleaseTemplateBean model);
    void pushTemplateSuccess(ResultBean model);
    void getTemplateDetailFail(String msg);
}
