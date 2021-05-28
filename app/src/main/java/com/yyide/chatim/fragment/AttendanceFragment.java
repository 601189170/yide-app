package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.AttendanceAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.presenter.AttendancePresenter;
import com.yyide.chatim.view.AttendanceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;


public class AttendanceFragment extends BaseMvpFragment<AttendancePresenter> implements AttendanceView {

    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    RecyclerView mHot;
    @BindView(R.id.tv_data)
    TextView tv_data;
    private View mBaseView;
    AttendanceAdapter announAdapter;
    IndexAdapter indexAdapter;
    public String TAG = AttendanceFragment.class.getSimpleName();
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
        if (SpData.getClassInfo() != null) {
            mvpPresenter.homeAttendance(SpData.getClassInfo().classesId, "", "");
        }
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    private void initView() {
        indexAdapter = new IndexAdapter();
        announAdapter = new AttendanceAdapter(announRoll);
        announRoll.setHintView(null);
        announRoll.setPlayDelay(5000);
        announRoll.setAdapter(announAdapter);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", AttendanceFragment.class.getSimpleName());
            getHomeAttendance();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getHomeAttendanceListSuccess(HomeAttendanceRsp model) {
        if(BaseConstant.REQUEST_SUCCES2 == model.getCode()){
            if(model.getData() != null && model.getData().size() > 0){
                tv_data.setVisibility(View.GONE);
                announAdapter.notifyData(model.getData());
                indexAdapter.setList(model.getData());
            } else {
                tv_data.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getHomeAttendanceFail(String msg) {
        Log.d(TAG, "getHomeAttendanceFail-->>" + msg);
    }
}
