package com.yyide.chatim.activity.leave;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.leave.LeaveCarbonCopyPeopleAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaveCarbonCopyPeopleActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.tv_carbon_copy_people)
    TextView tv_carbon_copy_people;

    List<ApproverRsp.DataBean.ListBean> data;
    @Override
    public int getContentViewID() {
        return R.layout.activity_leave_carbon_copy_people;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.carbon_copy_people);
        //initData();
        final String carbonCopyPeople = getIntent().getStringExtra("carbonCopyPeople");
        Log.e("TAG", "onCreate: "+carbonCopyPeople );
        data = JSON.parseArray(carbonCopyPeople, ApproverRsp.DataBean.ListBean.class);
        final String format = String.format(getString(R.string.carbon_copy_people_text), ""+data.size());
        tv_carbon_copy_people.setText(format);
        final LeaveCarbonCopyPeopleAdapter leaveCarbonCopyPeopleAdapter = new LeaveCarbonCopyPeopleAdapter(this, data);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(5)));
        recyclerview.setAdapter(leaveCarbonCopyPeopleAdapter);
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }
}