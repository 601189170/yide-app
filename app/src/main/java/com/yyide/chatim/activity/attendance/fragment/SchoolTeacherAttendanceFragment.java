package com.yyide.chatim.activity.attendance.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolTeacherAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;

import org.jetbrains.annotations.NotNull;

/**
 * desc 教师教职工考情详情
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolTeacherAttendanceFragment extends BaseFragment implements View.OnClickListener {
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean itemTeachersBean;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentSchoolTeacherAttendanceBinding mViewBinding;

    public static SchoolTeacherAttendanceFragment newInstance(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean teachersBean) {
        SchoolTeacherAttendanceFragment fragment = new SchoolTeacherAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("teachersBean", teachersBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemTeachersBean = (AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean) getArguments().getSerializable("teachersBean");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentSchoolTeacherAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mViewBinding.constraintLayout.setVisibility(View.GONE);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.recyclerview.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_top);
        mViewBinding.tvAll.setOnClickListener(this);
        mViewBinding.tvAbsenteeism.setOnClickListener(this);
        mViewBinding.tvLeave.setOnClickListener(this);
        mViewBinding.tvLate.setOnClickListener(this);
        mViewBinding.tvNormal.setOnClickListener(this);
        mViewBinding.tvAll.setChecked(true);
        mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (itemTeachersBean != null && itemTeachersBean.getTeachers() != null) {
            AttendanceCheckRsp.DataBean.AttendancesFormBean.TeachersBean teachers = itemTeachersBean.getTeachers();
            mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
            mViewBinding.tvDesc.setText(teachers.getThingName());
            mViewBinding.tvAttendanceTime.setText("考勤时间 " +
                    (!TextUtils.isEmpty(teachers.getRequiredTime()) ?
                            teachers.getRequiredTime() :
                            teachers.getApplyDate()));
            mViewBinding.tvAttendanceRate.setText(teachers.getRate());
            if (!TextUtils.isEmpty(teachers.getRate())) {
                try {
                    mViewBinding.progress.setProgress(Double.valueOf(teachers.getRate()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mViewBinding.tvBeTo.setText(teachers.getNumber() + "");
            mViewBinding.tvLateNum.setText(("0".equals(teachers.getGoOutStatus()) ? teachers.getLate() : teachers.getLeaveEarly()) + "");
            mViewBinding.tvLateName.setText("0".equals(teachers.getGoOutStatus()) ? "迟到" : "早退");
            mViewBinding.tvLeaveNum.setText(teachers.getLeave() + "");
            mViewBinding.tvNormalNum.setText(teachers.getApplyNum() + "");
            mViewBinding.tvAbsenteeismNum.setText(teachers.getAbsence() + "");
            adapter.setList(teachers.getPeople());
        }
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
                adapter.setList(itemTeachersBean.getTeachers() != null ? itemTeachersBean.getTeachers().getPeople() : null);
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemTeachersBean.getTeachers() != null ? itemTeachersBean.getTeachers().getAbsencePeople() : null);
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemTeachersBean.getTeachers() != null ? itemTeachersBean.getTeachers().getLeavePeople() : null);
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemTeachersBean.getTeachers() != null ? itemTeachersBean.getTeachers().getLatePeople() : null);
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemTeachersBean.getTeachers() != null ? itemTeachersBean.getTeachers().getApplyPeople() : null);
                break;
        }
    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean, BaseViewHolder>(R.layout.item_attendance_student) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean item) {
            holder.setText(R.id.tv_student_name, item.getName())
                    .setText(R.id.tv_student_time, item.getTime())
                    .setText(R.id.tv_student_event, item.getDeviceName())
                    .setText(R.id.tv_status, item.getStatusType());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }

}
