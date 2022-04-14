package com.yyide.chatim_pro.view.scan;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.BrandSearchRsp;

public interface ConfirmLoginView extends BaseView {

    void getClassBrandSuccess(BrandSearchRsp brandSearchRsp);
    void getClassBrandFail(String msg);

    void loginSuccess(BaseRsp baseRsp);
    void loginFail(String msg);
}
