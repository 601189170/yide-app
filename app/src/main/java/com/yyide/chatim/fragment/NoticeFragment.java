package com.yyide.chatim.fragment;

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

import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.activity.notice.NoticeDetailActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeHomePresenter;
import com.yyide.chatim.activity.notice.view.NoticeHomeView;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


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

    private boolean jump = true;
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
        ll_notice.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NoticeAnnouncementActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        mvpPresenter.getHomeNotice();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode()) || BaseConstant.TYPE_UPDATE_REMOTE_NOTICE.equals(messageEvent.getCode())) {
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
                MMKV.defaultMMKV().encode("hasNoticePermission",data.isHasNoticePermission());
//                jump = true;
                notice_content.setText(data.getContent());
                notice_time.setText(DateUtils.formatTime(data.getProductionTime().toString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
                if (!TextUtils.isEmpty(data.getTitle())) {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText("《" + data.getTitle() + "》");
                }
            } else {
//                jump = false;
                notice_content.setText("暂无消息公告");
            }
        }
    }

    @Override
    public void noticeHomeFail(String msg) {
        Log.e(TAG, "noticeHomeFail: " + msg);
        notice_content.setText("暂无消息公告");
    }
}
