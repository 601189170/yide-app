package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.ClassesPhotoBannerRsp;
import com.yyide.chatim.model.ClassesPhotoRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.StudentHonorRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ClassPhotoView;
import com.yyide.chatim.view.StudentHonorView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class ClassPhotoPresenter extends BasePresenter<ClassPhotoView> {
    public ClassPhotoPresenter(ClassPhotoView view) {
        attachView(view);
    }

    public void getClassPhotoList(String classesId) {
//        mvpView.showLoading();
        Map map = new HashMap<>();
        map.put("classesId", classesId);
        map.put("kind", 1);//1班级 2学校
        map.put("type", 1);//1图片 2视频
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getClassPhotoList(body), new ApiCallback<ClassesPhotoRsp>() {
            @Override
            public void onSuccess(ClassesPhotoRsp model) {
                mvpView.getClassesPhotoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getClassesPhotoFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
