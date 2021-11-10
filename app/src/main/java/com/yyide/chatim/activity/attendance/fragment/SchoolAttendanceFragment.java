package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentSchoolAttendanceBinding;
import com.yyide.chatim.dialog.AttendancePop;
import com.yyide.chatim.model.AttendanceRsp;
import com.yyide.chatim.presenter.AttendanceTwoPresenter;
import com.yyide.chatim.view.AttendanceCheckView;

/**
 * desc 校长考情学生
 * time 2021年5月31日15:52:14
 * other lrz
 */
public class SchoolAttendanceFragment extends BaseMvpFragment<AttendanceTwoPresenter> implements AttendanceCheckView {

    private FragmentSchoolAttendanceBinding mViewBinding;
    private String TAG = SchoolAttendanceFragment.class.getSimpleName();
    private AttendanceRsp.DataBean.AttendanceListBean attendanceRequestBean = new AttendanceRsp.DataBean.AttendanceListBean();

    public static SchoolAttendanceFragment newInstance(AttendanceRsp.DataBean.AttendanceListBean item) {
        SchoolAttendanceFragment fragment = new SchoolAttendanceFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attendanceRequestBean = (AttendanceRsp.DataBean.AttendanceListBean) getArguments().getSerializable("item");
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
        request();
    }

    private void initView(AttendanceRsp.DataBean item) {
        mViewBinding.swipeRefreshLayout.setOnRefreshListener(this::request);
        mViewBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        //1 学生 2 教职工
        if ("2".equals(attendanceRequestBean.getPeopleType())) {
            startFragment(attendanceRequestBean, item);
        } else {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), SchoolStudentAttendanceFragment.newInstance(item, attendanceRequestBean)).commit();
        }
        mViewBinding.tvAttendanceTitle.setText(attendanceRequestBean.getTheme());
        if (SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1) {
            mViewBinding.tvClassName.setClickable(true);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvClassName.setClickable(false);
            mViewBinding.tvClassName.setCompoundDrawables(null, null, null, null);
        }

        if (item.getHeadmasterAttendanceList() != null && item.getHeadmasterAttendanceList().size() > 1) {
            mViewBinding.tvAttendanceTitle.setClickable(true);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else {
            mViewBinding.tvAttendanceTitle.setClickable(false);
            mViewBinding.tvAttendanceTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        mViewBinding.tvAttendanceTitle.setOnClickListener(v -> {
            AttendancePop attendancePop = new AttendancePop(getActivity(), adapterEvent, "请选择考勤事件");
            adapterEvent.setList(item.getHeadmasterAttendanceList());
            attendancePop.setOnSelectListener(index -> {
                attendanceRequestBean = item.getHeadmasterAttendanceList().get(index);
                mViewBinding.tvAttendanceTitle.setText(attendanceRequestBean.getTheme());
                request();
            });
        });
    }

    private void request() {
        if (attendanceRequestBean == null) {
            attendanceRequestBean = new AttendanceRsp.DataBean.AttendanceListBean();
        }
        mvpPresenter.attendance(attendanceRequestBean);
    }

    private final BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder> adapterEvent = new BaseQuickAdapter<AttendanceRsp.DataBean.AttendanceListBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, AttendanceRsp.DataBean.AttendanceListBean item) {
            baseViewHolder.setText(R.id.className, item.getTheme());
            if (attendanceRequestBean != null
                    && !TextUtils.isEmpty(attendanceRequestBean.getType())
                    && attendanceRequestBean.getType().equals(item.getType())
                    && !TextUtils.isEmpty(attendanceRequestBean.getTheme())
                    && attendanceRequestBean.getTheme().equals(item.getTheme())) {
                baseViewHolder.getView(R.id.select).setVisibility(View.VISIBLE);
            } else {
                baseViewHolder.getView(R.id.select).setVisibility(View.GONE);
            }
            if (adapterEvent.getItemCount() - 1 == baseViewHolder.getAbsoluteAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
        }
    };

    private void startFragment(AttendanceRsp.DataBean.AttendanceListBean bean, AttendanceRsp.DataBean dataBean) {
        SchoolTeacherAttendanceFragment teacherAttendanceFragment = SchoolTeacherAttendanceFragment.newInstance(dataBean);
        SchoolEventTeacherAttendanceFragment eventTeacherAttendanceFragment = SchoolEventTeacherAttendanceFragment.newInstance(dataBean);
        teacherAttendanceFragment.setOnRefreshListener(isRefresh -> {
            mViewBinding.swipeRefreshLayout.setEnabled(isRefresh);
        });

        eventTeacherAttendanceFragment.setOnRefreshListener(isRefresh -> {
            mViewBinding.swipeRefreshLayout.setEnabled(isRefresh);
        });
        if (attendanceRequestBean != null && "2".equals(bean.getType())) {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), teacherAttendanceFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(mViewBinding.flContent.getId(), eventTeacherAttendanceFragment).commit();
        }
    }

    @Override
    protected AttendanceTwoPresenter createPresenter() {
        return new AttendanceTwoPresenter(this);
    }

    @Override
    public void getAttendanceSuccess(AttendanceRsp model) {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            AttendanceRsp.DataBean data = model.getData();
            if (data != null) {
                initView(data);
            }
        }
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