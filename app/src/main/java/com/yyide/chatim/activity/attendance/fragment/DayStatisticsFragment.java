package com.yyide.chatim.activity.attendance.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beiing.weekcalendar.WeekCalendar;
import com.beiing.weekcalendar.listener.DateSelectListener;
import com.beiing.weekcalendar.listener.GetViewHelper;
import com.beiing.weekcalendar.utils.CalendarUtil;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.attendance.DayStatisticsListAdapter;
import com.yyide.chatim.adapter.attendance.StudentDayStatisticsListAdapter;
import com.yyide.chatim.databinding.FragmentDayStatisticsBinding;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.dialog.SwichClassPop;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.StudentDayStatisticsBean;
import com.yyide.chatim.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private List<StudentDayStatisticsBean> studentDayStatisticsBeanList;
    private FragmentDayStatisticsBinding mViewBinding;
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private List<LeaveDeptRsp.DataBean> eventList = new ArrayList<>();

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

    private void initClassData() {
        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
        final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        classList.clear();
        for (GetUserSchoolRsp.DataBean.FormBean formBean : form) {
            final String classesName = formBean.classesName;
            final String classesId = formBean.classesId;
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            dataBean.setDeptId(Long.parseLong(classesId));
            dataBean.setDeptName(classesName);
            dataBean.setIsDefault(Objects.equals(classInfo.classesId, classesId) ? 1 : 0);
            classList.add(dataBean);
        }
    }

    private void initEventData() {
        eventList.clear();
        for (int i = 0; i < 3; i++) {
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            dataBean.setIsDefault(i == 1 ? 1 : 0);
            dataBean.setDeptName("学生出入校考勤上午到校" + i);
            dataBean.setDeptId(i + 100);
            eventList.add(dataBean);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentDayStatisticsBinding bind = FragmentDayStatisticsBinding.bind(view);
        mViewBinding = bind;
        eventDates = new ArrayList<>();
        initClassData();
        initEventData();
        final String time = DateUtils.switchTime(new Date(), "yyyy-MM-dd");
        mViewBinding.tvCurrentDate.setText(time);
        final Optional<LeaveDeptRsp.DataBean> eventOptional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        final LeaveDeptRsp.DataBean dataBean = eventOptional.get();
        mViewBinding.tvAttendanceType.setText(dataBean.getDeptName());
        if (eventList.size() <= 1) {
            mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_down);
            //设置图片大小，必须设置
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, drawable, null);
            mViewBinding.tvAttendanceType.setOnClickListener(v -> {
                final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), 3, eventList);
                deptSelectPop.setOnCheckedListener((id, dept) -> {
                    Log.e(TAG, "事件选择: id=" + id + ", dept=" + dept);
                    mViewBinding.tvAttendanceType.setText(dept);
                });
            });
        }
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
        mViewBinding.tvClassName.setText(clazzBean.getDeptName());
        if (classList.size() <= 1) {
            mViewBinding.tvSwitch.setVisibility(View.GONE);
        } else {
            mViewBinding.tvSwitch.setVisibility(View.VISIBLE);
            mViewBinding.tvSwitch.setOnClickListener(v -> {
                        final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), 2, classList);
                        deptSelectPop.setOnCheckedListener((id, dept) -> {
                            Log.e(TAG, "班级选择: id=" + id + ", dept=" + dept);
                            mViewBinding.tvClassName.setText(dept);
                        });
                    }
            );
        }

        final WeekCalendar weekCalendar = view.findViewById(R.id.week_calendar);
        weekCalendar.setGetViewHelper(new GetViewHelper() {
            @Override
            public View getDayView(int position, View convertView, ViewGroup parent, DateTime dateTime, boolean select) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_day, parent, false);
                }
                TextView tvDay = (TextView) convertView.findViewById(R.id.tv_day);
                tvDay.setText(dateTime.toString("d"));
                if (CalendarUtil.isToday(dateTime) && select) {
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.circular_blue);
                } else if (CalendarUtil.isToday(dateTime)) {
                    tvDay.setTextColor(getResources().getColor(R.color.colorTodayText));
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                } else if (select) {
                    tvDay.setTextColor(Color.WHITE);
                    tvDay.setBackgroundResource(R.drawable.circular_blue);
                } else {
                    tvDay.setTextColor(Color.BLACK);
                    tvDay.setBackgroundColor(Color.TRANSPARENT);
                }

                ImageView ivPoint = (ImageView) convertView.findViewById(R.id.iv_point);
                ivPoint.setVisibility(View.GONE);
                for (DateTime d : eventDates) {
                    if (CalendarUtil.isSameDay(d, dateTime)) {
                        ivPoint.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                return convertView;
            }

            @Override
            public View getWeekView(int position, View convertView, ViewGroup parent, String week) {
                Log.e(TAG, "getWeekView: ");
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_week, parent, false);
                }
                TextView tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
                tvWeek.setText(week);
                if (position == 0 || position == 6) {
                    tvWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return convertView;
            }
        });

        weekCalendar.setDateSelectListener(selectDate -> {
            String text = selectDate.toString("yyyy-MM-dd");
            this.mViewBinding.tvCurrentDate.setText(text);
            Log.e(TAG, "onDateSelect: " + text);
        });

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        initdata();
        if (SpData.getIdentityInfo().staffIdentity()){
            final DayStatisticsListAdapter dayStatisticsListAdapter = new DayStatisticsListAdapter(getContext(), data);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerview.setAdapter(dayStatisticsListAdapter);
        }else {
            final StudentDayStatisticsListAdapter studentDayStatisticsListAdapter = new StudentDayStatisticsListAdapter(getActivity(), studentDayStatisticsBeanList);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerview.setAdapter(studentDayStatisticsListAdapter);
        }
    }

    private void initdata() {
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

        studentDayStatisticsBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final StudentDayStatisticsBean studentDayStatisticsBean = new StudentDayStatisticsBean();
            studentDayStatisticsBean.setEventName("上午到校"+i);
            studentDayStatisticsBean.setTime("2021-03-04");
            studentDayStatisticsBean.setEventStatus((int)(Math.random()*10)+1);
            studentDayStatisticsBean.setEventTime("打卡时间：07:0"+i);
            studentDayStatisticsBeanList.add(studentDayStatisticsBean);
        }
    }


}