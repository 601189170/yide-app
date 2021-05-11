package com.yyide.chatim.presenter.leave;

import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.view.leave.AskForLeaveListView;
import com.yyide.chatim.view.leave.RequestLeaveView;

public class RequestLeavePresenter extends BasePresenter<RequestLeaveView> {
    public RequestLeavePresenter(RequestLeaveView view) {
        attachView(view);
    }
}
