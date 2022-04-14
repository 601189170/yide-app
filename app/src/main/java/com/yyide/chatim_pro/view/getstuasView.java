package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.DeviceUpdateRsp;
import com.yyide.chatim_pro.model.GetStuasRsp;
import com.yyide.chatim_pro.model.NewsEntity;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface getstuasView extends BaseView {

    void getData(GetStuasRsp rsp);

    void getDataFail(String msg);


    void getData2(DeviceUpdateRsp rsp);
    void getDataFail2(String msg);

}
