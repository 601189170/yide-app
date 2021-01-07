package com.yyide.chatim.presenter;



import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.VideoEntity;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.MainView;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        attachView(view);
    }

    public void getVideoList(HashMap<String, Object> map) {
        mvpView.showLoading();
        addSubscription(videoapiStores.getVideoList(map), new ApiCallback<List<VideoEntity>>() {
            @Override
            public void onSuccess(List<VideoEntity> model) {
                mvpView.getVideoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getVideoFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
