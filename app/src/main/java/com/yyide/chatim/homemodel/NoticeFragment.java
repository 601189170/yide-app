package com.yyide.chatim.homemodel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.activity.notice.NoticeDetailActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeHomePresenter;
import com.yyide.chatim.activity.notice.view.NoticeHomeView;
import com.yyide.chatim.adapter.NoiceAnnounAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.utils.DateUtils;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NoticeFragment extends BaseMvpFragment<NoticeHomePresenter> implements NoticeHomeView {
    private static final String TAG = "NoticeFragment";
    private View mBaseView;
    @BindView(R.id.notice_content)
    TextView notice_content;

    @BindView(R.id.notice_time)
    TextView notice_time;

    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    NoiceAnnounAdapter adapter;

    private boolean jump = false;
    private HomeNoticeRsp.DataBean data;

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
//        adapter=new NoiceAnnounAdapter(mRollPagerView);
//        mRollPagerView.setHintView(null);
//        mRollPagerView.setPlayDelay(5000);
//        mRollPagerView.setAdapter(adapter);
//        mRollPagerView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), NoticeAnnouncementActivity.class);
//                startActivity(intent);
//            }
//        });
        mvpPresenter.getHomeNotice();

        ll_notice.setOnClickListener(v -> {
            if (jump && data != null){
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("id",data.getId());
                intent.putExtra("status",data.getStatus());
                startActivity(intent);
            }
        });
    }

    @Override
    protected NoticeHomePresenter createPresenter() {
        return new NoticeHomePresenter(this);
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

    @Override
    public void noticeHome(HomeNoticeRsp homeNoticeRsp) {
        Log.e(TAG, "noticeHome: "+homeNoticeRsp );
        if (homeNoticeRsp.getCode() == 200) {
            data = homeNoticeRsp.getData();
            if (data != null){
                jump = true;
                notice_content.setText(data.getContent());
                notice_time.setText(DateUtils.switchTime(data.getCreatedDateTime(),"yyyy-MM-dd"));
            }else {
                jump = false;
                notice_content.setText("暂无消息公告");
            }
        }
    }

    @Override
    public void noticeHomeFail(String msg) {
        Log.e(TAG, "noticeHomeFail: "+msg );
    }
}
