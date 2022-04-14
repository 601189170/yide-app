package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.ClassesPhotoBannerRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HomeBannerView extends BaseView {
    void getClassBannerListSuccess(ClassesPhotoBannerRsp model);

    void getClassBannerListFail(String msg);
}
