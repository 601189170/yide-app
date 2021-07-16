package com.yyide.chatim.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityAttendanceClassStudentBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class AttendanceClassStudentActivity extends BaseActivity {

    private ActivityAttendanceClassStudentBinding viewBinding;
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean;
    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean gradeListBean;

    @Override
    public int getContentViewID() {
        return R.layout.activity_attendance_class_student;
    }

    public static void start(Context context, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item, int index) {
        Intent intent = new Intent(context, AttendanceClassStudentActivity.class);
        intent.putExtra("students", item);
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
        setDataView();
    }

    private void setDataView() {
        schoolPeopleAllFormBean = (AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean) getIntent().getSerializableExtra("students");
        int index = getIntent().getIntExtra("index", 0);
        if (schoolPeopleAllFormBean != null && schoolPeopleAllFormBean.getGradeList().size() > 0) {
            if (index < schoolPeopleAllFormBean.getGradeList().size()) {
                gradeListBean = schoolPeopleAllFormBean.getGradeList().get(index);
            } else {
                gradeListBean = schoolPeopleAllFormBean.getGradeList().get(0);
            }
            viewBinding.tvAttendanceTitle.setText(gradeListBean.getName());
            viewBinding.tvEventName.setText(gradeListBean.getName());
            viewBinding.tvAttendanceRate.setText(gradeListBean.getRate());
            if (!TextUtils.isEmpty(gradeListBean.getRate())) {
                try {
                    viewBinding.progress.setProgress(Double.valueOf(gradeListBean.getRate()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            viewBinding.tvNumber.setText("(" + gradeListBean.getNumber() + "人)");
            viewBinding.tvLateNum.setText(gradeListBean.getLate() + "");
            viewBinding.tvLeaveNum.setText(gradeListBean.getLeave() + "");
            viewBinding.tvAbsenteeismNum.setText(gradeListBean.getAbsence() + "");
            viewBinding.tvLeaveTitle.setText("1".equals(gradeListBean.goOutStatus) ? "早退" : "迟到");
            viewBinding.tvSign.setText("1".equals(gradeListBean.goOutStatus) ? "签退率" : "签到率");
            viewBinding.tvAbsenceDesc.setText("1".equals(gradeListBean.goOutStatus) ? "未签退" : "缺勤");
            adapter.setList(remove(gradeListBean.getClassForm()));

            if (schoolPeopleAllFormBean.getGradeList() != null && schoolPeopleAllFormBean.getGradeList().size() > 1) {
                viewBinding.tvAttendanceTitle.setClickable(true);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            } else {
                viewBinding.tvAttendanceTitle.setClickable(false);
                viewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            viewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                AttendancePop attendancePop = new AttendancePop(this, new WheelAdapter() {
                    @Override
                    public int getItemsCount() {
                        return schoolPeopleAllFormBean.getGradeList().size();
                    }

                    @Override
                    public Object getItem(int index) {
                        return schoolPeopleAllFormBean.getGradeList().get(index).getName();
                    }

                    @Override
                    public int indexOf(Object o) {
                        return schoolPeopleAllFormBean.getGradeList().indexOf(o);
                    }
                }, "");

                attendancePop.setOnSelectListener(position -> {
                    AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean bean = schoolPeopleAllFormBean.getGradeList().get(position);
                    viewBinding.tvAttendanceTitle.setText(bean.getName());
                    viewBinding.tvEventName.setText(bean.getName());
                    viewBinding.tvAttendanceRate.setText(bean.getRate());
                    adapter.setList(remove(schoolPeopleAllFormBean.getGradeList().get(position).getClassForm()));
                });
            });
        }
    }

    //使用iterator，这个是java和Android源码中经常使用到的一种方法，所以最为推荐
    public List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> remove(List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> list) {
        Iterator<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean item = sListIterator.next();
            if (item.getNumber() == 0) {
                sListIterator.remove();
            }
            if (gradeListBean != null) {
                item.setGoOutStatus(gradeListBean.goOutStatus);
            }
        }
        return list;
    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean.ClassFormBean item) {
            holder.setText(R.id.tv_event_name, item.getName())
                    .setText(R.id.tv_attendance_rate, item.getRate())
                    .setText(R.id.tv_sign, "1".equals(item.goOutStatus) ? "签退率" : "签到率")
                    .setText(R.id.tv_normal_num, item.getApplyNum() + "")
                    .setText(R.id.tv_absence_num, item.getAbsence() + "")
                    .setText(R.id.tv_absence, "1".equals(item.goOutStatus) ? "未签退" : "缺勤")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
        }
    };
}