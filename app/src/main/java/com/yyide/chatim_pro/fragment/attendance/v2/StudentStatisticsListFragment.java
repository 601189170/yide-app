package com.yyide.chatim_pro.fragment.attendance.v2;

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
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim_pro.adapter.attendance.v2.StudentWeekStatisticsListAdapter;
import com.yyide.chatim_pro.databinding.FragmentStatisticsListBinding;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceWeekMonthRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu tao
 * @version v2
 * @date 2021/11/4 14:10
 * @description
 */
public class StudentStatisticsListFragment extends Fragment {
    private static final String TAG = "StatisticsListFragment";
    private static final String ARG_TYPE = "type";

    private String type;
    private String mParam2;

    private StudentWeekStatisticsListAdapter dayStatisticsDetailListAdapter;

    public void setData(List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> data) {
        this.data = data;
    }

    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> data = new ArrayList<>();
    public StudentStatisticsListFragment() {
        // Required empty public constructor
    }

    public static StudentStatisticsListFragment newInstance(String param) {
        StudentStatisticsListFragment fragment = new StudentStatisticsListFragment();
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
        dayStatisticsDetailListAdapter = new StudentWeekStatisticsListAdapter(getContext(), data);
        bind.recyclerview.setVisibility(View.VISIBLE);
        bind.expandableListView.setVisibility(View.GONE);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerview.setAdapter(dayStatisticsDetailListAdapter);
        if (data !=null && data.isEmpty()) {
            bind.blankPage.setVisibility(View.VISIBLE);
        } else {
            bind.blankPage.setVisibility(View.GONE);
        }

    }
}