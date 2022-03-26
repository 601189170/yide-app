package com.yyide.chatim.fragment.leave;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.leave.AddApprovalActivity;
import com.yyide.chatim.activity.leave.AddCcActivity;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.adapter.leave.LeaveReasonTagAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.dialog.DeptSelectPop;
import com.yyide.chatim.dialog.OnCheckCallBack;
import com.yyide.chatim.dialog.SwitchStudentPop;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.LeaveApprovalBean;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.presenter.leave.StaffAskLeavePresenter;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.DatePickerDialogUtil;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.SpacesItemDecoration;
import com.yyide.chatim.view.leave.StaffAskLeaveView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教师家长/请假
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
    @BindView(R.id.etStartTime)
    EditText etStartTime;
    @BindView(R.id.etEndTime)
    EditText etEndTime;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.approvalList)
    RecyclerView approvalList;
    @BindView(R.id.ccList)
    RecyclerView ccList;
    @BindView(R.id.btn_commit)
    Button btn_commit;
    @BindView(R.id.iv_add_staff)
    ImageView iv_add_staff;
    @BindView(R.id.tv_approver)
    TextView tv_approver;
    @BindView(R.id.iv_add_approval)
    ImageView ivApproval;
    @BindView(R.id.tvBranch)
    TextView tvBranch;
    @BindView(R.id.tvName)
    TextView tvStudentName;
    @BindView(R.id.groupTop)
    Group groupTop;
    @BindView(R.id.cl_student)
    ConstraintLayout cl_student;
    private static int REQUEST_CODE = 100;
    private List<LeaveDeptRsp.DataBean> deptList = new ArrayList<>();
    private List<LeaveApprovalBean.Cc> ccDataList = new ArrayList<>();
    private List<LeaveApprovalBean.LeaveCommitBean> approvalData = new ArrayList<>();
    private String startTime;
    private String endTime;
    private String sponsorType = "";
    private String reason = "";
    private float hours;
    private String procId;
    private List<Long> carbonCopyPeopleId = new ArrayList<>();
    private List<ApproverRsp.DataBean.ListBean> carbonCopyPeopleList = new ArrayList<>();
    private String deptName;
    private String dept;//部门-班级= ID


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
        etStartTime.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        etEndTime.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框

        //隐藏审批人 抄送人  按钮
        ivApproval.setVisibility(View.INVISIBLE);
        iv_add_staff.setVisibility(View.INVISIBLE);

        if (SpData.getIdentityInfo().staffIdentity()) {
            tvBranch.setText("所选部门");
            groupTop.setVisibility(View.GONE);
        } else {
            tvBranch.setText("所在班级");
            //目前使用的身份名称- 暂无学生姓名
            groupTop.setVisibility(View.VISIBLE);
        }

        recyclerviewTagHint.setLayoutManager(flexboxLayoutManager);
        recyclerviewTagHint.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(5)));
        recyclerviewTagHint.setAdapter(leaveReasonTagAdapter);
        leaveReasonTagAdapter.setOnClickedListener(position -> {
            LeavePhraseRsp.DataBean tag = tags.get(position);
            if (!tag.isChecked()) {
                reason += tag.getTag();
            } else {
                reason = reason.replace(tag.getTag(), "");
            }
            Log.e(TAG, "onViewCreated: " + reason);
            editLeaveReason.setText(reason);

            //修改状态
            tag.setChecked(!tag.isChecked());
            leaveReasonTagAdapter.notifyDataSetChanged();
        });
        btn_commit.setAlpha(0.5f);
        btn_commit.setClickable(false);

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
        initCC();
        mvpPresenter.getClassList();
    }

    private void initCC() {
        ccList.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        ccList.setAdapter(mCCAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.etStartTime, R.id.etEndTime, R.id.btn_commit, R.id.tv_department, R.id.iv_add_staff, R.id.iv_add_approval})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.etStartTime:
                //showTime(getString(R.string.select_begin_time),startTime,startTimeListener);
                DatePickerDialogUtil.INSTANCE.showDateTime(getContext(), getString(R.string.select_begin_time), startTime, startTimeListener);
                break;
            case R.id.etEndTime:
                if (TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(startTime)) {
                    //showTime(getString(R.string.select_end_time), startTime,endTimeListener);
                    DatePickerDialogUtil.INSTANCE.showDateTime(getContext(), getString(R.string.select_end_time), startTime, endTimeListener);
                    break;
                }
                //showTime(getString(R.string.select_end_time), endTime,endTimeListener);
                DatePickerDialogUtil.INSTANCE.showDateTime(getContext(), getString(R.string.select_end_time), startTime, endTimeListener);
                break;
            //请假提交mai
            case R.id.btn_commit:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_commit)) {
                    commit();
                }
                break;
            case R.id.tv_department:

                break;
            case R.id.iv_add_staff:
                Intent intent1 = new Intent(getActivity(), AddCcActivity.class);
                if (ccDataList != null && ccDataList.size() > 0) {
                    intent1.putExtra("dataList", JSON.toJSONString(ccDataList));
                }
                startActivityForResult(intent1, REQUEST_CODE);
                break;
            case R.id.iv_add_approval:
                break;
            default:
                break;
        }
    }

    /**
     * 请假
     */
    private void commit() {
        if (TextUtils.isEmpty(startTime)) {
            ToastUtils.showShort(R.string.select_ask_for_leave_begin_time_tip);
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            ToastUtils.showShort(R.string.select_ask_for_leave_end_time_tip);
            return;
        }
        if (DateUtils.parseTimestamp(endTime, "") - DateUtils.parseTimestamp(startTime, "") <= 0) {
            ToastUtils.showShort(R.string.begin_time_not_gt_end_time_tip);
            return;
        }
        reason = editLeaveReason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            ToastUtils.showShort(R.string.ask_for_leave_reason_null_tip);
            return;
        }
        LeaveApprovalBean.LeaveRequestBean leaveRequestBean = new LeaveApprovalBean.LeaveRequestBean();
        leaveRequestBean.setStartTime(startTime);
        leaveRequestBean.setEndTime(endTime);
        leaveRequestBean.setReason(reason);
        leaveRequestBean.setHours(hours);

        if (classBean != null) {
            leaveRequestBean.setStudent(classBean.getStudentName());
            leaveRequestBean.setStudentId(classBean.getStudentId());
            dept = classBean.getId();
        }
        mvpPresenter.addLeave(leaveRequestBean, procId, sponsorType, dept, deptName, getAppList(), getCCList());
    }

    private List<LeaveApprovalBean.RequestParam> getAppList() {
        List<LeaveApprovalBean.LeaveCommitBean> data = mAdapter.getData();
        List<LeaveApprovalBean.RequestParam> datas = new ArrayList<>();
        if (data != null) {
            for (LeaveApprovalBean.LeaveCommitBean item : data) {
                datas.add(new LeaveApprovalBean.RequestParam(item.getId(), item.getName(), item.getUserId()));
            }
        }
        return datas;
    }

    //获取抄送人ids
    private String getCCList() {
        List<LeaveApprovalBean.Cc> data = mCCAdapter.getData();
        StringBuilder ids = new StringBuilder();
        if (data.size() > 0) {
            for (LeaveApprovalBean.Cc item : data) {
                ids.append(item.getCcId()).append(",");
            }
            return ids.toString();
        }
        return "";
    }

    @Override
    protected StaffAskLeavePresenter createPresenter() {
        return new StaffAskLeavePresenter(this);
    }

    private final OnDateSetListener startTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String timingTime = simpleDateFormat.format(new Date(millseconds));
        Log.d(TAG, "startTimeListener: " + timingTime);
        etStartTime.setText(timingTime);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        startTime = simpleDateFormat2.format(new Date(millseconds));
        requestApprover();
    };

    private final OnDateSetListener endTimeListener = (timePickerView, millseconds) -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String timingTime = simpleDateFormat.format(new Date(millseconds));
        Log.d(TAG, "endTimeListener: " + timingTime);
        etEndTime.setText(timingTime);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        endTime = simpleDateFormat2.format(new Date(millseconds));
        requestApprover();
    };

    private void requestApprover() {
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            String classId = "";
            if (classBean != null) {
                classId = classBean.getId();
            }
            mvpPresenter.getApprover(startTime, endTime, classId);
        }
    }

    @Override
    public void approver(LeaveApprovalBean approverRsp) {
        final LeaveApprovalBean.DataBean data = approverRsp.getData();
        if (approverRsp.getCode() != BaseConstant.REQUEST_SUCCESS2) {
            ToastUtils.showShort(approverRsp.getMessage());
            removeAllViews();
            return;
        }
        ccDataList = data.getCcList();
        if (ccDataList != null && ccDataList.size() > 0) {
            iv_add_staff.setVisibility(View.VISIBLE);
        }

        hours = data.getHours();
        sponsorType = data.getSponsorType();
        procId = data.getProcId();
        btn_commit.setAlpha(1f);
        btn_commit.setClickable(true);

        //数据处理逻辑
        final List<LeaveApprovalBean.Approver> peopleForm = data.getApprover();
        List<LeaveApprovalBean.LeaveCommitBean> dataList = new ArrayList<>();
        if (approvalData != null) {
            approvalData.clear();
        }
        if (peopleForm != null) {
            for (int i = 0; i < peopleForm.size(); i++) {
                LeaveApprovalBean.Approver approver = peopleForm.get(i);
                //需要默认显示审批人
                if (approver.getBranapprList() != null && approver.getBranapprList().size() > 0) {
                    LeaveApprovalBean.Branappr item = approver.getBranapprList().get(0);
                    dataList.add(new LeaveApprovalBean.LeaveCommitBean(approver.getId(), approver.getName(), item.getName(), item.getId(), item.getAvatar(), approver.getBranapprList()));
                }
            }
        }
        //添加审批人列表
        addApprovalList(dataList);
    }

    private void addApprovalList(List<LeaveApprovalBean.LeaveCommitBean> dataList) {
        if (approvalData != null && approvalData.size() > 0) {
            ivApproval.setVisibility(View.VISIBLE);
        }
        tv_approver.setVisibility(View.VISIBLE);
        approvalList.setLayoutManager(new LinearLayoutManager(getContext()));
        approvalList.setAdapter(mAdapter);
        mAdapter.setList(dataList);
    }

    private int appPosotion = -1;
    private final BaseQuickAdapter<LeaveApprovalBean.LeaveCommitBean, BaseViewHolder> mAdapter =
            new BaseQuickAdapter<LeaveApprovalBean.LeaveCommitBean, BaseViewHolder>(R.layout.item_approver_head) {
                @Override
                protected void convert(@NonNull BaseViewHolder holder, LeaveApprovalBean.LeaveCommitBean approver) {
                    holder.setText(R.id.tv_approver_name, approver.getApproverName());
                    if (mAdapter.getItemPosition(approver) == 0) {
                        holder.getView(R.id.viewTop).setVisibility(View.INVISIBLE);
                    } else if (mAdapter.getItemPosition(approver) == mAdapter.getData().size() - 1) {
                        holder.getView(R.id.viewLine).setVisibility(View.INVISIBLE);
                        //holder.getView(R.id.viewEnd).setVisibility(View.INVISIBLE);
                    }
                    if (mAdapter.getData().size() == 1) {
                        holder.getView(R.id.viewLine).setVisibility(View.INVISIBLE);
                    }
                    List<LeaveApprovalBean.Branappr> branapprList = approver.getBranapprList();
                    ImageView ivDel = holder.getView(R.id.ivDel);
                    ImageView iv_Add = holder.getView(R.id.iv_add_approval);
                    if (branapprList != null && branapprList.size() > 1) {
                        //ivDel.setVisibility(View.VISIBLE);
                        iv_Add.setVisibility(View.VISIBLE);
                        ivDel.setOnClickListener(v -> {
                            mAdapter.remove(approver);
                        });
                        holder.getView(R.id.iv_add_approval).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPosotion = getItemPosition(approver);
                                Intent intent = new Intent(getActivity(), AddApprovalActivity.class);
                                if (branapprList != null && branapprList.size() > 0) {
                                    intent.putExtra("dataList", JSON.toJSONString(branapprList));
                                }
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        });
                    } else {
                        iv_Add.setVisibility(View.GONE);
                        ivDel.setVisibility(View.GONE);
                    }

                    ImageView imageView = holder.getView(R.id.iv_user_head);
                    GlideUtil.loadImageHead(getContext(), approver.getAvatar(), imageView);
                }
            };

    private final BaseQuickAdapter<LeaveApprovalBean.Cc, BaseViewHolder> mCCAdapter = new BaseQuickAdapter<LeaveApprovalBean.Cc, BaseViewHolder>(R.layout.item_leave_cc) {
        @Override
        protected void convert(@NonNull BaseViewHolder holder, LeaveApprovalBean.Cc item) {
            holder.setText(R.id.tvName, item.getName());
            holder.getView(R.id.ivDel).setOnClickListener(v -> {
                mCCAdapter.remove(item);
            });
        }
    };

    @Override
    public void approverFail(String msg) {
        ToastUtils.showLong(msg);
        removeAllViews();
    }

    private void removeAllViews() {
        iv_add_staff.setVisibility(View.GONE);
        btn_commit.setAlpha(0.5f);
        btn_commit.setClickable(false);
        carbonCopyPeopleId.clear();
        carbonCopyPeopleList.clear();
    }

    @Override
    public void addLeave(BaseRsp baseRsp) {
        Log.e(TAG, "addTeacherLeave: " + baseRsp.toString());
        if (baseRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            final String id = baseRsp.getData();
            final Intent intent = new Intent(getActivity(), LeaveFlowDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            getActivity().finish();
        } else {
            ToastUtils.showShort("提交失败：" + baseRsp.getMsg());
        }
    }

    @Override
    public void addLeaveFail(String msg) {
        Log.e(TAG, "addTeacherLeaveFail: " + msg);
        ToastUtils.showShort("提交失败：" + msg);
    }

    @Override
    public void leavePhrase(LeavePhraseRsp leavePhraseRsp) {
        Log.e(TAG, "leavePhrase: " + leavePhraseRsp.toString());
        if (leavePhraseRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            final List<LeavePhraseRsp.DataBean> data = leavePhraseRsp.getData();
            tags.clear();
            tags.addAll(data);
            leaveReasonTagAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showLong(leavePhraseRsp.getMsg());
        }
    }

    @Override
    public void leavePhraseFail(String msg) {
        Log.e(TAG, "leavePhraseFail: " + msg);
        ToastUtils.showLong(msg);
    }

    @Override
    public void getClass(LeaveApprovalBean leaveApprovalBean) {
        LeaveApprovalBean.DataBean data = leaveApprovalBean.getData();
        if (leaveApprovalBean.getCode() != BaseConstant.REQUEST_SUCCESS2) {
            ToastUtils.showShort(leaveApprovalBean.getMessage());
            return;
        }

        LeaveApprovalBean.DeptBean deptBean = data.getDept();
        if (deptBean != null && !TextUtils.isEmpty(deptBean.getName())) {
            deptName = deptBean.getName();
            dept = deptBean.getId();
            tv_department.setText(deptName);
        }

        //家长身份
        List<LeaveApprovalBean.LeaveClassBean> classList = data.getClassList();
        if (classList != null && classList.size() > 0) {
            classBean = classList.get(0);
            setClassName(classBean);
            if (classList.size() > 1) {
                tvStudentName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_right), null);
                //选择班学生
                cl_student.setOnClickListener(v -> new SwitchStudentPop(getActivity(), classList, index).setOnSelectCallBack(new SwitchStudentPop.OnSelectCallBack() {
                    @Override
                    public void onSelectCallBacks(int position) {
                        index = position;
                        classBean = classList.get(position);
                        setClassName(classBean);
                        requestApprover();
                    }
                }));
            }
        }
    }

    private int index = 0;
    private LeaveApprovalBean.LeaveClassBean classBean;

    private void setClassName(LeaveApprovalBean.LeaveClassBean leaveClassBean) {
        deptName = leaveClassBean.getName();
        tvStudentName.setText(leaveClassBean.getStudentName());
        tv_department.setText(leaveClassBean.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            String ccList = data.getStringExtra("ccList");
            String approverList = data.getStringExtra("approverList");
            Log.e(TAG, "onActivityResult: " + ccList);
            if (!TextUtils.isEmpty(ccList)) {
                //抄送人
                final List<LeaveApprovalBean.Cc> dataBeans = JSON.parseArray(ccList, LeaveApprovalBean.Cc.class);
                mCCAdapter.setList(dataBeans);
            } else if (!TextUtils.isEmpty(approverList)) {
                //审批人
                LeaveApprovalBean.Branappr branapprBean = JSON.parseObject(approverList, LeaveApprovalBean.Branappr.class);
                if (branapprBean != null) {
                    LeaveApprovalBean.LeaveCommitBean leaveCommitBean = mAdapter.getData().get(appPosotion);
                    leaveCommitBean.setUserId(branapprBean.getId());
                    leaveCommitBean.setApproverName(branapprBean.getName());
                    leaveCommitBean.setAvatar(branapprBean.getAvatar());
                    mAdapter.notifyItemChanged(appPosotion, leaveCommitBean);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}