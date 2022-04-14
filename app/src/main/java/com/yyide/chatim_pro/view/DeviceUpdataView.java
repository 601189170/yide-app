package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.DeviceUpdateRsp;
import com.yyide.chatim_pro.model.GetStuasRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface DeviceUpdataView extends BaseView {

    void getData(DeviceUpdateRsp rsp);

    void getDataFail(String msg);


}
