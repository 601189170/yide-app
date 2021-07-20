package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceClassStudentActivity;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.databinding.FragmentSchoolMasterAttendanceBinding;
import com.yyide.chatim.model.AttendanceCheckRsp;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * desc 校长考情学生
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolStudentAttendanceFragment extends BaseFragment {

    private FragmentSchoolMasterAttendanceBinding mViewBinding;
    private String TAG = SchoolStudentAttendanceFragment.class.getSimpleName();
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean itemStudents;

    public static SchoolStudentAttendanceFragment newInstance(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean students) {
        SchoolStudentAttendanceFragment fragment = new SchoolStudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("students", students);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemStudents = (AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean) getArguments().getSerializable("students");
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
        adapter.setOnItemClickListener((adapter, view1, position) -> AttendanceClassStudentActivity.start(getContext(), itemStudents, position));
        setDataView(itemStudents);
    }

    private void setDataView(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item) {
        if (item != null) {
            if (itemStudents != null) {
                mViewBinding.clContent.setVisibility(View.VISIBLE);
                mViewBinding.tvEventName.setText(TextUtils.isEmpty(itemStudents.getThingName()) ? itemStudents.getAttName() : itemStudents.getThingName());
                mViewBinding.tvAttendanceRate.setText(itemStudents.getRate());
                if (!TextUtils.isEmpty(itemStudents.getRate())) {
                    try {
                        mViewBinding.progress.setProgress(Double.valueOf(itemStudents.getRate()).intValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mViewBinding.tvSign.setText("1".equals(itemStudents.getGoOutStatus()) ? "签退率" : "签到率");
                mViewBinding.tvAbsenceTitle.setText("1".equals(itemStudents.getGoOutStatus()) ? "未签退" : "缺勤");
                mViewBinding.tvLateNum.setText(("1".equals(itemStudents.getGoOutStatus()) ? itemStudents.getLeaveEarly() : itemStudents.getLate()) + "");
                mViewBinding.tvLeaveNum.setText(itemStudents.getLeave() + "");
                mViewBinding.tvAbsenteeismNum.setText(itemStudents.getAbsence() + "");
                mViewBinding.tvLate.setText("1".equals(itemStudents.getGoOutStatus()) ? "早退" : "迟到");
                adapter.setList(remove(itemStudents.getGradeList()));
            }
        }
    }

    //使用iterator，这个是java和Android源码中经常使用到的一种方法，所以最为推荐
    public List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> remove(List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> list) {
        Iterator<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean item = sListIterator.next();
            if (item.getNumber() == 0) {
                sListIterator.remove();
            }
            if (itemStudents != null) {
                item.setGoOutStatus(itemStudents.goOutStatus);
            }
        }
        return list;
    }


    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean item) {
            holder.setText(R.id.tv_event_name, item.getName())
                    .setText(R.id.tv_attendance_rate, item.getRate())
                    .setText(R.id.tv_normal_num, item.getApplyNum() + "")
                    .setText(R.id.tv_absence, "1".equals(item.getGoOutStatus()) ? "未签退" : "缺勤")
                    .setText(R.id.tv_sign, "1".equals(item.goOutStatus) ? "签退率" : "签到率")
                    .setText(R.id.tv_absence_num, item.getAbsence() + "")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }
}