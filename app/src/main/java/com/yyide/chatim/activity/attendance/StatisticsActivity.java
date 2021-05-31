package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.fragment.DayStatisticsFragment;
import com.yyide.chatim.activity.attendance.fragment.WeekStatisticsFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityStatisticsBinding;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends BaseActivity {

    private ActivityStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public int getContentViewID() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        initView();
        initViewPager();
    }

    private void initView() {
        mViewBinding.top.title.setText(R.string.attendance_title);
        mViewBinding.top.backLayout.setOnClickListener((v)->{
            finish();
        });
    }

    private void initViewPager() {
        List<String> listTab = new ArrayList<>();
        listTab.add("日统计");
        listTab.add("周统计");
        listTab.add("月统计");
        fragments.add(new DayStatisticsFragment());
        fragments.add(WeekStatisticsFragment.newInstance(listTab.get(1)));
        fragments.add(WeekStatisticsFragment.newInstance(listTab.get(2)));

        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setUserInputEnabled(false);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);//我的通知
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });
        new TabLayoutMediator(mViewBinding.tablayout, mViewBinding.viewpager, true, (tab, position) -> {
            //这里需要根据position修改tab的样式和文字等
            Log.e("TAG", "onConfigureTab: "+position );
            tab.setText(listTab.get(position));
        }).attach();
    }
}