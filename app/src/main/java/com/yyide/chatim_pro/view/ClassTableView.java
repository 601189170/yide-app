package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.SelectTableClassesRsp;
import com.yyide.chatim_pro.model.sitetable.SiteTableRsp;
import com.yyide.chatim_pro.model.table.ClassInfoBean;

import java.util.List;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface ClassTableView extends BaseView {

    void listAllBySchoolId(List<ClassInfoBean> rsp);

    void listAllBySchoolIdFail(String msg);


    void listTimeDataByApp(SiteTableRsp rsp);

    void listTimeDataByAppFail(String rsp);

    void selectTableClassListSuccess(SelectTableClassesRsp model);
    void selectTableClassListFail(String msg);

}
