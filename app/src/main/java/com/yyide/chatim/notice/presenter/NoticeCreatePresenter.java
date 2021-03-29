package com.yyide.chatim.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.notice.view.NoticeCreateView;
import com.yyide.chatim.notice.view.NoticeDetailView;

public class NoticeCreatePresenter extends BasePresenter<NoticeCreateView> {
    public NoticeCreatePresenter(NoticeCreateView view) {
        attachView(view);
    }
}
