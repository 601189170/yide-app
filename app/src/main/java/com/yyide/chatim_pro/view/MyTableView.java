package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.model.listAllBySchoolIdRsp;
import com.yyide.chatim_pro.model.table.MyTableBean;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface MyTableView extends BaseView {

    void SelectSchByTeaid(MyTableBean rsp);

    void SelectSchByTeaidFail(String msg);



}
