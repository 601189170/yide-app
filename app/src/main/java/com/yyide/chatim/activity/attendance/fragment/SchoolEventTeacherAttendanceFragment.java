package com.yyide.chatim.activity.attendance.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolEventTeacherAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

/**
 * desc 教师教职工事件考情详情
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolEventTeacherAttendanceFragment extends BaseFragment implements View.OnClickListener {
    private AttendanceRsp.DataBean itemTeachersBean;
    private AttendanceRsp.TeacherCourseFormBean teacherCourseForm;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentSchoolEventTeacherAttendanceBinding mViewBinding;
    private AttendanceRsp.DataBean.AttendanceListBean teachers = new AttendanceRsp.DataBean.AttendanceListBean();

    public static SchoolEventTeacherAttendanceFragment newInstance(AttendanceRsp.DataBean teachersBean) {
        SchoolEventTeacherAttendanceFragment fragment = new SchoolEventTeacherAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("teachersBean", teachersBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemTeachersBean = (AttendanceRsp.DataBean) getArguments().getSerializable("teachersBean");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentSchoolEventTeacherAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    public interface OnRefreshListener {
        void onRefreshListener(boolean isRefresh);
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefreshListener(verticalOffset >= 0);
            }
        });

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
        if (itemTeachersBean != null) {
            teachers = itemTeachersBean.getTeacherCourseForm().getBaseInfo();
            mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
            mViewBinding.tvEventName.setText(teachers.getEventName());

            mViewBinding.tvAttendanceRate.setText(teachers.getSignInOutRate());
            if (!TextUtils.isEmpty(teachers.getSignInOutRate())) {
                try {
                    //mViewBinding.progress.setProgress(Double.valueOf(teachers.getRate()).intValue());
                    setAnimation(mViewBinding.progress, Double.valueOf(teachers.getSignInOutRate()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mViewBinding.tvLateNum.setText(teachers.getLate() + "");
            mViewBinding.tvLateNum.setText(("1".equals(teachers.getAttendanceSignInOut()) ? teachers.getEarly() : teachers.getLate()) + "");
            mViewBinding.tvLateName.setText("1".equals(teachers.getAttendanceSignInOut()) ? "早退" : "迟到");
            mViewBinding.tvLate.setText("1".equals(teachers.getAttendanceSignInOut()) ? "早退" : "迟到");
            mViewBinding.tvAbsenteeism.setText("1".equals(teachers.getAttendanceSignInOut()) ? "未签退" : "缺勤");
            mViewBinding.tvAbsenteeismTitle.setText("1".equals(teachers.getAttendanceSignInOut()) ? "未签退" : "缺勤");
            mViewBinding.tvAttendanceDesc.setText("1".equals(teachers.getAttendanceSignInOut()) ? "签退率" : "出勤率");

            mViewBinding.tvLeaveNum.setText(teachers.getLeave() + "");
            mViewBinding.tvAbsenteeismNum.setText(teachers.getAbsenteeism() + "");
            teacherCourseForm = itemTeachersBean.getTeacherCourseForm();
            adapter.setList(teacherCourseForm != null ? teacherCourseForm.getAllRollList() : null);
        }
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
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
                adapter.setList(teacherCourseForm != null ? teacherCourseForm.getAllRollList() : null);
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? teacherCourseForm.getAbsenteeismList() : null);
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? teacherCourseForm.getLeaveList() : null);
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                if (itemTeachersBean != null) {
                    if ("1".equals(teachers.getAttendanceSignInOut())) {
                        adapter.setList(teacherCourseForm != null ? teacherCourseForm.getEarlyList() : null);
                    } else {
                        adapter.setList(teacherCourseForm != null ? teacherCourseForm.getLateList() : null);
                    }
                } else {
                    adapter.setList(null);
                }
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? teacherCourseForm.getNormalList() : null);
                break;
        }
    }

    private BaseQuickAdapter<AttendanceRsp.TeacherItemBean, BaseViewHolder> adapter = new BaseQuickAdapter<AttendanceRsp.TeacherItemBean, BaseViewHolder>(R.layout.item_attendance_school_event_teacher) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceRsp.TeacherItemBean item) {
            holder.setText(R.id.tv_student_name, !TextUtils.isEmpty(item.getUserName()) ? item.getUserName() : "未知姓名");
            TextView tvTime = holder.getView(R.id.tv_student_time);
            TextView tvName = holder.getView(R.id.tv_student_name);
            holder.setText(R.id.tv_student_time, "");
            holder.setText(R.id.tv_student_event, "");
            holder.setText(R.id.tv_status, "");
            //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
            if (!TextUtils.isEmpty(item.getAttendanceType())) {
                switch (item.getAttendanceType()) {
                    case "0"://正常
                        holder.setText(R.id.tv_status, "正常");
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#606266"));
                        break;
                    case "6":
                        holder.setText(R.id.tv_status, "1".equals(item.getAttendanceSignInOut()) ? "未签退" : "未签到");
                        break;
                    case "1"://缺勤
                        holder.setText(R.id.tv_status, "缺勤");
                        break;
                    case "3"://早退
                        holder.setText(R.id.tv_status, "早退");
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#63DAAB"));
                        break;
                    case "2"://迟到
                        holder.setText(R.id.tv_status, "1".equals(item.getAttendanceSignInOut()) ? "早退" : "迟到");
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#F66C6C"));
                        break;
                    case "5":
                        holder.setText(R.id.tv_status, "请假");
                        String startTime = DateUtils.formatTime(item.getStartTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        String endTime = DateUtils.formatTime(item.getEndTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        holder.setText(R.id.tv_student_event, "请假时间");
                        holder.setText(R.id.tv_student_time, startTime + "-" + endTime);
                        tvTime.setTextColor(Color.parseColor("#F6BD16"));
                        break;
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }

}
