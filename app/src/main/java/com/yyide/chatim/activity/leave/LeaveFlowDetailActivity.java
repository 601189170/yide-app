package com.yyide.chatim.activity.leave;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.leave.LeaveFlowAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveFlowBean;
import com.yyide.chatim.presenter.leave.LeaveDetailPresenter;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.leave.LeaveDetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaveFlowDetailActivity extends BaseMvpActivity<LeaveDetailPresenter> implements LeaveDetailView {
    private static final String TAG = "LeaveFlowDetailActivity";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recyclerview_flow)
    RecyclerView recyclerViewFlow;

    @BindView(R.id.tv_leave_title)
    TextView tv_leave_title;

    @BindView(R.id.tv_leave_time)
    TextView tv_leave_time;

    @BindView(R.id.tv_department_name)
    TextView tv_department_name;

    @BindView(R.id.tv_department)
    TextView tv_department;

    @BindView(R.id.tv_start_time)
    TextView tv_start_time;

    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    @BindView(R.id.tv_reason_for_leave_content)
    TextView tv_reason_for_leave_content;

    @BindView(R.id.btn_repeal)
    Button btn_repeal;

    @BindView(R.id.gp_approver)
    Group gp_approver;

    @BindView(R.id.tv_leave_flow_status)
    TextView tv_leave_flow_status;

    @BindView(R.id.cl_content)
    ConstraintLayout cl_content;

    @BindView(R.id.blank_page)
    LinearLayout blank_page;
    @BindView(R.id.cl_repeal)
    ConstraintLayout cl_repeal;

    @BindView(R.id.gp_copyer_list)
    Group gp_copyer_list;

    @BindView(R.id.ll_copyer_list)
    LinearLayout ll_copyer_list;

    @BindView(R.id.tv_flow_content)
    TextView tv_flow_content;

//    @BindView(R.id.tv_date)
//    TextView tv_date;
//
//    @BindView(R.id.tv_time)
//    TextView tv_time;

    @BindView(R.id.iv_flow_checked)
    ImageView iv_flow_checked;

    @BindView(R.id.iv_unfold)
    ImageView iv_unfold;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    private boolean unfold = false;
    List<LeaveFlowBean> leaveFlowBeanList = new ArrayList<>();
    List<ApproverRsp.DataBean.ListBean> leaveFlowCopyerList = new ArrayList<>();
    private LeaveFlowAdapter leaveFlowAdapter;
    private long id;
    private boolean updateList = false;

    @Override
    public int getContentViewID() {
        return R.layout.activity_leave_flow_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.detail);
        //type 1请假人 2审批人
        final int type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            btn_repeal.setVisibility(View.VISIBLE);
            gp_approver.setVisibility(View.GONE);
        } else {
            btn_repeal.setVisibility(View.GONE);
            gp_approver.setVisibility(View.VISIBLE);
        }
        id = getIntent().getLongExtra("id", -1);
        Log.e(TAG, "id: " + id);
        if (id == -1) {
            showBlankPage(true);
        } else {
            mvpPresenter.queryLeaveDetailsById(id);
        }
    }


    @Override
    protected LeaveDetailPresenter createPresenter() {
        return new LeaveDetailPresenter(this);
    }


    @OnClick(R.id.back_layout)
    public void click() {
        if (updateList) {
            setResult(RESULT_OK, getIntent());
        }
        finish();
    }

    @OnClick({R.id.btn_refuse, R.id.btn_pass})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_refuse:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_refuse)) {
                    mvpPresenter.processExaminationApproval(id, 1);
                }
                break;
            case R.id.btn_pass:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_refuse)) {
                    mvpPresenter.processExaminationApproval(id, 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (updateList) {
            setResult(RESULT_OK, getIntent());
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_repeal)
    public void repeal() {
        if (!ButtonUtils.isFastDoubleClick(R.id.btn_repeal)) {
            updateList = true;
            mvpPresenter.ondoApplyLeave(id);
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void leaveDetail(LeaveDetailRsp leaveDetailRsp) {
        Log.e(TAG, "leaveDetail: " + leaveDetailRsp.toString());
        showBlankPage(leaveDetailRsp.getData() == null);
        if (leaveDetailRsp.getCode() != 200) {
            ToastUtils.showShort(leaveDetailRsp.getMsg());
            return;
        }
        final LeaveDetailRsp.DataBean data = leaveDetailRsp.getData();
        leaveFlowBeanList.clear();
        if ("2".equals(data.getLeaveType())) {
            //教职工
            tv_department.setText(R.string.in_department);
            tv_department_name.setText(data.getDeptOrClassName());
        } else {
            //监护人
            tv_department.setText(getString(R.string.choose_student));
            tv_department_name.setText(data.getStudentName());
        }

        tv_leave_title.setText(data.getName());
        String initiateTime = data.getInitiateTime();
        tv_leave_time.setText(DateUtils.formatTime(initiateTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"));
        final String starttime = DateUtils.formatTime(data.getStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
        tv_start_time.setText(starttime);
        final String endtime = DateUtils.formatTime(data.getEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
        tv_end_time.setText(endtime);
        tv_reason_for_leave_content.setText(data.getReason());

        //审核结果: 0 审批拒绝 1 审批通过 2 审批中 3 已撤销
        final String approvalResult = data.getApprovalResult();
        leaveStatus(approvalResult, tv_leave_flow_status);
        final String approverName = data.getApproverName();
        String approvalTime = data.getApprovalTime();
        String[] initiateTimes = {"", ""};
        if (initiateTime != null) {
            initiateTime = DateUtils.formatTime(initiateTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
            initiateTimes = initiateTime.split(" ");
        }
        //"2021.05.19 13:54" to "05.19 13:54"
        String[] approvalTimes = {"", ""};
        if (approvalTime != null) {
            //"2021-05-19 14:17:36" to "05.19 13:54"
            approvalTime = DateUtils.formatTime(approvalTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
            approvalTimes = approvalTime.split(" ");
        }

        String undoTime = data.getUndoTime();
        String[] undoTimes = {"", ""};
        if (undoTime != null) {
            //"2021-05-19 14:17:36" to "05.19 13:54"
            undoTime = DateUtils.formatTime(undoTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
            undoTimes = undoTime.split(" ");
        }
//        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
//            leaveFlowBeanList.add(new LeaveFlowBean("" + initiateTimes[1], "" + initiateTimes[0], "发起申请", data.getInitName(), true, false, null));
//        } else {
            leaveFlowBeanList.add(new LeaveFlowBean("" + initiateTimes[1], "" + initiateTimes[0], "发起申请", data.getInitName(), true, false, data.getInitImage()));
//        }
        switch (approvalResult) {
            case "0":
                leaveFlowBeanList.add(new LeaveFlowBean("" + approvalTimes[1], "" + approvalTimes[0], "审批人", "" + approverName, true, true, data.getApproverImage()));
                break;
            case "1":
            case "2":
                final List<LeaveDetailRsp.DataBean.ListBean> list = data.getList();
                leaveFlowBeanList.add(new LeaveFlowBean("" + approvalTimes[1], "" + approvalTimes[0], "审批人", "" + approverName, "1".equals(approvalResult), list.isEmpty(), data.getApproverImage()));
                if (!list.isEmpty()) {
                    //leaveFlowCopyerList.addAll(list);
                    for (LeaveDetailRsp.DataBean.ListBean listBean : list) {
                        //leaveFlowBeanList.add(new LeaveFlowBean("", "", "抄送人", "" + listBean.getName(), "1".equals(approvalResult)));
                        final ApproverRsp.DataBean.ListBean bean = new ApproverRsp.DataBean.ListBean();
                        bean.setImage(listBean.getImage());
                        bean.setName(listBean.getName());
                        bean.setUserId(listBean.getUserId());
                        leaveFlowCopyerList.add(bean);
                    }
                }
                break;
            case "3":
                gp_copyer_list.setVisibility(View.GONE);
                leaveFlowBeanList.add(new LeaveFlowBean("" + undoTimes[1], "" + undoTimes[0], "我", "" + data.getName(), true, true, null));
                break;
            default:
                break;
        }
        leaveFlowAdapter = new LeaveFlowAdapter(this, leaveFlowBeanList);
        recyclerViewFlow.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlow.setAdapter(leaveFlowAdapter);
        //显示抄送人列表
        if (!leaveFlowCopyerList.isEmpty() && !"3".equals(approvalResult)) {
            gp_copyer_list.setVisibility(View.VISIBLE);
            tv_flow_content.setText(String.format(getString(R.string.carbon_copy_people_text), "" + leaveFlowCopyerList.size()));
            //tv_date.setText(approvalTimes[0]);
            //tv_time.setText(approvalTimes[1]);
            iv_unfold.setOnClickListener(v -> {
                        if (unfold) {
                            ll_copyer_list.setVisibility(View.GONE);
                            iv_unfold.setImageResource(R.drawable.icon_arrow_down);
                            unfold = false;
                            //滚动到底部
                            nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                        } else {
                            ll_copyer_list.setVisibility(View.VISIBLE);
                            iv_unfold.setImageResource(R.drawable.icon_arrow_up);
                            unfold = true;
                            //滚动到底部
                            nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                        }
                    }
            );
            if (Objects.equals(approvalResult, "2")) {
                iv_flow_checked.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i < leaveFlowCopyerList.size(); i++) {
                if (i < 3) {
                    final ApproverRsp.DataBean.ListBean listBean = leaveFlowCopyerList.get(i);
                    final View view1 = LayoutInflater.from(this).inflate(R.layout.item_approver_head, null);
                    final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                    final ImageView iv_user_head = view1.findViewById(R.id.iv_user_head);
                    tv_copyer_name.setText(listBean.getName());
                    showImage(listBean.getImage(), iv_user_head);
                    ll_copyer_list.addView(view1);
                    setViewLayoutParams(view1, StatusBarUtils.dip2px(this, 45), 0);
                }
            }
            if (leaveFlowCopyerList.size() > 3) {
                final View view1 = LayoutInflater.from(this).inflate(R.layout.item_approver_head, null);
                final TextView tv_copyer_name = view1.findViewById(R.id.tv_approver_name);
                final ImageView userHeadImage = view1.findViewById(R.id.iv_user_head);
                tv_copyer_name.setText("查看全部");
                userHeadImage.setImageResource(R.drawable.icon_read_more);
                view1.setOnClickListener(v -> {
                    final Intent intent = new Intent(LeaveFlowDetailActivity.this, LeaveCarbonCopyPeopleActivity.class);
                    intent.putExtra("carbonCopyPeople", JSON.toJSONString(leaveFlowCopyerList));
                    intent.putExtra("type", 2);
                    startActivity(intent);
                });
                ll_copyer_list.addView(view1);
            }

        }

    }

    private void showImage(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
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
    public void leaveDetailFail(String msg) {
        showBlankPage(true);
    }

    @Override
    public void repealResult(BaseRsp baseRsp) {
        Log.e(TAG, "repealResult: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            mvpPresenter.queryLeaveDetailsById(id);
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
        } else {
            ToastUtils.showShort("撤销失败：" + baseRsp.getMsg());
        }
    }

    @Override
    public void repealFail(String msg) {
        ToastUtils.showShort("撤销失败");
    }

    @Override
    public void processApproval(BaseRsp baseRsp) {
        Log.e(TAG, "processApproval: " + baseRsp.toString());
        if (baseRsp.getCode() == 200) {
            ToastUtils.showShort("审批成功");
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
        } else {
            ToastUtils.showShort("审批失败：" + baseRsp.getMsg());
        }
        finish();
    }

    @Override
    public void processApprovalFail(String msg) {
        Log.e(TAG, "processApprovalFail: " + msg);
        ToastUtils.showShort("审批失败");
        finish();
    }

    private void leaveStatus(String status, TextView view) {
        //审核结果: 0 审批拒绝 1 审批通过 2 审批中 3 已撤销
        switch (status) {
            case "0":
                view.setText(getString(R.string.refuse_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_refuse_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                cl_repeal.setVisibility(View.GONE);
                break;
            case "1":
                view.setText(getString(R.string.pass_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_pass_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                cl_repeal.setVisibility(View.GONE);
                break;
            case "2":
                view.setText(getString(R.string.approval_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_approval_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                cl_repeal.setVisibility(View.VISIBLE);
                break;
            case "3":
                view.setText(getString(R.string.repeal_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_repeal_shape);
                view.setTextColor(getResources().getColor(R.color.black11));
                cl_repeal.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void showBlankPage(boolean show) {
        if (show) {
            blank_page.setVisibility(View.VISIBLE);
            cl_content.setVisibility(View.GONE);
        } else {
            blank_page.setVisibility(View.GONE);
            cl_content.setVisibility(View.VISIBLE);
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