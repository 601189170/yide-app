package com.yyide.chatim.activity.attendance.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentFamilyHeadBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * desc 家长视角查看考勤
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class FamilyStudentAttendanceFragment extends BaseMvpFragment<AttendanceCheckPresenter> implements AttendanceCheckView, View.OnClickListener {

    private FragmentFamilyHeadBinding mViewBinding;
    private String TAG = FamilyStudentAttendanceFragment.class.getSimpleName();

    public static FamilyStudentAttendanceFragment newInstance() {
        FamilyStudentAttendanceFragment fragment = new FamilyStudentAttendanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentFamilyHeadBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        mvpPresenter.attendance(SpData.getClassInfo() != null ? SpData.getClassInfo().classesId : "");
    }

    private void initView() {
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewBinding.recyclerview.setAdapter(adapter);
    }

    private void setDataView(AttendanceCheckRsp.DataBean item) {
        List<AttendanceCheckRsp.DataBean.AttendancesFormBean> attendancesForm = item.getAttendancesForm();
        adapter.setList(attendancesForm);
        if (attendancesForm != null && attendancesForm.size() > 0) {
            AttendanceCheckRsp.DataBean.AttendancesFormBean.Students students = attendancesForm.get(0).getStudents();
            mViewBinding.tvAttendanceType.setText(students.getName());
        }
        mViewBinding.tvClassName.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : "");

//        mViewBinding.tvAttendanceType.setOnClickListener(v -> {
//            AttendancePop attendancePop = new AttendancePop(getActivity(), new WheelAdapter() {
//                @Override
//                public int getItemsCount() {
//                    return attendancesForm.size();
//                }
//
//                @Override
//                public Object getItem(int index) {
//                    return attendancesForm.get(index).getStudents().getName();
//                }
//
//                @Override
//                public int indexOf(Object o) {
//                    return attendancesForm.indexOf(o);
//                }
//            });
//            attendancePop.setOnSelectListener(index -> {
//                //itemStudents = item.getAttendancesForm().get(index).getStudents();
//            });
//        });
    }

    BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.AttendancesFormBean, BaseViewHolder>(R.layout.item_attendance_family_head) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.AttendancesFormBean item) {
            AttendanceCheckRsp.DataBean.AttendancesFormBean.Students students = item.getStudents();
            if (students != null) {
                ImageView img = holder.getView(R.id.iv_img);
                TextView leave_desc = holder.getView(R.id.tv_attendance_leave_desc);
                holder.setText(R.id.tv_attendance_card_time, getString(R.string.attendance_punch_card_text, students.getApplyDate()))
                        .setText(R.id.tv_attendance_time, getString(R.string.attendance_time_text, students.getRequiredTime()))
                        .setText(R.id.tv_event_name, students.getSection() > 0 ? students.getSubjectName() : students.getName());

                if (!TextUtils.isEmpty(students.getAttendanceType())) {
                    switch (students.getAttendanceType()) {//（0正常、1缺勤、2迟到/3早退,4无效打卡）
                        case "0":
                            leave_desc.setText("正常");
                            leave_desc.setTextColor(Color.parseColor("#5DADFF"));
                            img.setImageResource(R.drawable.icon_attendance_normal);
                            break;
                        case "1":
                            leave_desc.setText("未签退");
                            leave_desc.setTextColor(Color.parseColor("#919399"));
                            img.setImageResource(R.drawable.icon_attendance_no_sign_in);
                            break;
                        case "2":
                            leave_desc.setText("迟到");
                            leave_desc.setTextColor(Color.parseColor("#F66C6C"));
                            img.setImageResource(R.drawable.icon_attendance_late);
                            break;
                        case "3":
                            leave_desc.setText("早退");
                            leave_desc.setTextColor(Color.parseColor("#63DAAB"));
                            img.setImageResource(R.drawable.icon_attendance_leave_early);
                            break;
                        case "4":
                            leave_desc.setText("无效打卡");
                            leave_desc.setTextColor(Color.parseColor("#F6BD16"));
                            img.setImageResource(R.drawable.icon_attendance_ask_for_leave);
                            break;
                    }
                }
            }
        }
    };

    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

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
}