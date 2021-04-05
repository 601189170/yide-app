package com.yyide.chatim.activity.notice;

import com.jzxiang.pickerview.TimePickerDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeCreatePresenter;
import com.yyide.chatim.activity.notice.view.NoticeCreateView;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeCreateActivity extends BaseMvpActivity<NoticeCreatePresenter> implements NoticeCreateView, OnDateSetListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.cl_timing_time)
    ConstraintLayout cl_timing_time;
    @BindView(R.id.switch1)
    Switch mSwitch;
    @BindView(R.id.tv_parents)
    TextView tv_parents;
    @BindView(R.id.tv_faculty)
    TextView tv_faculty;
    @BindView(R.id.tv_student)
    TextView tv_student;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_create_title);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cl_timing_time.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @OnClick({R.id.tv_parents, R.id.tv_faculty, R.id.cl_timing_time})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_parents:
                tv_parents.setTextColor(Color.parseColor("#FFFFFF"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_select_bg));
                tv_faculty.setTextColor(Color.parseColor("#666666"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                break;
            case R.id.tv_faculty:
                tv_parents.setTextColor(Color.parseColor("#666666"));
                tv_parents.setBackground(getDrawable(R.drawable.btn_unselect_bg));
                tv_faculty.setTextColor(Color.parseColor("#FFFFFF"));
                tv_faculty.setBackground(getDrawable(R.drawable.btn_select_bg));
                break;
            case R.id.cl_timing_time://选择发布时间
                showTime();
                break;
        }
    }

    private TimePickerDialog mDialogAll;

    private void showTime() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择日期时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.text_212121))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_666666))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.constraintLayout_select)
    public void select() {
        jupm(this, NoticeScopeActivity.class);
    }

    @Override
    protected NoticeCreatePresenter createPresenter() {
        return new NoticeCreatePresenter(this);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        toastShow(millseconds + "");
    }
}