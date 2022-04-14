package com.yyide.chatim_pro.Talble.View;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.HomeTimeTable;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.model.listTimeDataByAppRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface listTimeDataByAppView extends BaseView {


    void SelectSchByTeaid(SelectSchByTeaidRsp rsp);

    void SelectSchByTeaidFail(String msg);

    void SelectHomeTimeTbale(HomeTimeTable msg);
}
