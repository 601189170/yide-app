package com.yyide.chatim.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.AppListRsp;
import com.yyide.chatim.model.HomeBannerRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppView;
import com.yyide.chatim.view.HomeBannerView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class HomeBannerPresenter extends BasePresenter<HomeBannerView> {
    public HomeBannerPresenter(HomeBannerView view) {
        attachView(view);
    }

    public void getClassPhotoList(int classId, int schoolId) {
        Map<String, Object> map = new HashMap<>();
        map.put("classesId", classId);
        map.put("schoolId", schoolId);
        map.put("kind", 1);//1班级 2学校
        map.put("type", 1);//1图片 2视频
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.getClassPhotoList(body), new ApiCallback<HomeBannerRsp>() {
            @Override
            public void onSuccess(HomeBannerRsp model) {
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
