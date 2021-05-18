package com.yyide.chatim.view;



import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.selectListByAppRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface NoteBookView extends BaseView {


    void listByApp(ListByAppRsp rsp);

    void selectListByApp(ListByAppRsp rsp);

    void universityListByApp(ListByAppRsp rsp);

    void listByAppDataFail(String rsp);

    void selectListByAppRsp(selectListByAppRsp rsp);

    void selectListByAppRspFail(String rsp);



}
