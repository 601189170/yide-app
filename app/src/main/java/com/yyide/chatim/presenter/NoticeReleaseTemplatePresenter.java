package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeReleaseTemplateBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoticeReleasedTemplateView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class NoticeReleaseTemplatePresenter extends BasePresenter<NoticeReleasedTemplateView> {
    public NoticeReleaseTemplatePresenter(NoticeReleasedTemplateView view) {
        attachView(view);
    }

    public void templateNoticeList(int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("current", current);
        map.put("size", size);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mvpView.showLoading();
        addSubscription(dingApiStores.templateNoticeList(body), new ApiCallback<NoticeReleaseTemplateBean>() {
            @Override
            public void onSuccess(NoticeReleaseTemplateBean model) {
                mvpView.getNoticeReleasedList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getNoticeReleasedFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
