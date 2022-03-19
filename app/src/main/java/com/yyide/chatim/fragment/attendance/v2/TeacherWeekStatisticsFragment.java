package com.yyide.chatim.fragment.attendance.v2;

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
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.database.ScheduleDaoUtil;
import com.yyide.chatim.databinding.FragmentWeekStatisticsBinding;
import com.yyide.chatim.databinding.LayoutWeekStatisticsHeadStudentEventBinding;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceWeekMonthRsp;
import com.yyide.chatim.presenter.attendance.v2.TeacherWeekMonthStatisticsPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.attendace.v2.TeacherWeekMonthStatisticsView;

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
 * @description 班主任或任课老师查看周、月统计信息
 */
public class TeacherWeekStatisticsFragment extends BaseMvpFragment<TeacherWeekMonthStatisticsPresenter> implements TeacherWeekMonthStatisticsView, SwipeRefreshLayout.OnRefreshListener {
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
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean> studentsBeanList = new ArrayList<>();
    //迟到的数据列表
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> latePeople = new ArrayList<>();
    //请假的数据类别
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> leavePeople = new ArrayList<>();
    //缺勤的数据类别
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> absencePeople = new ArrayList<>();
    //早退的数据列表
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> leaveEarlyPeople = new ArrayList<>();
    private String currentClass;//当前班级
    private String currentStudentId;//当前学生id
    private int dialogType;
    private boolean identity = false;//确定是教师还是学生 true是学生  false 是老师
    private boolean eventType;//确定是否是课程考勤 true 事件考勤 false 课程考勤
    private boolean refresh;
    private boolean first = true;
    private String historyEvent;//上一次选择的事件
    private String historyEventId;//上一次选择的事件
    private String historyEventType;
    private String beginDate = "2000-01-01 00:00:00";
    //请求数据需要的时间
    private String startTime;
    private String endTime;

    public TeacherWeekStatisticsFragment(String type, String theme, String serverId, String eventType) {
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
//            mViewBinding.ivLeft.setVisibility(View.VISIBLE);
        }
        mViewBinding.tvWeek.setText(String.format(format1, monthWeek0, monthWeek1));
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
        if (TextUtils.isEmpty(currentClass)) {
            ToastUtils.showShort("当前账号没有班级，不能查询考勤数据！");
            if (refresh) {
                refresh = false;
                mViewBinding.swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        mvpPresenter.queryAppTeacherThreeAttendanceWeekMonth(currentClass, startTime, endTime);
    }

    private void initClassData() {
        if (SpData.getIdentityInfo().staffIdentity()) {
            dialogType = 2;
        } else {
            dialogType = 4;
        }
//
//        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
//        final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
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
//                if (classesName.equals(classesStudentName)) {
//                    dataBean.setIsDefault(1);
//                    currentClass = dataBean.getClassId();
//                    currentStudentId = dataBean.getDeptId();
//                }
//            } else {
//                dataBean.setDeptName(studentName);
//                if (studentName.equals(classesStudentName)) {
//                    dataBean.setIsDefault(1);
//                    currentClass = dataBean.getClassId();
//                    currentStudentId = dataBean.getDeptId();
//                }
//            }
//            classList.add(dataBean);
//        }
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
        initClassData();
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
            //month = i + 1;
            //currentMonth = month;
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
        final TeacherStatisticsListFragment absenceListFragment = TeacherStatisticsListFragment.newInstance(listTab.get(0));
        absenceListFragment.setData(absencePeople);
        fragments.add(absenceListFragment);
        final TeacherStatisticsListFragment lateListFragment = TeacherStatisticsListFragment.newInstance(listTab.get(1));
        lateListFragment.setData(latePeople);
        fragments.add(lateListFragment);
        final TeacherStatisticsListFragment leaveListFragment = TeacherStatisticsListFragment.newInstance(listTab.get(2));
        leaveListFragment.setData(leavePeople);
        fragments.add(leaveListFragment);
        if (eventType) {
            final TeacherStatisticsListFragment leaveEarlyFragment = TeacherStatisticsListFragment.newInstance(listTab.get(3));
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
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            tvClassName.setText(clazzBean.getDeptName());
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
                                tvClassName.setText(dataBean.getDeptName());
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
                    showData(dataBean1.getDeptId(), dataBean1.getType() + "");
                });
            });
        }
    }

    @Override
    protected TeacherWeekMonthStatisticsPresenter createPresenter() {
        return new TeacherWeekMonthStatisticsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void attendanceStatisticsSuccess(TeacherAttendanceWeekMonthRsp attendanceWeekStatsRsp) {
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
            showBlank(false);
            final List<TeacherAttendanceWeekMonthRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = attendanceWeekStatsRsp.getData().getClassroomTeacherAttendanceList();
            final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean weeksCourseForm = attendanceWeekStatsRsp.getData().getWeeksCourseForm();
            final List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean> weeksEvenList = attendanceWeekStatsRsp.getData().getWeeksEvenList();

            for (int i = 0; i < classroomTeacherAttendanceList.size(); i++) {
                final TeacherAttendanceWeekMonthRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(i);
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
                    if (weeksCourseForm != null) {
                        studentsBeanList.add(weeksCourseForm);
                    }
                } else {
                    if (weeksEvenList != null) {
                        for (TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean weeksMonthListBean : weeksEvenList) {
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
                    historyEventId = eventList.get(0).getDeptId();
                    historyEventType = eventList.get(0).getType();
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
                //showData(TextUtils.isEmpty(historyEvent) ? studentsBeanList.get(0).getTheme() : historyEvent);
                final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean weeksEvenListBean = studentsBeanList.get(0);
                showData(TextUtils.isEmpty(historyEventId) ? weeksEvenListBean.getServerId() : historyEventId, TextUtils.isEmpty(historyEventType) ? weeksEvenListBean.getType() + "" : historyEventType);
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
        //每次更新数据默认选择缺勤rb
        mViewBinding.rgAttendanceType.check(R.id.rb_absence);
        TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean weeksMonthListBean = null;
        for (TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean weeksMonthListBean1 : studentsBeanList) {
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
            weeksMonthListBean = new TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean();
        }

        //事件考勤 or 课程考勤
        eventType = weeksMonthListBean.getType() != 2;
        if (isWeekStatistics) {
            setWeek(true);
        } else {
            setMonth(true);
        }
        //final String goOutStatus = weeksMonthListBean.getSignInOutRate();
        //通过identity eventType 判断显示布局
        if (!identity && eventType) {
            //教师事件考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.VISIBLE);
            if (isWeekStatistics) {
                mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
            } else {
                mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
            }
            mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_man_time));
            if (weeksMonthListBean.getAttendanceAppNumberVoForm() != null) {
                //缺勤人数
                mViewBinding.layoutHeadTeacherEvent.tvAbsenceNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getAbsenteeism()));
                //请假人数
                mViewBinding.layoutHeadTeacherEvent.tvAskForLeaveNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getLeave()));
                //迟到人数
                mViewBinding.layoutHeadTeacherEvent.tvLateNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getLate()));
                //早退人数
                mViewBinding.layoutHeadTeacherEvent.tvLateEarlyNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getEarly()));
            } else {
                //缺勤人数
                mViewBinding.layoutHeadTeacherEvent.tvAbsenceNum.setText(String.valueOf(0));
                //请假人数
                mViewBinding.layoutHeadTeacherEvent.tvAskForLeaveNum.setText(String.valueOf(0));
                //迟到人数
                mViewBinding.layoutHeadTeacherEvent.tvLateNum.setText(String.valueOf(0));
                //早退人数
                mViewBinding.layoutHeadTeacherEvent.tvLateEarlyNum.setText(String.valueOf(0));
            }
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

            if (weeksMonthListBean.getAttendanceAppNumberVoForm() != null) {
                //课程总数
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourseNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getCourseNumber()));
                //缺勤人数
                mViewBinding.layoutHeadTeacherCourse.tvAbsenceNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getAbsenteeism()));
                //请假人数
                mViewBinding.layoutHeadTeacherCourse.tvAskForLeaveNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getLeave()));
                //迟到人数
                mViewBinding.layoutHeadTeacherCourse.tvLateNum.setText(String.valueOf(weeksMonthListBean.getAttendanceAppNumberVoForm().getLate()));
            } else {
                //课程总数
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourseNum.setText(String.valueOf(0));
                //缺勤人数
                mViewBinding.layoutHeadTeacherCourse.tvAbsenceNum.setText(String.valueOf(0));
                //请假人数
                mViewBinding.layoutHeadTeacherCourse.tvAskForLeaveNum.setText(String.valueOf(0));
                //迟到人数
                mViewBinding.layoutHeadTeacherCourse.tvLateNum.setText(String.valueOf(0));
            }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        if (!TextUtils.isEmpty(msg) && "请检查网络连接".equals(msg)) {
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