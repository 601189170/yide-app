package com.yyide.chatim.activity.attendance.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.activity.attendance.AttendanceSchoolTeacherAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolTeacherAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim.widget.treeview.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc 教师教职工考情详情
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolTeacherAttendanceFragment extends BaseFragment implements View.OnClickListener {
    private AttendanceRsp.DataBean itemTeachersBean;
    private AttendanceRsp.DataBean.AttendanceListBean teachers;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentSchoolTeacherAttendanceBinding mViewBinding;
    private AttendanceSchoolTeacherAdapter adapter;
    private AttendanceRsp.TeacherCourseFormBean teacherCourseForm;

    public static SchoolTeacherAttendanceFragment newInstance(AttendanceRsp.DataBean teachersBean) {
        SchoolTeacherAttendanceFragment fragment = new SchoolTeacherAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("teachersBean", teachersBean);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnRefreshListener {
        void onRefreshListener(boolean isRefresh);
    }

    private SchoolEventTeacherAttendanceFragment.OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(SchoolEventTeacherAttendanceFragment.OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
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
        mViewBinding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefreshListener(verticalOffset >= 0);
            }
        });
        mViewBinding.tvAbsenteeism.setOnClickListener(this);
        mViewBinding.tvLeave.setOnClickListener(this);
        mViewBinding.tvLate.setOnClickListener(this);
        mViewBinding.tvAbsenteeism.setChecked(true);
        mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
        setData();
    }

    private final List<TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean>> dataToBind = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (itemTeachersBean != null) {
            teacherCourseForm = itemTeachersBean.getTeacherCourseForm();
            if (teacherCourseForm != null) {
                teachers = itemTeachersBean.getTeacherCourseForm().getBaseInfo();
                if (teachers != null) {
                    mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
                    mViewBinding.tvEventName.setText(!TextUtils.isEmpty(teachers.getEventName()) ? teachers.getEventName() : teachers.getTheme());
                    mViewBinding.tvAttendanceRate.setText(teachers.getSignInOutRate());
                    if (!TextUtils.isEmpty(teachers.getSignInOutRate())) {
                        try {
                            setAnimation(mViewBinding.progress, Double.valueOf(teachers.getSignInOutRate()).intValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mViewBinding.tvLateNum.setText(teachers.getLate() + "");
//                    mViewBinding.tvLateName.setText("1".equals(teachers.getAttendanceSignInOut()) ? "早退" : "迟到");
//                    mViewBinding.tvLate.setText("1".equals(teachers.getAttendanceSignInOut()) ? "早退" : "迟到");
//                    mViewBinding.tvAttendanceDesc.setText("1".equals(teachers.getAttendanceSignInOut()) ? "签退率" : "出勤率");
                    mViewBinding.tvLeaveNum.setText(teachers.getLeave() + "");
                    mViewBinding.tvAbsenteeismNum.setText(teachers.getAbsenteeism() + "");
                    //mViewBinding.tvNum.setText((teachers.getAbsencePeople() != null ? teachers.getAbsencePeople().size() : 0) + "人");
                    dataToBind.addAll(convertDataToTreeNode(teacherCourseForm.getAbsenteeismList()));
                    adapter = new AttendanceSchoolTeacherAdapter(R.layout.item_attendance_school_teacher, dataToBind);
                    mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    mViewBinding.recyclerview.setAdapter(adapter);
                    adapter.setEmptyView(R.layout.empty_top);
                    adapter.setOnTreeClickedListener(new SingleLayoutTreeAdapter.OnTreeClickedListener() {
                        @Override
                        public void onNodeClicked(View view, TreeNode node, int position) {
                            TextView tvStatus = view.findViewById(R.id.tv_status);
                            if (node.isExpand()) {
                                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_up), null);
                            } else {
                                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_down), null);
                            }
                        }

                        @Override
                        public void onLeafClicked(View view, TreeNode node, int position) {

                        }
                    });
                }
            }
        }
    }

    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(800);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }

    private List<TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean>> convertDataToTreeNode(List<AttendanceRsp.TeacherItemBean> datas) {
        List<TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean>> nodes = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            List<TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean>> childs = new ArrayList<>();
            AttendanceRsp.TeacherItemBean peopleBean = datas.get(i);
            AttendanceRsp.TeacherItemBean.CourseInfoFormListBean cfb = new AttendanceRsp.TeacherItemBean.CourseInfoFormListBean();
            cfb.setCourseName(peopleBean.getUserName());
            cfb.setSection(peopleBean.getSectionNumber() + "");
            TreeNode treeNode = new TreeNode(cfb, -1);
            for (AttendanceRsp.TeacherItemBean.CourseInfoFormListBean childItem : peopleBean.getCourseInfoFormList()) {
                TreeNode child = new TreeNode<>(childItem, -1);
                child.setParent(treeNode);
                childs.add(child);
            }
            treeNode.setChildren(childs);
            nodes.add(treeNode);
        }
        return nodes;
    }

    @Override
    public void onClick(View v) {
        mViewBinding.tvAbsenteeism.setChecked(false);
        mViewBinding.tvLeave.setChecked(false);
        mViewBinding.tvLate.setChecked(false);
        mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.text_1E1E1E));
        switch (v.getId()) {
            case R.id.tv_absenteeism:
                mViewBinding.tvAbsenteeism.setChecked(true);
                mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? convertDataToTreeNode(teacherCourseForm.getAbsenteeismList()) : null);
                //mViewBinding.tvNum.setText((teachers != null ? teachers.getAbsencePeople().size() : 0) + "人");
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? convertDataToTreeNode(teacherCourseForm.getLeaveList()) : null);
                //mViewBinding.tvNum.setText((teachers != null ? teachers.getLeavePeople().size() : 0) + "人");
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teacherCourseForm != null ? convertDataToTreeNode(teacherCourseForm.getLateList()) : null);
                //mViewBinding.tvNum.setText((teachers != null ? teachers.getLatePeople().size() : 0) + "人");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }

}
