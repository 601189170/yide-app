package com.yyide.chatim.fragment.attendance.v2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim.adapter.attendance.v2.StudentWeekStatisticsListAdapter;
import com.yyide.chatim.adapter.attendance.v2.TeacherWeekStatisticsListAdapter;
import com.yyide.chatim.databinding.FragmentStatisticsListBinding;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceWeekMonthRsp;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: "+type+",hashCode,"+this.hashCode()+" data="+ JSON.toJSONString(data));
        final FragmentStatisticsListBinding bind = FragmentStatisticsListBinding.bind(view);
        weekStatisticsListAdapter = new TeacherWeekStatisticsListAdapter(getContext(), data);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerview.setAdapter(weekStatisticsListAdapter);
        weekStatisticsListAdapter.setOnClickedListener(position -> {
            final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean peopleBean = data.get(position);
            peopleBean.setChecked(!peopleBean.getChecked());
            //weekStatisticsListAdapter.notifyDataSetChanged();
            weekStatisticsListAdapter.notifyItemChanged(position);
        });
        if (data !=null && data.isEmpty()) {
            bind.blankPage.setVisibility(View.VISIBLE);
        } else {
            bind.blankPage.setVisibility(View.GONE);
        }

    }
}