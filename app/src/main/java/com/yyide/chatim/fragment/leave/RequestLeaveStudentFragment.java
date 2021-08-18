package com.yyide.chatim.fragment.leave;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.leave.LeaveCarbonCopyPeopleActivity;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.adapter.leave.LeaveCourseSectionAdapter;
import com.yyide.chatim.adapter.leave.LeaveReasonTagAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.CourseSectionBean;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.presenter.leave.StaffAskLeavePresenter;
import com.yyide.chatim.presenter.leave.StudentAskLeavePresenter;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.leave.StaffAskLeaveView;
import com.yyide.chatim.view.leave.StudentAskLeaveView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestLeaveStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestLeaveStudentFragment extends BaseMvpFragment<StudentAskLeavePresenter> implements StudentAskLeaveView {
    private static final String TAG = RequestLeaveStudentFragment.class.getSimpleName();;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private List<LeavePhraseRsp.DataBean> tags = new ArrayList<>();
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
    @BindView(R.id.btn_commit)
    Button btn_commit;
    @BindView(R.id.gp_according_course)
    Group gp_according_course;
    @BindView(R.id.gp_according_date)
    Group gp_according_date;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.ll_approver_list)
    LinearLayout ll_approver_list;
    @BindView(R.id.ll_copyer_list)
    LinearLayout ll_copyer_list;
    private String startTime;
    private String endTime;
    private String leaveReason = "2";
    private String reason="";
    private String classesId;
    private List<Long> carbonCopyPeopleId;
    private List<ApproverRsp.DataBean.ListBean> carbonCopyPeopleList;
    private String classesName;
    private TimePickerDialog mDialogAll;
    private LeaveCourseSectionAdapter leaveCourseSectionAdapter;
    private List<LeaveDeptRsp.DataBean> classList = new ArrayList<>();
    private String studentId;
    private String studentUserId;

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
            LeavePhraseRsp.DataBean tag = tags.get(position);
            //editLeaveReason.setText(tag.getTag());
            if (!tag.isChecked()) {
                reason += tag.getTag();
            }else {
                reason = reason.replace(tag.getTag(),"");
            }
            Log.e(TAG, "onViewCreated: "+reason);
            editLeaveReason.setText(reason);

            //修改状态
            tag.setChecked(!tag.isChecked());
            leaveReasonTagAdapter.notifyDataSetChanged();
        });
        accordingCourseLeave(false);
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
                default:
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
        //初始化班级列表
        initClassData();
        initClassView();
        //tv_department.setText(classesName);
        btn_commit.setAlpha(0.5f);
        btn_commit.setClickable(false);
        //请求审批流程
        if (!TextUtils.isEmpty(classesId)){
            mvpPresenter.getApprover(classesId);
        }else {
            ToastUtils.showShort("当前账号没有指定班级，不能使用请假功能，请联系管理员！");
        }

        mvpPresenter.queryLeavePhraseList(1);

        editLeaveReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reason = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
    protected StudentAskLeavePresenter createPresenter() {
        return new StudentAskLeavePresenter(this);
    }

    @OnClick({R.id.cl_start_time, R.id.cl_end_time,R.id.cl_ask_for_leave_date,R.id.btn_commit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.cl_start_time:
                showTime("选择开始时间", startTime,startTimeListener);
                break;
            case R.id.cl_end_time:
                if (TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(startTime)){
                    showTime("选择结束时间", startTime,endTimeListener);
                    break;
                }
                showTime("选择结束时间", endTime,endTimeListener);
                break;
            case R.id.cl_ask_for_leave_date:
                showTime("选择请假日期", "",dateTimeListener);
                break;
            case R.id.btn_commit:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_commit)){
                    commit();
                }
                break;
            default:
                break;
        }
    }

    private void commit(){
        /*
         * {
         *     "startTime": "2021-05-18 17:08:00",
         *     "endTime": "2021-05-24 17:08:00",
         *     "leaveReason": "1",
         *     "reason": "被传染了新冠肺炎",
         *     "classId": 2076,
         *     "className": "高二1班",
         *     "carbonCopyPeopleId": null,
         *     "list": []
         * }
         */
        if (TextUtils.isEmpty(startTime)){
            ToastUtils.showShort("请选择请假开始时间");
            return;
        }
        if (TextUtils.isEmpty(endTime)){
            ToastUtils.showShort("请选择请假结束时间");
            return;
        }
        if (DateUtils.parseTimestamp(endTime,"")-DateUtils.parseTimestamp(startTime,"")<=0){
            ToastUtils.showShort("请假结束时间应该大于开始时间");
            return;
        }
        reason = editLeaveReason.getText().toString();
        if (TextUtils.isEmpty(reason)){
            ToastUtils.showShort("请假事由不能为空");
            return;
        }

        mvpPresenter.addStudentLeave(startTime,endTime,leaveReason,reason,classesId,studentId,studentUserId,classesName,carbonCopyPeopleId);
    }


    private void initTags() {
//        tags.add("小孩生病了");
//        tags.add("家里有事");
//        tags.add("小孩课外辅导");
//
//        for (int i = 0; i < 8; i++) {
//            courses.add(new CourseSectionBean("第" + (i + 1) + "节", "课程" + i, false));
//        }
    }

    private void showTime(String title, String currentMillseconds,OnDateSetListener onDateSetListener) {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        long oneYears = 3L * 365 * 1000 * 60 * 60 * 24L;
        long currentSelectedTime = 0;
        if (!TextUtils.isEmpty(currentMillseconds)){
            currentSelectedTime = DateUtils.parseTimestamp(currentMillseconds,"");
        }
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(onDateSetListener)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId(title)
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()-oneYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(currentSelectedTime == 0?System.currentTimeMillis():currentSelectedTime)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
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

    private final OnDateSetListener startTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String timingTime = simpleDateFormat.format(new Date(millseconds));
        Log.d(TAG, "startTimeListener: " + timingTime);
        tvStartTime.setText(timingTime);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        startTime = simpleDateFormat2.format(new Date(millseconds));
    };

    private final OnDateSetListener endTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String timingTime = simpleDateFormat.format(new Date(millseconds));
        Log.d(TAG, "endTimeListener: " + timingTime);
        tvEndTime.setText(timingTime);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        endTime = simpleDateFormat2.format(new Date(millseconds));
    };

    private final OnDateSetListener dateTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String timingTime = simpleDateFormat.format(new Date(millseconds));
        Log.d(TAG, "endTimeListener: " + timingTime);
        tv_ask_for_leave_date.setText(timingTime);
    };

    @Override
    public void approver(ApproverRsp approverRsp) {
        Log.e(TAG, "approver: " + approverRsp.toString());
        final ApproverRsp.DataBean data = approverRsp.getData();
        ll_approver_list.removeAllViews();
        ll_copyer_list.removeAllViews();
        if (approverRsp.getCode() != 200){
            btn_commit.setAlpha(0.5f);
            btn_commit.setClickable(false);
            ToastUtils.showShort(approverRsp.getMsg());
            return;
        }
        if (data != null){
            //审批人
            final ApproverRsp.DataBean.PeopleFormBean peopleForm = data.getPeopleForm();
            if (peopleForm == null){
                btn_commit.setAlpha(0.5f);
                btn_commit.setClickable(false);
                ToastUtils.showLong("没有指定审批人，不能使用请假功能！");
                return;
            }

            btn_commit.setAlpha(1f);
            btn_commit.setClickable(true);
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
            final TextView tv_approver_name = view.findViewById(R.id.tv_approver_name);
            final ImageView approverUserHead = view.findViewById(R.id.iv_user_head);
            showImage(peopleForm.getImage(),approverUserHead);
            tv_approver_name.setText(peopleForm.getName());
            ll_approver_list.addView(view);
            //超送人
            final List<ApproverRsp.DataBean.ListBean> list = data.getList();
            if (list!= null && !list.isEmpty()){
                carbonCopyPeopleId = new ArrayList<>();
                carbonCopyPeopleList = new ArrayList<>();
                for (ApproverRsp.DataBean.ListBean listBean : list) {
                    final long userId = listBean.getUserId();
                    carbonCopyPeopleId.add(userId);
                    carbonCopyPeopleList.add(listBean);
                    if (carbonCopyPeopleId.size()<=3){
                        final String name = listBean.getName();
                        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
                        final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                        final ImageView iv_user_head = view1.findViewById(R.id.iv_user_head);
                        showImage(listBean.getImage(),iv_user_head);
                        tv_copyer_name.setText(name);
                        ll_copyer_list.addView(view1);
                        //setViewLayoutParams(view1, StatusBarUtils.dip2px(getContext(),45),0);
                    }
                }
                //更多
                if (carbonCopyPeopleId.size()>3){
                    final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
                    final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                    final ImageView userHeadImage = view1.findViewById(R.id.iv_user_head);
                    tv_copyer_name.setText("查看全部");
                    userHeadImage.setImageResource(R.drawable.icon_read_more);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getActivity(), LeaveCarbonCopyPeopleActivity.class);
                            intent.putExtra("carbonCopyPeople", JSON.toJSONString(carbonCopyPeopleList));
                            intent.putExtra("type",1);
                            startActivity(intent);
                        }
                    });
                    ll_copyer_list.addView(view1);
                }
            }
        }
    }
    private void showImage(String url,ImageView imageView){
        if (TextUtils.isEmpty(url)){
            return;
        }
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
    @Override
    public void approverFail(String msg) {
        Log.e(TAG, "approverFail: "+msg );
        ToastUtils.showLong(msg);
    }

    @Override
    public void addStudentLeave(BaseRsp baseRsp) {
        Log.e(TAG, "addStudentLeave: "+baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            final Long id = Long.valueOf(baseRsp.getData());
            final Intent intent = new Intent(getActivity(), LeaveFlowDetailActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
            getActivity().finish();
        }else {
            ToastUtils.showShort("提交失败："+baseRsp.getMsg());
        }
    }

    @Override
    public void addStudentLeaveFail(String msg) {
        ToastUtils.showShort("提交失败："+msg);
    }

    @Override
    public void leavePhrase(LeavePhraseRsp leavePhraseRsp) {
        if (leavePhraseRsp.getCode() == 200){
            final List<LeavePhraseRsp.DataBean> data = leavePhraseRsp.getData();
            tags.clear();
            tags.addAll(data);
            leaveReasonTagAdapter.notifyDataSetChanged();
        }else {
            ToastUtils.showLong(leavePhraseRsp.getMsg());
        }
    }

    @Override
    public void leavePhraseFail(String msg) {
        Log.e(TAG, "leavePhraseFail: "+msg );
        ToastUtils.showLong(msg);
    }
    /**
     * 重设 view 的宽高
     */
    public static void setViewLayoutParams(View view, int nWidth, int nHeight) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.width = nWidth;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(lp);
    }

    /**
     * 初始化学生所在班级列表，供当前账号用户选择
     */
    private void initClassData() {
        final List<GetUserSchoolRsp.DataBean.FormBean> form = SpData.getIdentityInfo().form;
        //final GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
        classList.clear();
        final String classesStudentName = SpData.getClassesStudentName();
        for (GetUserSchoolRsp.DataBean.FormBean formBean : form) {
            final String studentName = formBean.classesStudentName;
            final String studentId = formBean.studentId;
            final String classesId = formBean.classesId;
            final String studentUserId = formBean.studentUserId;
            final LeaveDeptRsp.DataBean dataBean = new LeaveDeptRsp.DataBean();
            dataBean.setDeptId(studentId);
            dataBean.setClassId(classesId);
            dataBean.setDeptName(studentName);
            dataBean.setStudentUserId(studentUserId);
            dataBean.setIsDefault(0);
            if (studentName.equals(classesStudentName)){
                dataBean.setIsDefault(1);
                this.studentId = dataBean.getDeptId();
                this.studentUserId = dataBean.getStudentUserId();
            }
            classList.add(dataBean);
        }
        if (!classList.isEmpty()) {
//            final LeaveDeptRsp.DataBean dataBean = classList.get(0);
//            dataBean.setIsDefault(1);
//            studentId = dataBean.getDeptId();
//            studentUserId = dataBean.getStudentUserId();
        } else {
            Log.e(TAG, "initClassData: 当前账号没有学生" );
            tv_department.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initClassView() {
        final Optional<LeaveDeptRsp.DataBean> classOptional = classList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (classOptional.isPresent()) {
            final LeaveDeptRsp.DataBean clazzBean = classOptional.get();
            tv_department.setText(clazzBean.getDeptName());
            studentId = clazzBean.getDeptId();
            classesId = clazzBean.getClassId();
            studentUserId = clazzBean.getStudentUserId();
            if (classList.size() <= 1) {
                tv_department.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.icon_right);
                //设置图片大小，必须设置
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_department.setCompoundDrawables(null, null, drawable, null);
                tv_department.setOnClickListener(v -> {
                            final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), 4, classList);
                            deptSelectPop.setOnCheckedListener((dataBean) -> {
                                Log.e(TAG, "班级选择: " + dataBean.toString());
                                tv_department.setText(dataBean.getDeptName());
                                studentId = dataBean.getDeptId();
                                classesId = dataBean.getClassId();
                                studentUserId = dataBean.getStudentUserId();
                                mvpPresenter.getApprover(TextUtils.isEmpty(classesId)?"0":classesId);
                            });
                        }
                );
            }
        }
    }
}