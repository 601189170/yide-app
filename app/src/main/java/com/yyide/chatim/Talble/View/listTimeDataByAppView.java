package com.yyide.chatim.Talble.View;



import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface listTimeDataByAppView extends BaseView {





    void SelectSchByTeaid(SelectSchByTeaidRsp rsp);

    void SelectSchByTeaidFail(String msg);
}
