package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.ResultBean;

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
