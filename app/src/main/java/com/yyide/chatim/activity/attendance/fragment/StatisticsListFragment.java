package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim.databinding.FragmentStatisticsListBinding;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsListFragment extends Fragment {
    private static final String TAG = "StatisticsListFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";

    // TODO: Rename and change types of parameters
    private String type;
    private String mParam2;
    private WeekStatisticsListAdapter weekStatisticsListAdapter;

    public void setData(List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> data) {
        this.data = data;
    }

    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> data = new ArrayList<>();
    public StatisticsListFragment() {
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
    public static StatisticsListFragment newInstance(String param) {
        StatisticsListFragment fragment = new StatisticsListFragment();
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
//        if (data == null){
//            return;
//        }
        final FragmentStatisticsListBinding bind = FragmentStatisticsListBinding.bind(view);
        weekStatisticsListAdapter = new WeekStatisticsListAdapter(getContext(), data);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerview.setAdapter(weekStatisticsListAdapter);
        weekStatisticsListAdapter.setOnClickedListener(position -> {
            final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean peopleBean = data.get(position);
            peopleBean.setChecked(!peopleBean.isChecked());
            weekStatisticsListAdapter.notifyDataSetChanged();
        });

        if (data !=null && data.isEmpty()) {
            bind.blankPage.setVisibility(View.VISIBLE);
        } else {
            bind.blankPage.setVisibility(View.GONE);
        }

    }
}