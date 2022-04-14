package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.PreparesLessonRep;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.PreparesLessonView;

import okhttp3.RequestBody;

public class PreparesLessonPresenter extends BasePresenter<PreparesLessonView> {
    public PreparesLessonPresenter(PreparesLessonView view) {
        attachView(view);
    }

    public void addLessons(PreparesLessonRep preparesLessonRep) {
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(preparesLessonRep));
        mvpView.showLoading();
        addSubscription(dingApiStores.addLessons(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
                    mvpView.addPreparesLessonSuccess(model.getMsg());
                } else {
                    mvpView.addPreparesLessonFail(model.getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addPreparesLessonFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void updateLessons(PreparesLessonRep preparesLessonRep) {
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(preparesLessonRep));
        mvpView.showLoading();
        addSubscription(dingApiStores.updateLessons(body), new ApiCallback<ResultBean>() {
            @Override
            public void onSuccess(ResultBean model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
                    mvpView.addPreparesLessonSuccess(model.getMsg());
                } else {
                    mvpView.addPreparesLessonFail(model.getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addPreparesLessonFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
