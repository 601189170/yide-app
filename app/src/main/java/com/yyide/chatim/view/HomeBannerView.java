package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.HomeBannerRsp;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface HomeBannerView extends BaseView {
    void getClassBannerListSuccess(HomeBannerRsp model);

    void getClassBannerListFail(String msg);
}
