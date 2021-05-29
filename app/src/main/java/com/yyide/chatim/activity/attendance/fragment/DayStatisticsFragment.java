package com.yyide.chatim.activity.attendance.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beiing.weekcalendar.WeekCalendar;
import com.beiing.weekcalendar.listener.DateSelectListener;
import com.beiing.weekcalendar.listener.GetViewHelper;
import com.beiing.weekcalendar.utils.CalendarUtil;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.DayStatisticsListAdapter;
import com.yyide.chatim.databinding.FragmentDayStatisticsBinding;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayStatisticsFragment extends Fragment {
    private static final String TAG = DayStatisticsFragment.class.getSimpleName();
    private List<DateTime> eventDates;
    private List<DayStatisticsBean> data;
    private FragmentDayStatisticsBinding binding;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentDayStatisticsBinding bind = FragmentDayStatisticsBinding.bind(view);
        this.binding = bind;
        eventDates = new ArrayList<>();
        final String time = DateUtils.switchTime(new Date(), "yyyy-MM-dd");
        this.binding.tvCurrentDate.setText(time);
        final WeekCalendar weekCalendar = view.findViewById(R.id.week_calendar);
        weekCalendar.setGetViewHelper(new GetViewHelper() {
            @Override
            public View getDayView(int position, View convertView, ViewGroup parent, DateTime dateTime, boolean select) {
                if(convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_day, parent, false);
                }
                TextView tvDay = (TextView) convertView.findViewById(R.id.tv_day);
                tvDay.setText(dateTime.toString("d"));
                if(CalendarUtil.isToday(dateTime) && select){
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.circular_blue);
                } else if(CalendarUtil.isToday(dateTime)){
                    tvDay.setTextColor(getResources().getColor(R.color.colorTodayText));
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                } else if(select){
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.circular_blue);
                } else {
                    tvDay.setTextColor(Color.BLACK);
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                }

                ImageView ivPoint = (ImageView) convertView.findViewById(R.id.iv_point);
                ivPoint.setVisibility(View.GONE);
                for (DateTime d : eventDates) {
                    if(CalendarUtil.isSameDay(d, dateTime)){
                        ivPoint.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                return convertView;
            }

            @Override
            public View getWeekView(int position, View convertView, ViewGroup parent, String week) {
                Log.e(TAG, "getWeekView: " );
                if(convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_week, parent, false);
                }
                TextView tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
                tvWeek.setText(week);
                if(position == 0 || position == 6){
                    tvWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return convertView;
            }
        });

        weekCalendar.setDateSelectListener(selectDate -> {
            String text = selectDate.toString("yyyy-MM-dd");
            this.binding.tvCurrentDate.setText(text);
            Log.e(TAG, "onDateSelect: "+text );
        });

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        initdata();
        final DayStatisticsListAdapter dayStatisticsListAdapter = new DayStatisticsListAdapter(getContext(), data);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(dayStatisticsListAdapter);
    }

    private void initdata(){
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final DayStatisticsBean dayStatisticsBean = new DayStatisticsBean();
            dayStatisticsBean.setTime("2021-03-04");
            dayStatisticsBean.setAbsence(23);
            dayStatisticsBean.setDue(12);
            dayStatisticsBean.setLate(34);
            dayStatisticsBean.setNormal(32);
            dayStatisticsBean.setRate(50);
            dayStatisticsBean.setAsk_for_leave(32);
            data.add(dayStatisticsBean);
        }
    }


}