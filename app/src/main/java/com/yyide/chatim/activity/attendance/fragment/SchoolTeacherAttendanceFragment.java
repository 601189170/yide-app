package com.yyide.chatim.activity.attendance.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.activity.attendance.AttendanceSchoolTeacherAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolTeacherAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;
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
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean itemTeachersBean;
    private AttendanceCheckRsp.DataBean.AttendancesFormBean.TeachersBean teachers;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentSchoolTeacherAttendanceBinding mViewBinding;
    private AttendanceSchoolTeacherAdapter adapter;

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

        mViewBinding.tvAbsenteeism.setOnClickListener(this);
        mViewBinding.tvLeave.setOnClickListener(this);
        mViewBinding.tvLate.setOnClickListener(this);
        mViewBinding.tvAbsenteeism.setChecked(true);
        mViewBinding.tvAbsenteeism.setTextColor(getResources().getColor(R.color.white));
        setData();
    }

    private List<TreeNode<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean>> dataToBind = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (itemTeachersBean != null && itemTeachersBean.getTeachers() != null) {
            teachers = itemTeachersBean.getTeachers();
            mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
            mViewBinding.tvEventName.setText(TextUtils.isEmpty(teachers.getThingName()) ? teachers.getName() : teachers.getThingName());
            mViewBinding.tvAttendanceRate.setText(teachers.getRate());
            if (!TextUtils.isEmpty(teachers.getRate())) {
                try {
                    mViewBinding.progress.setProgress(Double.valueOf(teachers.getRate()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mViewBinding.tvLateNum.setText(teachers.getLate() + "");
//            mViewBinding.tvLateName.setText("1".equals(teachers.getGoOutStatus()) ? "早退" : "迟到");
//            mViewBinding.tvLate.setText("1".equals(teachers.getGoOutStatus()) ? "早退" : "迟到");
//            mViewBinding.tvAttendanceDesc.setText("1".equals(teachers.getGoOutStatus()) ? "签退率" : "签到率");
            mViewBinding.tvLeaveNum.setText(teachers.getLeave() + "");
            mViewBinding.tvAbsenteeismNum.setText(teachers.getAbsence() + "");
            mViewBinding.tvNum.setText((teachers.getAbsencePeople() != null ? teachers.getAbsencePeople().size() : 0) + "人");
            dataToBind.addAll(convertDataToTreeNode(teachers.getAbsencePeople()));
            adapter = new AttendanceSchoolTeacherAdapter(R.layout.item_attendance_school_teacher, dataToBind, teachers.goOutStatus);
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

    private List<TreeNode<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean>> convertDataToTreeNode(List<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean> datas) {
        List<TreeNode<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean>> nodes = new ArrayList<>();
        Map<String, TreeNode<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean>> map = new HashMap();
        for (int i = 0; i < datas.size(); i++) {
            List<TreeNode<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean>> childs = new ArrayList<>();
            AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean peopleBean = datas.get(i);
            TreeNode treeNode = new TreeNode(peopleBean, -1);
            for (AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean childItem : peopleBean.getSpecialPeople()) {
                TreeNode child = new TreeNode<>(childItem, -1);
                child.setParent(treeNode);
                childs.add(child);
            }
            treeNode.setChildren(childs);
            nodes.add(treeNode);
            map.put(i + "", treeNode);
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
                adapter.setList(teachers != null ? convertDataToTreeNode(teachers.getAbsencePeople()) : null);
                mViewBinding.tvNum.setText((teachers != null ? teachers.getAbsencePeople().size() : 0) + "人");
                break;
            case R.id.tv_leave:
                mViewBinding.tvLeave.setChecked(true);
                mViewBinding.tvLeave.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teachers != null ? convertDataToTreeNode(teachers.getLeavePeople()) : null);
                mViewBinding.tvNum.setText((teachers != null ? teachers.getLeavePeople().size() : 0) + "人");
                break;
            case R.id.tv_late:
                mViewBinding.tvLate.setChecked(true);
                mViewBinding.tvLate.setTextColor(getResources().getColor(R.color.white));
                adapter.setList(teachers != null ? convertDataToTreeNode(teachers.getLatePeople()) : null);
                mViewBinding.tvNum.setText((teachers != null ? teachers.getLatePeople().size() : 0) + "人");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }

}
