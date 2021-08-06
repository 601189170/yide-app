package com.yyide.chatim.view.scan;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.BrandSearchRsp;

public interface ConfirmLoginView extends BaseView {

    void getClassBrandSuccess(BrandSearchRsp brandSearchRsp);
    void getClassBrandFail(String msg);

    void loginSuccess(BaseRsp baseRsp);
    void loginFail(String msg);
}
