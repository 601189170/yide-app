package com.yyide.chatim.activity.attendance.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim.databinding.FragmentStatisticsListDetailBinding;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsListDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsListDetailFragment extends Fragment {
    private static final String TAG = StatisticsListDetailFragment.class.getSimpleName();
    private static final String ARG_TYPE = "type";
    private String type;
    private String mParam2;
    private WeekStatisticsListAdapter weekStatisticsListAdapter;
    private final List<Fragment> fragments = new ArrayList<>();
    private FragmentStatisticsListDetailBinding viewBinding;

    //应到的数据列表
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> allPeople = new ArrayList<>();
    //正常考勤数据列表
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> normalPeople = new ArrayList<>();
    //缺勤的数据类别
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> absencePeople = new ArrayList<>();
    //请假的数据类别
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> leavePeople = new ArrayList<>();
    //迟到的数据列表
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> latePeople = new ArrayList<>();

    public void setData(AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean data) {
        this.data = data;
    }

    private AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean data;

    public StatisticsListDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment StatisticsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsListDetailFragment newInstance(String param) {
        StatisticsListDetailFragment fragment = new StatisticsListDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
            Log.e(TAG, "onCreate: " + type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_list_detail, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initdata();
        Log.e(TAG, "onViewCreated: " + type + ",data=" + JSON.toJSONString(data));
        viewBinding = FragmentStatisticsListDetailBinding.bind(view);
        //应到人数tv_be_to
        viewBinding.tvBeTo.setText(String.valueOf(data.getNumber()));
        //正常人数tv_normal_num
        viewBinding.tvNormalNum.setText(String.valueOf(data.getApplyNum()));
        //缺勤人数tv_absenteeism_num
        viewBinding.tvAbsenteeismNum.setText(String.valueOf(data.getAbsence()));
        //请假人数tv_leave_num
        viewBinding.tvLeaveNum.setText(String.valueOf(data.getLeave()));
        //迟到人数tv_late_num
        viewBinding.tvLateNum.setText(String.valueOf(data.getLate()));
        //签到率tv_attendance_rate
        viewBinding.tvAttendanceRate.setText(data.getRate());
        //tv_attendance_time考勤时间
        viewBinding.tvAttendanceTime.setText(data.getStartTime());
        //考勤进度
        final String rate = data.getRate();
        try {
            final float aFloat = Float.parseFloat(rate);
            int rateInt = (int) (float) aFloat;
            viewBinding.progress.setProgress(rateInt, true);
        } catch (Exception exception) {
            Log.e(TAG, "onViewCreated: " + exception.getLocalizedMessage());
        }
        //viewpager
        allPeople.clear();
        final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean> people = data.getPeople();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean person : people) {
            allPeople.add(castDataType(person));
        }

        normalPeople.clear();
        final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean> applyPeople = data.getApplyPeople();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean person : applyPeople) {
            normalPeople.add(castDataType(person));
        }

        absencePeople.clear();
        final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean> absencePeopleList = data.getAbsencePeople();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean person : absencePeopleList) {
            absencePeople.add(castDataType(person));
        }

        leavePeople.clear();
        final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean> leavePeopleList = data.getLeavePeople();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean person : leavePeopleList) {
            leavePeople.add(castDataType(person));
        }

        latePeople.clear();
        final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean> latePeopleList = data.getLatePeople();
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean person : latePeopleList) {
            latePeople.add(castDataType(person));
        }
        initPeopleListView();
    }

    private AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean castDataType(AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean.PeopleBean peopleBean) {
        final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean peopleBean1 =
                new AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean();
        peopleBean1.setName(peopleBean.getName());
        peopleBean1.setDeviceName(peopleBean.getDeviceName());
        peopleBean1.setEndDate(peopleBean.getEndDate());
        peopleBean1.setStartDate(peopleBean.getStartDate());
        peopleBean1.setPath(peopleBean.getPath());
        peopleBean1.setStatus(peopleBean.getStatus());
        peopleBean1.setSection(peopleBean.getSection());
        peopleBean1.setTime(peopleBean.getTime());
        peopleBean1.setThingName(peopleBean.getThingName());
        peopleBean1.setSubjectName(peopleBean.getSubjectName());
        return peopleBean1;
    }

    private void initPeopleListView() {
        viewBinding.clAttendanceType.setVisibility(View.VISIBLE);
        List<String> listTab = new ArrayList<>();
        listTab.add("应到");
        listTab.add("正常");
        listTab.add("缺勤");
        listTab.add("请假");
        listTab.add("迟到");

        final StatisticsListFragment allListFragment = StatisticsListFragment.newInstance(listTab.get(0));
        allListFragment.setData(allPeople);
        fragments.add(allListFragment);
        final StatisticsListFragment normalListFragment = StatisticsListFragment.newInstance(listTab.get(1));
        normalListFragment.setData(normalPeople);
        fragments.add(normalListFragment);
        final StatisticsListFragment absenceListFragment = StatisticsListFragment.newInstance(listTab.get(2));
        absenceListFragment.setData(absencePeople);
        fragments.add(absenceListFragment);
        final StatisticsListFragment leaveListFragment = StatisticsListFragment.newInstance(listTab.get(3));
        leaveListFragment.setData(leavePeople);
        fragments.add(leaveListFragment);

        final StatisticsListFragment lateFragment = StatisticsListFragment.newInstance(listTab.get(4));
        lateFragment.setData(latePeople);
        fragments.add(lateFragment);

        viewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewBinding.viewpager.setUserInputEnabled(false);
        viewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.e(TAG, "switch fragment: " + position);
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });

        viewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            Log.e(TAG, "initPeopleListView: " + checkedId);
            switch (checkedId) {
                case R.id.rb_all_people:
                    viewBinding.viewpager.setCurrentItem(0);
                    break;
                case R.id.rb_normal:
                    viewBinding.viewpager.setCurrentItem(1);
                    break;
                case R.id.rb_absence:
                    viewBinding.viewpager.setCurrentItem(2);
                    break;
                case R.id.rb_leave:
                    viewBinding.viewpager.setCurrentItem(3);
                    break;
                case R.id.rb_late:
                    viewBinding.viewpager.setCurrentItem(4);
                    break;

                default:
                    break;
            }
        });
    }
}