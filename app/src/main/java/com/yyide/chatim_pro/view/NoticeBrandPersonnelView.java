package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NoticeBrandBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeBrandPersonnelView extends BaseView {
    void getBrandPersonnelList(NoticeBrandBean model);
    void getSitePersonnelList(NoticeBrandBean model);
    void getBrandPersonnelFail(String msg);
}
