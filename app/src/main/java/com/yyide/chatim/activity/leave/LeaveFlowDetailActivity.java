package com.yyide.chatim.activity.leave;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.leave.LeaveFlowAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.fragment.leave.AskForLeaveListFragment;
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment;
import com.yyide.chatim.fragment.leave.RequestLeaveStudentFragment;
import com.yyide.chatim.model.LeaveFlowBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaveFlowDetailActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recyclerview_flow)
    RecyclerView recyclerViewFlow;

    List<LeaveFlowBean> leaveFlowBeanList = new ArrayList<>();
    private LeaveFlowAdapter leaveFlowAdapter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_leave_flow_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.detail);
        initData();
        leaveFlowAdapter = new LeaveFlowAdapter(this, leaveFlowBeanList);
        recyclerViewFlow.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlow.setAdapter(leaveFlowAdapter);
    }

    private void initData(){
        leaveFlowBeanList.add(new LeaveFlowBean("09:25","03.06","发起申请","某某某",true));
        leaveFlowBeanList.add(new LeaveFlowBean("09:27","03.06","审批人","某某某",true));
        leaveFlowBeanList.add(new LeaveFlowBean("10:29","03.06","审批人","某某某",false));
        leaveFlowBeanList.add(new LeaveFlowBean("12:50","03.06","审批人","某某某",false));
    }
    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

}