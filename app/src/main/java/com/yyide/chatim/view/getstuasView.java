package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.NewsEntity;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface getstuasView extends BaseView {

    void getData(GetStuasRsp rsp);

    void getDataFail(String msg);


}
