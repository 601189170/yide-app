package com.yyide.chatim.view;

import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NewAppRspJ;

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
