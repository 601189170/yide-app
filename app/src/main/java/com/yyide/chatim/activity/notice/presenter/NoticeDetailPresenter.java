package com.yyide.chatim.activity.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeDetailView;

public class NoticeDetailPresenter extends BasePresenter<NoticeDetailView> {
    public NoticeDetailPresenter(NoticeDetailView view) {
        attachView(view);
    }
}
