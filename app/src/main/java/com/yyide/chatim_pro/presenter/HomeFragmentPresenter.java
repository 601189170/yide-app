package com.yyide.chatim_pro.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.TodoRsp;
import com.yyide.chatim_pro.model.UploadRep;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.HomeFragmentView;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class HomeFragmentPresenter extends BasePresenter<HomeFragmentView> {

    public HomeFragmentPresenter(HomeFragmentView view) {
        attachView(view);
    }

    public void getUserSchool() {
        addSubscription(dingApiStores.getUserSchool(), new ApiCallback<GetUserSchoolRsp>() {
            @Override
            public void onSuccess(GetUserSchoolRsp model) {
                mvpView.getUserSchool(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /*public void getNotice() {
        addSubscription(dingApiStores.noticeShow(), new ApiCallback<NoticeMyReleaseDetailBean>() {
            @Override
            public void onSuccess(NoticeMyReleaseDetailBean model) {
                mvpView.getNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void confirmNotice(long id) {
        mvpView.showLoading();
        addSubscription(dingApiStores.confirmNotice(id), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                mvpView.confirmNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }*/

}
