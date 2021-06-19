package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.fragment.StatisticsListDetailFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityStatisticsDetailBinding;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  liu tao
 * @date 2021-06-19
 * 考勤统计详情页
 */
public class StatisticsDetailActivity extends BaseActivity {
    private static final String TAG = StatisticsDetailActivity.class.getSimpleName();
    private ActivityStatisticsDetailBinding mViewBinding;
    private AttendanceDayStatsRsp.DataBean.AttendancesFormBean attendancesFormBean = null;
    private List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean> studentLists = new ArrayList<>();
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> listTab = new ArrayList<>();
    private String currentClass;

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
        currentClass = getIntent().getStringExtra("currentClass");
        mViewBinding.tvEventClass.setText(currentClass);
        Log.e(TAG, "onCreate: "+data );
        attendancesFormBean = JSON.parseObject(data, AttendanceDayStatsRsp.DataBean.AttendancesFormBean.class);
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
        final String attNameA = attendancesFormBean.getAttNameA();
        mViewBinding.tvEventName.setText(attNameA);
        studentLists = attendancesFormBean.getStudentLists();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean studentListsBean : studentLists) {
            final String identityType = attendancesFormBean.getIdentityType();
            String name = "";
            if ("N".equals(identityType)){
                name = studentListsBean.getThingName();
                Log.e(TAG, "tab name: "+name );
                listTab.add(name);
            }else {
                name = studentListsBean.getSubjectName();
                Log.e(TAG, "tab name: "+name );
                listTab.add(name);
            }
            final StatisticsListDetailFragment statisticsListDetailFragment = StatisticsListDetailFragment.newInstance(name);
            statisticsListDetailFragment.setData(studentListsBean);
            fragments.add(statisticsListDetailFragment);
        }



        Log.e(TAG, "tab list : "+JSON.toJSONString(listTab) );
        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//        mViewBinding.viewpager.setUserInputEnabled(false);
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
    }
}