package com.yyide.chatim_pro.fragment.attendance.v2;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.database.ScheduleDaoUtil;
import com.yyide.chatim_pro.databinding.FragmentWeekStatisticsBinding;
import com.yyide.chatim_pro.dialog.DeptSelectPop;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.LeaveDeptRsp;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.presenter.attendance.v2.StudentWeekMonthStatisticsPresenter;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.view.attendace.v2.StudentWeekMonthStatisticsView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liu tao
 * @version v2
 * @date 2021/11/4 9:10
 * @description 家长查看学生周、月考勤数据
 */
public class StudentWeekStatisticsFragment extends BaseMvpFragment<StudentWeekMonthStatisticsPresenter> implements StudentWeekMonthStatisticsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "WeekStatisticsFragment";
    private FragmentWeekStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    private static final String ARG_TYPE = "type";
    //当前界面类型 周统计 月统计
    private String type;
    //月
    private DateTime month;
    //最小月限制
    private int minMonth;
    private DateTime currentMonth;
    //周
    private DateTime week;
    private DateTime currentWeek;
    //判断当前是周统计还是月统计
    private boolean isWeekStatistics;
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private List<LeaveDeptRsp.DataBean> eventList = new ArrayList<>();
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean> studentsBeanList = new ArrayList<>();
    //迟到的数据列表
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> latePeople = new ArrayList<>();
    //请假的数据类别
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> leavePeople = new ArrayList<>();
    //缺勤的数据类别
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> absencePeople = new ArrayList<>();
    //早退的数据列表
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> leaveEarlyPeople = new ArrayList<>();
    private String currentClass;//当前班级
    private String currentStudentId;//当前学生id
    private String currentStudentName;
    private int dialogType;
    private boolean identity = true;//确定是教师还是学生 true是学生  false 是老师
    private boolean eventType;//确定是否是课程考勤 true 事件考勤 false 课程考勤
    private boolean refresh;
    private boolean first = true;
    private String historyEvent;//上一次选择的事件
    private String historyEventId;//上一次选择的事件
    private String historyEventType;//上一次选择的事件
    private String beginDate = "2000-01-01 00:00:00";
    //请求数据需要的时间
    private String startTime;
    private String endTime;

    public StudentWeekStatisticsFragment(String type, String theme, String serverId, String eventType) {
        // Required empty public constructor
        this.type = type;
        this.historyEvent = theme;
        this.historyEventId = serverId;
        this.historyEventType = eventType;
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: isWeekStatistics:" + isWeekStatistics + ",first:" + first);
        if (first && !isWeekStatistics) {
            first = false;
            //queryAttStatsData(currentClass, currentDate, currentPage);
        }
    }

    private void setMonth(boolean init) {
        Log.e(TAG, "currentMonth" + currentMonth + "month：" + month);
        if (!init) {
            if (month.compareTo(currentMonth) > 0) {
                Log.e(TAG, "setMonth: 不能查询未来的事件或者超过时间范围的事件");
                return;
            }
        }
        if (DateUtils.minMonth(beginDate, month)) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        if (month.compareTo(currentMonth) >= 0) {
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
        }
        final String time = ScheduleDaoUtil.INSTANCE.toStringTime(month, "yyyy.MM");
        mViewBinding.tvWeek.setText(time);
        if (!init) {
            //final DateTime dateTime = DateTime.now().withMonthOfYear(month);
            startTime = month.dayOfMonth().withMinimumValue().toString("yyyy-MM-dd ") + "00:00:00";
            endTime = month.dayOfMonth().withMaximumValue().toString("yyyy-MM-dd ") + "23:59:59";
            queryAttStatsData();
        }
    }

    private void setWeek(boolean init) {
        Log.e(TAG, "currentWeek" + currentWeek + "week：" + week);
        if (!init) {
            if (week.compareTo(currentWeek) > 0) {
                Log.e(TAG, "setWeek: 不能查询未来的事件或者超过时间范围的事件");
                return;
            }
        }
        final String format1 = getActivity().getString(R.string.week);
        final DateTime firstDayOfWeek = week.minusDays(week.getDayOfWeek() % 7 - 1);
        final DateTime lastDayOfWeek = firstDayOfWeek.plusDays(6);
        startTime = firstDayOfWeek.toString("yyyy-MM-dd ") + "00:00:00";
        endTime = lastDayOfWeek.toString("yyyy-MM-dd ") + "23:59:59";
        final String monthWeek0 = firstDayOfWeek.toString("MM.dd");
        final String monthWeek1 = lastDayOfWeek.toString("MM.dd");
        Log.e(TAG, "startTime= " + startTime + "endTime = " + endTime);
        if (week.compareTo(currentWeek) >= 0) {
            mViewBinding.ivRight.setVisibility(View.INVISIBLE);
        } else {
            mViewBinding.ivRight.setVisibility(View.VISIBLE);
            //mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.VISIBLE);
        }
        if (DateUtils.minWeek(beginDate, week)) {
            mViewBinding.ivLeft.setVisibility(View.INVISIBLE);
            final String minDate = DateUtils.formatTime(beginDate, null, "MM.dd");
            mViewBinding.tvWeek.setText(String.format(format1, minDate, monthWeek1));
        } else {
            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
            mViewBinding.tvWeek.setText(String.format(format1, monthWeek0, monthWeek1));
        }
        if (!init) {
            queryAttStatsData();
        }
    }

    /**
     * 请求考勤统计数据
     */
    private void queryAttStatsData() {
        Log.e(TAG, "请求考勤统计数据: currentClass=" + currentClass + ", currentStudentId=" + currentStudentId + ",startTime=" + startTime + ",endTime=" + endTime);
        if (TextUtils.isEmpty(currentClass) || TextUtils.isEmpty(currentStudentId)) {
            ToastUtils.showShort("当前账号没有班级，不能查询考勤数据！");
            if (refresh) {
                refresh = false;
                mViewBinding.swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        mvpPresenter.queryAppStudentAttendanceWeekMonth(currentClass, currentStudentId, startTime, endTime);
    }
    /**
     * 初始化
     * @param studentBeanList
     */
    private void initClassData(List<StudentAttendanceWeekMonthRsp.DataBean.StudentBean> studentBeanList){
        dialogType = 4;
        classList.clear();
        for (StudentAttendanceWeekMonthRsp.DataBean.StudentBean studentBean : studentBeanList) {
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            dataBean.setDeptId(studentBean.getStudentId());
            dataBean.setClassId(studentBean.getClassId());
            dataBean.setDeptName(studentBean.getStudentName());
            dataBean.setIsDefault(0);
            if (currentStudentId.equals(studentBean.getStudentId())) {
                dataBean.setIsDefault(1);
            }
            classList.add(dataBean);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentWeekStatisticsBinding.bind(view);
        Log.e(TAG, "onViewCreated: " + type);
        if (getString(R.string.weekly_statistics).equals(type)) {
            isWeekStatistics = true;
        }
        //初始化班级view
        final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        if (classInfo != null){
            currentClass = classInfo.classesId;
            currentStudentId = classInfo.studentId;
            //mViewBinding.tvClassName.setText(classInfo.classesStudentName);
        }
        //initClassData();
        //initClassView();
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mViewBinding.swipeRefreshLayout.setEnabled(verticalOffset >= 0);//页面滑动到顶部，才可以下拉刷新
        });
        if (isWeekStatistics) {
            final DateTime now = DateTime.now();
            week = now;
            currentWeek = now;
            setWeek(false);
        } else {
            //获取当前默认日期
            final Calendar calendar = Calendar.getInstance();
            Log.e(TAG, "calendar: " + calendar);
            final int i = calendar.get(Calendar.MONTH);
            Log.e(TAG, "月: " + i);
            month = DateTime.now();
            currentMonth = month;
            setMonth(false);
        }

        mViewBinding.ivLeft.setOnClickListener(v -> {
            //向左选择日期
            if (isWeekStatistics) {
                //week--;
                week = week.minusWeeks(1);
                setWeek(false);
            } else {
                //month--;
                month = month.minusMonths(1);
                setMonth(false);
            }
        });

        mViewBinding.ivRight.setOnClickListener(v -> {
            //向右选择日期
            if (isWeekStatistics) {
                //week++;
                week = week.plusWeeks(1);
                setWeek(false);
            } else {
                //month++;
                month = month.plusMonths(1);
                setMonth(false);
            }

        });
    }

    private void initPeopleListView() {
        mViewBinding.clAttendanceType.setVisibility(View.VISIBLE);
        List<String> listTab = new ArrayList<>();
        listTab.add(getString(R.string.attendance_absence));
        listTab.add(getString(R.string.attendance_late));
        listTab.add(getString(R.string.ask_for_leave));
        if (eventType) {
            listTab.add(getString(R.string.attendance_leave_early));
        }
        mViewBinding.rbLeaveEarly.setVisibility(eventType ? View.VISIBLE : View.GONE);

        fragments.clear();
        final StudentStatisticsListFragment absenceListFragment = StudentStatisticsListFragment.newInstance(listTab.get(0));
        absenceListFragment.setData(absencePeople);
        fragments.add(absenceListFragment);
        final StudentStatisticsListFragment lateListFragment = StudentStatisticsListFragment.newInstance(listTab.get(1));
        lateListFragment.setData(latePeople);
        fragments.add(lateListFragment);
        final StudentStatisticsListFragment leaveListFragment = StudentStatisticsListFragment.newInstance(listTab.get(2));
        leaveListFragment.setData(leavePeople);
        fragments.add(leaveListFragment);
        if (eventType) {
            final StudentStatisticsListFragment leaveEarlyFragment = StudentStatisticsListFragment.newInstance(listTab.get(3));
            leaveEarlyFragment.setData(leaveEarlyPeople);
            fragments.add(leaveEarlyFragment);
        }

        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setUserInputEnabled(false);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.e(TAG, "switch fragment: " + position);
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
                Log.e(TAG, "onPageSelected: " + position);
                switch (position) {
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
        TextView tvClassName;
        if (identity && eventType) {
            //家长事件考勤
            tvClassName = mViewBinding.layoutHeadStudentEvent.tvClassName;
        } else if (!identity && eventType) {
            //教师事件类型
            tvClassName = mViewBinding.layoutHeadTeacherEvent.tvClassName;
        } else {
            //课程类型
            tvClassName = mViewBinding.layoutHeadTeacherCourse.tvClassName;
        }
        tvClassName.setText(currentStudentName);
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            //tvClassName.setText(clazzBean.getDeptName());
            currentClass = clazzBean.getClassId();
            currentStudentId = clazzBean.getDeptId();
            if (classList.size() <= 1) {
                tvClassName.setCompoundDrawables(null, null, null, null);
            } else {
                final Drawable drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_white_arrow_down);
                //设置图片大小，必须设置
                assert drawable != null;
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvClassName.setCompoundDrawables(null, null, drawable, null);
                tvClassName.setOnClickListener(v -> {
                            final DeptSelectPop deptSelectPop = new DeptSelectPop(requireActivity(), dialogType, classList);
                            deptSelectPop.setOnCheckedListener(dataBean -> {
                                Log.e(TAG, "班级选择: " + dataBean);
                                //tvClassName.setText(dataBean.getDeptName());
                                //班级id
                                currentClass = dataBean.getClassId();
                                //学生id
                                currentStudentId = dataBean.getDeptId();
                                //resetDate();
                                queryAttStatsData();
                            });
                        }
                );
            }
        }
        if (classList.isEmpty()) {
            Log.e(TAG, "initClassData: 当前账号没有学生");
            tvClassName.setVisibility(View.GONE);
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
                deptSelectPop.setOnCheckedListener(dataBean1 -> {
                    Log.e(TAG, "事件选择: id=" + dataBean1.getDeptId() + ", dept=" + dataBean1.getDeptName());
                    mViewBinding.tvAttendanceType.setText(dataBean1.getDeptName());
                    historyEvent = dataBean1.getDeptName();
                    historyEventId = dataBean1.getDeptId();
                    historyEventType = dataBean1.getType();
                    showData(dataBean1.getDeptId(), dataBean1.getType());
                });
            });
        }
    }

    @Override
    protected StudentWeekMonthStatisticsPresenter createPresenter() {
        return new StudentWeekMonthStatisticsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void attendanceStatisticsSuccess(StudentAttendanceWeekMonthRsp attendanceWeekStatsRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + attendanceWeekStatsRsp.toString());
        if (refresh) {
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        eventList.clear();
        studentsBeanList.clear();
        mViewBinding.tvAttendanceType.setVisibility(View.GONE);
        if (attendanceWeekStatsRsp.getCode() == 200) {
            if (attendanceWeekStatsRsp.getData() == null) {
                ToastUtils.showShort("" + attendanceWeekStatsRsp.getMsg());
            }

            if (attendanceWeekStatsRsp.getData() == null || attendanceWeekStatsRsp.getData().getClassroomTeacherAttendanceList() == null) {
                showBlank(false);
                showData(null, null);
                return;
            }
            final List<StudentAttendanceWeekMonthRsp.DataBean.StudentBean> studentInfos = attendanceWeekStatsRsp.getData().getStudentInfos();
            currentStudentName = attendanceWeekStatsRsp.getData().getClassName();
            if (studentInfos !=null && !studentInfos.isEmpty()){
                initClassData(studentInfos);
            }

            showBlank(false);
            final List<StudentAttendanceWeekMonthRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = attendanceWeekStatsRsp.getData().getClassroomTeacherAttendanceList();
            final StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean weeksMonthStatisticalForm = attendanceWeekStatsRsp.getData().getWeeksMonthStatisticalForm();
            final List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean> weeksMonthList = weeksMonthStatisticalForm.getWeeksMonthList();
            final StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean weeksMonthStatisticalCourseForm = weeksMonthStatisticalForm.getWeeksMonthStatisticalCourseForm();

            for (int i = 0; i < classroomTeacherAttendanceList.size(); i++) {
                final StudentAttendanceWeekMonthRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(i);
                final String name = classroomTeacherAttendanceListBean.getTheme();
                final String serverId = classroomTeacherAttendanceListBean.getServerId();
                final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
                if (name.equals(historyEvent) || (TextUtils.isEmpty(historyEvent) && i == 0)) {
                    dataBean.setIsDefault(1);
                } else {
                    dataBean.setIsDefault(0);
                }
                dataBean.setDeptName(name);
                dataBean.setDeptId(serverId);
                dataBean.setType(classroomTeacherAttendanceListBean.getType() + "");
                eventList.add(dataBean);
                if (classroomTeacherAttendanceListBean.getType() == 2) {
                    if (weeksMonthStatisticalCourseForm != null) {
                        studentsBeanList.add(weeksMonthStatisticalCourseForm);
                    }
                } else {
                    if (weeksMonthList != null) {
                        for (StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean weeksMonthListBean : weeksMonthList) {
                            if (weeksMonthListBean.getServerId().equals(classroomTeacherAttendanceListBean.getServerId())) {
                                studentsBeanList.add(weeksMonthListBean);
                            }
                        }
                    }
                }
            }
            //设置事件
            if (eventList.size() != 0) {
                final Optional<LeaveDeptRsp.DataBean> eventOptional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
                if (!eventOptional.isPresent()) {
                    eventList.get(0).setIsDefault(1);
                    historyEvent = eventList.get(0).getDeptName();
                    historyEventType = eventList.get(0).getType();
                    historyEventId = eventList.get(0).getDeptId();
                }
                mViewBinding.tvAttendanceType.setVisibility(View.VISIBLE);
                mViewBinding.viewpager.setVisibility(View.VISIBLE);
                //默认选择第一个事件
                //eventList.get(0).setIsDefault(1);
                //初始化事件view
                initEventView();
            } else {
                mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            }

            //默认选择第一个事件的统计
            if (studentsBeanList.size() != 0) {
                final StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean weeksMonthListBean = studentsBeanList.get(0);
                //showData(weeksMonthListBean.getServerId(),weeksMonthListBean.getType()+"");
                showData(TextUtils.isEmpty(historyEventId) ? weeksMonthListBean.getServerId() : historyEventId, TextUtils.isEmpty(historyEventType) ? weeksMonthListBean.getType() + "" : historyEventType);
            } else {
                ToastUtils.showShort("未查询到数据");
                showData(null, null);
            }
        } else {
            //ToastUtils.showShort("温馨提示：" + attendanceWeekStatsRsp.getMsg());
            mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            //showData(null);
            showBlank(true);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showData(String serverId, String type) {
        Log.e(TAG, "showData: serverId=" + serverId + ", type=" + type);
        //每次更新数据默认选择缺勤rb
        mViewBinding.rgAttendanceType.check(R.id.rb_absence);
        StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean weeksMonthListBean = null;
        for (StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean weeksMonthListBean1 : studentsBeanList) {
            if ("2".equals(type) && weeksMonthListBean1.getType() == 2) {
                weeksMonthListBean = weeksMonthListBean1;
                break;
            }
            if ("2".equals(type)) {
                continue;
            }
            if (Objects.equals(weeksMonthListBean1.getServerId(), serverId)) {
                weeksMonthListBean = weeksMonthListBean1;
                break;
            }
        }
        if (weeksMonthListBean == null) {
            Log.e(TAG, "showData: data is null");
            weeksMonthListBean = new StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean();
        }

        //事件考勤 or 课程考勤
        if (TextUtils.isEmpty(type)) {
            eventType = weeksMonthListBean.getType() != 2;
        } else {
            eventType = !Objects.equals(type, "2");
        }

        if (isWeekStatistics) {
            setWeek(true);
        } else {
            setMonth(true);
        }
        final String goOutStatus = weeksMonthListBean.getSignInOutRate();
        //通过identity eventType 判断显示布局
        if (identity && eventType) {
            //学生事件考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.VISIBLE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.GONE);
            if (isWeekStatistics) {
                mViewBinding.layoutHeadStudentEvent.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
            } else {
                mViewBinding.layoutHeadStudentEvent.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
            }
            mViewBinding.layoutHeadStudentEvent.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_time));
            //考勤进度
            String rate = weeksMonthListBean.getSignInOutRate();
            try {
                final float aFloat = Float.parseFloat(rate);
                int rateInt = (int) (float) aFloat;
                mViewBinding.layoutHeadStudentEvent.progress.setProgress(rateInt, true);
            } catch (Exception exception) {
                Log.e(TAG, "onViewCreated: " + exception.getLocalizedMessage());
                rate = "--";
            }


            mViewBinding.layoutHeadStudentEvent.tvAttendanceRate.setText(rate);
            setAttendanceRateTitle(goOutStatus, mViewBinding.layoutHeadStudentEvent.textview1);
            //缺勤人数
            mViewBinding.layoutHeadStudentEvent.tvAbsenceNum.setText(String.valueOf(weeksMonthListBean.getAbsenteeism()));
            //请假人数
            mViewBinding.layoutHeadStudentEvent.tvAskForLeaveNum.setText(String.valueOf(weeksMonthListBean.getLeave()));
            //迟到人数
            mViewBinding.layoutHeadStudentEvent.tvLateNum.setText(String.valueOf(weeksMonthListBean.getLate()));
            //早退人数
            mViewBinding.layoutHeadStudentEvent.tvLateEarlyNum.setText(String.valueOf(weeksMonthListBean.getEarly()));
            initClassView();
        } else {
            //教师学生课程考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.VISIBLE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.GONE);
            if (isWeekStatistics) {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourse.setText(R.string.weekly_course_count);
            } else {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourse.setText(getString(R.string.monthly_course_count));
            }
            if (identity) {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_time));
            } else {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_man_time));
            }

            //课程总数
            mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourseNum.setText(String.valueOf(weeksMonthListBean.getCourseNumber()));
            //缺勤人数
            mViewBinding.layoutHeadTeacherCourse.tvAbsenceNum.setText(String.valueOf(weeksMonthListBean.getAbsenteeism()));
            //请假人数
            mViewBinding.layoutHeadTeacherCourse.tvAskForLeaveNum.setText(String.valueOf(weeksMonthListBean.getLeave()));
            //迟到人数
            mViewBinding.layoutHeadTeacherCourse.tvLateNum.setText(String.valueOf(weeksMonthListBean.getLate()));
            initClassView();
        }

        //0正常、1缺勤、2迟到/3早退,4请假
        latePeople.clear();
        if (weeksMonthListBean.getLateList() != null) {
            latePeople.addAll(weeksMonthListBean.getLateList());
        }
        absencePeople.clear();
        if (weeksMonthListBean.getAbsenteeismList() != null) {
            absencePeople.addAll(weeksMonthListBean.getAbsenteeismList());
        }

        leavePeople.clear();
        if (weeksMonthListBean.getLeaveList() != null) {
            leavePeople.addAll(weeksMonthListBean.getLeaveList());
        }

        leaveEarlyPeople.clear();
        if (weeksMonthListBean.getEarlyList() != null) {
            leaveEarlyPeople.addAll(weeksMonthListBean.getEarlyList());
        }
        //mViewBinding.viewpager.notify();
        initPeopleListView();
    }

    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        if (!TextUtils.isEmpty(msg) && "请检查网络连接".equals(msg)){
            ToastUtils.showShort(msg);
        }
        if (refresh) {
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        mViewBinding.clAttendanceType.setVisibility(View.GONE);
        mViewBinding.viewpager.setVisibility(View.GONE);
        showBlank(true);
    }

    private void showBlank(boolean show) {
        mViewBinding.blankPage.setVisibility(show ? View.VISIBLE : View.GONE);
        mViewBinding.collapsingtoolbarlayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        queryAttStatsData();
    }

    /**
     * 设置出勤rate的标题
     *
     * @param goOutStatus
     * @param textView
     */
    private void setAttendanceRateTitle(String goOutStatus, TextView textView) {
        if ("1".equals(goOutStatus)) {
            textView.setText(R.string.attendance_sign_out_rate_colon);
            return;
        }
        textView.setText(R.string.attendance_rate_colon);
    }
}