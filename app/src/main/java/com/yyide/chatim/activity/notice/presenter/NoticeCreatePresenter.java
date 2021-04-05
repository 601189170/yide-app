package com.yyide.chatim.activity.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeCreateView;

public class NoticeCreatePresenter extends BasePresenter<NoticeCreateView> {
    public NoticeCreatePresenter(NoticeCreateView view) {
        attachView(view);
    }
}
