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
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentSchoolAttendanceBinding;
import com.yyide.chatim.databinding.FragmentSchoolMasterAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.presenter.AttendanceCheckPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import org.jetbrains.annotations.NotNull;

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
        AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolPeopleAllFormBean = new AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean();
        if (item.schoolPeopleAllForm != null) {
            if (item.schoolPeopleAllForm.size() > index) {
                schoolPeopleAllFormBean = item.schoolPeopleAllForm.get(index);
            } else {
                schoolPeopleAllFormBean = item.schoolPeopleAllForm.get(0);
            }
        }

        if ("N".equals(schoolPeopleAllFormBean.getPeopleType())) {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolTeacherAttendanceFragment.newInstance(schoolPeopleAllFormBean)).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolStudentAttendanceFragment.newInstance(schoolPeopleAllFormBean)).commit();
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
            AttendancePop attendancePop = new AttendancePop(getActivity(), new WheelAdapter() {
                @Override
                public int getItemsCount() {
                    return item.schoolPeopleAllForm.size();
                }

                @Override
                public Object getItem(int index) {
                    return item.schoolPeopleAllForm.get(index).getAttName();
                }

                @Override
                public int indexOf(Object o) {
                    return item.schoolPeopleAllForm.indexOf(o);
                }
            }, "");

            attendancePop.setOnSelectListener(index -> {
                AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean bean = item.schoolPeopleAllForm.get(index);
                mViewBinding.tvAttendanceTitle.setText(bean.attName);
                if ("N".equals(bean.getPeopleType())) {
                    getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolTeacherAttendanceFragment.newInstance(bean)).commit();
                } else {
                    getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolStudentAttendanceFragment.newInstance(bean)).commit();
                }
            });
        });
    }

    @Override
    protected AttendanceCheckPresenter createPresenter() {
        return new AttendanceCheckPresenter(this);
    }

    @Override
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
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
        Log.d(TAG, "getHomeAttendanceFail-->>" + msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding = null;
    }
}