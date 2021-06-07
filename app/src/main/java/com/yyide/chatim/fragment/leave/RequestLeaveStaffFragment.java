package com.yyide.chatim.fragment.leave;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
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
import com.yyide.chatim.activity.leave.AddressBookActivity;
import com.yyide.chatim.activity.leave.LeaveCarbonCopyPeopleActivity;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.activity.notice.NoticeScopeActivity;
import com.yyide.chatim.adapter.leave.LeaveReasonTagAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.presenter.leave.StaffAskLeavePresenter;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.leave.StaffAskLeaveView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestLeaveStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestLeaveStaffFragment extends BaseMvpFragment<StaffAskLeavePresenter> implements StaffAskLeaveView {
    private static final String TAG = RequestLeaveStaffFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private List<LeavePhraseRsp.DataBean> tags = new ArrayList<>();
    private LeaveReasonTagAdapter leaveReasonTagAdapter;
    @BindView(R.id.recyclerview_tag_hint)
    RecyclerView recyclerviewTagHint;
    @BindView(R.id.et_reason_leave)
    EditText editLeaveReason;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.ll_approver_list)
    LinearLayout ll_approver_list;
    @BindView(R.id.ll_copyer_list)
    LinearLayout ll_copyer_list;
    @BindView(R.id.btn_commit)
    Button btn_commit;
    @BindView(R.id.iv_add_staff)
    ImageView iv_add_staff;
    private static int REQUEST_CODE = 100;
    private TimePickerDialog mDialogAll;
    private List<LeaveDeptRsp.DataBean> deptList = new ArrayList<>();
    private String startTime;
    private String endTime;
    private String leaveReason = "2";
    private String reason="";
    private long deptId;
    private List<Long> carbonCopyPeopleId = new ArrayList<>();
    private List<ApproverRsp.DataBean.ListBean> carbonCopyPeopleList = new ArrayList<>();
    private String deptName;

    public RequestLeaveStaffFragment() {
        // Required empty public constructor
    }

    public static RequestLeaveStaffFragment newInstance(String param1) {
        RequestLeaveStaffFragment fragment = new RequestLeaveStaffFragment();
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
        return inflater.inflate(R.layout.fragment_request_leave_staff, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initTags();
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
            reason += tag.getTag();
            editLeaveReason.setText(reason);
        });
        btn_commit.setAlpha(0.5f);
        btn_commit.setClickable(false);
        mvpPresenter.queryDeptInfoByTeacherId();
        mvpPresenter.queryLeavePhraseList(2);

        iv_add_staff.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), AddressBookActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.cl_start_time, R.id.cl_end_time,R.id.btn_commit,R.id.tv_department})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.cl_start_time:
                showTime("选择开始时间",startTime,startTimeListener);
                break;
            case R.id.cl_end_time:
                if (TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(startTime)){
                    showTime("选择结束时间", startTime,endTimeListener);
                    break;
                }
                showTime("选择结束时间", endTime,endTimeListener);
                break;
            //请假提交
            case R.id.btn_commit:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_commit)){
                    commit();
                }
                break;
            case R.id.tv_department:
                if (deptList.size()>1){
                    final DeptSelectPop deptSelectPop = new DeptSelectPop(getActivity(), 1,deptList);
                    deptSelectPop.setOnCheckedListener((id, dept) -> {
                        deptName = dept;
                        deptId = id;
                        tv_department.setText(dept);
                        mvpPresenter.getApprover(id);
                    });
                }
                break;
            default:
                break;
        }
    }

    private void initTags() {
//        tags.add("小孩生病了");
//        tags.add("家里有事");
//        tags.add("小孩课外辅导");
    }

    /**
     * 请假
     */
    private void commit(){
        //{
        //     "startTime":"2021-05-17 17:08:00",
        //    "endTime":"2021-05-24 17:08:00",
        //    "leaveReason":"2",
        //    "reason":"被学生传染了新冠肺炎啊",
        //    "deptId":"1568",
        //    "carbonCopyPeopleId":[],
        //    "deptName":"班主任"
        //}
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

        mvpPresenter.addTeacherLeave(startTime,endTime,leaveReason,reason,deptId,deptName,carbonCopyPeopleId);
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
    protected StaffAskLeavePresenter createPresenter() {
        return new StaffAskLeavePresenter(this);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void leaveDept(LeaveDeptRsp leaveDeptRsp) {
        Log.e(TAG, "leaveDept: "+leaveDeptRsp.toString() );
        if (leaveDeptRsp.getCode() != 200){
            ToastUtils.showShort(""+leaveDeptRsp.getMsg());
            return;
        }
        final List<LeaveDeptRsp.DataBean> data = leaveDeptRsp.getData();
        if (!data.isEmpty()) {
            final Optional<LeaveDeptRsp.DataBean> dataBeanOptional = data.stream().filter(it -> it.getIsDefault() == 1).findFirst();
            LeaveDeptRsp.DataBean dataBean;
            dataBean = dataBeanOptional.orElseGet(() -> data.get(0));
            deptName = dataBean.getDeptName();
            tv_department.setText(deptName);
            deptId = dataBean.getDeptId();
            mvpPresenter.getApprover(deptId);
        }
        if (data.size()>1){
            Drawable drawable = getResources().getDrawable(R.drawable.icon_right);
            //设置图片大小，必须设置
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_department.setCompoundDrawables(null, null, drawable, null);
        }
        deptList.addAll(data);
    }

    @Override
    public void leaveDeptFail(String msg) {

    }

    @Override
    public void approver(ApproverRsp approverRsp) {
        final ApproverRsp.DataBean data = approverRsp.getData();
        if (approverRsp.getCode() != 200){
            ToastUtils.showShort(approverRsp.getMsg());
            iv_add_staff.setVisibility(View.GONE);
            return;
        }
        if (data != null){
            iv_add_staff.setVisibility(View.VISIBLE);
            //审批人
            ll_approver_list.removeAllViews();
            ll_copyer_list.removeAllViews();
            btn_commit.setAlpha(1f);
            btn_commit.setClickable(true);
            final ApproverRsp.DataBean.PeopleFormBean peopleForm = data.getPeopleForm();
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
            final TextView tv_approver_name = view.findViewById(R.id.tv_approver_name);
            tv_approver_name.setText(peopleForm.getName());
            ll_approver_list.addView(view);
            //超送人
            final List<ApproverRsp.DataBean.ListBean> list = data.getList();
            if (list!= null && !list.isEmpty()){
//                carbonCopyPeopleId = new ArrayList<>();
//                carbonCopyPeopleList = new ArrayList<>();
//                for (ApproverRsp.DataBean.ListBean listBean : list) {
//                    final long userId = listBean.getUserId();
//                    carbonCopyPeopleId.add(userId);
//                    carbonCopyPeopleList.add(listBean);
//                    if (carbonCopyPeopleId.size()<3){
//                        final String name = listBean.getName();
//                        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
//                        final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
//                        final ImageView iv_user_head = view1.findViewById(R.id.iv_user_head);
//                        showImage(listBean.getImage(),iv_user_head);
//                        tv_copyer_name.setText(name);
//                        ll_copyer_list.addView(view1);
//                    }
//                }
                //更多
//                if (carbonCopyPeopleId.size()>3){
//                    final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
//                    final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
//                    final ImageView userHeadImage = view1.findViewById(R.id.iv_user_head);
//                    tv_copyer_name.setText("查看全部");
//                    userHeadImage.setImageResource(R.drawable.icon_read_more);
//                    view1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            final Intent intent = new Intent(getActivity(), LeaveCarbonCopyPeopleActivity.class);
//                            intent.putExtra("carbonCopyPeople", JSON.toJSONString(carbonCopyPeopleList));
//                            intent.putExtra("type",1);
//                            startActivity(intent);
//                        }
//                    });
//                    ll_copyer_list.addView(view1);
//                }
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

    }

    @Override
    public void addTeacherLeave(BaseRsp baseRsp) {
        Log.e(TAG, "addTeacherLeave: "+baseRsp.toString() );
        if (baseRsp.getCode() == 200) {
            final Long id = Long.valueOf(baseRsp.getData());
            final Intent intent = new Intent(getActivity(), LeaveFlowDetailActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
            getActivity().finish();
        } else {
            ToastUtils.showShort("提交失败："+baseRsp.getMsg());
        }
    }

    @Override
    public void addTeacherLeaveFail(String msg) {
        Log.e(TAG, "addTeacherLeaveFail: "+msg );
        ToastUtils.showShort("提交失败："+msg);
    }

    @Override
    public void leavePhrase(LeavePhraseRsp leavePhraseRsp) {
        if (leavePhraseRsp.getCode() == 200){
            final List<LeavePhraseRsp.DataBean> data = leavePhraseRsp.getData();
            tags.clear();
            tags.addAll(data);
            leaveReasonTagAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void leavePhraseFail(String msg) {

    }

    private List<AddressBookRsp.DataBean> filterCopyerList(List<AddressBookRsp.DataBean> list) {
        final Iterator<AddressBookRsp.DataBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            final AddressBookRsp.DataBean dataBean = iterator.next();
            if (carbonCopyPeopleId.contains(dataBean.getTeacherId())) {
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            String deptList = data.getStringExtra("deptList");
            Log.e(TAG, "onActivityResult: " + deptList);
            if (!TextUtils.isDigitsOnly(deptList)){
                final List<AddressBookRsp.DataBean> dataBeans1 = JSON.parseArray(deptList, AddressBookRsp.DataBean.class);
                final List<AddressBookRsp.DataBean> dataBeans = filterCopyerList(dataBeans1);
                for (int i = 0; i < dataBeans.size(); i++) {
                    final AddressBookRsp.DataBean dataBean = dataBeans.get(i);
                    carbonCopyPeopleId.add(dataBean.getTeacherId());
                    final ApproverRsp.DataBean.ListBean listBean = new ApproverRsp.DataBean.ListBean();
                    listBean.setImage(dataBean.getImage());
                    listBean.setName(dataBean.getTeacherName());
                    listBean.setUserId(dataBean.getTeacherId());
                    carbonCopyPeopleList.add(listBean);
                    if (carbonCopyPeopleId.size() <=2) {
                        final String name = dataBean.getTeacherName();
                        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
                        final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                        final ImageView iv_user_head = view1.findViewById(R.id.iv_user_head);
                        showImage(dataBean.getImage(), iv_user_head);
                        tv_copyer_name.setText(name);
                        ll_copyer_list.addView(view1);
                        setViewLayoutParams(view1, StatusBarUtils.dip2px(getContext(),45),0);
                    }
                }

//                if (carbonCopyPeopleId.size() == 3){
//                    final ApproverRsp.DataBean.ListBean dataBean = carbonCopyPeopleList.get(2);
//                    final String name = dataBean.getName();
//                    final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
//                    final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
//                    final ImageView iv_user_head = view1.findViewById(R.id.iv_user_head);
//                    showImage(dataBean.getImage(), iv_user_head);
//                    tv_copyer_name.setText(name);
//                    ll_copyer_list.addView(view1);
//                }
                //更多
                final int childCount = ll_copyer_list.getChildCount();
                if (carbonCopyPeopleId.size()>2 && childCount<=2){
                    final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_approver_head, null);
                    ll_copyer_list.removeView(view1);
                    final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                    final ImageView userHeadImage = view1.findViewById(R.id.iv_user_head);
                    tv_copyer_name.setText("查看全部");
                    userHeadImage.setImageResource(R.drawable.icon_read_more);
                    view1.setOnClickListener(v -> {
                        final Intent intent = new Intent(getActivity(), LeaveCarbonCopyPeopleActivity.class);
                        intent.putExtra("carbonCopyPeople", JSON.toJSONString(carbonCopyPeopleList));
                        intent.putExtra("type",1);
                        startActivity(intent);
                    });
                    ll_copyer_list.addView(view1);
                }
            }
        }
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
}