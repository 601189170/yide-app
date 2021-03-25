package com.yyide.chatim.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.notice.view.NoticeDetailView;

public class NoticeDetailPresenter extends BasePresenter<NoticeDetailView> {
    public NoticeDetailPresenter(NoticeDetailView view) {
        attachView(view);
    }
}
