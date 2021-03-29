package com.yyide.chatim.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.notice.presenter.NoticeTemplatePresenter;
import com.yyide.chatim.notice.view.NoticeCreateView;
import com.yyide.chatim.notice.view.NoticeTemplateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeTemplateActivity extends BaseMvpActivity<NoticeTemplatePresenter> implements NoticeTemplateView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_template;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_template_title);
        initViewPager();
    }

    private void initViewPager() {
        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        List<String> list = new ArrayList<>();
        list.add("全部");
        list.add("热门");
        list.add("活动");
        list.add("安全");
        list.add("假期");

        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return NoticeTemplateListFragment.newInstance("");//我的通知
            }

            @Override
            public int getItemCount() {
                return list.isEmpty() ? 0 : list.size();
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里需要根据position修改tab的样式和文字等
                tab.setText(list.get(position));
            }
        }).attach();
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.ll_add)
    public void addClick() {
        jupm(this, NoticeCreateActivity.class);
    }

    @Override
    protected NoticeTemplatePresenter createPresenter() {
        return null;
    }

    @Override
    public void showError() {

    }
}