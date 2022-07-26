package com.yyide.chatim_pro.fragment;

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

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.newnotice.NewNoticeAnnouncementActivity;
import com.yyide.chatim_pro.activity.notice.presenter.NoticeHomePresenter;
import com.yyide.chatim_pro.activity.notice.view.NoticeHomeView;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.model.EventMessage;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


/**
 * 通知公告
 */
public class NoticeFragment extends BaseMvpFragment<NoticeHomePresenter> implements NoticeHomeView {
    private static final String TAG = "NoticeFragment";
    private View mBaseView;
    @BindView(R.id.notice_time)
    TextView notice_time;
    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_notice_author)
    TextView tv_notice_author;

    private boolean jump = true;
    private NoticeMyReleaseDetailBean.DataBean data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.notice_fragmnet_new, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        ll_notice.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewNoticeAnnouncementActivity.class);
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
    public void noticeHome(NoticeMyReleaseDetailBean homeNoticeRsp) {
        Log.e(TAG, "noticeHome: " + homeNoticeRsp);
        if (isAdded()) {
            if (homeNoticeRsp.code == BaseConstant.REQUEST_SUCCESS) {
                data = homeNoticeRsp.data;
                if (data != null) {
                    notice_time.setVisibility(View.VISIBLE);
                    tv_notice_author.setVisibility(View.VISIBLE);
                    if (DateUtils.isToday(DateUtils.parseTimestamp(data.timerDate, ""))) {
                        notice_time.setText(getString(R.string.notice_toDay, DateUtils.formatTime(data.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm")));
                    } else if (DateUtils.isYesterday(DateUtils.parseTimestamp(data.timerDate, ""))) {
                        notice_time.setText(getString(R.string.notice_yesterday, DateUtils.formatTime(data.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm")));
                    } else {
                        notice_time.setText(DateUtils.formatTime(data.timerDate, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
                    }
                    tv_notice_author.setText(data.publisher);
                    if (!TextUtils.isEmpty(data.title)) {
                        tv_title.setText(data.title);
                    }
                } else {
                    tv_title.setText("暂无公告");
                    notice_time.setVisibility(View.GONE);
                    tv_notice_author.setVisibility(View.GONE);
                }
            } else {
                tv_title.setText("暂无公告");
                notice_time.setVisibility(View.GONE);
                tv_notice_author.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void noticeHomeFail(String msg) {
        Log.e(TAG, "noticeHomeFail: " + msg);
        tv_title.setText("暂无公告");
    }
}
