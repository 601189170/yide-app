package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface ClassTableView extends BaseView {

    void listAllBySchoolId(listAllBySchoolIdRsp rsp);

    void listAllBySchoolIdFail(String msg);



}
