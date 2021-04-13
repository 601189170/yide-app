package com.yyide.chatim.activity.notice.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.activity.notice.view.NoticeTemplateListFragmentView;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.TemplateListRsp;
import com.yyide.chatim.net.ApiCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class NoticeTemplateListFragmentPresenter extends BasePresenter<NoticeTemplateListFragmentView> {
    public NoticeTemplateListFragmentPresenter(NoticeTemplateListFragmentView view) {
        attachView(view);
    }

    public void noticeTemplateList(String name,long tempId){
        Log.e("NoticeTemplateListFragm", "noticeTemplateList: "+name +","+tempId);
        mvpView.showLoading();
        Map map = new HashMap<String,Object>();
        map.put("title",null);
        map.put("id",tempId);
        String toJSONString = JSON.toJSONString(map);
        Log.e("NoticeTemplateListFragm", "noticeTemplateList: "+toJSONString );
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),toJSONString);
        addSubscription(dingApiStores.selectMessagePage(body),new ApiCallback<TemplateListRsp>(){
            @Override
            public void onSuccess(TemplateListRsp model) {
                mvpView.noticeTemplateList(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.noticeTemplateListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
