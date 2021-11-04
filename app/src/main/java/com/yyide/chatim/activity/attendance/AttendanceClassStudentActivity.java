package com.yyide.chatim.activity.attendance;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityAttendanceClassStudentBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.model.AttendanceSchoolGradeRsp;
import com.yyide.chatim.presenter.SchoolGradePresenter;
import com.yyide.chatim.view.SchoolGradeView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AttendanceClassStudentActivity extends BaseMvpActivity<SchoolGradePresenter> implements SchoolGradeView {

    private ActivityAttendanceClassStudentBinding viewBinding;
    private AttendanceRsp.DataBean.AttendanceListBean gradeBean;
    private String TAG = AttendanceClassStudentActivity.class.getSimpleName();
    private long gradeId;

    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance_class_student;
    }

    public static void start(Context context, AttendanceRsp.DataBean.AttendanceListBean gradeBean) {
        Intent intent = new Intent(context, AttendanceClassStudentActivity.class);
        intent.putExtra("gradeBean", gradeBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAttendanceClassStudentBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.top.title.setText(R.string.attendance_title);
        viewBinding.top.backLayout.setOnClickListener(v -> finish());
        viewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.recyclerview.setAdapter(adapter);

        viewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mvpPresenter.getMyAppList(gradeBean);
        });
        viewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        viewBinding.clContent.setVisibility(View.GONE);
        gradeBean = (AttendanceRsp.DataBean.AttendanceListBean) getIntent().getSerializableExtra("gradeBean");
        mvpPresenter.getMyAppList(gradeBean);
    }

    @Override
    protected SchoolGradePresenter createPresenter() {
        return new SchoolGradePresenter(this);
    }

    private void setDataView(AttendanceRsp.DataBean dataBean) {
        gradeId = gradeBean.getGradeId();
        if (dataBean != null) {
            if (dataBean.getGradeInfoList() != null
                    && dataBean.getGradeInfoList().size() > 0) {
                viewBinding.tvAttendanceTitle.setClickable(true);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            } else {
                viewBinding.tvAttendanceTitle.setClickable(false);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            viewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                gradeId = gradeBean.getGradeId();
                AttendancePop attendancePop = new AttendancePop(this, adapterEvent, "请选择年级");
                adapterEvent.setList(dataBean.getGradeInfoList());
                attendancePop.setOnSelectListener(position -> {
                    gradeBean = dataBean.getGradeInfoList().get(position);
                    gradeId = gradeBean.getGradeId();
                    setData();
                });
            });
        }

    }

    private final BaseQuickAdapter adapterEvent = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, AttendanceRsp.DataBean.AttendanceListBean item) {
            baseViewHolder.setText(R.id.className, item.getGradeName());
            baseViewHolder.getView(R.id.select).setVisibility(gradeId == item.getGradeId() ? View.VISIBLE : View.GONE);
            if (adapterEvent.getItemCount() - 1 == baseViewHolder.getAbsoluteAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        }
    };

    private void setData() {
        viewBinding.clContent.setVisibility(View.VISIBLE);
        viewBinding.tvAttendanceTitle.setText(gradeBean.getGradeName());
        viewBinding.tvEventName.setText(gradeBean.getGradeName() + "(人)");
        viewBinding.tvAttendanceRate.setText(gradeBean.getSignInOutRate());
        if (!TextUtils.isEmpty(gradeBean.getSignInOutRate())) {
            try {
                setAnimation(viewBinding.progress, Integer.parseInt(gradeBean.getSignInOutRate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//            viewBinding.tvNumber.setText("(" + gradeListBean.getNumber() + "人)");
        viewBinding.tvLateNum.setText(gradeBean.getLate() + "");
        viewBinding.tvLeaveNum.setText(gradeBean.getLeave() + "");
        viewBinding.tvAbsenteeismNum.setText(gradeBean.getAbsenteeism() + "");
        viewBinding.tvLeaveTitle.setText("1".equals(gradeBean.getAttendanceSignInOut()) ? "早退" : "迟到");
        viewBinding.tvSign.setText("1".equals(gradeBean.getAttendanceSignInOut()) ? "签退率" : "出勤率");
        viewBinding.tvAbsenceDesc.setText("1".equals(gradeBean.getAttendanceSignInOut()) ? "未签退" : "缺勤");
//        adapter.setList(remove(gradeBean.getClassForm()));
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }


    private final BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder> adapter = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceRsp.DataBean.AttendanceListBean item) {
            holder.setText(R.id.tv_event_name, item.getGradeName())
                    .setText(R.id.tv_attendance_rate, item.getSignInOutRate())
                    .setText(R.id.tv_sign, "1".equals(item.getAttendanceSignInOut()) ? "签退率" : "出勤率")
                    .setText(R.id.tv_normal_num, item.getNormal() + "")
                    .setText(R.id.tv_absence_num, item.getAbsenteeism() + "")
                    .setText(R.id.tv_absence, "1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
        }
    };

    @Override
    public void showError() {

    }

    @Override
    public void getGradeListSuccess(AttendanceRsp model) {
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (model.getData() != null) {
                setDataView(model.getData());
                setData();
            }
        }
    }

    @Override
    public void getFail(String msg) {
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, msg);
    }
}