package com.yyide.chatim.activity.notice.presenter;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;

public class NoticeAnnouncementPresenter extends BasePresenter<NoticeScopeView> {
    public NoticeAnnouncementPresenter(NoticeScopeView view) {
        attachView(view);
    }
}
