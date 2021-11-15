package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.fragment.attendance.DayStatisticsFragment;
import com.yyide.chatim.fragment.attendance.WeekStatisticsFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityStatisticsBinding;
import com.yyide.chatim.fragment.attendance.v2.StudentDayStatisticsFragment;
import com.yyide.chatim.fragment.attendance.v2.StudentWeekStatisticsFragment;
import com.yyide.chatim.fragment.attendance.v2.TeacherDayStatisticsFragment;
import com.yyide.chatim.fragment.attendance.v2.TeacherWeekStatisticsFragment;

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
        String theme = getIntent().getStringExtra("theme");
        String serverId = getIntent().getStringExtra("serverId");
        String type = getIntent().getStringExtra("type");

        if (TextUtils.isEmpty(type)) {
            theme = "";
            serverId = "";
            type = "";
        }

        List<String> listTab = new ArrayList<>();
        listTab.add(getString(R.string.daily_statistics));
        listTab.add(getString(R.string.weekly_statistics));
        listTab.add(getString(R.string.monthly_statistics));
        // 旧接口方案
//        fragments.add(new DayStatisticsFragment());
//        fragments.add(new WeekStatisticsFragment(listTab.get(1)));
//        fragments.add(new WeekStatisticsFragment(listTab.get(2)));
        if (SpData.getIdentityInfo().staffIdentity()) {
            fragments.add(new TeacherDayStatisticsFragment(theme));
            fragments.add(new TeacherWeekStatisticsFragment(listTab.get(1), theme,serverId,type));
            fragments.add(new TeacherWeekStatisticsFragment(listTab.get(2), theme,serverId,type));
        } else {
            fragments.add(new StudentDayStatisticsFragment(theme));
            fragments.add(new StudentWeekStatisticsFragment(listTab.get(1), theme,serverId,type));
            fragments.add(new StudentWeekStatisticsFragment(listTab.get(2), theme,serverId,type));
        }
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
        new TabLayoutMediator(mViewBinding.tablayout, mViewBinding.viewpager, true, false,(tab, position) -> {
            //这里需要根据position修改tab的样式和文字等
            Log.e("TAG", "onConfigureTab: "+position );
            tab.setText(listTab.get(position));
        }).attach();
    }
}