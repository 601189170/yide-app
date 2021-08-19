package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.adapter.WheelAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentSchoolAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import java.util.ArrayList;
import java.util.List;

/**
 * desc 校长考情学生
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolAttendanceFragment extends BaseMvpFragment<AttendanceCheckPresenter> implements AttendanceCheckView {

    private FragmentSchoolAttendanceBinding mViewBinding;
    private String TAG = SchoolAttendanceFragment.class.getSimpleName();
    private int index;

    public static SchoolAttendanceFragment newInstance(int index) {
        SchoolAttendanceFragment fragment = new SchoolAttendanceFragment();
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
        mViewBinding = FragmentSchoolAttendanceBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.attendance("");
    }

    private void initView(AttendanceCheckRsp.DataBean item) {
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mvpPresenter.attendance("");
        });
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean = new AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean();
        if (item.schoolPeopleAllForm != null) {
            if (item.schoolPeopleAllForm.size() > index) {
                schoolPeopleAllFormBean = item.schoolPeopleAllForm.get(index);
            } else {
                schoolPeopleAllFormBean = item.schoolPeopleAllForm.get(0);
            }
        }

        if ("N".equals(schoolPeopleAllFormBean.getPeopleType())) {
            startFragment(schoolPeopleAllFormBean);
        } else {
            int position = 0;
            if (item.attendancesForm != null) {
               position = index - item.attendancesForm.size();
            }
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolStudentAttendanceFragment.newInstance(schoolPeopleAllFormBean, position)).commit();
        }
        mViewBinding.tvAttendanceTitle.setText(schoolPeopleAllFormBean.attName);
        if (SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1) {
            mViewBinding.tvClassName.setClickable(true);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvClassName.setClickable(false);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, null, null);
        }

        if (item.schoolPeopleAllForm != null && item.schoolPeopleAllForm.size() > 1) {
            mViewBinding.tvAttendanceTitle.setClickable(true);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvAttendanceTitle.setClickable(false);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
            AttendancePop attendancePop = new AttendancePop(getActivity(), adapterEvent, "请选择考勤事件");
            adapterEvent.setList(item.schoolPeopleAllForm);
            attendancePop.setOnSelectListener(index -> {
                this.index = index;
                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean bean = item.schoolPeopleAllForm.get(index);
                mViewBinding.tvAttendanceTitle.setText(bean.attName);
                if ("N".equals(bean.getPeopleType())) {
                    startFragment(bean);
                } else {
                    int position = 0;
                    if (item.attendancesForm != null) {
                        position = index - item.attendancesForm.size();
                    }
                    getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolStudentAttendanceFragment.newInstance(bean, position)).commit();
                }
            });
        });
    }

    private final BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean, BaseViewHolder> adapterEvent = new BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item) {
            baseViewHolder.setText(R.id.className, item.getAttName());
            baseViewHolder.getView(R.id.select).setVisibility(index == baseViewHolder.getAdapterPosition() ? View.VISIBLE : View.GONE);
            if (adapterEvent.getItemCount() - 1 == baseViewHolder.getAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        }
    };

    private void startFragment(AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean bean) {
        SchoolTeacherAttendanceFragment teacherAttendanceFragment = SchoolTeacherAttendanceFragment.newInstance(bean);
        SchoolEventTeacherAttendanceFragment eventTeacherAttendanceFragment = SchoolEventTeacherAttendanceFragment.newInstance(bean);
        teacherAttendanceFragment.setOnRefreshListener(isRefresh -> {
            mViewBinding.swipeRefreshLayout.setEnabled(isRefresh);
        });

        eventTeacherAttendanceFragment.setOnRefreshListener(isRefresh -> {
            mViewBinding.swipeRefreshLayout.setEnabled(isRefresh);
        });
        if ("Y".equals(bean.getIdentityType())) {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), teacherAttendanceFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), eventTeacherAttendanceFragment).commit();
        }
    }

    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            AttendanceCheckRsp.DataBean data = model.getData();
            if (data != null) {
                setListData(data);
            }
        }
    }

    private void setListData(AttendanceCheckRsp.DataBean dataBean) {
        List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> schoolPeopleAllFormBeanList = new ArrayList<>();
        if (dataBean.getAttendancesForm() != null && dataBean.getAttendancesForm().size() > 0) {
            for (AttendanceCheckRsp.DataBean.AttendancesFormBean item : dataBean.getAttendancesForm()) {
                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolBean = new AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean();
                AttendanceCheckRsp.DataBean.AttendancesFormBean.TeachersBean teachers = item.getTeachers();
                if (teachers != null) {
                    schoolBean.setAbsence(teachers.getAbsence());
                    schoolBean.setApplyNum(teachers.getApplyNum());
                    schoolBean.setAttName(teachers.getName());
                    schoolBean.setLate(teachers.getLate());
                    schoolBean.setLeave(teachers.getLeave());
                    schoolBean.setNumber(teachers.getNumber());
                    schoolBean.setThingName(teachers.getThingName());
                    schoolBean.setRequiredTime(teachers.getRequiredTime());
                    schoolBean.setIdentityType(item.getIdentityType());
                    schoolBean.setRate(teachers.getRate());
                    schoolBean.setPeopleType(item.getPeopleType());
                    schoolBean.setTeachers(teachers);
                }
                schoolPeopleAllFormBeanList.add(schoolBean);
            }
        }
        if (dataBean.getSchoolPeopleAllForm() != null) {
            schoolPeopleAllFormBeanList.addAll(schoolPeopleAllFormBeanList.size(), dataBean.getSchoolPeopleAllForm());
        }
        dataBean.schoolPeopleAllForm = schoolPeopleAllFormBeanList;
        initView(dataBean);
    }

    @Override
    public void getAttendanceFail(String msg) {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getHomeAttendanceFail-->>" + msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }
}