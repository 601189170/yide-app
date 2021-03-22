package com.yyide.chatim.homemodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.model.SchoolRsp;

import java.io.IOException;

import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NoticeFragment extends BaseFragment {

    private View mBaseView;

    OkHttpClient mOkHttpClient = new OkHttpClient();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.notice_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();



    }

    void listTimeData(int schoolId, String schoolName) {
        SchoolRsp rsp=new SchoolRsp();
        rsp.schoolId=schoolId;
        rsp.schoolName=schoolName;
        RequestBody requestBody = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(rsp));

        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.URL_IP + "/timetable/cloud-timetable/timetable/listTimeData")
                .addHeader("Authorization", SpData.User().token)
                .post(requestBody)
                .build();
        //发起请求

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String data = response.body().string();
//                Log.e("TAG", "mOkHttpClient==>: " + data);
//                SelectUserSchoolRsp bean = JSON.parseObject(data, SelectUserSchoolRsp.class);
//                if (bean.code==BaseConstant.REQUEST_SUCCES2){
//                    Tologin(bean.data.username,bean.data.password, String.valueOf(schoolId));
//                }
            }
        });
    }
}
