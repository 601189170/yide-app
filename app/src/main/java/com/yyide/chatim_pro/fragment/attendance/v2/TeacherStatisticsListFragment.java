package com.yyide.chatim_pro.fragment.attendance.v2;

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

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.adapter.attendance.v2.TeacherWeekStatisticsFlatListAdapter;
import com.yyide.chatim_pro.adapter.attendance.v2.TeacherWeekStatisticsListAdapter;
import com.yyide.chatim_pro.databinding.FragmentStatisticsListBinding;
import com.yyide.chatim_pro.model.attendance.EventBean;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author liu tao
 * @version v2
 * @date 2021/11/4 14:10
 * @description
 */
public class TeacherStatisticsListFragment extends Fragment {
    private static final String TAG = "StatisticsListFragment";
    private static final String ARG_TYPE = "type";

    private String type;
    private TeacherWeekStatisticsListAdapter weekStatisticsListAdapter;
    private TeacherWeekStatisticsFlatListAdapter weekStatisticsFlatListAdapter;
    private List<EventBean> eventBeanList;
    public void setData(List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> data) {
        this.data = data;
    }

    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> data = new ArrayList<>();
    public TeacherStatisticsListFragment() {
        // Required empty public constructor
    }

    public static TeacherStatisticsListFragment newInstance(String param) {
        TeacherStatisticsListFragment fragment = new TeacherStatisticsListFragment();
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
            Log.e(TAG, "onCreate: "+ type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_list, container, false);
    }

    private List<EventBean> toEventBeanList(EventBean event,List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> eventBeans){
        List<EventBean> eventBeanList = new ArrayList<>();
        for (TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean datum : eventBeans) {
            EventBean eventBean = new EventBean();
            eventBean.setUserName(datum.getUserName());
            eventBean.setUserId(datum.getUserId());
            eventBean.setSectionNumber(datum.getSectionNumber());
            eventBean.setType(0);
            if (event == null) {
                eventBean.setChecked(false);
            } else {
                if (Objects.equals(event.getUserId(), datum.getUserId())) {
                    eventBean.setChecked(!event.getChecked());
                }
            }
            eventBeanList.add(eventBean);
            if (datum.getCourseInfoFormList() == null || !eventBean.getChecked()){
                continue;
            }
            for (TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean courseInfoFormListBean : datum.getCourseInfoFormList()) {
                EventBean eventBean1 = new EventBean();
                eventBean1.setUserName(datum.getUserName());
                eventBean1.setUserId(datum.getUserId());
                eventBean1.setType(1);
                eventBean1.setAttendanceType(courseInfoFormListBean.getAttendanceType());
                eventBean1.setRequiredTime(courseInfoFormListBean.getRequiredTime());
                eventBean1.setSignInTime(courseInfoFormListBean.getSignInTime());
                eventBean1.setClockName(courseInfoFormListBean.getClockName());
                eventBean1.setSortName(courseInfoFormListBean.getSortName());
                eventBean1.setEventName(courseInfoFormListBean.getEventName());
                eventBean1.setSection(courseInfoFormListBean.getSection());
                eventBean1.setTimeFrame(courseInfoFormListBean.getTimeFrame());
                eventBean1.setCourseInfo(courseInfoFormListBean.getCourseInfo());
                eventBean1.setCourseName(courseInfoFormListBean.getCourseName());
                eventBean1.setCourseStartTime(courseInfoFormListBean.getCourseStartTime());
                eventBean1.setSectionOrder(courseInfoFormListBean.getSectionOrder());
                eventBean1.setLeaveStartTime(courseInfoFormListBean.getLeaveStartTime());
                eventBean1.setLeaveEndTime(courseInfoFormListBean.getLeaveEndTime());
                eventBean1.setAttendanceSignInOut(courseInfoFormListBean.getAttendanceSignInOut());
                eventBeanList.add(eventBean1);
            }
        }
        return eventBeanList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentStatisticsListBinding bind = FragmentStatisticsListBinding.bind(view);
        bind.expandableListView.setVisibility(View.GONE);
        bind.recyclerview.setVisibility(View.VISIBLE);
        eventBeanList = toEventBeanList(null,data);
        weekStatisticsFlatListAdapter = new TeacherWeekStatisticsFlatListAdapter();
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerview.setAdapter(weekStatisticsFlatListAdapter);
        weekStatisticsFlatListAdapter.setOnItemClickListener((adapter, view1, position) -> {
            final EventBean eventBean = eventBeanList.get(position);
            if (eventBean.getType() == 0) {
                Log.e(TAG, "onViewCreated: position=" + position + ",点击" + eventBean.getUserName());
                eventBeanList = toEventBeanList(eventBean, data);
                weekStatisticsFlatListAdapter.setList(eventBeanList);
            }
        });
        weekStatisticsFlatListAdapter.setList(eventBeanList);
//        weekStatisticsListAdapter = new TeacherWeekStatisticsListAdapter(getContext(), data);
//        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        bind.recyclerview.setAdapter(weekStatisticsListAdapter);
//        weekStatisticsListAdapter.setOnClickedListener(position -> {
//            final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean peopleBean = data.get(position);
//            peopleBean.setChecked(!peopleBean.getChecked());
//            //weekStatisticsListAdapter.notifyDataSetChanged();
//            weekStatisticsListAdapter.notifyItemChanged(position);
//        });
        //group数据
//       ArrayList<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> mGroupList = new ArrayList<>();
//        //item数据
//        ArrayList<List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean>> mItemSet = new ArrayList<>();
//        for (TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean datum : data) {
//            mGroupList.add(datum);
//            mItemSet.add(datum.getCourseInfoFormList());
//        }
//        TeacherWeekStatisticsExpandableListAdapter adapter = new TeacherWeekStatisticsExpandableListAdapter(requireContext(), mGroupList, mItemSet);
//        bind.expandableListView.setAdapter(adapter);
//        bind.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);
        if (data !=null && data.isEmpty()) {
            bind.blankPage.setVisibility(View.VISIBLE);
        } else {
            bind.blankPage.setVisibility(View.GONE);
        }

    }
}