package com.yyide.chatim_pro.presenter;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BasePresenter;
import com.yyide.chatim_pro.kotlin.network.base.BaseResponse;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.TodoRsp;
import com.yyide.chatim_pro.model.UserMsgNoticeRsp;
import com.yyide.chatim_pro.net.ApiCallback;
import com.yyide.chatim_pro.view.UserNoticeView;

import java.util.HashMap;

import okhttp3.RequestBody;

public class UserNoticePresenter extends BasePresenter<UserNoticeView> {
    public UserNoticePresenter(UserNoticeView view) {
        attachView(view);
    }

    /**
     * 获取用户通知信息列表
     *
     * @param current
     * @param size
     */
    public void getUserNoticePage(int current, int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", current);
        map.put("pageSize", size);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.queryUserMessageNotice(body), new ApiCallback<UserMsgNoticeRsp>() {
            @Override
            public void onSuccess(UserMsgNoticeRsp model) {
                mvpView.getUserNoticePageSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getUserNoticePageFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    //获取未处理消息数量
    public void getMessageNumber() {
//        mvpView.showLoading();
        HashMap<String, Object> map = new HashMap();
        map.put("pageNo", 1);
        map.put("pageSize", 5);
        map.put("status", 1);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getMessageTransaction(body), new ApiCallback<BaseResponse<TodoRsp>>() {
            @Override
            public void onSuccess(BaseResponse<TodoRsp> model) {
                if (model.getCode() == BaseConstant.REQUEST_SUCCESS2 && model.getData() != null) {
                    TodoRsp data = model.getData();
                    if (data.getList() != null && data.getList().size() > 0) {
                        TodoRsp.TodoItemBean itemBean = data.getList().get(0);
                        int count = 0;
                        for (TodoRsp.TodoItemBean item : data.getList()){
                            if (item.getStatus() == 0){
                                count++;
                            }
                        }
                        mvpView.messageNumberSuccess(count, itemBean.getStartTime(), itemBean.getTitle());
                    } else {
                        mvpView.messageNumberSuccess(0, "", "");
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.messageNumberFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
