package com.yyide.chatim.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.notice.view.NoticeHomeView;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.FaceUploadView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FaceUploadPresenter extends BasePresenter<FaceUploadView> {
    public FaceUploadPresenter(FaceUploadView view) {
        attachView(view);
    }

    public void updateFaceData(String name, long classId,long depId, String facePath) {
        Log.e("FaceUploadPresenter", "updateFaceData: name="+name +",classId="+classId+",depId="+depId+",facePath="+facePath);
        mvpView.showLoading();
        if (!SpData.getIdentityInfo().staffIdentity()) {
            Map<String, RequestBody> params = new HashMap<>();
            params.put("name", convertToRequestBody(name));
            params.put("classId", convertToRequestBody("" + classId));
            File file = new File(facePath);
            boolean exists = file.exists();
            Log.e("FaceUploadPresenter", facePath + "数据是否存在: " + exists);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            addSubscription(dingApiStores.toStudentOss(params, part), new ApiCallback<BaseRsp>() {
                @Override
                public void onSuccess(BaseRsp model) {
                    mvpView.faceUploadSuccess(model);
                }

                @Override
                public void onFailure(String msg) {
                    mvpView.faceUploadFail(msg);
                }

                @Override
                public void onFinish() {
                    mvpView.hideLoading();
                }
            });
        } else {
            Map<String, RequestBody> params = new HashMap<>();
            params.put("name", convertToRequestBody(name));
            //params.put("depId", convertToRequestBody("" + depId));
            File file = new File(facePath);
            boolean exists = file.exists();
            Log.e("FaceUploadPresenter", facePath + "数据是否存在: " + exists);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            addSubscription(dingApiStores.toTeacherOss(params, part), new ApiCallback<BaseRsp>() {
                @Override
                public void onSuccess(BaseRsp model) {
                    mvpView.faceUploadSuccess(model);
                }

                @Override
                public void onFailure(String msg) {
                    mvpView.faceUploadFail(msg);
                }

                @Override
                public void onFinish() {
                    mvpView.hideLoading();
                }
            });
        }
    }

    public void getFaceData(String name, long classId,long depId){
        Log.e("FaceUploadPresenter", "getFaceData: name="+name +",classId="+classId+",depId="+depId);
        mvpView.showLoading();
        if (!SpData.getIdentityInfo().staffIdentity()) {
            Map<String, Object> params = new HashMap<>();
            params.put("name",name);
            params.put("classId",classId);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(params));
            addSubscription(dingApiStores.getStudentOss(body), new ApiCallback<FaceOssBean>() {
                @Override
                public void onSuccess(FaceOssBean model) {
                    mvpView.getFaceDataSuccess(model);
                }

                @Override
                public void onFailure(String msg) {
                    mvpView.getFaceDataFail(msg);
                }

                @Override
                public void onFinish() {
                    mvpView.hideLoading();
                }
            });
        }else {
            Map<String, Object> params = new HashMap<>();
            params.put("name",name);
            params.put("depId",depId);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(params));
            addSubscription(dingApiStores.getTeacherOss(body), new ApiCallback<FaceOssBean>() {
                @Override
                public void onSuccess(FaceOssBean model) {
                    mvpView.getFaceDataSuccess(model);
                }

                @Override
                public void onFailure(String msg) {
                    mvpView.getFaceDataFail(msg);
                }

                @Override
                public void onFinish() {
                    mvpView.hideLoading();
                }
            });
        }
    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }
}

