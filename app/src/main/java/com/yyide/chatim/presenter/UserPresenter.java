package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.tencent.imsdk.utils.FileUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.UpdateUserInfo;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.utils.FileUtils;
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
                if(model.getCode() == -1){
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

    public void uploadFile(String filePath) {
        final File file = new File(filePath);
        if (file == null) {
            return;
        }
        mvpView.showLoading();
        String base64File = FileUtils.encodeBase64File(filePath);
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("base64File", base64File, fileRequestBody);
        addSubscription(dingApiStores.uploadImg(requestImgPart), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {
                mvpView.hideLoading();
                mvpView.uploadFileSuccess("");
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.uploadFileFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
