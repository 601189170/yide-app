package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NoticeBrandBean;

/**
 * 作者：lrz on 2021年4月5日
 */
public interface NoticeBrandPersonnelView extends BaseView {
    void getBrandPersonnelList(NoticeBrandBean model);
    void getSitePersonnelList(NoticeBrandBean model);
    void getBrandPersonnelFail(String msg);
}
