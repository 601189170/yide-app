package com.yyide.chatim.activity.notice.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.activity.notice.view.NoticeTemplateView;
import com.yyide.chatim.model.TemplateTypeRsp;
import com.yyide.chatim.net.ApiCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class NoticeTemplatePresenter extends BasePresenter<NoticeTemplateView> {
    public NoticeTemplatePresenter(NoticeTemplateView view) {
        attachView(view);
    }

    public void getTemplateType(){
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        map.put("current",1);
        map.put("size",20);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getTemplateTypePage(body), new ApiCallback<TemplateTypeRsp>() {
            @Override
            public void onSuccess(TemplateTypeRsp model) {
                mvpView.templateType(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.templateTypeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });

    }
}
