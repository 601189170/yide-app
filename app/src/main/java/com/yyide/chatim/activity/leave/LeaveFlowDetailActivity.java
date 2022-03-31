package com.yyide.chatim.activity.leave;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.leave.LeaveFlowAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.DialogTodoLeaveRefuseBinding;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveFlowBean;
import com.yyide.chatim.presenter.leave.LeaveDetailPresenter;
import com.yyide.chatim.utils.ButtonUtils;
import com.yyide.chatim.utils.YDToastUtil;
import com.yyide.chatim.view.leave.LeaveDetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
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

    @BindView(R.id.iv_flow_checked)
    ImageView iv_flow_checked;

    @BindView(R.id.iv_unfold)
    ImageView iv_unfold;
    @BindView(R.id.btn_more)
    Button btn_more;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.groupStudent)
    Group groupStudent;
    @BindView(R.id.tvStudentName)
    TextView tvStudentName;

    private boolean unfold = false;
    List<LeaveFlowBean> leaveFlowBeanList = new ArrayList<>();
    private LeaveFlowAdapter leaveFlowAdapter;
    private String id;
    private String taskId;
    private String processId;
    private boolean updateList = false;
    private int status;

    @Override
    public int getContentViewID() {
        return R.layout.activity_leave_flow_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.ask_for_leave);
        //type 1请假人 2审批人
        int type = getIntent().getIntExtra("type", 1);
        status = getIntent().getIntExtra("status", -1);
        if (type == 1) {
//            btn_repeal.setVisibility(View.VISIBLE);
            gp_approver.setVisibility(View.GONE);
        } else {
//            btn_repeal.setVisibility(View.GONE);
            gp_approver.setVisibility(View.VISIBLE);
        }
        cl_content.setVisibility(View.INVISIBLE);
        id = getIntent().getStringExtra("id");
        Log.e(TAG, "id: " + id);
        if (TextUtils.isEmpty(id)) {
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

    @OnClick({R.id.btn_refuse, R.id.btn_pass, R.id.btn_more, R.id.btn_repeal})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_refuse:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_refuse)) {
                    showReason(1);
                }
                break;
            case R.id.btn_pass:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_pass)) {
                    mvpPresenter.processExaminationApproval(taskId, 2, "");
                }
                break;
            case R.id.btn_more:
//                showGlobalActionPopup(btn_more);
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_more)) {
                    mvpPresenter.backLeave(taskId);
                }
                break;
            case R.id.btn_repeal:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_repeal)) {
                    updateList = true;
                    showReason(2);
                }
                break;
            default:
                break;
        }
    }

    private void showReason(int type) {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        DialogTodoLeaveRefuseBinding vb = DialogTodoLeaveRefuseBinding.inflate(getLayoutInflater());
        dialog.setContentView(vb.getRoot());
        vb.tvCancel.setOnClickListener(v -> dialog.dismiss());
        vb.tvConfirm.setOnClickListener(v ->
                {
                    String reason = vb.etInput.getText().toString().trim();
                    if (TextUtils.isEmpty(reason)) {
                        YDToastUtil.showMessage("请输入拒绝理由");
                    } else {
                        if (type == 1) {
                            mvpPresenter.processExaminationApproval(taskId, 0, reason);
                        } else {
                            mvpPresenter.ondoApplyLeave(processId, reason);
                        }
                    }
                }
        );
        dialog.show();
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
    }

    private void showGlobalActionPopup(View v) {
        String[] listItems = new String[]{
                "Change Skin"
        };
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    final String[] items = new String[]{"转交", "回退"};
                    new QMUIDialog.MenuDialogBuilder(LeaveFlowDetailActivity.this)
                            .addItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //QDSkinManager.changeSkin(which + 1);
                                    dialog.dismiss();
                                }
                            })
                            .setSkinManager(QMUISkinManager.defaultInstance(LeaveFlowDetailActivity.this))
                            .create()
                            .show();
                }
                if (mGlobalAction != null) {
                    mGlobalAction.dismiss();
                }
            }
        };
        mGlobalAction = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 100),
                QMUIDisplayHelper.dp2px(this, 110),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 10))
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(v);
    }

    private QMUIPopup mGlobalAction;
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                final String[] items = new String[]{"蓝色（默认）", "黑色", "白色"};
                new QMUIDialog.MenuDialogBuilder(LeaveFlowDetailActivity.this)
                        .addItems(items, (dialog, which) -> {
                            //QDSkinManager.changeSkin(which + 1);
                            dialog.dismiss();
                        })
                        .setSkinManager(QMUISkinManager.defaultInstance(LeaveFlowDetailActivity.this))
                        .create()
                        .show();
            }
            if (mGlobalAction != null) {
                mGlobalAction.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (updateList) {
            setResult(RESULT_OK, getIntent());
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void leaveDetail(LeaveDetailRsp leaveDetailRsp) {
        Log.e(TAG, "leaveDetail: " + leaveDetailRsp.toString());
        showBlankPage(leaveDetailRsp.getData() == null);
        if (leaveDetailRsp.getCode() != BaseConstant.REQUEST_SUCCESS2) {
            ToastUtils.showShort(leaveDetailRsp.getMessage());
            return;
        }
        cl_content.setVisibility(View.VISIBLE);
        final LeaveDetailRsp.DataDTO data = leaveDetailRsp.getData();
        leaveFlowBeanList.clear();
        LeaveDetailRsp.DataDTO.ApprJsonDTO apprJson = data.getApprJson();
        if ("1".equals(data.getIdentity())) {
            //教职工
            tv_department.setText(R.string.in_department);
            tv_department_name.setText(data.getDept());
            groupStudent.setVisibility(View.GONE);
        } else {
            //监护人
            tv_department.setText(getString(R.string.leave_class));
            tv_department_name.setText(data.getDept());
            groupStudent.setVisibility(View.VISIBLE);
            tvStudentName.setText(apprJson.getStudent());
        }

        tv_leave_time.setText(data.getCreateTime());
        taskId = data.getTaskId();
        processId = data.getProcInstId();
        if (data.isBack()) {
            btn_more.setVisibility(View.GONE);
        }
        tv_leave_title.setText(data.getTitle());
        tv_start_time.setText(apprJson.getStartTime());
        tv_end_time.setText(apprJson.getEndTime());
        tv_reason_for_leave_content.setText(apprJson.getReason());
        //审核结果: 0 审批拒绝 1 审批通过 2 审批中 3 已撤销
        leaveStatus(data.getStatus(), tv_leave_flow_status);

        if (data.getHiApprNodeList() != null) {
            for (int i = 0; i < data.getHiApprNodeList().size(); i++) {
                LeaveDetailRsp.DataDTO.HiApprNodeListDTO hiApprNodeListDTO = data.getHiApprNodeList().get(i);
                if (i == 1 && "1".equals(hiApprNodeListDTO.getStatus())) {
                    btn_more.setVisibility(View.GONE);
                }
            }
        }

        if (data.getHiApprNodeList() != null && data.getCcList() != null && data.getCcList().size() > 0) {
            LeaveDetailRsp.DataDTO.HiApprNodeListDTO listDTO = new LeaveDetailRsp.DataDTO.HiApprNodeListDTO();
            listDTO.setCc(true);
            listDTO.setCcList(data.getCcList());
            data.getHiApprNodeList().add(listDTO);
        }

        //审批流程
        leaveFlowAdapter = new LeaveFlowAdapter();
        recyclerViewFlow.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlow.setAdapter(leaveFlowAdapter);
        leaveFlowAdapter.setList(data.getHiApprNodeList());
    }

    @Override
    public void leaveDetailFail(String msg) {
        showBlankPage(true);
    }

    @Override
    public void repealResult(BaseRsp baseRsp) {
        Log.e(TAG, "repealResult: " + baseRsp.toString());
        if (baseRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            mvpPresenter.queryLeaveDetailsById(id);
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
        } else {
            ToastUtils.showShort("撤销失败：" + baseRsp.getMessage());
        }
    }

    @Override
    public void repealFail(String msg) {
        ToastUtils.showShort("撤销失败");
    }

    @Override
    public void processApproval(BaseRsp baseRsp) {
        Log.e(TAG, "processApproval: " + baseRsp.toString());
        if (baseRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            ToastUtils.showShort("审批成功");
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
        } else {
            ToastUtils.showShort("审批失败：" + baseRsp.getMessage());
        }
        finish();
    }

    @Override
    public void processApprovalFail(String msg) {
        Log.e(TAG, "processApprovalFail: " + msg);
        ToastUtils.showShort("审批失败");
        finish();
    }

    @Override
    public void leaveBack(BaseRsp baseRsp) {
        if (baseRsp.getCode() == BaseConstant.REQUEST_SUCCESS2) {
            ToastUtils.showShort("回退成功");
            mvpPresenter.queryLeaveDetailsById(id);
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_LEAVE, ""));
        } else {
            ToastUtils.showShort("回退失败：" + baseRsp.getMessage());
        }
    }

    private void leaveStatus(String leaveStatus, TextView view) {
        //审核结果: 0 审批拒绝 1 审批中 2 审批通过  3 已撤销
        switch (leaveStatus) {
            case "0":
                view.setText(getString(R.string.refuse_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_refuse_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                cl_repeal.setVisibility(View.GONE);
                break;
            case "1":
                view.setText(getString(R.string.approval_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_approval_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                if (status == 1) {
                    cl_repeal.setVisibility(View.VISIBLE);
                } else {
                    cl_repeal.setVisibility(View.GONE);
                }
                break;
            case "2":
                view.setText(getString(R.string.pass_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_pass_shape);
                view.setTextColor(getResources().getColor(R.color.black9));
                cl_repeal.setVisibility(View.GONE);
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
        if (blank_page != null && cl_content != null) {
            if (show) {
                blank_page.setVisibility(View.VISIBLE);
                cl_content.setVisibility(View.GONE);
            } else {
                blank_page.setVisibility(View.GONE);
                cl_content.setVisibility(View.VISIBLE);
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