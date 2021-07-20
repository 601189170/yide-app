package com.yyide.chatim.fragment;

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

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.activity.attendance.StatisticsActivity;
import com.yyide.chatim.adapter.AttendanceAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.AttendancePresenter;
import com.yyide.chatim.view.AttendanceCheckView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isSchool = false;

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
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PRESIDENT.equals(SpData.getIdentityInfo().status)) {//校长
            isSchool = true;
            mvpPresenter.homeAttendance("");
        } else {
            isSchool = true;
            mvpPresenter.homeAttendance(SpData.getClassInfo() != null ? SpData.getClassInfo().classesId : "");
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
        startActivity(new Intent(getContext(), StatisticsActivity.class));
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
    public void getAttendanceSuccess(AttendanceCheckRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null) {
                setData(model.getData());
            }
        }
    }

    private void setData(AttendanceCheckRsp.DataBean dataBean) {
        List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> schoolPeopleAllFormBeanList = new ArrayList<>();
        if (dataBean.getAttendancesForm() != null && dataBean.getAttendancesForm().size() > 0) {
            for (AttendanceCheckRsp.DataBean.AttendancesFormBean itemBean : dataBean.getAttendancesForm()) {
                AttendanceCheckRsp.DataBean.AttendancesFormBean.Students item = itemBean.getStudents();
                if (itemBean.getStudents() != null) {
                    AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean schoolBean = new AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean();
                    schoolBean.setStudents(item);
                    schoolPeopleAllFormBeanList.add(schoolBean);
                }
            }
        }
        announRoll.setAdapter(announAdapter);
        constraintLayout.setVisibility((schoolPeopleAllFormBeanList != null && schoolPeopleAllFormBeanList.size() > 0) ? View.GONE : View.VISIBLE);
        announAdapter.notifyData(dataBean.getAttendancesForm());
        indexAdapter.setList(schoolPeopleAllFormBeanList);
    }

    @Override
    public void getAttendanceFail(String msg) {
        Log.e("TAG", "getAttendanceFail==>: " + msg);
    }
}