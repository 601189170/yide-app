package com.yyide.chatim.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.notice.presenter.NoticeConfirmDetailPresenter;
import com.yyide.chatim.notice.view.NoticeConfirmDetailView;

import butterknife.BindView;

public class NoticeConfirmDetailActivity extends BaseMvpActivity<NoticeConfirmDetailPresenter> implements NoticeConfirmDetailView {

    @BindView(R.id.tablelayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;

    @Override
    public int getContentViewID() {
        return R.layout.activity_confirm_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewPager();
    }

    private void initViewPager() {
        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0){
                    return NoticeItemFragment.newInstance();
                } else {
                    return NoticeItemFragment.newInstance();
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里需要根据position修改tab的样式和文字等
                if(position == 0){
                    tab.setText("未确认(10)");
                } else {
                    tab.setText("已确认(20)");
                }
            }
        }).attach();
    }

    @Override
    protected NoticeConfirmDetailPresenter createPresenter() {
        return new NoticeConfirmDetailPresenter(this);
    }

    @Override
    public void showError() {

    }
}