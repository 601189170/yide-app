package com.yyide.chatim.fragment.attendance.v2;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.DayStatisticsDetailListAdapter;
import com.yyide.chatim.adapter.attendance.v2.TeacherDayStatisticsDetailListAdapter;
import com.yyide.chatim.databinding.FragmentStatisticsListDetailBinding;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author liu tao
 * @version v2
 * @date 2021/11/3 10:16
 * @description 班主任或任课老师查看日统计信息 对应事件详情
 */
public class TeacherStatisticsListDetailFragment extends Fragment {
    private static final String TAG = TeacherStatisticsListDetailFragment.class.getSimpleName();
    private static final String ARG_TYPE = "type";
    private String type;
    private String mParam2;
    private final List<Fragment> fragments = new ArrayList<>();
    private FragmentStatisticsListDetailBinding viewBinding;

    //应到的数据列表
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> allPeople = new ArrayList<>();
    //正常考勤数据列表
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> normalPeople = new ArrayList<>();
    //缺勤的数据类别
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> absencePeople = new ArrayList<>();
    //请假的数据类别
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> leavePeople = new ArrayList<>();
    //迟到的数据列表
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> latePeople = new ArrayList<>();
    //早退
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> leaveEarlyPeople = new ArrayList<>();

    public void setData(TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean data) {
        this.data = data;
    }

    private TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean data;

    public TeacherStatisticsListDetailFragment() {
        // Required empty public constructor
    }


    public static TeacherStatisticsListDetailFragment newInstance(String param) {
        TeacherStatisticsListDetailFragment fragment = new TeacherStatisticsListDetailFragment();
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
        viewBinding.tvBeTo.setText(String.valueOf(data.getTotalNumber()));
        //正常人数tv_normal_num
        viewBinding.tvNormalNum.setText(String.valueOf(data.getNormal()));
        //缺勤人数tv_absenteeism_num
        viewBinding.tvAbsenteeismNum.setText(String.valueOf(data.getAbsenteeism()));
        //请假人数tv_leave_num
        viewBinding.tvLeaveNum.setText(String.valueOf(data.getLeave()));
        //迟到人数tv_late_num
        //viewBinding.tvLateNum.setText(String.valueOf(data.getLate()));
        //出勤率tv_attendance_rate
        viewBinding.tvAttendanceRate.setText(data.getSignInOutRate());
        //tv_attendance_time考勤时间
        final String attendance_time_text = getString(R.string.attendance_time_text);
        if (data.getType() == 2) {
            viewBinding.tvAttendanceTime.setText(String.format(attendance_time_text,DateUtils.formatTime(data.getCourseTime(), "", "HH:mm")));
        } else {
            viewBinding.tvAttendanceTime.setText(String.format(attendance_time_text,DateUtils.formatTime(data.getRequiredTime(), "", "HH:mm")));
        }

        if ("1".equals(data.getAttendanceSignInOut())) {
            //签退
            viewBinding.textview1.setText("签退率:");
            viewBinding.tvAbsence.setText("未签退");
            viewBinding.rbLate.setText("早退");
            viewBinding.rbAbsence.setText("未签退");
            viewBinding.tvLate.setText("早退");
            viewBinding.tvLateNum.setText(String.valueOf(data.getEarly()));
        } else {
            //签到
            viewBinding.textview1.setText("出勤率:");
            viewBinding.tvAbsence.setText("缺勤");
            viewBinding.rbLate.setText("迟到");
            viewBinding.rbAbsence.setText("缺勤");
            viewBinding.tvLate.setText("迟到");
            viewBinding.tvLateNum.setText(String.valueOf(data.getLate()));
        }
        //考勤进度
        final String rate = data.getSignInOutRate();
        try {
            final float aFloat = Float.parseFloat(rate);
            int rateInt = (int) (float) aFloat;
            viewBinding.progress.setProgress(rateInt, true);
        } catch (Exception exception) {
            Log.e(TAG, "onViewCreated: " + exception.getLocalizedMessage());
        }
        //viewpager
        allPeople.clear();
        final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> people = data.getAllRollList();
        allPeople.addAll(people);

        normalPeople.clear();
        final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> applyPeople = data.getNormalList();
        normalPeople.addAll(applyPeople);

        absencePeople.clear();
        final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> absencePeopleList = data.getAbsenteeismList();
        absencePeople.addAll(absencePeopleList);

        leavePeople.clear();
        final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> leavePeopleList = data.getLeaveList();
        leavePeople.addAll(leavePeopleList);

        if (Objects.equals(data.getAttendanceSignInOut(), "1")) {
            leaveEarlyPeople.clear();
            final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> leaveEarlyPeopleList = data.getEarlyList();
            leaveEarlyPeople.addAll(leaveEarlyPeopleList);
        } else {
            latePeople.clear();
            final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> latePeopleList = data.getLateList();
            latePeople.addAll(latePeopleList);
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

    //26025351|163343191|212818853|158947220|161871426|261080355|34928708
    private void initPeopleListView() {
        List<String> listTab = new ArrayList<>();
        listTab.add("应到");
        listTab.add("正常");
        listTab.add("缺勤");
        listTab.add("请假");
        listTab.add("迟到");
        showBlank(allPeople.isEmpty());
        TeacherDayStatisticsDetailListAdapter dayStatisticsListAdapter = new TeacherDayStatisticsDetailListAdapter(getContext(), allPeople);
        viewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.recyclerview.setAdapter(dayStatisticsListAdapter);

        viewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_all_people:
                    //viewBinding.viewpager.setCurrentItem(0);
                    dayStatisticsListAdapter.setData(allPeople);
                    dayStatisticsListAdapter.notifyDataSetChanged();
                    showBlank(allPeople.isEmpty());
                    break;
                case R.id.rb_normal:
                    //viewBinding.viewpager.setCurrentItem(1);
                    dayStatisticsListAdapter.setData(normalPeople);
                    dayStatisticsListAdapter.notifyDataSetChanged();
                    showBlank(normalPeople.isEmpty());
                    break;
                case R.id.rb_absence:
                    //viewBinding.viewpager.setCurrentItem(2);
                    dayStatisticsListAdapter.setData(absencePeople);
                    dayStatisticsListAdapter.notifyDataSetChanged();
                    showBlank(absencePeople.isEmpty());
                    break;
                case R.id.rb_leave:
                    //viewBinding.viewpager.setCurrentItem(3);
                    dayStatisticsListAdapter.setData(leavePeople);
                    dayStatisticsListAdapter.notifyDataSetChanged();
                    showBlank(leavePeople.isEmpty());
                    break;
                case R.id.rb_late:
                    //viewBinding.viewpager.setCurrentItem(4);
                    if ("1".equals(data.getAttendanceSignInOut())) {
                        dayStatisticsListAdapter.setData(leaveEarlyPeople);
                        dayStatisticsListAdapter.notifyDataSetChanged();
                        showBlank(leaveEarlyPeople.isEmpty());
                    } else {
                        dayStatisticsListAdapter.setData(latePeople);
                        dayStatisticsListAdapter.notifyDataSetChanged();
                        showBlank(latePeople.isEmpty());
                    }
                    break;

                default:
                    break;
            }
        });
    }

    private void showBlank(boolean show) {
        viewBinding.blankPage.setVisibility(show ? View.VISIBLE : View.GONE);
        //mViewBinding.recyclerview.setVisibility(show?View.GONE:View.VISIBLE);
    }
}