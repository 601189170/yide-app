package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.yyide.chatim.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayStatisticsFragment extends Fragment {

    public DayStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayStatisticsFragment newInstance(String param1, String param2) {
        DayStatisticsFragment fragment = new DayStatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_statistics, container, false);
    }
}