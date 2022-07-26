package com.yyide.chatim_pro.fragment.attendance.v2;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beiing.weekcalendar.WeekCalendar;
import com.beiing.weekcalendar.listener.GetViewHelper;
import com.beiing.weekcalendar.utils.CalendarUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.adapter.attendance.v2.StudentDayStatisticsListAdapter;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.database.ScheduleDaoUtil;
import com.yyide.chatim_pro.databinding.FragmentDayStatisticsBinding;
import com.yyide.chatim_pro.dialog.DeptSelectPop;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.LeaveDeptRsp;
import com.yyide.chatim_pro.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim_pro.presenter.attendance.v2.StudentDayStatisticsPresenter;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.view.attendace.v2.StudentDayStatisticsView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liu tao
 * @version v2
 * @date 2021/11/4 9:10
 * @description 家长查看学生日统计考勤数据
 */
public class StudentDayStatisticsFragment extends BaseMvpFragment<StudentDayStatisticsPresenter> implements StudentDayStatisticsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = StudentDayStatisticsFragment.class.getSimpleName();
    private List<DateTime> eventDates;
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
    private String historyEventId;//上一次选择的事件
    private String historyEventType;//上一次选择的事件
    private StudentDayStatisticsListAdapter dayStatisticsListAdapter;
    private boolean refresh;
    private DateTime lastDateTime;

    //存储事件类型
    private List<StudentAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = new ArrayList<>();
    //课程考勤列表
    private List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> courseBasicVoForm = new ArrayList<>();
    //事件考勤列表 多个考勤类型
    private List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> eventBasicVoList = new ArrayList<>();

    private List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> data = new ArrayList<>();

    public StudentDayStatisticsFragment(String theme,String serverId,String eventType) {
        // Required empty public constructor
        this.historyEvent = theme;
        this.historyEventId = serverId;
        this.historyEventType = eventType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_statistics, container, false);
    }

    /**
     * 初始化
     * @param studentBeanList
     */
    private void initClassData(List<StudentAttendanceDayRsp.DataBean.StudentBean> studentBeanList){
        dialogType = 4;
        classList.clear();
        for (StudentAttendanceDayRsp.DataBean.StudentBean studentBean : studentBeanList) {
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
        if (classList.isEmpty()) {
            Log.e(TAG, "initClassData: 当前账号没有学生");
            mViewBinding.tvClassName.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentDayStatisticsBinding.bind(view);
        eventDates = new ArrayList<>();
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
        String time = DateUtils.switchTime(new Date(), "yyyy-MM-dd");
        final String time1 = DateUtils.formatTime(time, "yyyy-MM-dd", "MM月");
        mViewBinding.tvCurrentDate.setText(time1);
        currentDate = DateUtils.formatTime(time, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
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
            currentDate = DateUtils.formatTime(text, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
            queryAttStatsData(currentClass, currentDate);
        });

        weekCalendar.setWeekChangedListener(dateTime -> {
            String text = dateTime.toString("yyyy-MM-dd");
            final String time2 = DateUtils.formatTime(text, "yyyy-MM-dd", "MM月");
            this.mViewBinding.tvCurrentDate.setText(time2);
            Log.e(TAG, "onDateSelect: " + text);
        });

        RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        //统计-日-学生家长视角
        dayStatisticsListAdapter = new StudentDayStatisticsListAdapter(getContext(), data);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(dayStatisticsListAdapter);
        //请求数据
        lastDateTime = DateTime.now();
        queryAttStatsData(currentClass, currentDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            //mViewBinding.tvClassName.setText(clazzBean.getDeptName());
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
                            deptSelectPop.setOnCheckedListener(dataBean -> {
                                Log.e(TAG, "班级选择:" + dataBean.toString());
                                //mViewBinding.tvClassName.setText(dataBean.getDeptName());
                                //班级id
                                currentClass = dataBean.getClassId();
                                //学生id
                                currentStudentId = dataBean.getDeptId();
                                currentClassName = dataBean.getDeptName();
                                queryAttStatsData(currentClass, currentDate);
                            });
                        }
                );
            }
        }
    }

    /**
     * 查询日考勤统计数据
     *
     * @param classId
     * @param date    拆分为 startTime, endTime
     */
    private void queryAttStatsData(String classId, String date) {
        if (TextUtils.isEmpty(classId) || TextUtils.isEmpty(currentStudentId)) {
            ToastUtils.showShort("当前账号没有班级，不能查询考勤数据！");
            if (refresh) {
                refresh = false;
                mViewBinding.swipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        final DateTime dateTime = DateTime.parse(date, dateTimeFormatter);
        String startTime = dateTime.toString("yyyy-MM-dd ") + "00:00:00";
        String endTime = dateTime.toString("yyyy-MM-dd ") + "23:59:59";
        if (dateTime.compareTo(DateTime.now()) > 0) {
            ToastUtils.showShort("不支持查询未来时间");
            mViewBinding.weekCalendar.setSelectDateTime(lastDateTime);
            return;
        }
        lastDateTime = ScheduleDaoUtil.INSTANCE.toDateTime(date);
        mvpPresenter.queryAppStudentAttendanceDay(classId,currentStudentId ,startTime, endTime);
    }

    @Override
    protected StudentDayStatisticsPresenter createPresenter() {
        return new StudentDayStatisticsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewBinding = null;
    }

    private void showBlank(boolean show) {
        mViewBinding.blankPage.setVisibility(show ? View.VISIBLE : View.GONE);
        //mViewBinding.recyclerview.setVisibility(show?View.GONE:View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void attendanceStatisticsSuccess(StudentAttendanceDayRsp studentAttendanceDayRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + studentAttendanceDayRsp.toString());
        if (refresh) {
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        if (studentAttendanceDayRsp.getCode() == 200) {
            if (studentAttendanceDayRsp.getData() == null) {
                ToastUtils.showShort("" + studentAttendanceDayRsp.getMsg());
            }
            data.clear();
            classroomTeacherAttendanceList.clear();
            courseBasicVoForm.clear();
            eventBasicVoList.clear();

            if (studentAttendanceDayRsp.getData() == null || studentAttendanceDayRsp.getData().getClassroomTeacherAttendanceList() == null) {
                mViewBinding.tvAttendanceType.setVisibility(View.GONE);
                showBlank(true);
                notifyAdapter();
                return;
            }
            //设置班级学生名
            final List<StudentAttendanceDayRsp.DataBean.StudentBean> studentInfos = studentAttendanceDayRsp.getData().getStudentInfos();
            final String className = studentAttendanceDayRsp.getData().getClassName();
            final String studentId = studentAttendanceDayRsp.getData().getStudentId();
            if (studentInfos !=null && !studentInfos.isEmpty()){
                if (!TextUtils.isEmpty(className)) {
                    mViewBinding.tvClassName.setVisibility(View.VISIBLE);
                    mViewBinding.tvClassName.setText(className);
                } else {
                    mViewBinding.tvClassName.setVisibility(View.GONE);
                }
                initClassData(studentInfos);
                initClassView();
            }
            final List<StudentAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = studentAttendanceDayRsp.getData().getClassroomTeacherAttendanceList();
            this.classroomTeacherAttendanceList.addAll(classroomTeacherAttendanceList);
            final StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean appStudentDailyStatisticalForm = studentAttendanceDayRsp.getData().getAppStudentDailyStatisticalForm();
            final List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> eventList = appStudentDailyStatisticalForm.getEventList();
            final List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> courseFormList = appStudentDailyStatisticalForm.getCourseFormList();

            if ((eventList == null || eventList.isEmpty()) && (courseFormList == null || courseFormList.isEmpty())) {
                this.eventList.clear();
                currentEvent = "";
                mViewBinding.tvAttendanceType.setText("");
                mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, null, null);
                showBlank(true);
                return;
            }
            showBlank(false);
            if (eventList != null) {
                this.eventBasicVoList.addAll(eventList);
            }
            if (courseFormList != null) {
                this.courseBasicVoForm.addAll(courseFormList);
            }
            initEventData();
        } else {
            //ToastUtils.showShort("温馨提示：" + studentAttendanceDayRsp.getMsg());
            mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            data.clear();
            classroomTeacherAttendanceList.clear();
            courseBasicVoForm.clear();
            eventBasicVoList.clear();
            notifyAdapter();
            showBlank(true);
        }
    }


    private void showData(String serverId,String type) {
        if ("2".equals(type) && !courseBasicVoForm.isEmpty()) {
            data.clear();
            data.addAll(courseBasicVoForm);
            //更新布局
            notifyAdapter();
            return;
        }
        data.clear();
        for (StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean eventBasicVoListBean : eventBasicVoList) {
            if (Objects.equals(eventBasicVoListBean.getServerId(), serverId)) {
                data.add(eventBasicVoListBean);
            }
        }
        //更新布局
        notifyAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initEventData() {
        eventList.clear();
        for (int i = 0; i < classroomTeacherAttendanceList.size(); i++) {
            final StudentAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(i);
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            if (classroomTeacherAttendanceListBean.getTheme().equals(historyEvent) || (TextUtils.isEmpty(historyEvent) && i == 0)) {
                dataBean.setIsDefault(1);
                currentEvent = classroomTeacherAttendanceListBean.getTheme();
                if (classroomTeacherAttendanceListBean.getType() == 2) {
                    data.addAll(courseBasicVoForm);
                } else {
                    for (StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean eventBasicVoListBean : eventBasicVoList) {
                        if (eventBasicVoListBean.getServerId().equals(classroomTeacherAttendanceListBean.getServerId())) {
                            data.add(eventBasicVoListBean);
                        }
                    }
                }
                //更新布局
                notifyAdapter();
            } else {
                dataBean.setIsDefault(0);
            }
            //dataBean.setIsDefault(i == 0 ? 1 : 0);
            dataBean.setDeptName(classroomTeacherAttendanceListBean.getTheme());
            final String serverId = classroomTeacherAttendanceListBean.getServerId();
            final int type = classroomTeacherAttendanceListBean.getType();
            dataBean.setDeptId(serverId);
            dataBean.setType(type + "");
            eventList.add(dataBean);
        }


        final Optional<LeaveDeptRsp.DataBean> optional = eventList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (!optional.isPresent()) {
            eventList.get(0).setIsDefault(1);
            final StudentAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(0);
            currentEvent = classroomTeacherAttendanceListBean.getTheme();
            historyEvent = currentEvent;
            historyEventId = classroomTeacherAttendanceListBean.getServerId();
            historyEventType = classroomTeacherAttendanceListBean.getType()+"";
            showData(historyEventId,historyEventType);
            //data.addAll(attendancesFormBeanList.get(0).getStudentLists());
            //更新布局
            //notifyAdapter();
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
                deptSelectPop.setOnCheckedListener(dataBean1 -> {
                    Log.e(TAG, "事件选择: id=" + dataBean1.getDeptId() + ", dept=" + dataBean1.getDeptName());
                    mViewBinding.tvAttendanceType.setText(dataBean1.getDeptName());
                    currentEvent = dataBean1.getDeptName();
                    historyEvent = dataBean1.getDeptName();
                    historyEventId = dataBean1.getDeptId();
                    historyEventType = dataBean1.getType();
                    showData(dataBean1.getDeptId(), dataBean1.getType());
                });
            });
        }
    }

    private void notifyAdapter() {
        dayStatisticsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void attendanceStatisticsFail(String msg) {
        Log.e(TAG, "attendanceStatisticsFail: " + msg);
        if (refresh) {
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
        queryAttStatsData(currentClass, currentDate);
    }
}