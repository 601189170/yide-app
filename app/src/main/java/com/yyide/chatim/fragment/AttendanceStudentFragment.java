package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.indicator.DrawableIndicator;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.FragmentAttendanceStudentBinding;
import com.yyide.chatim.home.BannerStudentAttendanceAdapter;
import com.yyide.chatim.home.BannerTeacherAttendanceAdapter;
import com.yyide.chatim.model.AttendanceStudentRsp;
import com.yyide.chatim.model.AttendanceTeacherRsp;
import com.yyide.chatim.presenter.AttendanceStudentPresenter;
import com.yyide.chatim.utils.LogUtil;
import com.yyide.chatim.view.AttendanceStudentView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 13:18
 * @Description : 文件描述
 */
public class AttendanceStudentFragment extends BaseMvpFragment<AttendanceStudentPresenter> implements AttendanceStudentView {

    private FragmentAttendanceStudentBinding mViewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = FragmentAttendanceStudentBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        mViewBinding.attendanceBanner.addBannerLifecycleObserver(this);
        DrawableIndicator indicator = new DrawableIndicator(getContext(), R.mipmap.icon_banner_normal_white50, R.mipmap.icon_banner_select_white);
        //获取前一天的时间
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String format = formatter.format(calendar.getTime());
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().staffIdentity()) {
            mvpPresenter.getAttendTeacherBanner(format, format);
        } else {
            mvpPresenter.getAttendStudentBanner(format, format);
        }
        mViewBinding.attendanceBanner.setIndicator(indicator);
        mViewBinding.attendanceBanner.isAutoLoop(true);
        mViewBinding.attendanceBanner.setLoopTime(3000);
        mViewBinding.attendanceBanner.start();
    }

    @Override
    protected AttendanceStudentPresenter createPresenter() {
        return new AttendanceStudentPresenter(this);
    }

    @Override
    public void getAttendanceFail(String rsp) {
        mViewBinding.bannerConstrain.setVisibility(View.GONE);
    }

    @Override
    public void getStudentAttendanceSuccess(List<AttendanceStudentRsp.AttendanceAdapterBean> rsp) {
        if (!rsp.isEmpty()) {
            mViewBinding.bannerConstrain.setVisibility(View.VISIBLE);
            mViewBinding.attendanceBanner.setAdapter(new BannerStudentAttendanceAdapter(rsp));
        } else {
            mViewBinding.bannerConstrain.setVisibility(View.GONE);

        }

    }

    @Override
    public void getTeacherAttendanceSuccess(List<AttendanceTeacherRsp.AttendanceTeacherAdapterBean> rsp) {
        if (rsp.isEmpty()) {
            mViewBinding.bannerConstrain.setVisibility(View.VISIBLE);
            mViewBinding.attendanceBanner.setAdapter(new BannerTeacherAttendanceAdapter(rsp));
        } else {
            mViewBinding.bannerConstrain.setVisibility(View.GONE);
        }


    }
}
