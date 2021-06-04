package com.yyide.chatim.activity.attendance.fragment;

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
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentSchoolMasterAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * desc 校长考情学生
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolStudentAttendanceFragment extends BaseMvpFragment<AttendanceCheckPresenter> implements AttendanceCheckView {

    private FragmentSchoolMasterAttendanceBinding mViewBinding;
    private String TAG = SchoolStudentAttendanceFragment.class.getSimpleName();

    public static SchoolStudentAttendanceFragment newInstance(int index) {
        SchoolStudentAttendanceFragment fragment = new SchoolStudentAttendanceFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
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
        mViewBinding = FragmentSchoolMasterAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding.clContent.setVisibility(View.GONE);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.recyclerview.setAdapter(adapter);
        mvpPresenter.attendance("");
    }

    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    private void setDataView(AttendanceCheckRsp.DataBean item) {
        if (item.getSchoolPeopleAllForm() != null && item.getSchoolPeopleAllForm().size() > 0) {
            AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean itemStudents = item.getSchoolPeopleAllForm().get(0);

            if (itemStudents != null) {
                mViewBinding.clContent.setVisibility(View.VISIBLE);
                mViewBinding.tvAttendanceTitle.setText(itemStudents.getAttName());
                mViewBinding.tvEventName.setText(itemStudents.getAttName());
                mViewBinding.tvAttendanceRate.setText(itemStudents.getRate());
                if (!TextUtils.isEmpty(itemStudents.getRate())) {
                    try {
                        mViewBinding.progress.setProgress(Double.valueOf(itemStudents.getRate()).intValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mViewBinding.tvNumber.setText("(" + itemStudents.getNumber() + "人)");
                mViewBinding.tvLateNum.setText(itemStudents.getLate() + "");
                mViewBinding.tvLeaveNum.setText(itemStudents.getLeave() + "");
                mViewBinding.tvAbsenteeismNum.setText(itemStudents.getAbsence() + "");
                adapter.setList(itemStudents.getGradeList());

                List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> schoolPeopleAllForm = item.getSchoolPeopleAllForm();
                mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
                    AttendancePop attendancePop = new AttendancePop(getActivity(), new WheelAdapter() {
                        @Override
                        public int getItemsCount() {
                            return schoolPeopleAllForm.size();
                        }

                        @Override
                        public Object getItem(int index) {
                            return schoolPeopleAllForm.get(index).getAttName();
                        }

                        @Override
                        public int indexOf(Object o) {
                            return schoolPeopleAllForm.indexOf(o);
                        }
                    }, "");
                    attendancePop.setOnSelectListener(index -> {
                        mViewBinding.tvAttendanceTitle.setText(schoolPeopleAllForm.get(index).getAttName());
                        adapter.setList(schoolPeopleAllForm.get(index).getGradeList());
                    });
                });

            }
        }
    }

    private BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean, BaseViewHolder>(R.layout.item_school) {
        @Override
        protected void convert(@NotNull BaseViewHolder holder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean.GradeListBean item) {
            holder.setText(R.id.tv_event_name, item.getName())
                    .setText(R.id.tv_attendance_rate, item.getRate())
                    .setText(R.id.tv_normal_num, item.getApplyNum() + "")
                    .setText(R.id.tv_absence_num, item.getAbsence() + "")
                    .setText(R.id.tv_ask_for_leave_num, item.getLeave() + "");
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

}