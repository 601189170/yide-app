package com.yyide.chatim.presenter;



import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.model.selectListByAppRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.HomeFragmentView;
import com.yyide.chatim.view.NoteBookView;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class NoteBookPresenter extends BasePresenter<NoteBookView> {

    public NoteBookPresenter(NoteBookView view) {
        attachView(view);
    }




    public void listByApp() {
        mvpView.showLoading();
        addSubscription(dingApiStores.listByApp(), new ApiCallback<listByAppRsp>() {
            @Override
            public void onSuccess(listByAppRsp model) {
                mvpView.listByApp(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.listByAppDataFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }






}
