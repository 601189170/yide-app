package com.yyide.chatim.homemodel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.tv_title)
    TextView tv_title;

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
        EventBus.getDefault().register(this);
        mvpPresenter.getHomeNotice();

        ll_notice.setOnClickListener(v -> {
            if (jump && data != null) {
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", data.getId());
                intent.putExtra("status", data.getStatus());
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", NoticeFragment.class.getSimpleName());
            mvpPresenter.getHomeNotice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected NoticeHomePresenter createPresenter() {
        return new NoticeHomePresenter(this);
    }

    @Override
    public void noticeHome(HomeNoticeRsp homeNoticeRsp) {
        Log.e(TAG, "noticeHome: " + homeNoticeRsp);
        if (homeNoticeRsp.getCode() == 200) {
            data = homeNoticeRsp.getData();
            if (data != null) {
                jump = true;
                notice_content.setText(data.getContent());
                notice_time.setText(DateUtils.formatTime(data.getProductionTime().toString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                if (!TextUtils.isEmpty(data.getTitle())) {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText("《" + data.getTitle() + "》");
                }
            } else {
                jump = false;
                notice_content.setText("暂无消息公告");
            }
        }
    }

    @Override
    public void noticeHomeFail(String msg) {
        Log.e(TAG, "noticeHomeFail: " + msg);
    }
}
