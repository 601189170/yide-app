package com.yyide.chatim.fragment.attendance.v2;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.beiing.weekcalendar.WeekCalendar;
import com.beiing.weekcalendar.listener.GetViewHelper;
import com.beiing.weekcalendar.listener.WeekChangeListener;
import com.beiing.weekcalendar.utils.CalendarUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.v2.TeacherStatisticsDetailActivity;
import com.yyide.chatim.adapter.attendance.v2.TeacherDayStatisticsListAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentDayStatisticsBinding;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim.presenter.attendance.v2.TeacherDayStatisticsPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.attendace.v2.TeacherDayStatisticsView;

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
 * @date 2021/11/3 10:16
 * @description 班主任或任课老师查看日统计信息
 */
public class TeacherDayStatisticsFragment extends BaseMvpFragment<TeacherDayStatisticsPresenter> implements TeacherDayStatisticsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = TeacherDayStatisticsFragment.class.getSimpleName();
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
    private TeacherDayStatisticsListAdapter dayStatisticsListAdapter;
    private boolean refresh;

    //存储事件类型
    private List<TeacherAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = new ArrayList<>();
    //课程考勤列表
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> courseBasicVoForm = new ArrayList<>();
    //事件考勤列表 多个考勤类型
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> eventBasicVoList = new ArrayList<>();

    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> data = new ArrayList<>();

    public TeacherDayStatisticsFragment(String theme,String serverId,String eventType) {
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

    private void initClassData() {
        if (SpData.getIdentityInfo().staffIdentity()) {
            dialogType = 2;
        } else {
            dialogType = 4;
        }
        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
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
                if (classesName.equals(classesStudentName)) {
                    dataBean.setIsDefault(1);
                }
            } else {
                dataBean.setDeptName(studentName);
                if (studentName.equals(classesStudentName)) {
                    dataBean.setIsDefault(1);
                }
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
        //统计-日-班主任和教师视角
        dayStatisticsListAdapter = new TeacherDayStatisticsListAdapter(getContext(), data);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(dayStatisticsListAdapter);
        dayStatisticsListAdapter.setOnClickedListener(position -> {
            //进入考勤详情页
            final String jsonString = JSON.toJSONString(data);
            Log.e(TAG, "进入考勤详情页: " + jsonString);
            final Intent intent = new Intent(getActivity(), TeacherStatisticsDetailActivity.class);
            intent.putExtra("data", jsonString);
            intent.putExtra("position", position);
            intent.putExtra("currentClass", currentClassName);
            startActivity(intent);
        });

        //请求数据
        queryAttStatsData(currentClass, currentDate);
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
                            deptSelectPop.setOnCheckedListener(dataBean -> {
                                Log.e(TAG, "班级选择:" + dataBean.toString());
                                mViewBinding.tvClassName.setText(dataBean.getDeptName());
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
        if (TextUtils.isEmpty(classId)){
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
        }
        mvpPresenter.queryAppTeacherThreeAttendanceDay(classId, startTime, endTime);
    }

    @Override
    protected TeacherDayStatisticsPresenter createPresenter() {
        return new TeacherDayStatisticsPresenter(this);
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
    public void attendanceStatisticsSuccess(TeacherAttendanceDayRsp teacherAttendanceDayRsp) {
        Log.e(TAG, "attendanceStatisticsSuccess: " + teacherAttendanceDayRsp.toString());
        if (refresh) {
            refresh = false;
            mViewBinding.swipeRefreshLayout.setRefreshing(false);
        }
        if (teacherAttendanceDayRsp.getCode() == 200) {
            if (teacherAttendanceDayRsp.getData() == null) {
                ToastUtils.showShort("" + teacherAttendanceDayRsp.getMsg());
            }
            data.clear();
            classroomTeacherAttendanceList.clear();
            courseBasicVoForm.clear();
            eventBasicVoList.clear();

            if (teacherAttendanceDayRsp.getData() == null || teacherAttendanceDayRsp.getData().getClassroomTeacherAttendanceList() == null) {
                mViewBinding.tvAttendanceType.setVisibility(View.GONE);
                showBlank(true);
                notifyAdapter();
                return;
            }
            final List<TeacherAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean> classroomTeacherAttendanceList = teacherAttendanceDayRsp.getData().getClassroomTeacherAttendanceList();
            this.classroomTeacherAttendanceList.addAll(classroomTeacherAttendanceList);
            final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> courseBasicVoForm = teacherAttendanceDayRsp.getData().getCourseBasicVoForm();
            final List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> eventBasicVoList = teacherAttendanceDayRsp.getData().getEventBasicVoList();
            if ((courseBasicVoForm == null || courseBasicVoForm.isEmpty()) && (eventBasicVoList == null || eventBasicVoList.isEmpty())) {
                eventList.clear();
                currentEvent = "";
                mViewBinding.tvAttendanceType.setText("");
                mViewBinding.tvAttendanceType.setCompoundDrawables(null, null, null, null);
                showBlank(true);
                return;
            }
            showBlank(false);
            if (eventBasicVoList != null) {
                this.eventBasicVoList.addAll(eventBasicVoList);
            }
            if (courseBasicVoForm != null) {
                this.courseBasicVoForm.addAll(courseBasicVoForm);
            }
            initEventData();
        } else {
            //ToastUtils.showShort("温馨提示：" + teacherAttendanceDayRsp.getMsg());
            mViewBinding.tvAttendanceType.setVisibility(View.GONE);
            //showData(null);
            data.clear();
            classroomTeacherAttendanceList.clear();
            courseBasicVoForm.clear();
            eventBasicVoList.clear();
            eventList.clear();
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
        for (TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean eventBasicVoListBean : eventBasicVoList) {
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
            final TeacherAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(i);
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            if (classroomTeacherAttendanceListBean.getTheme().equals(historyEvent) || (TextUtils.isEmpty(historyEvent) && i == 0)) {
                dataBean.setIsDefault(1);
                currentEvent = classroomTeacherAttendanceListBean.getTheme();
                if (classroomTeacherAttendanceListBean.getType() == 2) {
                    data.addAll(courseBasicVoForm);
                } else {
                    for (TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean eventBasicVoListBean : eventBasicVoList) {
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
            final TeacherAttendanceDayRsp.DataBean.ClassroomTeacherAttendanceListBean classroomTeacherAttendanceListBean = classroomTeacherAttendanceList.get(0);
            currentEvent = classroomTeacherAttendanceList.get(0).getTheme();
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
                    historyEventType = dataBean1.getType();
                    historyEventId = dataBean1.getDeptId();
                    showData(dataBean1.getDeptId(), dataBean1.getType()+"");
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
        data.clear();
        classroomTeacherAttendanceList.clear();
        courseBasicVoForm.clear();
        eventBasicVoList.clear();
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