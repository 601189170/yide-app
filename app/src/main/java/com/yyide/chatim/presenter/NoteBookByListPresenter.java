package com.yyide.chatim.presenter;


import com.alibaba.fastjson.JSON;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.NoteBookView;
import com.yyide.chatim.view.NoteByListBookView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class NoteBookByListPresenter extends BasePresenter<NoteByListBookView> {

    public NoteBookByListPresenter(NoteByListBookView view) {
        attachView(view);
    }

    public void NoteBookByList(String departmentId, String name, String phone, String jobNumber, String size, String current) {
        mvpView.showLoading();
        Map map = new HashMap();
        map.put("departmentId", departmentId);
        map.put("name", name);
        map.put("phone", phone);
        map.put("jobNumber", jobNumber);
        Map map2 = new HashMap();
        map2.put("size", size);
        map2.put("current", current);
        map.put("page", map2);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));

        addSubscription(dingApiStores.teacherlist(body), new ApiCallback<TeacherlistRsp>() {
            @Override
            public void onSuccess(TeacherlistRsp model) {
                mvpView.TeacherlistRsp(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.TeacherlistRspFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getStudentList(String classesIs) {
        mvpView.showLoading();
        Map map = new HashMap();
        map.put("classesId", classesIs);
        map.put("name", "");
        map.put("size", 150);
        map.put("current", 1);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        addSubscription(dingApiStores.getStudentList(body), new ApiCallback<TeacherlistRsp>() {
            @Override
            public void onSuccess(TeacherlistRsp model) {
                mvpView.studentListRsp(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.studentListRspFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
