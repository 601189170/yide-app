package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends BaseMvpActivity<AttendanceCheckPresenter> implements AttendanceCheckView, View.OnClickListener {

    private ActivityAttendanceBinding mViewBinding;
    private BaseQuickAdapter<String, BaseViewHolder> baseQuickAdapter;
    private String TAG = AttendanceActivity.class.getSimpleName();
    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        initView();
        if (SpData.getClassInfo() != null) {
            mvpPresenter.attendance(SpData.getClassInfo().classesId);
        }
    }

    private void initView() {
        mViewBinding.top.title.setText(R.string.attendance_title);
        mViewBinding.top.tvRight.setVisibility(View.VISIBLE);
        mViewBinding.top.tvRight.setText(R.string.statistics_title);
        mViewBinding.top.tvRight.setTextColor(getResources().getColor(R.color.colorPrimary));
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_attendance) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, String s) {
                holder.setText(R.id.tv_item, s);
            }
        };
        mViewBinding.top.tvRight.setOnClickListener(v -> jupm(AttendanceActivity.this, StatisticsActivity.class));

        mViewBinding.recyclerview.setAdapter(baseQuickAdapter);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item" + i);
        }
        baseQuickAdapter.setList(list);

        mViewBinding.tvAll.setOnClickListener(this);
        mViewBinding.tvAbsenteeism.setOnClickListener(this);
        mViewBinding.tvLeave.setOnClickListener(this);
        mViewBinding.tvLate.setOnClickListener(this);
        mViewBinding.tvNormal.setOnClickListener(this);
        mViewBinding.tvSwitch.setOnClickListener(v -> {
            List<String> lists = new ArrayList<>();
            lists.add("高一一班");
            lists.add("高一二班");
            lists.add("高一三班");
            new AttendancePop(this, lists);
        });
        mViewBinding.top.backLayout.setOnClickListener(v -> {
            finish();
        });
        mViewBinding.tvAll.setChecked(true);
        mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View v) {
        mViewBinding.tvAll.setChecked(false);
        mViewBinding.tvAbsenteeism.setChecked(false);
        mViewBinding.tvLeave.setChecked(false);
        mViewBinding.tvLate.setChecked(false);
        mViewBinding.tvNormal.setChecked(false);
        mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        switch (v.getId()) {
            case R.id.tv_all:
                mViewBinding.tvAll.setChecked(true);
                mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void setDataView(){

    }

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        if(BaseConstant.REQUEST_SUCCES2 == model.getCode()){
            List<AttendanceCheckRsp.DataBean> data = model.getData();
            if(data != null){
                setDataView();
            }
        }
    }

    @Override
    public void getAttendanceFail(String msg) {
        Log.d(TAG, "getHomeAttendanceFail-->>" + msg);
    }

    @Override
    public void showError() {

    }
}