package com.yyide.chatim.activity.attendance.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.AttendanceHomePresenter;
import com.yyide.chatim.presenter.AttendanceTeacherPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * desc 教师教职工考情详情
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class TeacherStudentAttendanceFragment extends BaseMvpFragment<AttendanceTeacherPresenter> implements AttendanceCheckView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private AttendanceRsp.TeacherCourseFormBean itemStudents;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentAttendanceBinding mViewBinding;
    private AttendanceRsp.DataBean.AttendanceListBean itemParams;
    private String classesId;

    public static TeacherStudentAttendanceFragment newInstance(AttendanceRsp.DataBean.AttendanceListBean item) {
        TeacherStudentAttendanceFragment fragment = new TeacherStudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemParams = (AttendanceRsp.DataBean.AttendanceListBean) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getAttendance();
    }

    @Override
    protected AttendanceTeacherPresenter createPresenter() {
        return new AttendanceTeacherPresenter(this);
    }

    private void getAttendance() {
        if (itemParams != null) {
            mvpPresenter.attendance(classesId, itemParams.getServerId(), itemParams.getType(), itemParams.getAttendanceTimeId());
        }
    }

    private void initView() {
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mViewBinding.swipeRefreshLayout.setEnabled(verticalOffset >= 0);//页面滑动到顶部，才可以下拉刷新
        });
//        mViewBinding.constraintLayout.setVisibility(View.GONE);
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

        if (SpData.getClassInfo() != null) {
            classesId = SpData.getClassInfo().classesId;
            mViewBinding.tvClassName.setText(SpData.getClassInfo().classesName);
        }
    }

    @Override
    public void onClick(View v) {
        initClickView();
        switch (v.getId()) {
            case R.id.tv_all:
                mViewBinding.tvAll.setChecked(true);
                mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getAllRollList() : null);
                break;
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getAbsenteeismList() : null);
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getLeaveList() : null);
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                if (itemStudents != null) {
                    adapter.setList("1".equals(itemParams.getAttendanceSignInOut()) ? itemStudents.getEarlyList() : itemStudents.getLateList());
                }
                break;
            case R.id.tv_normal:
                mViewBinding.tvNormal.setChecked(true);
                mViewBinding.tvNormal.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(itemStudents != null ? itemStudents.getNormalList() : null);
                break;
        }
    }

    private void initClickView() {
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
    }

    @SuppressLint("SetTextI18n")
    private void setDataView(AttendanceRsp.DataBean item) {
        mViewBinding.tvClassName.setOnClickListener(v -> {
            List<GetUserSchoolRsp.DataBean.FormBean> classList = SpData.getIdentityInfo().form;
            AttendancePop attendancePop = new AttendancePop(getActivity(), adapterClass, "请选择班级");
            name = mViewBinding.tvClassName.getText().toString().trim();
            adapterClass.setList(classList);
            attendancePop.setOnSelectListener(index -> {
                mViewBinding.tvClassName.setText(classList.get(index).classesName);
                classesId = classList.get(index).classesId;
                getAttendance();
            });
        });
        if (item != null) {
            itemStudents = item.getTeacherCourseForm();
            mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                AttendancePop attendancePop = new AttendancePop(getActivity(), adapterEvent, "请选择考勤事件");
                adapterEvent.setList(item.getClassroomTeacherAttendanceList());
                attendancePop.setOnSelectListener(index -> {
                    itemParams = item.getClassroomTeacherAttendanceList().get(index);
                    getAttendance();
                });
            });

            if (SpData.getIdentityInfo() != null) {
                if (SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1) {
                    mViewBinding.tvClassName.setClickable(true);
                    mViewBinding.tvClassName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
                } else {
                    mViewBinding.tvClassName.setClickable(false);
                    mViewBinding.tvClassName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            if (item.getClassroomTeacherAttendanceList() != null && item.getClassroomTeacherAttendanceList().size() > 1) {
                mViewBinding.tvAttendanceTitle.setClickable(true);
                mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            } else {
                mViewBinding.tvAttendanceTitle.setClickable(false);
                mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            if (item.getTeacherCourseForm() != null) {
                setData(item.getTeacherCourseForm().getBaseInfo());
            }
        }
    }

    private String name;
    private final BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder> adapterClass = new BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, GetUserSchoolRsp.DataBean.FormBean item) {
            baseViewHolder.setText(R.id.className, item.classesName);
            if (adapterClass.getItemCount() - 1 == baseViewHolder.getAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
            baseViewHolder.getView(R.id.select).setVisibility(name.equals(item.classesName) ? View.VISIBLE : View.GONE);
        }
    };

    private final BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder> adapterEvent = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, AttendanceRsp.DataBean.AttendanceListBean item) {
            baseViewHolder.setText(R.id.className, item.getTheme());
            if (itemParams != null
                    && itemParams.getAttendanceTimeId().equals(item.getAttendanceTimeId())
                    && itemParams.getServerId().equals(item.getServerId())) {
                baseViewHolder.getView(R.id.select).setVisibility(View.VISIBLE);
            } else {
                baseViewHolder.getView(R.id.select).setVisibility(View.GONE);
            }

            if (adapterEvent.getItemCount() - 1 == baseViewHolder.getAbsoluteAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        }
    };


    @SuppressLint("SetTextI18n")
    private void setData(AttendanceRsp.DataBean.AttendanceListBean item) {
        if (item != null) {
            mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
            mViewBinding.tvAttendanceTitle.setText(item.getTheme());
            mViewBinding.tvDesc.setText(TextUtils.isEmpty(item.getEventName()) ? item.getTheme() : item.getEventName());
            mViewBinding.tvAttendanceTime.setText(item.getCourseTime());

            mViewBinding.tvSign.setText("1".equals(item.getAttendanceSignInOut()) ? "签退率" : "出勤率");
            mViewBinding.tvLateName.setText("1".equals(item.getAttendanceSignInOut()) ? "早退" : "迟到");
            mViewBinding.tvLate.setText("1".equals(item.getAttendanceSignInOut()) ? "早退" : "迟到");
            mViewBinding.tvAbsenteeismName.setText("1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤");
            mViewBinding.tvAbsenteeism.setText("1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤");
            mViewBinding.tvAttendanceRate.setText(item.getSignInOutRate());
            if (!TextUtils.isEmpty(item.getSignInOutRate())) {
                try {
                    setAnimation(mViewBinding.progress, Double.valueOf(item.getSignInOutRate()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mViewBinding.tvBeTo.setText(item.getTotalNumber() + "");
            mViewBinding.tvLateNum.setText(("1".equals(item.getAttendanceSignInOut()) ? item.getEarly() : item.getLate()) + "");
            mViewBinding.tvAbsenteeismNum.setText(item.getAbsenteeism() + "");
            mViewBinding.tvNormalNum.setText(item.getNormal() + "");
            mViewBinding.tvLeaveNum.setText(item.getLeave() + "");
        } else {
            mViewBinding.progress.setProgress(0);
            mViewBinding.tvAttendanceTitle.setText("");
            mViewBinding.tvDesc.setText("");
            mViewBinding.tvAttendanceTime.setText("");
            mViewBinding.tvAttendanceRate.setText("0");
            mViewBinding.tvBeTo.setText("0");
            mViewBinding.tvLateNum.setText("0");
            mViewBinding.tvLeaveNum.setText("0");
            mViewBinding.tvNormalNum.setText("0");
            mViewBinding.tvAbsenteeismNum.setText("0");
        }
        initClickView();
        mViewBinding.tvAll.setChecked(true);
        mViewBinding.tvAll.setTextColor(getResources().getColor(R.color.white));
        adapter.setList(itemStudents != null ? itemStudents.getAllRollList() : null);
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }

    @Override
    public void onRefresh() {
        getAttendance();
    }

    private BaseQuickAdapter<AttendanceRsp.TeacherItemBean, BaseViewHolder> adapter = new BaseQuickAdapter<AttendanceRsp.TeacherItemBean, BaseViewHolder>(R.layout.item_attendance_student) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceRsp.TeacherItemBean item) {
            holder.setText(R.id.tv_student_name, !TextUtils.isEmpty(item.getUserName()) ? item.getUserName() : "未知姓名");
            TextView tvTime = holder.getView(R.id.tv_student_time);
            holder.setText(R.id.tv_student_time, "");
            holder.setText(R.id.tv_student_event, "");
            holder.setText(R.id.tv_status, "");
            if (!TextUtils.isEmpty(item.getAttendanceType())) {
                switch (item.getAttendanceType()) {
                    case "0"://正常
//                        holder.setText(R.id.tv_status, item.getStatusType());
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#606266"));
                        break;
                    case "1"://缺勤
                    case "6":
                        holder.setText(R.id.tv_status, "1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤");
                        break;
                    case "3"://早退
                        holder.setText(R.id.tv_status, "早退");
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#63DAAB"));
                        break;
                    case "2"://迟到
                        holder.setText(R.id.tv_status, "迟到");
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#F66C6C"));
                        break;
                    case "5"://请假
                        holder.setText(R.id.tv_status, "请假");
                        String startTime = DateUtils.formatTime(item.getStartTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        String endTime = DateUtils.formatTime(item.getEndTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        holder.setText(R.id.tv_student_event, "请假时间");
                        holder.setText(R.id.tv_student_time, startTime + "-" + endTime);
                        tvTime.setTextColor(Color.parseColor("#F6BD16"));
                        break;
                    case "4":
                        //无效打卡
                        break;
                }
            }
        }
    };

    @Override
    public void getAttendanceSuccess(AttendanceRsp model) {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null) {
                setDataView(model.getData());
            }
        }
    }

    @Override
    public void getAttendanceFail(String msg) {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getHomeAttendanceFail-->>" + msg);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }

}
