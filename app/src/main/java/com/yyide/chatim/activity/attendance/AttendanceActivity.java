package com.yyide.chatim.activity.attendance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends BaseMvpActivity<AttendanceCheckPresenter> implements AttendanceCheckView, View.OnClickListener {

    private ActivityAttendanceBinding mViewBinding;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private AttendanceCheckRsp.DataBean.AttendancesFormBean.StudentsBean itemStudents;
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
        mViewBinding.top.tvRight.setOnClickListener(v -> jupm(AttendanceActivity.this, StatisticsActivity.class));
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerview.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_top);
        mViewBinding.tvAll.setOnClickListener(this);
        mViewBinding.tvAbsenteeism.setOnClickListener(this);
        mViewBinding.tvLeave.setOnClickListener(this);
        mViewBinding.tvLate.setOnClickListener(this);
        mViewBinding.tvNormal.setOnClickListener(this);
        mViewBinding.top.backLayout.setOnClickListener(v -> { finish(); });
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
                adapter.setList(itemStudents != null ? itemStudents.getPeople() : null);
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getAbsencePeople() : null);
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getLeavePeople() : null);
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getLatePeople() : null);
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getPeople() : null);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataView(AttendanceCheckRsp.DataBean item){
        if(item.getAttendancesForm() != null && item.getAttendancesForm().size() > 0){
            mViewBinding.tvSwitch.setOnClickListener(v -> {

            });

            mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                AttendancePop attendancePop = new AttendancePop(this, item.getAttendancesForm());
                attendancePop.setOnSelectListener(index -> {
                    itemStudents = item.getAttendancesForm().get(index).getStudents();
                    setData();
                });
            });
            itemStudents = item.getAttendancesForm().get(0).getStudents();
            setData();
        }
    }

    private void setData() {
        if(SpData.getIdentityInfo() != null){
            switch (SpData.getIdentityInfo().status){
                case GetUserSchoolRsp.DataBean.TYPE_PRESIDENT://校长
                    break;
                case GetUserSchoolRsp.DataBean.TYPE_CLASS_TEACHER://班主任
                case GetUserSchoolRsp.DataBean.TYPE_TEACHER://教师-老师
                case GetUserSchoolRsp.DataBean.TYPE_PARENTS://家长-监护人
                    break;
            }

            if(SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1){
                mViewBinding.tvSwitch.setVisibility(View.VISIBLE);
            } else {
                mViewBinding.tvSwitch.setVisibility(View.GONE);
            }

            if(SpData.getClassInfo() != null){
                mViewBinding.tvClassName.setText(SpData.getClassInfo().classesName);
            }
        }

        if(itemStudents != null){
            mViewBinding.tvAttendanceTitle.setText(itemStudents.getName());
            mViewBinding.tvDesc.setText(itemStudents.getName());
            mViewBinding.tvAttendanceTime.setText("考勤时间 " + DateUtils.switchTime(new Date(), "HH:mm"));
            mViewBinding.tvAttendanceRate.setText(itemStudents.getRate());
            if(!TextUtils.isEmpty(itemStudents.getRate())){
                try {
                    mViewBinding.progress.setProgress(new Double(itemStudents.getRate()).intValue());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            mViewBinding.tvBeTo.setText(itemStudents.getNumber() + "");
            mViewBinding.tvLateNum.setText(itemStudents.getLate() + "");
            mViewBinding.tvLeaveNum.setText(itemStudents.getLeave() + "");
            mViewBinding.tvNormalNum.setText(itemStudents.getApplyNum() + "");
            mViewBinding.tvAbsenteeismNum.setText(itemStudents.getAbsence() + "");

            adapter.setList(itemStudents.getPeople());
        }
    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean, BaseViewHolder>(R.layout.item_attendance_student) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean item) {
            holder.setText(R.id.tv_student_name, item.getName())
                    .setText(R.id.tv_student_time, item.getTime())
                    .setText(R.id.tv_status, item.getStatusValue());
        }
    };

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        if(BaseConstant.REQUEST_SUCCES2 == model.getCode()){
            AttendanceCheckRsp.DataBean data = model.getData();
            if(data != null){
                setDataView(data);
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