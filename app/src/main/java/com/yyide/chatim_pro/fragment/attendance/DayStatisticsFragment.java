package com.yyide.chatim_pro.fragment.attendance;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.beiing.weekcalendar.WeekCalendar;
import com.beiing.weekcalendar.listener.GetViewHelper;
import com.beiing.weekcalendar.utils.CalendarUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.activity.attendance.StatisticsDetailActivity;
import com.yyide.chatim_pro.adapter.attendance.DayStatisticsListAdapter;
import com.yyide.chatim_pro.adapter.attendance.StudentDayStatisticsListAdapter;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.databinding.FragmentDayStatisticsBinding;
import com.yyide.chatim_pro.dialog.DeptSelectPop;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.LeaveDeptRsp;
import com.yyide.chatim_pro.presenter.attendance.DayStatisticsPresenter;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.view.attendace.DayStatisticsView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayStatisticsFragment extends BaseMvpFragment<DayStatisticsPresenter> implements DayStatisticsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = DayStatisticsFragment.class.getSimpleName();
    private List<DateTime> eventDates;
    private final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean> data = new ArrayList<>();
    private final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean> attendancesFormBeanList = new ArrayList<>();

    private FragmentDayStatisticsBinding mViewBinding;
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private List<LeaveDeptRsp.DataBean> eventList = new ArrayList<>();
    private String currentDate;//当前选择的日期
    private String currentClass;//当前班级
    private String currentStudentId;//当前学生id
    private int dialogType;
    private String currentClassName;//当前班级
    private String currentEvent;//当前事件
    private String historyEvent;//上一次选择的事件
    private DayStatisticsListAdapter dayStatisticsListAdapter;
    private StudentDayStatisticsListAdapter studentDayStatisticsListAdapter;
    private boolean refresh;

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
//        if (!SpData.getIdentityInfo().staffIdentity()) {
//            final String studentName = SpData.getIdentityInfo().studentName;
//            mViewBinding.tvClassName.setText(studentName);
//            return;
//        }
        if (SpData.getIdentityInfo().staffIdentity()) {
            dialogType = 2;
        } else {
            dialogType = 4;
        }
//        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
//        //final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
//        classList.clear();
//        final String classesStudentName = SpData.getClassesStudentName();
//        for (GetUserSchoolRsp.DataBean.FormBean formBean : form) {
//            final String classesName = formBean.classesName;
//            final String studentName = formBean.classesStudentName;
//            final String classesId = formBean.classesId;
//            final String studentId = formBean.studentId;
//            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
//            dataBean.setDeptId(studentId);
//            dataBean.setClassId(classesId);
//            dataBean.setIsDefault(0);
//            if (SpData.getIdentityInfo().staffIdentity()) {
//                dataBean.setDeptName(classesName);
//                if (classesName.equals(classesStudentName)){
//                    dataBean.setIsDefault(1);
//                }
//            } else {
//                dataBean.setDeptName(studentName);
//                if (studentName.equals(classesStudentName)){
//                    dataBean.setIsDefault(1);
//                }
//            }
//            classList.add(dataBean);
//        }

        if (classList.isEmpty()) {
            Log.e(TAG, "initClassData: 当前账号没有学生" );
            mViewBinding.tvClassName.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentDayStatisticsBinding.bind(view);
        eventDates = new ArrayList<>();
        initClassData();
        initClassView();
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mViewBinding.swipeRefreshLayout.setEnabled(verticalOffset >= 0);//页面滑动到顶部，才可以下拉刷新
        });
        String time = DateUtils.switchTime(new Date(), "yyyy-MM-dd");
        final String time1 = DateUtils.formatTime(time, "yyyy-MM-dd", "MM月");
        mViewBinding.tvCurrentDate.setText(time1);
        currentDate = DateUtils.formatTime(time,"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss");
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
            final String time2 = DateUtils.formatTime(text, "yyyy-MM-dd", "MM月");
            this.mViewBinding.tvCurrentDate.setText(time2);
            Log.e(TAG, "onDateSelect: " + text);
            currentDate = DateUtils.formatTime(text,"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss");;
            queryAttStatsData(currentClass,currentDate);
        });

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        if (SpData.getIdentityInfo().staffIdentity()) {
            //统计-日-班主任和教师视角
            dayStatisticsListAdapter = new DayStatisticsListAdapter(getContext(), data);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerview.setAdapter(dayStatisticsListAdapter);
            dayStatisticsListAdapter.setOnClickedListener(position -> {
                //进入考勤详情页
                //final AttendanceStatsRsp.DataBean.AttendancesFormBean attendancesFormBean = data.get(position);
                AttendanceDayStatsRsp.DataBean.AttendancesFormBean attendancesFormBeanData = null;
                for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean attendancesFormBean : attendancesFormBeanList) {
                        if (currentEvent.equals(attendancesFormBean.getAttNameA())){
                            attendancesFormBeanData = attendancesFormBean;
                            break;
                        }
                }
                if (attendancesFormBeanData == null){
                    return;
                }
                final String jsonString = JSON.toJSONString(attendancesFormBeanData);
                Log.e(TAG, "进入考勤详情页: "+jsonString );
                final Intent intent = new Intent(getActivity(), StatisticsDetailActivity.class);
                intent.putExtra("data",jsonString);
                intent.putExtra("position",position);
                intent.putExtra("currentClass",currentClassName);
                startActivity(intent);
            });
        } else {
            //统计-日-家长视角
            studentDayStatisticsListAdapter = new StudentDayStatisticsListAdapter(getActivity(), data);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerview.setAdapter(studentDayStatisticsListAdapter);
        }
        //请求数据
        queryAttStatsData(currentClass,currentDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            mViewBinding.tvClassName.setText(clazzBean.getDeptName());
            currentClass = clazzBean.getClassId();
            currentStudentId = clazzBean.getDeptId();
            currentClassName = clazzBean.getDeptName();
            if (classList.size() <= 1) {
                mViewBinding.tvClassName.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow_down);
                //设置图片大小，必须设置
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mViewBinding.tvClassName.setCompoundDrawables(null, null, drawable, null);
                mViewBinding.tvClassName.setOnClickListener(v -> {
                            final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), dialogType, classList);
                            deptSelectPop.setOnCheckedListener((dataBean) -> {
                                Log.e(TAG, "班级选择:" + dataBean.toString());
                                mViewBinding.tvClassName.setText(dataBean.getDeptName());
                                //班级id
                                currentClass = dataBean.getClassId();
                                //学生id
                                currentStudentId = dataBean.getDeptId();
                                currentClassName = dataBean.getDeptName();
                                queryAttStatsData(currentClass,currentDate);
                            });
                        }
                );
            }
        }
    }

    private void queryAttStatsData(String classId,String searchTime){
        mvpPresenter.getAttendanceStatsData(currentStudentId,classId,searchTime,"1");
    }

    @Override
    protected DayStatisticsPresenter createPresenter() {
        return new DayStatisticsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    private void showBlank(boolean show){
        mViewBinding.blankPage.setVisibility(show?View.VISIBLE:View.GONE);
        //mViewBinding.recyclerview.setVisibility(show?View.GONE:View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void attendanceStatisticsSuccess(AttendanceDayStatsRsp attendanceWeekStatsRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + attendanceWeekStatsRsp.toString());
        if (refresh){
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        if (attendanceWeekStatsRsp.getCode() == 200){
            if (attendanceWeekStatsRsp.getData() == null){
                ToastUtils.showShort(""+attendanceWeekStatsRsp.getMsg());
            }
            attendancesFormBeanList.clear();
            data.clear();
            if (attendanceWeekStatsRsp.getData() == null || attendanceWeekStatsRsp.getData().getAttendancesForm() == null) {
                mViewBinding.tvAttendanceType.setVisibility(View.GONE);
                showBlank(true);
                notifyAdapter();
                return;
            }

            final List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean> attendancesForm = attendanceWeekStatsRsp.getData().getAttendancesForm();
            if (attendancesForm == null || attendancesForm.isEmpty()){
                eventList.clear();
                currentEvent = "";
                mViewBinding.tvAttendanceType.setText("");
                mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, null, null);
                showBlank(true);
                return;
            }
            showBlank(false);
            attendancesFormBeanList.addAll(attendancesForm);
//            currentEvent = attendancesFormBeanList.get(0).getAttNameA();
//            data.addAll(attendancesFormBeanList.get(0).getStudentLists());
//            //更新布局
//            if (SpData.getIdentityInfo().staffIdentity()) {
//                dayStatisticsListAdapter.notifyDataSetChanged();
//            }else {
//                studentDayStatisticsListAdapter.notifyDataSetChanged();
//            }
            initEventData();
        } else {
            ToastUtils.showShort("服务器异常："+attendanceWeekStatsRsp.getCode()+","+attendanceWeekStatsRsp.getMsg());
            mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            //showData(null);
            showBlank(true);
        }
    }


    private void showData(String eventName){
        List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean> studentListsBean = null;
        for (AttendanceDayStatsRsp.DataBean.AttendancesFormBean attendancesFormBean : attendancesFormBeanList) {
            if (attendancesFormBean.getAttNameA().equals(eventName)) {
                studentListsBean = attendancesFormBean.getStudentLists();
                break;
            }
        }

        if (studentListsBean == null){
            Log.e(TAG, "showData: data is null");
            return;
        }
        data.clear();
        data.addAll(studentListsBean);
        //更新布局
        notifyAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initEventData() {
        eventList.clear();
        for (int i = 0; i < attendancesFormBeanList.size(); i++) {
            final AttendanceDayStatsRsp.DataBean.AttendancesFormBean attendancesFormBean = attendancesFormBeanList.get(i);
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            if (attendancesFormBean.getAttNameA().equals(historyEvent) || (TextUtils.isEmpty(historyEvent) && i == 0)) {
                dataBean.setIsDefault(1);
                currentEvent = attendancesFormBeanList.get(i).getAttNameA();
                data.addAll(attendancesFormBeanList.get(i).getStudentLists());
                //更新布局
                notifyAdapter();
            } else {
                dataBean.setIsDefault(0);
            }
            //dataBean.setIsDefault(i == 0 ? 1 : 0);
            dataBean.setDeptName(attendancesFormBean.getAttNameA());
            dataBean.setDeptId(i + "100");
            eventList.add(dataBean);
        }
        final Optional<LeaveDeptRsp.DataBean> optional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (!optional.isPresent()){
            eventList.get(0).setIsDefault(1);
            currentEvent = attendancesFormBeanList.get(0).getAttNameA();
            data.addAll(attendancesFormBeanList.get(0).getStudentLists());
            //更新布局
            notifyAdapter();
        }

        final Optional<LeaveDeptRsp.DataBean> eventOptional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        final LeaveDeptRsp.DataBean dataBean = eventOptional.get();
        mViewBinding.tvAttendanceType.setVisibility(View.VISIBLE);
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
                    currentEvent = dept;
                    historyEvent = dept;
                    showData(dept);
                });
            });
        }
    }

    private void notifyAdapter() {
        if (SpData.getIdentityInfo().staffIdentity()) {
            dayStatisticsListAdapter.notifyDataSetChanged();
        } else {
            studentDayStatisticsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        if (refresh){
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        eventList.clear();
        currentEvent = "";
        mViewBinding.tvAttendanceType.setText("");
        mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, null, null);
        showBlank(true);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        queryAttStatsData(currentClass,currentDate);
    }
}