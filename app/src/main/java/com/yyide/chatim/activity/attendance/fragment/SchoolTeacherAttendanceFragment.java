package com.yyide.chatim.activity.attendance.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentSchoolTeacherAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * desc 教师教职工考情详情
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolTeacherAttendanceFragment extends BaseMvpFragment<AttendanceCheckPresenter> implements AttendanceCheckView, View.OnClickListener {
    private AttendanceCheckRsp.DataBean.AttendancesFormBean.TeachersBean itemStudents;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private FragmentSchoolTeacherAttendanceBinding mViewBinding;
    private int index;

    public static SchoolTeacherAttendanceFragment newInstance(int index) {
        SchoolTeacherAttendanceFragment fragment = new SchoolTeacherAttendanceFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
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
        getAttendance();
    }

    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    private void getAttendance() {
        mvpPresenter.attendance(SpData.getClassInfo() != null ? SpData.getClassInfo().classesId : "");
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
                adapter.setList(itemStudents != null ? itemStudents.getApplyPeople() : null);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataView(AttendanceCheckRsp.DataBean item) {
        if (item.getAttendancesForm() != null && item.getAttendancesForm().size() > 0) {
            index = item.getSchoolPeopleAllForm() != null ? index - item.getSchoolPeopleAllForm().size() : 0;
            if (index > item.getAttendancesForm().size()) {
                index = 0;
            }
            itemStudents = item.getAttendancesForm().get(index).getTeachers();
        }
        mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
            AttendancePop attendancePop = new AttendancePop(getActivity(), new WheelAdapter() {
                @Override
                public int getItemsCount() {
                    return item.getAttendancesForm().size();
                }

                @Override
                public Object getItem(int index) {
                    return item.getAttendancesForm().get(index).getAttNameA();
                }

                @Override
                public int indexOf(Object o) {
                    return item.getAttendancesForm().indexOf(o);
                }
            }, "");
            attendancePop.setOnSelectListener(index -> {
                itemStudents = item.getAttendancesForm().get(index).getTeachers();
                setData();
            });
        });

        if (SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1) {
            mViewBinding.tvClassName.setClickable(true);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvClassName.setClickable(false);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, null, null);
        }

        if (item.getAttendancesForm() != null && item.getAttendancesForm().size() > 1) {
            mViewBinding.tvAttendanceTitle.setClickable(true);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvAttendanceTitle.setClickable(false);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (SpData.getIdentityInfo() != null) {
            switch (SpData.getIdentityInfo().status) {
                case GetUserSchoolRsp.DataBean.TYPE_PRESIDENT://校长
                    break;
                case GetUserSchoolRsp.DataBean.TYPE_CLASS_TEACHER://班主任
                case GetUserSchoolRsp.DataBean.TYPE_TEACHER://教师-老师
                case GetUserSchoolRsp.DataBean.TYPE_PARENTS://家长-监护人
                    break;
            }
        }

        if (itemStudents != null) {
            mViewBinding.constraintLayout.setVisibility(View.VISIBLE);
            mViewBinding.tvAttendanceTitle.setText(itemStudents.getName());
            mViewBinding.tvDesc.setText(itemStudents.getName());
            mViewBinding.tvAttendanceTime.setText("考勤时间 " + (!TextUtils.isEmpty(itemStudents.getRequiredTime()) ? itemStudents.getRequiredTime() : ""));
            mViewBinding.tvAttendanceRate.setText(itemStudents.getRate());
            if (!TextUtils.isEmpty(itemStudents.getRate())) {
                try {
                    mViewBinding.progress.setProgress(Double.valueOf(itemStudents.getRate()).intValue());
                } catch (Exception e) {
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

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean, BaseViewHolder>(R.layout.item_attendance_student) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.AttendancesFormBean.Students.PeopleBean item) {
            holder.setText(R.id.tv_student_name, item.getName())
                    .setText(R.id.tv_student_time, item.getTime())
                    .setText(R.id.tv_status, item.getStatusType());
        }
    };

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            AttendanceCheckRsp.DataBean data = model.getData();
            if (data != null) {
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
