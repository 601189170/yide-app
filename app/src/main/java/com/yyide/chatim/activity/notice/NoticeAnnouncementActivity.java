package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.fragment.NoticeFragment;
import com.yyide.chatim.model.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通知公告
 * author lrz
 * time 2021/03/24
 */
public class NoticeAnnouncementActivity extends BaseMvpActivity<NoticeAnnouncementPresenter> implements NoticeAnnouncementView {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    boolean other = false;
    boolean other2 = false;
    private boolean hasNoticePermission;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_announcement;
    }

    List<BaseMvpFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_announcement_title);
        EventBus.getDefault().register(this);
        hasNoticePermission = MMKV.defaultMMKV().decodeBool("hasNoticePermission", false);
        initViewPager();
    }

    private void initViewPager() {
        //家长身份不能添加公告
        if (!SpData.getIdentityInfo().staffIdentity() || !hasNoticePermission) {
            floatingActionButton.setVisibility(View.GONE);
            mTablayout.setVisibility(View.GONE);
        }
        //我的通知
        NoticeAnnouncementListFragment my_notice = NoticeAnnouncementListFragment.newInstance("my_notice");
        fragments.add(my_notice);
        if (SpData.getIdentityInfo().staffIdentity() && hasNoticePermission) {
            //我的发布
            PublishNoticAnnouncementListFragment my_release = PublishNoticAnnouncementListFragment.newInstance("my_release");
            fragments.add(my_release);
        }
        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return fragments.get(0);
                } else {
                    return fragments.get(1);
                }
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true, (tab, position) -> {
            //这里需要根据position修改tab的样式和文字等
            if (position == 0) {
                tab.setText(R.string.notice_tab_my_notice);
            } else {
                tab.setText(R.string.notice_tab_my_release);
            }
        }).attach();
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.fab)
    public void fabClick() {
        other = true;
        other2 = true;
        startActivity(new Intent(this, NoticeTemplateActivity.class));
    }

    @Override
    protected NoticeAnnouncementPresenter createPresenter() {
        return null;
    }

    @Override
    public void showError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_NOTICE_TAB.equals(messageEvent.getCode())) {
            mViewpager.setCurrentItem(1);
        }
    }
}