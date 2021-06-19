package com.yyide.chatim.activity.attendance.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentWeekStatisticsBinding;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.presenter.attendance.WeekStatisticsPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.attendace.WeekMonthStatisticsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekStatisticsFragment extends BaseMvpFragment<WeekStatisticsPresenter> implements WeekMonthStatisticsView {
    private static final String TAG = "WeekStatisticsFragment";
    private FragmentWeekStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    private static final String ARG_TYPE = "type";
    //当前界面类型 周统计 月统计
    private int currentPage = 1;
    private String type;
    //月
    private int month;
    private int currentMonth;
    //周
    private int week;
    private int currentWeek;
    //判断当前是周统计还是月统计
    private boolean isWeekStatistics;
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private List<LeaveDeptRsp.DataBean> eventList = new ArrayList<>();
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean> studentsBeanList = new ArrayList<>();
    //迟到的数据列表
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> latePeople = new ArrayList<>();
    //请假的数据类别
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> leavePeople = new ArrayList<>();
    //缺勤的数据类别
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> absencePeople = new ArrayList<>();
    //早退的数据列表
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> leaveEarlyPeople = new ArrayList<>();
    private String currentDate;//当前选择的日期
    private String currentClass;//当前班级

    public WeekStatisticsFragment() {
        // Required empty public constructor
    }


    public static WeekStatisticsFragment newInstance(String type) {
        WeekStatisticsFragment fragment = new WeekStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_statistics, container, false);
    }

    private void setMonth() {
        if (month > currentMonth || month < 1) {
            Log.e(TAG, "setMonth: 不能查询未来的事件或者超过时间范围的事件");
            return;
        }
        if (currentMonth == month) {
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else if (month == 1) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        mViewBinding.tvWeek.setText(String.format(getActivity().getString(R.string.month), month));
        queryAttStatsData(currentClass, currentDate,currentPage);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeek() {
        if (week > currentWeek || week < 1) {
            Log.e(TAG, "setWeek: 不能查询未来的事件或者超过时间范围的事件");
            return;
        }
        if (currentWeek == week) {
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else if (week == 1) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final String[] monthWeek = DateUtils.getFirstDayAndLastDayByMonthWeek(year, month, week);
        final String format1 = getActivity().getString(R.string.week);
        mViewBinding.tvWeek.setText(String.format(format1, monthWeek[0], monthWeek[1]));
        queryAttStatsData(currentClass, currentDate,currentPage);
    }

    /**
     * 请求考勤统计数据
     *
     * @param classId    班级id
     * @param searchTime 时间
     */
    private void queryAttStatsData(String classId, String searchTime,int page) {
        if (page<1){
            page = 1;
        }
        mvpPresenter.getAttendanceStatsData(classId, searchTime, isWeekStatistics ? "2" : "3",page);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentWeekStatisticsBinding.bind(view);
        if ("周统计".equals(type)) {
            isWeekStatistics = true;
        }
        //初始化班级view
        initClassData();
        initClassView();
        if (isWeekStatistics) {
            mViewBinding.tvWeeklyStatistical.setText("每周统计");
            final Calendar calendar = Calendar.getInstance();
            final int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
            week = weekOfMonth;
            currentWeek = weekOfMonth;
            setWeek();
        } else {
            mViewBinding.tvWeeklyStatistical.setText("每月统计");
            //获取当前默认日期
            final Calendar calendar = Calendar.getInstance();
            Log.e(TAG, "calendar: " + calendar);
            final int i = calendar.get(Calendar.MONTH);
            Log.e(TAG, "月: " + i);
            month = i + 1;
            currentMonth = month;
            setMonth();
        }

        //queryAttStatsData(currentClass, currentDate,currentPage);
        mViewBinding.ivLeft.setOnClickListener(v -> {
            //向左选择日期
            if (isWeekStatistics) {
                week--;
                currentPage++;
                setWeek();
            } else {
                month--;
                currentPage++;
                setMonth();
            }
        });
        mViewBinding.ivRight.setOnClickListener(v -> {
            //向右选择日期
            if (isWeekStatistics) {
                week++;
                currentPage--;
                setWeek();
            } else {
                month++;
                currentPage--;
                setMonth();
            }

        });
        //initPeopleListView();

    }

    private void initPeopleListView() {
        mViewBinding.clAttendanceType.setVisibility(View.VISIBLE);
        List<String> listTab = new ArrayList<>();
        listTab.add("缺勤");
        listTab.add("迟到");
        listTab.add("请假");
        listTab.add("早退");
        final StatisticsListFragment absenceListFragment = StatisticsListFragment.newInstance(listTab.get(0));
        absenceListFragment.setData(absencePeople);
        fragments.add(absenceListFragment);
        final StatisticsListFragment lateListFragment = StatisticsListFragment.newInstance(listTab.get(1));
        lateListFragment.setData(latePeople);
        fragments.add(lateListFragment);
        final StatisticsListFragment leaveListFragment = StatisticsListFragment.newInstance(listTab.get(2));
        leaveListFragment.setData(leavePeople);
        fragments.add(leaveListFragment);
        final StatisticsListFragment leaveEarlyFragment = StatisticsListFragment.newInstance(listTab.get(3));
        leaveEarlyFragment.setData(leaveEarlyPeople);
        fragments.add(leaveEarlyFragment);

        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setUserInputEnabled(false);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.e(TAG, "switch fragment: "+position );
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });
        mViewBinding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(TAG, "onPageSelected: "+position );
                switch (position){
                    case 0:
                        mViewBinding.rgAttendanceType.check(R.id.rb_absence);
                        break;
                    case 1:
                        mViewBinding.rgAttendanceType.check(R.id.rb_late);
                        break;
                    case 2:
                        mViewBinding.rgAttendanceType.check(R.id.rb_leave);
                        break;
                    case 3:
                        mViewBinding.rgAttendanceType.check(R.id.rb_leave_early);
                        break;
                    default:
                        break;
                }

            }
        });

        mViewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            Log.e(TAG, "initPeopleListView: "+checkedId );
            switch (checkedId) {
                case R.id.rb_absence:
                    mViewBinding.viewpager.setCurrentItem(0);
                    break;
                case R.id.rb_late:
                    mViewBinding.viewpager.setCurrentItem(1);
                    break;
                case R.id.rb_leave:
                    mViewBinding.viewpager.setCurrentItem(2);
                    break;
                case R.id.rb_leave_early:
                    mViewBinding.viewpager.setCurrentItem(3);
                    break;

                default:
                    break;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
        mViewBinding.tvClassName.setText(clazzBean.getDeptName());
        currentClass = "" + clazzBean.getDeptId();
        if (classList.size() <= 1) {
            mViewBinding.tvSwitch.setVisibility(View.GONE);
        } else {
            mViewBinding.tvSwitch.setVisibility(View.VISIBLE);
            mViewBinding.tvSwitch.setOnClickListener(v -> {
                        final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), 2, classList);
                        deptSelectPop.setOnCheckedListener((id, dept) -> {
                            Log.e(TAG, "班级选择: id=" + id + ", dept=" + dept);
                            mViewBinding.tvClassName.setText(dept);
                            currentClass = "" + id;
                            currentPage = 1;
                            queryAttStatsData(currentClass, currentDate,1);
                        });
                    }
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initEventView() {
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
                    showData(dept);
                });
            });
        }
    }

    @Override
    protected WeekStatisticsPresenter createPresenter() {
        return new WeekStatisticsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void attendanceStatisticsSuccess(AttendanceWeekStatsRsp attendanceWeekStatsRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + attendanceWeekStatsRsp.toString());
        if (attendanceWeekStatsRsp.getCode() == 200) {
            if (attendanceWeekStatsRsp.getData() == null) {
                showBlank(true);
                return;
            }
            showBlank(false);
            final List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean> attendancesForm = attendanceWeekStatsRsp.getData().getAttendancesForm();
            eventList.clear();
            studentsBeanList.clear();
            for (AttendanceWeekStatsRsp.DataBean.AttendancesFormBean attendancesFormBean : attendancesForm) {
                final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean students = attendancesFormBean.getStudents();
                studentsBeanList.add(students);
                //考勤时间类型
                final String name = students.getName();
                final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
                dataBean.setIsDefault(0);
                dataBean.setDeptName(name);
                dataBean.setDeptId(0);
                eventList.add(dataBean);
            }
            //设置事件
            if (eventList.size() != 0) {
                //默认选择第一个事件
                eventList.get(0).setIsDefault(1);
                //初始化事件view
                initEventView();
            }

            //默认选择第一个事件的统计
            if (studentsBeanList.size() != 0) {
                showData(studentsBeanList.get(0).getName());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showData(String eventName) {
        //每次更新数据默认选择缺勤rb
        mViewBinding.rgAttendanceType.check(R.id.rb_absence);
        AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean studentsBean = null;
        for (AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean studentsBean1 : studentsBeanList) {
            if (studentsBean1.getName().equals(eventName)) {
                studentsBean = studentsBean1;
                break;
            }
        }
        if (studentsBean == null) {
            Log.e(TAG, "showData: data is null");
            return;
        }

        //缺勤人数
        mViewBinding.tvAbsenceNum.setText(String.valueOf(studentsBean.getAbsence()));
        //请假人数
        mViewBinding.tvAskForLeaveNum.setText(String.valueOf(studentsBean.getLeave()));
        //迟到人数
        mViewBinding.tvLateNum.setText(String.valueOf(studentsBean.getLate()));
        //早退人数
        mViewBinding.tvLateEarlyNum.setText(String.valueOf(studentsBean.getLeaveEarly()));

        latePeople.clear();
        latePeople.addAll(filterNullStatusData(studentsBean.getLatePeople()));
        absencePeople.clear();
        absencePeople.addAll(filterNullStatusData(studentsBean.getAbsencePeople()));
        leavePeople.clear();
        leavePeople.addAll(filterNullStatusData(studentsBean.getLeavePeople()));
        leaveEarlyPeople.clear();
        leaveEarlyPeople.addAll(filterNullStatusData(studentsBean.getLeaveEarlyPeople()));
        //mViewBinding.viewpager.notify();
        initPeopleListView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> filterNullStatusData(List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> peopleBeanList) {
        return peopleBeanList.stream().filter(it -> !TextUtils.isEmpty(it.getStatus())).collect(Collectors.toList());
    }

    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        showBlank(true);
    }

    private void showBlank(boolean show){
        mViewBinding.blankPage.setVisibility(show?View.VISIBLE:View.GONE);
        mViewBinding.constraintLayout.setVisibility(show?View.GONE:View.VISIBLE);
    }
}