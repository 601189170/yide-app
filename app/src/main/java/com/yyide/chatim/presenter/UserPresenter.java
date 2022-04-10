package com.yyide.chatim.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.kotlin.network.base.BaseResponse;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.MyUserInfo;
import com.yyide.chatim.model.UpdateUserInfo;
import com.yyide.chatim.model.UploadRep;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.UserView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class UserPresenter extends BasePresenter<UserView> {

    public UserPresenter(UserView view) {
        attachView(view);
    }

    public void update(String id, String avatar, String email) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
//        map.put("sex", sex);
        map.put("avatar", avatar);
        map.put("email", email);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.updateUserInfo(body), new ApiCallback<UpdateUserInfo>() {

            @Override
            public void onSuccess(UpdateUserInfo model) {
                mvpView.hideLoading();
                if (model.getCode() != BaseConstant.REQUEST_SUCCESS2) {
                    mvpView.updateFail(model.getMsg());
                } else {
                    mvpView.updateSuccess(model.getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.updateFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
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
                        mvpView.uploadFileSuccess(model.getData().get(0).getUrl());
                    } else {
                        mvpView.uploadFileFail(model.getMessage());
                    }
                } else {
                    mvpView.uploadFileFail(model.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.uploadFileFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getUserInfo() {
        mvpView.showLoading();
        //类型 学生：1、老师：2
        addSubscription(dingApiStores.userInfo(), new ApiCallback<BaseResponse<UserInfo>>() {
            @Override
            public void onSuccess(BaseResponse<UserInfo> model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS2) {
                    mvpView.getUserInfoSuccess(model.getData());
                }
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

    public void getFaceData() {
        mvpView.showLoading();
        Map<String, Object> params = new HashMap<>();
        //类型 学生：1、老师：2
        params.put("type", SpData.getIdentityInfo().getIdentity());
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
}
