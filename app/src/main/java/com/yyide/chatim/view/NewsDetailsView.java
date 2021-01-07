package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.NewsDetailsEntity;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface NewsDetailsView extends BaseView {

    void getNewsDetailsSuccess(NewsDetailsEntity newsEntity);

    void getNewsDetailsFail(String msg);

}
