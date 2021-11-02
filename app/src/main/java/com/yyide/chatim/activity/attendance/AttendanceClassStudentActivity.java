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
import com.yyide.chatim.model.AttendanceSchoolGradeRsp;
import com.yyide.chatim.presenter.SchoolGradePresenter;
import com.yyide.chatim.view.SchoolGradeView;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AttendanceClassStudentActivity extends BaseMvpActivity<SchoolGradePresenter> implements SchoolGradeView {

    private ActivityAttendanceClassStudentBinding viewBinding;
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean;
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean gradeListBean;
    private String TAG = AttendanceClassStudentActivity.class.getSimpleName();
    private int index;
    private long gradeId;

    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance_class_student;
    }

    public static void start(Context context, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean gradeListBean, int index) {
        Intent intent = new Intent(context, AttendanceClassStudentActivity.class);
        intent.putExtra("students", item);
        intent.putExtra("gradeListBean", gradeListBean);
        intent.putExtra("index", index);
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
            mvpPresenter.getMyAppList();
        });
        viewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        viewBinding.clContent.setVisibility(View.GONE);
        setDataView();
    }

    @Override
    protected SchoolGradePresenter createPresenter() {
        return new SchoolGradePresenter(this);
    }

    private void setDataView() {
        schoolPeopleAllFormBean = (AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean) getIntent().getSerializableExtra("students");
        gradeListBean = (AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean) getIntent().getSerializableExtra("gradeListBean");
        gradeId = gradeListBean.getGradeId();
        index = getIntent().getIntExtra("index", 0);
        if (schoolPeopleAllFormBean != null && schoolPeopleAllFormBean.getGradeList().size() > 0) {
            mvpPresenter.getMyAppList();
            if (schoolPeopleAllFormBean.getGradeList() != null && schoolPeopleAllFormBean.getGradeList().size() > 1) {
                viewBinding.tvAttendanceTitle.setClickable(true);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            } else {
                viewBinding.tvAttendanceTitle.setClickable(false);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            viewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                gradeId = gradeListBean.getGradeId();
                AttendancePop attendancePop = new AttendancePop(this, adapterEvent, "请选择年级");
                adapterEvent.setList(schoolPeopleAllFormBean.getGradeList());
                attendancePop.setOnSelectListener(position -> {
                    gradeListBean = schoolPeopleAllFormBean.getGradeList().get(position);
                    gradeListBean.classForm = schoolPeopleAllFormBean.getGradeList().get(position).getClassForm();
                    gradeId = gradeListBean.getGradeId();
                    setData();
                });
            });
        }
    }

    private final BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean, BaseViewHolder> adapterEvent = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean item) {
            baseViewHolder.setText(R.id.className, item.getName());
            baseViewHolder.getView(R.id.select).setVisibility(gradeId == item.getGradeId() ? View.VISIBLE : View.GONE);
            if (adapterEvent.getItemCount() - 1 == baseViewHolder.getAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        }
    };

    private void setData() {
        viewBinding.clContent.setVisibility(View.VISIBLE);
        viewBinding.tvAttendanceTitle.setText(gradeListBean.getName());
        viewBinding.tvEventName.setText(gradeListBean.getName() + "(人)");
        viewBinding.tvAttendanceRate.setText(gradeListBean.getRate());
        if (!TextUtils.isEmpty(gradeListBean.getRate())) {
            try {
                //viewBinding.progress.setProgress(Double.valueOf(gradeListBean.getRate()).intValue());
                setAnimation(viewBinding.progress, Integer.parseInt(gradeListBean.getRate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//            viewBinding.tvNumber.setText("(" + gradeListBean.getNumber() + "人)");
        viewBinding.tvLateNum.setText(gradeListBean.getLate() + "");
        viewBinding.tvLeaveNum.setText(gradeListBean.getLeave() + "");
        viewBinding.tvAbsenteeismNum.setText(gradeListBean.getAbsence() + "");
        viewBinding.tvLeaveTitle.setText("1".equals(gradeListBean.goOutStatus) ? "早退" : "迟到");
        viewBinding.tvSign.setText("1".equals(gradeListBean.goOutStatus) ? "签退率" : "出勤率");
        viewBinding.tvAbsenceDesc.setText("1".equals(gradeListBean.goOutStatus) ? "未签退" : "缺勤");
        adapter.setList(remove(gradeListBean.getClassForm()));
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }

    //使用iterator，这个是java和Android源码中经常使用到的一种方法，所以最为推荐
    public List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> remove(List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> list) {
        Iterator<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean item = sListIterator.next();
            if (item.getNumber() <= 0) {
                sListIterator.remove();
            }
            if (gradeListBean != null) {
                item.setGoOutStatus(gradeListBean.goOutStatus);
            }
        }
        return list;
    }

    //使用iterator，这个是java和Android源码中经常使用到的一种方法，所以最为推荐
    public List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> removeGradeListBean(List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> list) {
        Iterator<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean item = sListIterator.next();
            if (item.getNumber() == 0) {
                sListIterator.remove();
            }
            if (gradeListBean != null) {
                item.setGoOutStatus(gradeListBean.goOutStatus);
            }
        }
        return list;
    }


    private final BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean, BaseViewHolder> adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean item) {
            holder.setText(R.id.tv_event_name, item.getName())
                    .setText(R.id.tv_attendance_rate, item.getRate())
                    .setText(R.id.tv_sign, "1".equals(item.goOutStatus) ? "签退率" : "出勤率")
                    .setText(R.id.tv_normal_num, item.getApplyNum() + "")
                    .setText(R.id.tv_absence_num, item.getAbsence() + "")
                    .setText(R.id.tv_absence, "1".equals(item.goOutStatus) ? "未签退" : "缺勤")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
        }
    };

    @Override
    public void showError() {

    }

    @Override
    public void getGradeListSuccess(AttendanceCheckRsp model) {
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        if (model.code == BaseConstant.REQUEST_SUCCES2) {
            if (model.data != null) {
                if (model.data.schoolPeopleAllForm != null && model.data.schoolPeopleAllForm.size() > 0) {
                    for (int i = 0; i < model.data.schoolPeopleAllForm.size(); i++) {
                        AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolItem = model.data.schoolPeopleAllForm.get(i);
                        List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> gradeListBeans = removeGradeListBean(schoolItem.getGradeList());
                        if (i == index && gradeListBeans != null) {
                            for (int j = 0; j < gradeListBeans.size(); j++) {
                                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean gradeItem = gradeListBeans.get(j);
                                gradeItem.goOutStatus = schoolItem.goOutStatus;
                                if (gradeItem.gradeId == gradeId) {
                                    this.gradeListBean = gradeItem;
                                }
                            }
                        }
                    }
                    setData();
                }
            }
        }
    }

    @Override
    public void getFail(String msg) {
        viewBinding.swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, msg);
    }
}