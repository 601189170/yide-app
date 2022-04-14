package com.yyide.chatim_pro.view;

import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.NewAppRspJ;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/8 13:37
 * @Description : 文件描述
 */
public interface NewAppView extends BaseView {
    void getMyAppListSuccess(NewAppRspJ model);

    void getNewMyAppFail(String msg);

    void getNewAppListSuccess(NewAppRspJ.AppsDTO model);

    void getNewAppListFail(String msg);
}
