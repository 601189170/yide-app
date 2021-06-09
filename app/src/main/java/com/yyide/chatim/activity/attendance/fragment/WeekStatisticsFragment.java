package com.yyide.chatim.activity.attendance.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.yyide.chatim.R;
import com.yyide.chatim.databinding.FragmentWeekStatisticsBinding;
import com.yyide.chatim.utils.DateUtils;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekStatisticsFragment extends Fragment {
    private static final String TAG = "WeekStatisticsFragment";
    private FragmentWeekStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    private static final String ARG_TYPE = "type";
    //当前界面类型 周统计 月统计
    private String type;
    //月
    private int month;
    private int currentMonth;
    //周
    private int week;
    private int currentWeek;
    //判断当前是周统计还是月统计
    private boolean isWeekStatistics;

    public WeekStatisticsFragment() {
        // Required empty public constructor
    }


    public static WeekStatisticsFragment newInstance(String type) {
        WeekStatisticsFragment fragment = new WeekStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_statistics, container, false);
    }

    private void setMonth() {
        if (month > currentMonth || month < 1) {
            Log.e(TAG, "setMonth: 不能查询未来的事件或者超过时间范围的事件");
            return;
        }
        if (currentMonth == month) {
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else if (month == 1) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        mViewBinding.tvWeek.setText(String.format(getActivity().getString(R.string.month), month));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeek(){
        if (week>currentWeek || week <1){
            Log.e(TAG, "setWeek: 不能查询未来的事件或者超过时间范围的事件");
            return;
        }
        if (currentWeek == week){
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else if (week == 1) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final String[] monthWeek = DateUtils.getFirstDayAndLastDayByMonthWeek(year, month, week);
        final String format1 = getActivity().getString(R.string.week);
        mViewBinding.tvWeek.setText(String.format(format1,monthWeek[0],monthWeek[1]));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentWeekStatisticsBinding.bind(view);
        if ("周统计".equals(type)) {
            isWeekStatistics = true;
        }
        if (isWeekStatistics) {
            final Calendar calendar = Calendar.getInstance();
            final int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
            week = weekOfMonth;
            currentWeek = weekOfMonth;
            setWeek();
        } else {
            //获取当前默认日期
            final Calendar calendar = Calendar.getInstance();
            Log.e(TAG, "calendar: " + calendar);
            final int i = calendar.get(Calendar.MONTH);
            Log.e(TAG, "月: " + i);
            month = i + 1;
            currentMonth = month;
            setMonth();
        }
        mViewBinding.ivLeft.setOnClickListener(v -> {
            //向左选择日期
            if (isWeekStatistics) {
                week--;
                setWeek();
            } else {
                month--;
                setMonth();
            }
        });
        mViewBinding.ivRight.setOnClickListener(v -> {
            //向右选择日期
            if (isWeekStatistics) {
                week++;
                setWeek();
            } else {
                month++;
                setMonth();
            }

        });
        List<String> listTab = new ArrayList<>();
        listTab.add("缺勤");
        listTab.add("迟到");
        listTab.add("请假");
        listTab.add("早退");
        listTab.add("未签到");
        fragments.add(StatisticsListFragment.newInstance(listTab.get(0)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(1)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(2)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(3)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(4)));

        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setUserInputEnabled(false);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });
        mViewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_absence:
                    mViewBinding.viewpager.setCurrentItem(0);
                    break;
                case R.id.rb_late:
                    mViewBinding.viewpager.setCurrentItem(1);
                    break;
                case R.id.rb_leave:
                    mViewBinding.viewpager.setCurrentItem(2);
                    break;
                case R.id.rb_leave_early:
                    mViewBinding.viewpager.setCurrentItem(3);
                    break;
                case R.id.rb_not_sign_in:
                    mViewBinding.viewpager.setCurrentItem(4);
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }
}