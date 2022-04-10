package com.yyide.chatim.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.notice.view.NoticeHomeView;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.UploadRep;
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

    /**
     * 图片上传
     *
     * @param file
     * @param
     */
    public void uploadFile(File file) {
        if (file == null) {
            return;
        }
        mvpView.showLoading();
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part requestImgPart = MultipartBody.Part.create(fileRequestBody);
        // 创建MultipartBody.Part，用于封装文件数据
        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("file", "fileName.jpg", fileRequestBody);
        addSubscription(dingApiStores.uploadImg(requestImgPart), new ApiCallback<UploadRep>() {
            @Override
            public void onSuccess(UploadRep model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS2) {
                    if (model.getData() != null && model.getData().size() > 0 && model.getData().get(0) != null
                            && !TextUtils.isEmpty(model.getData().get(0).getUrl())) {
                        mvpView.faceUploadSuccess(model.getData().get(0).getUrl());
                    } else {
                        mvpView.faceUploadFail(model.getMessage());
                    }
                } else {
                    mvpView.faceUploadFail(model.getMessage());
                }
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

    public void updateFace(String imgPath) {
        mvpView.showLoading();
        Map<String, Object> params = new HashMap<>();
        //类型 学生：1、老师：2
        if (SpData.getIdentityInfo().staffIdentity()) {
            params.put("type", 2);
        } else {
            params.put("type", 1);
        }
        params.put("userId", SpData.getIdentityInfo().getId());
        params.put("imgPath", imgPath);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(params));
        addSubscription(dingApiStores.updateFace(body), new ApiCallback<BaseRsp>() {
            @Override
            public void onSuccess(BaseRsp model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS2) {
                    mvpView.updateSuccess();
                } else {
                    mvpView.updateFail(model.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getFaceData() {
        mvpView.showLoading();
        Map<String, Object> params = new HashMap<>();
        //类型 学生：1、老师：2
        if (SpData.getIdentityInfo().staffIdentity()) {
            params.put("type", 2);
        } else {
            params.put("type", 1);
        }
        params.put("userId", SpData.getIdentityInfo().getId());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(params));
        addSubscription(dingApiStores.getFaceOss(body), new ApiCallback<FaceOssBean>() {
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

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }
}

