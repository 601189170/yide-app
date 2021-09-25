package com.yyide.chatim.presenter;


import android.text.TextUtils;

import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.model.UploadRep;
import com.yyide.chatim.model.WeeklyDescBean;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;

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
//        mvpView.showLoading();
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

    public void getHomeTodo() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap();
        map.put("current", 1);
        map.put("size", 5);
        map.put("isOperation", 0);
        addSubscription(dingApiStores.getMessageTransaction(map), new ApiCallback<TodoRsp>() {
            @Override
            public void onSuccess(TodoRsp model) {
                mvpView.getIndexMyNotice(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getIndexMyNoticeFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getNotice() {
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
    }

    public void copywriter() {
        addSubscription(dingApiStores.copywriter(), new ApiCallback<WeeklyDescBean>() {
            @Override
            public void onSuccess(WeeklyDescBean model) {
                mvpView.getCopywriter(model);
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
        addSubscription(dingApiStores.uploadImg(requestImgPart, null), new ApiCallback<UploadRep>() {
            @Override
            public void onSuccess(UploadRep model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
                    mvpView.uploadFileSuccess(model.getData());
                } else {
                    mvpView.getFail(model.getMessage());
                }
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
}
