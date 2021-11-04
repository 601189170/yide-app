package com.yyide.chatim.activity.attendance.v2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.fragment.attendance.StatisticsListDetailFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityStatisticsDetailBinding;
import com.yyide.chatim.fragment.attendance.v2.TeacherStatisticsListDetailFragment;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  liu tao
 * @date 2021-06-19
 * 考勤统计详情页
 */
public class TeacherStatisticsDetailActivity extends BaseActivity {
    private static final String TAG = TeacherStatisticsDetailActivity.class.getSimpleName();
    private ActivityStatisticsDetailBinding mViewBinding;
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> attendancesFormBean = new ArrayList<>();;
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> listTab = new ArrayList<>();
    private String currentClass;
    private int position;

    @Override
    public int getContentViewID() {
        return R.layout.activity_statistics_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityStatisticsDetailBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        final String data = getIntent().getStringExtra("data");
        position = getIntent().getIntExtra("position", 0);
        currentClass = getIntent().getStringExtra("currentClass");
        mViewBinding.tvEventClass.setText(currentClass);
        Log.e(TAG, "onCreate: "+data );
        attendancesFormBean = JSON.parseArray(data, TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.class);
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
        if (attendancesFormBean == null){
            return;
        }
        final String attNameA = attendancesFormBean.get(0).getTheme();
        mViewBinding.tvEventName.setText(attNameA);
        for (TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean eventBasicVoListBean : attendancesFormBean) {
            final String eventName = eventBasicVoListBean.getEventName();
            listTab.add(eventName);
            final TeacherStatisticsListDetailFragment statisticsListDetailFragment = TeacherStatisticsListDetailFragment.newInstance(eventName);
            statisticsListDetailFragment.setData(eventBasicVoListBean);
            fragments.add(statisticsListDetailFragment);
        }

        Log.e(TAG, "tab list : "+JSON.toJSONString(listTab) );
        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.e(TAG, "createFragment: "+position );
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });
        new TabLayoutMediator(mViewBinding.tablayout, mViewBinding.viewpager, true, (tab, position) -> {
            //这里需要根据position修改tab的样式和文字等
            final String name = listTab.get(position);
            Log.e(TAG, "initViewPager: "+name );
            tab.setText(name);
        }).attach();

        //默认选中第几个tab
        mViewBinding.viewpager.setCurrentItem(position,false);
    }
}