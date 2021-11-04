package com.yyide.chatim.activity.attendance.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceClassStudentActivity;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolMasterAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * desc 校长查看学生考勤: 课堂 事件
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolStudentAttendanceFragment extends BaseFragment {

    private FragmentSchoolMasterAttendanceBinding mViewBinding;
    private String TAG = SchoolStudentAttendanceFragment.class.getSimpleName();
    private AttendanceRsp.DataBean dataBean;
    private AttendanceRsp.DataBean.AttendanceListBean itemStudents;

    public static SchoolStudentAttendanceFragment newInstance(AttendanceRsp.DataBean dataBean) {
        SchoolStudentAttendanceFragment fragment = new SchoolStudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("dataBean", dataBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataBean = (AttendanceRsp.DataBean) getArguments().getSerializable("dataBean");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentSchoolMasterAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding.clContent.setVisibility(View.GONE);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view1, position) ->
        {
            AttendanceRsp.DataBean.AttendanceListBean gradeListBean = (AttendanceRsp.DataBean.AttendanceListBean) adapter.getItem(position);
            if (itemStudents != null) {
                gradeListBean.setType(itemStudents.getType());
                gradeListBean.setAttendanceTimeId(itemStudents.getAttendanceTimeId());
                gradeListBean.setServerId(itemStudents.getServerId());
                gradeListBean.setAttendanceSignInOut(itemStudents.getAttendanceSignInOut());
            }
            AttendanceClassStudentActivity.start(getContext(), gradeListBean);
        });
        setDataView();
    }

    private void setDataView() {
        if (dataBean != null) {
            AttendanceRsp.StudentCourseFormBean courseFormBean = dataBean.getStudentCourseFormBean();
            if (courseFormBean != null) {
                itemStudents = dataBean.getStudentCourseFormBean().getBaseInfo();
                if (itemStudents != null) {
                    mViewBinding.clContent.setVisibility(View.VISIBLE);
                    mViewBinding.tvEventName.setText(itemStudents.getTheme());
                    mViewBinding.tvAttendanceRate.setText(itemStudents.getSignInOutRate());
                    if (!TextUtils.isEmpty(itemStudents.getSignInOutRate())) {
                        try {
                            setAnimation(mViewBinding.progress, Double.valueOf(itemStudents.getSignInOutRate()).intValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mViewBinding.tvSign.setText("1".equals(itemStudents.getAttendanceSignInOut()) ? "签退率" : "出勤率");
                    mViewBinding.tvAbsenceTitle.setText("1".equals(itemStudents.getAttendanceSignInOut()) ? "未签退" : "缺勤");
                    mViewBinding.tvLateNum.setText(("1".equals(itemStudents.getAttendanceSignInOut()) ? itemStudents.getEarly() : itemStudents.getLate()) + "");
                    mViewBinding.tvLeaveNum.setText(itemStudents.getLeave() + "");
                    mViewBinding.tvAbsenteeismNum.setText(itemStudents.getAttendanceSignInOut() + "");
                    mViewBinding.tvLate.setText("1".equals(itemStudents.getAttendanceSignInOut()) ? "早退" : "迟到");
                    adapter.setList(courseFormBean.getAttendanceAppGradeInfoFormList());
                }
            }
            //获取选中的年级事件
            List<AttendanceRsp.DataBean.AttendanceListBean> headmasterAttendanceList = dataBean.getHeadmasterAttendanceList();
            if (headmasterAttendanceList != null) {
                for (AttendanceRsp.DataBean.AttendanceListBean item : headmasterAttendanceList) {
                    if (item.getGradeId() == itemStudents.getGradeId()) {
                        itemStudents = item;
                        break;
                    }
                }
            }
        }
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }

    private BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder> adapter = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceRsp.DataBean.AttendanceListBean item) {
            holder.setText(R.id.tv_event_name, item.getGradeName())
                    .setText(R.id.tv_attendance_rate, item.getSignInOutRate())
                    .setText(R.id.tv_normal_num, item.getNormal() + "")
                    .setText(R.id.tv_absence, "1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤")
                    .setText(R.id.tv_sign, "1".equals(item.getAttendanceSignInOut()) ? "签退率" : "出勤率")
                    .setText(R.id.tv_absence_num, item.getAbsenteeism() + "")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }
}