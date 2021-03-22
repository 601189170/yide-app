package com.yyide.chatim.presenter;


import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.ClassTableView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class ClassTablePresenter extends BasePresenter<ClassTableView> {


    public ClassTablePresenter(ClassTableView view) {
        attachView(view);
    }

    public void listAllBySchoolId() {

        mvpView.showLoading();
        addSubscription(dingApiStores.listAllBySchoolId(), new ApiCallback<listAllBySchoolIdRsp>() {
            @Override
            public void onSuccess(listAllBySchoolIdRsp model) {
//                mvpView.hideLoading();
                mvpView.listAllBySchoolId(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.listAllBySchoolIdFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


}
