package com.yyide.chatim.activity.leave;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.leave.LeaveFlowAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveFlowBean;
import com.yyide.chatim.presenter.leave.LeaveDetailPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.leave.LeaveDetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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

    List<LeaveFlowBean> leaveFlowBeanList = new ArrayList<>();
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
        if (updateList){
            setResult(RESULT_OK,getIntent());
        }
        finish();
    }

    @OnClick({R.id.btn_refuse, R.id.btn_pass})
    public void click(View view) {
        switch (view.getId()){
            case R.id.btn_refuse:
                mvpPresenter.processExaminationApproval(id,1);
                break;
            case R.id.btn_pass:
                mvpPresenter.processExaminationApproval(id,0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (updateList){
            setResult(RESULT_OK,getIntent());
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_repeal)
    public void repeal() {
        updateList = true;
        mvpPresenter.ondoApplyLeave(id);
    }

    @Override
    public void showError() {

    }

    @Override
    public void leaveDetail(LeaveDetailRsp leaveDetailRsp) {
        Log.e(TAG, "leaveDetail: " + leaveDetailRsp.toString());
        showBlankPage(leaveDetailRsp.getData() == null);
        if (leaveDetailRsp.getCode() !=200){
            ToastUtils.showShort(leaveDetailRsp.getMsg());
            return;
        }
        final LeaveDetailRsp.DataBean data = leaveDetailRsp.getData();
        leaveFlowBeanList.clear();

        tv_leave_title.setText(data.getName());
        String initiateTime = data.getInitiateTime();
        tv_leave_time.setText(DateUtils.formatTime(initiateTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"));
        tv_department_name.setText(data.getDeptOrClassName());
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
        initiateTime = DateUtils.formatTime(initiateTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
        final String[] initiateTimes = initiateTime.split(" ");
        //"2021.05.19 13:54" to "05.19 13:54"
        String[] approvalTimes = {"", ""};
        if (approvalTime != null) {
            //"2021-05-19 14:17:36" to "05.19 13:54"
            approvalTime = DateUtils.formatTime(approvalTime, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
            approvalTimes = approvalTime.split(" ");
        }

        leaveFlowBeanList.add(new LeaveFlowBean("" + initiateTimes[1], "" + initiateTimes[0], "发起申请", data.getName(), true));
        leaveFlowBeanList.add(new LeaveFlowBean("" + approvalTimes[1], "" + approvalTimes[0], "审批人", "" + approverName, "1".equals(approvalResult)));
        final List<LeaveDetailRsp.DataBean.ListBean> list = data.getList();
        if (!list.isEmpty()){
            for (LeaveDetailRsp.DataBean.ListBean listBean : list) {
                leaveFlowBeanList.add(new LeaveFlowBean("", "", "抄送人", "" + listBean.getUserName(), "1".equals(approvalResult)));
            }
        }

        leaveFlowAdapter = new LeaveFlowAdapter(this, leaveFlowBeanList);
        recyclerViewFlow.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlow.setAdapter(leaveFlowAdapter);
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
        } else {
            ToastUtils.showShort("撤销失败");
        }
    }

    @Override
    public void repealFail(String msg) {
        ToastUtils.showShort("撤销失败");
    }

    @Override
    public void processApproval(BaseRsp baseRsp) {
        Log.e(TAG, "processApproval: "+baseRsp.toString());
        if (baseRsp.getCode() == 200){
            ToastUtils.showShort("审批成功");
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
            finish();
        } else {
            ToastUtils.showShort("审批失败");
            finish();
        }
    }

    @Override
    public void processApprovalFail(String msg) {
        Log.e(TAG, "processApprovalFail: "+msg );
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
}