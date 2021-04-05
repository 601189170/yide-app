package com.yyide.chatim.presenter;



import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.mobileRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.LoginView;
import com.yyide.chatim.view.UserView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class UserPresenter extends BasePresenter<UserView> {

    public UserPresenter(UserView view) {
        attachView(view);
    }

    public void update(String id, String sex, String birthday, String email){
        addSubscription(dingApiStores.updateUserInfo(id, sex, birthday, email), new ApiCallback() {
            @Override
            public void onSuccess(Object model) {
                mvpView.updateSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updateFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
