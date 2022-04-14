package com.yyide.chatim_pro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.activity.attendance.StatisticsActivity;
import com.yyide.chatim_pro.adapter.AttendanceAdapter;
import com.yyide.chatim_pro.adapter.IndexAdapter;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.base.BaseMvpFragment;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.model.EventMessage;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.presenter.AttendancePresenter;
import com.yyide.chatim_pro.view.AttendanceCheckView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class AttendanceTeacherFragment extends BaseMvpFragment<AttendancePresenter> implements AttendanceCheckView {

    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    RecyclerView mHot;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    private View mBaseView;
    AttendanceAdapter announAdapter;
    IndexAdapter indexAdapter;
    public String TAG = AttendanceTeacherFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_attence_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        getHomeAttendance();
    }

    private void getHomeAttendance() {
        if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {//校长
            mvpPresenter.homeAttendance("", "");
        } else {
            mvpPresenter.homeAttendance(SpData.getClassInfo() != null ? SpData.getClassInfo().classesId : "", "");
        }
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    private void initView() {
        indexAdapter = new IndexAdapter();
        announAdapter = new AttendanceAdapter(announRoll);
        //处理是否为校长身份
        announRoll.setHintView(null);
        announRoll.setPlayDelay(5000);

        mHot.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mHot.setAdapter(indexAdapter);
        ViewPager viewPager = announRoll.getViewPager();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int dex = position % announAdapter.list.size();
                //Log.e("TAG", "onPageSelected==>: " + dex);
                indexAdapter.setIndex(dex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.constraintLayout})
    public void click(View v) {
//        AttendanceActivity.start(getActivity(), 0);
        if (SpData.getClassInfo() != null) {
            startActivity(new Intent(getContext(), StatisticsActivity.class));
        } else {
            ToastUtils.showShort("名下无班级考勤");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", AttendanceTeacherFragment.class.getSimpleName());
            announAdapter.notifyData(null);
            getHomeAttendance();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getAttendanceSuccess(AttendanceRsp model) {
        if (BaseConstant.REQUEST_SUCCESS == model.getCode()) {
            if (model.getData() != null) {
                setData(model.getData());
            } else {
                announAdapter.notifyData(new ArrayList<>());
                indexAdapter.setList(new ArrayList<>());
            }
        }
    }

    private void setData(AttendanceRsp.DataBean dataBean) {
        announRoll.setAdapter(announAdapter);
        constraintLayout.setVisibility(dataBean.getClassroomTeacherAttendanceList().size() > 0 ? View.GONE : View.VISIBLE);
        announAdapter.notifyData(dataBean.getClassroomTeacherAttendanceList());
        if (dataBean.getClassroomTeacherAttendanceList() != null && dataBean.getClassroomTeacherAttendanceList().size() > 1) {
            mHot.setVisibility(View.VISIBLE);
        } else {
            mHot.setVisibility(View.GONE);
        }
        indexAdapter.setList(dataBean.getClassroomTeacherAttendanceList());
    }

    @Override
    public void getAttendanceFail(String msg) {
        announAdapter.notifyData(new ArrayList<>());
        indexAdapter.setList(new ArrayList<>());
        Log.e("TAG", "getAttendanceFail==>: " + msg);
    }
}
