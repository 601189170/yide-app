package com.yyide.chatim.activity.newnotice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.newnotice.fragment.NoticeMyReceivedFragment;
import com.yyide.chatim.activity.newnotice.fragment.NoticePushFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityNoticeBinding;

import java.util.ArrayList;
import java.util.List;

public class NewNoticeAnnouncementActivity extends BaseActivity {

    private ActivityNoticeBinding noticeBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeBinding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(noticeBinding.getRoot());
        initView();
    }

    private void initView() {
        noticeBinding.top.title.setText(R.string.notice_announcement_title);
        noticeBinding.top.backLayout.setOnClickListener(v -> {
            finish();
        });
        List<String> mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.notice_tab_my_received));
        mTitles.add(getString(R.string.notice_tab_my_push));
        mTitles.add(getString(R.string.notice_tab_push));
        noticeBinding.viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = NoticeMyReceivedFragment.newInstance();
                switch (position) {
                    case 0:
                    case 2:
                        fragment = NoticeMyReceivedFragment.newInstance();
                        break;
                    case 1:
                        fragment = NoticePushFragment.newInstance();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }

        });
        noticeBinding.slidingTabLayout.setViewPager(noticeBinding.viewpager);
        noticeBinding.slidingTabLayout.setCurrentTab(0);
    }
}