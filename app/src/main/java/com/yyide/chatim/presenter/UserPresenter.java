package com.yyide.chatim.presenter;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.UpdateUserInfo;
import com.yyide.chatim.model.UploadRep;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.UserView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Header;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class UserPresenter extends BasePresenter<UserView> {

    public UserPresenter(UserView view) {
        attachView(view);
    }

    public void update(String id, String sex, String birthday, String email) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("sex", sex);
        map.put("birthdayDate", birthday);
        map.put("email", email);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        addSubscription(dingApiStores.updateUserInfo(body), new ApiCallback<UpdateUserInfo>() {

            @Override
            public void onSuccess(UpdateUserInfo model) {
                mvpView.hideLoading();
                if (model.getCode() == -1) {
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
                if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
                    mvpView.uploadFileSuccess(model.getUrl());
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

    public void getFaceData(String name, int classId){
        Log.e("FaceUploadPresenter", "getFaceData: name="+name +",classId="+classId);
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
}
