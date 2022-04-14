package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.MyAppListRsp;
import com.yyide.chatim_pro.model.ResultBean;

/**
 * 作者：Rance on 2021年4月5日
 */
public interface AppManagerView extends BaseView {
    void getMyAppListSuccess(MyAppListRsp model);

    void getMyAppFail(String msg);

//    void getAppListSuccess(AppItemBean model);
//
//    void getAppListFail(String msg);

    void deleteSuccess(ResultBean model);

    void deleteFail(String msg);
}
