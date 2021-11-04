package com.yyide.chatim.fragment.attendance;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
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
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekStatisticsFragment extends BaseMvpFragment<WeekStatisticsPresenter> implements WeekMonthStatisticsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "WeekStatisticsFragment";
    private FragmentWeekStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    private static final String ARG_TYPE = "type";
    //当前界面类型 周统计 月统计
    private int currentPage = 1;
    private String type;
    //月
    private int month;
    //最小月限制
    private int minMonth;
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
    private String currentStudentId;//当前学生id
    private int dialogType;
    private boolean identity;//确定是教师还是学生 true是学生  false 是老师
    private boolean eventType;//确实是否是课程考勤 true 事件考勤 false 课程考勤
    private String beginDate;
    private boolean refresh;
    private boolean first = true;
    private String historyEvent;//上一次选择的事件
    public WeekStatisticsFragment(String type) {
        // Required empty public constructor
        this.type = type;
    }


    public static WeekStatisticsFragment newInstance(String type) {
        WeekStatisticsFragment fragment = new WeekStatisticsFragment("");
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: isWeekStatistics:"+isWeekStatistics +",first:"+first);
        if (first && !isWeekStatistics) {
            first = false;
            //queryAttStatsData(currentClass, currentDate, currentPage);
        }
    }

    private void setMonth(boolean init) {
        Log.e(TAG, "currentMonth"+currentMonth+"month："+month );
        if (!init){
            if (month > currentMonth || month < 1) {
                Log.e(TAG, "setMonth: 不能查询未来的事件或者超过时间范围的事件");
                return;
            }
        }
        if (identity && eventType) {
            //学生事件考勤
            if (DateUtils.minMonth(beginDate, month)) {
                mViewBinding.layoutHeadStudentEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadStudentEvent.ivLeft.setVisibility(View.VISIBLE);
            }

            if (currentMonth == month) {
                mViewBinding.layoutHeadStudentEvent.ivRight.setVisibility(View.INVISIBLE);
            } else if (month == 1) {
                mViewBinding.layoutHeadStudentEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else{
                mViewBinding.layoutHeadStudentEvent.ivRight.setVisibility(View.VISIBLE);
            }
            mViewBinding.layoutHeadStudentEvent.tvWeek.setText(String.format(getActivity().getString(R.string.month), month));
        }else if(!identity && eventType){
            //教师事件考勤
            if (DateUtils.minMonth(beginDate, month)) {
                mViewBinding.layoutHeadTeacherEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherEvent.ivLeft.setVisibility(View.VISIBLE);
            }
            if (currentMonth == month) {
                mViewBinding.layoutHeadTeacherEvent.ivRight.setVisibility(View.INVISIBLE);
            } else if (month == 1) {
                mViewBinding.layoutHeadTeacherEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherEvent.ivRight.setVisibility(View.VISIBLE);
            }
            mViewBinding.layoutHeadTeacherEvent.tvWeek.setText(String.format(getActivity().getString(R.string.month), month));
        }else {
            if (DateUtils.minMonth(beginDate, month)) {
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.VISIBLE);
            }
            if (currentMonth == month) {
                mViewBinding.layoutHeadTeacherCourse.ivRight.setVisibility(View.INVISIBLE);
            } else if (month == 1) {
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherCourse.ivRight.setVisibility(View.VISIBLE);
            }
            mViewBinding.layoutHeadTeacherCourse.tvWeek.setText(String.format(getActivity().getString(R.string.month), month));
        }
        if (!init){
            queryAttStatsData(currentClass, currentDate,currentPage);
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeek(boolean init) {
        Log.e(TAG, "currentWeek"+currentWeek+"week："+week );
        if (!init){
            if (week > currentWeek || week < 1) {
                Log.e(TAG, "setWeek: 不能查询未来的事件或者超过时间范围的事件");
                return;
            }
        }
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final String[] monthWeek = DateUtils.getFirstDayAndLastDayByMonthWeek(year, month, week);
        final String format1 = getActivity().getString(R.string.week);

        if (identity && eventType) {
            if (currentWeek == week) {
                mViewBinding.layoutHeadStudentEvent.ivRight.setVisibility(View.INVISIBLE);
            } else if (week == 1) {
                mViewBinding.layoutHeadStudentEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadStudentEvent.ivRight.setVisibility(View.VISIBLE);
                mViewBinding.layoutHeadStudentEvent.ivLeft.setVisibility(View.VISIBLE);
            }
            mViewBinding.layoutHeadStudentEvent.tvWeek.setText(String.format(format1, monthWeek[0], monthWeek[1]));
        }else if(!identity && eventType){
            if (currentWeek == week) {
                mViewBinding.layoutHeadTeacherEvent.ivRight.setVisibility(View.INVISIBLE);
            } else if (week == 1) {
                mViewBinding.layoutHeadTeacherEvent.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherEvent.ivRight.setVisibility(View.VISIBLE);
                mViewBinding.layoutHeadTeacherEvent.ivLeft.setVisibility(View.VISIBLE);
            }
            mViewBinding.layoutHeadTeacherEvent.tvWeek.setText(String.format(format1, monthWeek[0], monthWeek[1]));
        }else {
            if (currentWeek == week) {
                mViewBinding.layoutHeadTeacherCourse.ivRight.setVisibility(View.INVISIBLE);
            } else if (week == 1) {
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.INVISIBLE);
            } else {
                mViewBinding.layoutHeadTeacherCourse.ivRight.setVisibility(View.VISIBLE);
                //mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.VISIBLE);
            }
            if (DateUtils.minWeek(beginDate,monthWeek[0])){
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.INVISIBLE);
                final String minDate = DateUtils.formatTime(beginDate, null, "MM.dd");
                mViewBinding.layoutHeadTeacherCourse.tvWeek.setText(String.format(format1, minDate, monthWeek[1]));
            }else {
                mViewBinding.layoutHeadTeacherCourse.ivLeft.setVisibility(View.VISIBLE);
                mViewBinding.layoutHeadTeacherCourse.tvWeek.setText(String.format(format1, monthWeek[0], monthWeek[1]));
            }

        }
        if (!init){
            queryAttStatsData(currentClass, currentDate,currentPage);
        }
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
        mvpPresenter.getAttendanceStatsData(currentStudentId,classId, searchTime, isWeekStatistics ? "2" : "3",page);
    }

    private void initClassData() {
//        if (!SpData.getIdentityInfo().staffIdentity()){
//            final String studentName = SpData.getIdentityInfo().studentName;
//            mViewBinding.tvClassName.setText(studentName);
//            return;
//        }
        if (SpData.getIdentityInfo().staffIdentity()) {
            dialogType = 2;
        } else {
            dialogType = 4;
        }

        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
        final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        classList.clear();
        final String classesStudentName = SpData.getClassesStudentName();
        for (GetUserSchoolRsp.DataBean.FormBean formBean : form) {
            final String classesName = formBean.classesName;
            final String studentName = formBean.classesStudentName;
            final String classesId = formBean.classesId;
            final String studentId = formBean.studentId;
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            dataBean.setDeptId(studentId);
            dataBean.setClassId(classesId);
            dataBean.setIsDefault(0);
            if (SpData.getIdentityInfo().staffIdentity()) {
                dataBean.setDeptName(classesName);
                if (classesName.equals(classesStudentName)){
                    dataBean.setIsDefault(1);
                }
            } else {
                dataBean.setDeptName(studentName);
                if (studentName.equals(classesStudentName)){
                    dataBean.setIsDefault(1);
                }
            }
            classList.add(dataBean);
        }
        if (classList.isEmpty()) {
            Log.e(TAG, "initClassData: 当前账号没有学生" );
            mViewBinding.tvClassName.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentWeekStatisticsBinding.bind(view);
        Log.e(TAG, "onViewCreated: "+type );
        if (getString(R.string.weekly_statistics).equals(type)) {
            isWeekStatistics = true;
        }
        //确定当前用户身份
        identity = !SpData.getIdentityInfo().staffIdentity();

        //初始化班级view
        initClassData();
        initClassView();
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mViewBinding.swipeRefreshLayout.setEnabled(verticalOffset >= 0);//页面滑动到顶部，才可以下拉刷新
        });
        if (isWeekStatistics) {
            final Calendar calendar = Calendar.getInstance();
            final int weekOfMonth = calendar.get(Calendar.WEEK_OF_YEAR);
            week = weekOfMonth;
            currentWeek = weekOfMonth;
            setWeek(false);
        } else {
            //获取当前默认日期
            final Calendar calendar = Calendar.getInstance();
            Log.e(TAG, "calendar: " + calendar);
            final int i = calendar.get(Calendar.MONTH);
            Log.e(TAG, "月: " + i);
            month = i + 1;
            currentMonth = month;
            setMonth(false);
        }
    }

    private void initPeopleListView() {
        mViewBinding.clAttendanceType.setVisibility(View.VISIBLE);
        List<String> listTab = new ArrayList<>();
        listTab.add(getString(R.string.attendance_absence));
        listTab.add(getString(R.string.attendance_late));
        listTab.add(getString(R.string.ask_for_leave));
        if (eventType){
            listTab.add(getString(R.string.attendance_leave_early));
        }
        mViewBinding.rbLeaveEarly.setVisibility(eventType?View.VISIBLE:View.GONE);

        fragments.clear();
        final StatisticsListFragment absenceListFragment = StatisticsListFragment.newInstance(listTab.get(0));
        absenceListFragment.setData(absencePeople);
        fragments.add(absenceListFragment);
        final StatisticsListFragment lateListFragment = StatisticsListFragment.newInstance(listTab.get(1));
        lateListFragment.setData(latePeople);
        fragments.add(lateListFragment);
        final StatisticsListFragment leaveListFragment = StatisticsListFragment.newInstance(listTab.get(2));
        leaveListFragment.setData(leavePeople);
        fragments.add(leaveListFragment);
        if (eventType){
            final StatisticsListFragment leaveEarlyFragment = StatisticsListFragment.newInstance(listTab.get(3));
            leaveEarlyFragment.setData(leaveEarlyPeople);
            fragments.add(leaveEarlyFragment);
        }

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

//        if (SpData.getIdentityInfo().staffIdentity() && !absencePeople.isEmpty()) {
//            mViewBinding.tvPeopleCount.setVisibility(View.VISIBLE);
//            mViewBinding.tvPeopleCount.setText(String.format(getString(R.string.people_number), absencePeople.size()));
//        } else {
//            mViewBinding.tvPeopleCount.setVisibility(View.GONE);
//        }

        mViewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_absence:
                    mViewBinding.viewpager.setCurrentItem(0);
//                    if (absencePeople.isEmpty() || !SpData.getIdentityInfo().staffIdentity()){
//                        mViewBinding.tvPeopleCount.setVisibility(View.GONE);
//                    }else {
//                        mViewBinding.tvPeopleCount.setVisibility(View.VISIBLE);
//                        mViewBinding.tvPeopleCount.setText(String.format(getString(R.string.people_number),absencePeople.size()));
//                    }
                    break;
                case R.id.rb_late:
                    mViewBinding.viewpager.setCurrentItem(1);
//                    if (latePeople.isEmpty() || !SpData.getIdentityInfo().staffIdentity()){
//                        mViewBinding.tvPeopleCount.setVisibility(View.GONE);
//                    }else {
//                        mViewBinding.tvPeopleCount.setVisibility(View.VISIBLE);
//                        mViewBinding.tvPeopleCount.setText(String.format(getString(R.string.people_number),latePeople.size()));
//                    }
                    break;
                case R.id.rb_leave:
                    mViewBinding.viewpager.setCurrentItem(2);
//                    if (leavePeople.isEmpty() || !SpData.getIdentityInfo().staffIdentity()){
//                        mViewBinding.tvPeopleCount.setVisibility(View.GONE);
//                    }else {
//                        mViewBinding.tvPeopleCount.setVisibility(View.VISIBLE);
//                        mViewBinding.tvPeopleCount.setText(String.format(getString(R.string.people_number),leavePeople.size()));
//                    }
                    break;
                case R.id.rb_leave_early:
                    mViewBinding.viewpager.setCurrentItem(3);
//                    if (leaveEarlyPeople.isEmpty()  || !SpData.getIdentityInfo().staffIdentity()){
//                        mViewBinding.tvPeopleCount.setVisibility(View.GONE);
//                    }else {
//                        mViewBinding.tvPeopleCount.setVisibility(View.VISIBLE);
//                        mViewBinding.tvPeopleCount.setText(String.format(getString(R.string.people_number),leaveEarlyPeople.size()));
//                    }
                    break;

                default:
                    break;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()){
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            mViewBinding.tvClassName.setText(clazzBean.getDeptName());
            currentClass = clazzBean.getClassId();
            currentStudentId = clazzBean.getDeptId();
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
                                Log.e(TAG, "班级选择: " +dataBean.toString());
                                mViewBinding.tvClassName.setText(dataBean.getDeptName());
                                //班级id
                                currentClass = dataBean.getClassId();
                                //学生id
                                currentStudentId = dataBean.getDeptId();
                                currentPage = 1;
                                queryAttStatsData(currentClass, currentDate,1);
                            });
                        }
                );
            }
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
                    historyEvent = dept;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void attendanceStatisticsSuccess(AttendanceWeekStatsRsp attendanceWeekStatsRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + attendanceWeekStatsRsp.toString());
        if (refresh){
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        if (attendanceWeekStatsRsp.getCode() == 200) {
            if (attendanceWeekStatsRsp.getData() == null){
                ToastUtils.showShort(""+attendanceWeekStatsRsp.getMsg());
            }

            if (attendanceWeekStatsRsp.getData() == null || attendanceWeekStatsRsp.getData().getAttendancesForm() == null) {
                showBlank(false);
                showData(null);
                return;
            }
            showBlank(false);
            final List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean> attendancesForm = attendanceWeekStatsRsp.getData().getAttendancesForm();
            eventList.clear();
            studentsBeanList.clear();
            for (int i = 0; i < attendancesForm.size(); i++) {
                final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean attendancesFormBean = attendancesForm.get(i);
                final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean students = attendancesFormBean.getStudents();
                students.setIdentityType(attendancesFormBean.getIdentityType());
                studentsBeanList.add(students);
                //考勤时间类型
                final String name = students.getName();
                final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
                if (name.equals(historyEvent) || (TextUtils.isEmpty(historyEvent) && i == 0)) {
                    dataBean.setIsDefault(1);
                } else {
                    dataBean.setIsDefault(0);
                }
                dataBean.setDeptName(name);
                dataBean.setDeptId("0");
                eventList.add(dataBean);
            }
//            for (AttendanceWeekStatsRsp.DataBean.AttendancesFormBean attendancesFormBean : attendancesForm) {
//                final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean students = attendancesFormBean.getStudents();
//                students.setIdentityType(attendancesFormBean.getIdentityType());
//                studentsBeanList.add(students);
//                //考勤时间类型
//                final String name = students.getName();
//                final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
//                dataBean.setIsDefault(0);
//                dataBean.setDeptName(name);
//                dataBean.setDeptId("0");
//                eventList.add(dataBean);
//            }
            //设置事件
            if (eventList.size() != 0) {
                final Optional<LeaveDeptRsp.DataBean> eventOptional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
                if (!eventOptional.isPresent()){
                    eventList.get(0).setIsDefault(1);
                    historyEvent = eventList.get(0).getDeptName();
                }
                mViewBinding.tvAttendanceType.setVisibility(View.VISIBLE);
                //默认选择第一个事件
                //eventList.get(0).setIsDefault(1);
                //初始化事件view
                initEventView();
            } else {
                mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            }

            //默认选择第一个事件的统计
            if (studentsBeanList.size() != 0) {
                showData(TextUtils.isEmpty(historyEvent)?studentsBeanList.get(0).getName():historyEvent);
            }else {
                ToastUtils.showShort("未查询到数据");
                showData(null);
            }
        } else {
            ToastUtils.showShort("服务器异常："+attendanceWeekStatsRsp.getCode()+","+attendanceWeekStatsRsp.getMsg());
            mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            //showData(null);
            showBlank(true);
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
            //return;
            studentsBean = new AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean();
        }
        beginDate = studentsBean.getBeginDate();
        Log.e(TAG, "beginDate: " + beginDate);
        //事件考勤 or 课程考勤
        eventType = "N".equals(studentsBean.getIdentityType());
        if (isWeekStatistics) {
            setWeek(true);
        } else {
            setMonth(true);
        }
        final int goOutStatus = studentsBean.getGoOutStatus();
        //通过identity eventType 判断显示布局
        if (identity && eventType) {
            //学生事件考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.VISIBLE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.GONE);
            if (isWeekStatistics){
                mViewBinding.layoutHeadStudentEvent.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
            }else {
                mViewBinding.layoutHeadStudentEvent.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
            }
            mViewBinding.layoutHeadStudentEvent.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_time));
            //考勤进度
            final String rate = studentsBean.getRate();
            try {
                final float aFloat = Float.parseFloat(rate);
                int rateInt = (int) (float) aFloat;
                mViewBinding.layoutHeadStudentEvent.progress.setProgress(rateInt, true);;
            } catch (Exception exception) {
                Log.e(TAG, "onViewCreated: " + exception.getLocalizedMessage());
            }


            mViewBinding.layoutHeadStudentEvent.tvAttendanceRate.setText(rate);
            setAttendanceRateTitle(goOutStatus,mViewBinding.layoutHeadStudentEvent.textview1);
            //缺勤人数
            mViewBinding.layoutHeadStudentEvent.tvAbsenceNum.setText(String.valueOf(studentsBean.getAbsence()));
            //请假人数
            mViewBinding.layoutHeadStudentEvent.tvAskForLeaveNum.setText(String.valueOf(studentsBean.getLeave()));
            //迟到人数
            mViewBinding.layoutHeadStudentEvent.tvLateNum.setText(String.valueOf(studentsBean.getLate()));
            //早退人数
            mViewBinding.layoutHeadStudentEvent.tvLateEarlyNum.setText(String.valueOf(studentsBean.getLeaveEarly()));

            mViewBinding.layoutHeadStudentEvent.ivLeft.setOnClickListener(v -> {
                //向左选择日期
                if (isWeekStatistics) {
                    week--;
                    currentPage++;
                    setWeek(false);
                } else {
                    month--;
                    currentPage++;
                    setMonth(false);
                }
            });
            mViewBinding.layoutHeadStudentEvent.ivRight.setOnClickListener(v -> {
                //向右选择日期
                if (isWeekStatistics) {
                    week++;
                    currentPage--;
                    setWeek(false);
                } else {
                    month++;
                    currentPage--;
                    setMonth(false);
                }

            });

        } else if (!identity && eventType) {
            //教师事件考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.VISIBLE);
            if (isWeekStatistics){
                mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
            }else {
                mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
            }
            mViewBinding.layoutHeadTeacherEvent.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_man_time));

            //缺勤人数
            mViewBinding.layoutHeadTeacherEvent.tvAbsenceNum.setText(String.valueOf(studentsBean.getAbsence()));
            //请假人数
            mViewBinding.layoutHeadTeacherEvent.tvAskForLeaveNum.setText(String.valueOf(studentsBean.getLeave()));
            //迟到人数
            mViewBinding.layoutHeadTeacherEvent.tvLateNum.setText(String.valueOf(studentsBean.getLate()));
            //早退人数
            mViewBinding.layoutHeadTeacherEvent.tvLateEarlyNum.setText(String.valueOf(studentsBean.getLeaveEarly()));

            mViewBinding.layoutHeadTeacherEvent.ivLeft.setOnClickListener(v -> {
                //向左选择日期
                if (isWeekStatistics) {
                    week--;
                    currentPage++;
                    setWeek(false);
                } else {
                    month--;
                    currentPage++;
                    setMonth(false);
                }
            });
            mViewBinding.layoutHeadTeacherEvent.ivRight.setOnClickListener(v -> {
                //向右选择日期
                if (isWeekStatistics) {
                    week++;
                    currentPage--;
                    setWeek(false);
                } else {
                    month++;
                    currentPage--;
                    setMonth(false);
                }

            });
        }  else {
            //教师学生课程考勤
            mViewBinding.layoutHeadStudentEvent.getRoot().setVisibility(View.GONE);
            mViewBinding.layoutHeadTeacherCourse.getRoot().setVisibility(View.VISIBLE);
            mViewBinding.layoutHeadTeacherEvent.getRoot().setVisibility(View.GONE);
            if (isWeekStatistics){
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatistical.setText(getString(R.string.weekly_statistical));
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourse.setText(R.string.weekly_course_count);
            }else {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatistical.setText(getString(R.string.monthly_statistical));
                mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourse.setText(getString(R.string.monthly_course_count));
            }
            if (identity){
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_time));
            }else {
                mViewBinding.layoutHeadTeacherCourse.tvWeeklyStatisticalSubhead.setText(getString(R.string.statistical_man_time));
            }

            //课程总数
            mViewBinding.layoutHeadTeacherCourse.tvNumberOfCourseNum.setText(String.valueOf(studentsBean.getSchNum()));
            //缺勤人数
            mViewBinding.layoutHeadTeacherCourse.tvAbsenceNum.setText(String.valueOf(studentsBean.getAbsence()));
            //请假人数
            mViewBinding.layoutHeadTeacherCourse.tvAskForLeaveNum.setText(String.valueOf(studentsBean.getLeave()));
            //迟到人数
            mViewBinding.layoutHeadTeacherCourse.tvLateNum.setText(String.valueOf(studentsBean.getLate()));

            mViewBinding.layoutHeadTeacherCourse.ivLeft.setOnClickListener(v -> {
                //向左选择日期
                if (isWeekStatistics) {
                    week--;
                    currentPage++;
                    setWeek(false);
                } else {
                    month--;
                    currentPage++;
                    setMonth(false);
                }
            });
            mViewBinding.layoutHeadTeacherCourse.ivRight.setOnClickListener(v -> {
                //向右选择日期
                if (isWeekStatistics) {
                    week++;
                    currentPage--;
                    setWeek(false);
                } else {
                    month++;
                    currentPage--;
                    setMonth(false);
                }

            });
        }
        final String identityType = studentsBean.getIdentityType();
        //0正常、1缺勤、2迟到/3早退,4请假
        latePeople.clear();
        latePeople.addAll(filterNullStatusData("2",identityType,studentsBean.getLatePeople()));
        absencePeople.clear();
        absencePeople.addAll(filterNullStatusData("1",identityType,studentsBean.getAbsencePeople()));
        leavePeople.clear();
        leavePeople.addAll(filterNullStatusData("4",identityType,studentsBean.getLeavePeople()));
        leaveEarlyPeople.clear();
        leaveEarlyPeople.addAll(filterNullStatusData("3",identityType,studentsBean.getLeaveEarlyPeople()));
        //mViewBinding.viewpager.notify();
        initPeopleListView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> filterNullStatusData(String status,String identityType,List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> peopleBeanList) {
        //return peopleBeanList.stream().filter(it -> !TextUtils.isEmpty(it.getStatus())).collect(Collectors.toList());
        if (peopleBeanList == null){
            return new ArrayList<>();
        }
        for (AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean peopleBean : peopleBeanList) {
            peopleBean.setStatus(status);
            peopleBean.setIdentityType(identityType);
        }
        return peopleBeanList;
    }

    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        if (refresh){
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        showBlank(true);
    }

    private void showBlank(boolean show){
        mViewBinding.blankPage.setVisibility(show?View.VISIBLE:View.GONE);
        mViewBinding.collapsingtoolbarlayout.setVisibility(show?View.GONE:View.VISIBLE);
        //mViewBinding.constraintLayout.setVisibility(show?View.GONE:View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        queryAttStatsData(currentClass, currentDate, currentPage);
    }

    /**
     * 设置出勤rate的标题
     * @param goOutStatus
     * @param textView
     */
    private void setAttendanceRateTitle(int goOutStatus, TextView textView){
        if (goOutStatus == 1){
            textView.setText(R.string.attendance_sign_out_rate_colon);
            return;
        }
        textView.setText(R.string.attendance_rate_colon);
    }
}