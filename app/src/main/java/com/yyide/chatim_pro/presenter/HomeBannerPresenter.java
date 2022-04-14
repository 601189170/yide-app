package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.model.ClassesPhotoBannerRsp;
import com.yyide.chatim_pro.model.ClassesPhotoRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.HomeBannerView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class HomeBannerPresenter extends BasePresenter<HomeBannerView> {
    public HomeBannerPresenter(HomeBannerView view) {
        attachView(view);
    }

    public void getClassPhotoList(String classId, long schoolId) {
        Map<String, Object> map = new HashMap<>();
        map.put("classesId", classId);
        map.put("schoolId", schoolId);
        map.put("kind", 1);//1班级 2学校
        map.put("type", 1);//1图片 2视频
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getClassPhotoList(body), new ApiCallback<ClassesPhotoBannerRsp>() {
            @Override
            public void onSuccess(ClassesPhotoBannerRsp model) {
                if (model.getCode() == 200) {
                    mvpView.getClassBannerListSuccess(model);
                } else {
                    mvpView.getClassBannerListFail(model.getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getClassBannerListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
