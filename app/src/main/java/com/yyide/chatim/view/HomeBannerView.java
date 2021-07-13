package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ClassesPhotoBannerRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HomeBannerView extends BaseView {
    void getClassBannerListSuccess(ClassesPhotoBannerRsp model);

    void getClassBannerListFail(String msg);
}
