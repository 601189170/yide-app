package com.yyide.chatim.fragment.leave;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.leave.LeaveCourseSectionAdapter;
import com.yyide.chatim.adapter.leave.LeaveReasonTagAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.CourseSectionBean;
import com.yyide.chatim.presenter.leave.RequestLeavePresenter;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.leave.RequestLeaveView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestLeaveStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestLeaveStudentFragment extends BaseMvpFragment<RequestLeavePresenter> implements RequestLeaveView {
    private static final String TAG = "NoticeAnnouncementListF";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private List<String> tags = new ArrayList<>();
    private List<CourseSectionBean> courses = new ArrayList<>();
    private LeaveReasonTagAdapter leaveReasonTagAdapter;
    @BindView(R.id.recyclerview_tag_hint)
    RecyclerView recyclerviewTagHint;
    @BindView(R.id.recyclerview_course)
    RecyclerView recyclerViewCourse;//课程
    @BindView(R.id.et_reason_leave)
    EditText editLeaveReason;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.rg_time_type)
    RadioGroup radioGroup;
    @BindView(R.id.tv_ask_for_leave_date)
    TextView tv_ask_for_leave_date;
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @BindView(R.id.gp_according_course)
    Group gp_according_course;
    @BindView(R.id.gp_according_date)
    Group gp_according_date;

    private TimePickerDialog mDialogAll;
    private LeaveCourseSectionAdapter leaveCourseSectionAdapter;

    public RequestLeaveStudentFragment() {
        // Required empty public constructor
    }

    public static RequestLeaveStudentFragment newInstance(String param1) {
        RequestLeaveStudentFragment fragment = new RequestLeaveStudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.e(TAG, "onCreate: " + mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_leave_student, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTags();
        leaveReasonTagAdapter = new LeaveReasonTagAdapter(getActivity(), tags);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity());
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。

        recyclerviewTagHint.setLayoutManager(flexboxLayoutManager);
        recyclerviewTagHint.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(5)));
        recyclerviewTagHint.setAdapter(leaveReasonTagAdapter);
        leaveReasonTagAdapter.setOnClickedListener(position -> {
            String tag = tags.get(position);
            editLeaveReason.setText(tag);
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_according_course:
                    Log.d(TAG, "根据课程请假");
                    accordingCourseLeave(true);
                    break;
                case R.id.rb_according_date:
                    Log.d(TAG, "根据日期请假");
                    accordingCourseLeave(false);
                    break;
            }
        });

        leaveCourseSectionAdapter = new LeaveCourseSectionAdapter(getActivity(), courses);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerViewCourse.setLayoutManager(gridLayoutManager);
        recyclerViewCourse.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(5)));
        recyclerViewCourse.setAdapter(leaveCourseSectionAdapter);
        leaveCourseSectionAdapter.setOnClickedListener(position -> {
            final CourseSectionBean courseSectionBean = courses.get(position);
            courseSectionBean.setChecked(!courseSectionBean.isChecked());
            setCheckAll();
            leaveCourseSectionAdapter.notifyDataSetChanged();
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()){
                Log.e(TAG, "initView: 代码触发，不处理监听事件。" );
                return;
            }
            setCheckAll(isChecked);
        });
    }

    /**
     * 判断是否根据课程请假
     * @param course
     */
    private void accordingCourseLeave(boolean course){
        if (course){
            gp_according_course.setVisibility(View.VISIBLE);
            gp_according_date.setVisibility(View.GONE);
        }else {
            gp_according_course.setVisibility(View.GONE);
            gp_according_date.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setCheckAll(){
        final long count = courses.stream().map(CourseSectionBean::isChecked).filter(it -> it).count();
        checkBox.setChecked(count == courses.size());
    }

    private void setCheckAll(boolean isChecked){
        for (CourseSectionBean cours : courses) {
            cours.setChecked(isChecked);
        }
        leaveCourseSectionAdapter.notifyDataSetChanged();
    }

    @Override
    protected RequestLeavePresenter createPresenter() {
        return new RequestLeavePresenter(this);
    }

    @OnClick({R.id.cl_start_time, R.id.cl_end_time,R.id.cl_ask_for_leave_date})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.cl_start_time:
                showTime("选择开始时间", startTimeListener);
                break;
            case R.id.cl_end_time:
                showTime("选择结束时间", endTimeListener);
                break;
            case R.id.cl_ask_for_leave_date:
                showTime("选择请假日期", dateTimeListener);
                break;
            default:
                break;
        }
    }

    private void initTags() {
        tags.add("小孩生病了");
        tags.add("家里有事");
        tags.add("小孩课外辅导");

        for (int i = 0; i < 8; i++) {
            courses.add(new CourseSectionBean("第" + (i + 1) + "节", "课程" + i, false));
        }
    }

    private void showTime(String title, OnDateSetListener onDateSetListener) {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(onDateSetListener)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId(title)
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_212121))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(16)
                .build();
        mDialogAll.show(getActivity().getSupportFragmentManager(), "all");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private OnDateSetListener startTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Long time = Long.valueOf(millseconds);
        String timingTime = simpleDateFormat.format(new Date(time));
        Log.d(TAG, "startTimeListener: " + timingTime);
        tvStartTime.setText(timingTime);
    };

    private OnDateSetListener endTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Long time = Long.valueOf(millseconds);
        String timingTime = simpleDateFormat.format(new Date(time));
        Log.d(TAG, "endTimeListener: " + timingTime);
        tvEndTime.setText(timingTime);
    };

    private OnDateSetListener dateTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Long time = Long.valueOf(millseconds);
        String timingTime = simpleDateFormat.format(new Date(time));
        Log.d(TAG, "endTimeListener: " + timingTime);
        tv_ask_for_leave_date.setText(timingTime);
    };
}