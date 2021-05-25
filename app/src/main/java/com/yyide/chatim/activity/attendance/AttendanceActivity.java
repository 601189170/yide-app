package com.yyide.chatim.activity.attendance;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAttendanceBinding mViewBinding;
    private BaseQuickAdapter<String, BaseViewHolder> baseQuickAdapter;
    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        mViewBinding.top.title.setText(R.string.attendance_title);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_attendance) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, String s) {
                holder.setText(R.id.tv_item, s);
            }
        };
        mViewBinding.recyclerview.setAdapter(baseQuickAdapter);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++){
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
    }

    @Override
    public void onClick(View v) {
        mViewBinding.tvAll.setChecked(false);
        mViewBinding.tvAbsenteeism.setChecked(false);
        mViewBinding.tvLeave.setChecked(false);
        mViewBinding.tvLate.setChecked(false);
        mViewBinding.tvNormal.setChecked(false);
        switch (v.getId()){
            case R.id.tv_all:
                mViewBinding.tvAll.setChecked(true);
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                break;
        }
    }
}