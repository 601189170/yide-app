package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.ClassesHomeworkAnnounAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.WorkPresenter;
import com.yyide.chatim.view.WorkView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class WorkFragment extends BaseMvpFragment<WorkPresenter> implements WorkView {

    @BindView(R.id.rollPagerView)
    RollPagerView rollPagerView;
    @BindView(R.id.recyclerview)
    RecyclerView mHot;
    private View mBaseView;
    ClassesHomeworkAnnounAdapter announAdapter;
    IndexAdapter indexAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_work_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        getData();
        initView();
    }

    private void getData() {
        if (SpData.getClassInfo() != null) {
            mvpPresenter.getWorkInfo(SpData.getClassInfo().classesId);
        }
    }

    private void initView() {
        indexAdapter = new IndexAdapter();
        rollPagerView.setHintView(null);

        announAdapter = new ClassesHomeworkAnnounAdapter(rollPagerView);
        rollPagerView.setPlayDelay(5000);
        rollPagerView.setAdapter(announAdapter);
        mHot.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mHot.setAdapter(indexAdapter);
        ViewPager viewPager = rollPagerView.getViewPager();
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

    @Override
    protected WorkPresenter createPresenter() {
        return new WorkPresenter(this);
    }

    @Override
    public void getWorkSuccess(SelectSchByTeaidRsp model) {
        if (model.code == BaseConstant.REQUEST_SUCCES2 && model != null && model.data != null) {
            if (model.data.size() > 0) {
                List<SelectSchByTeaidRsp.DataBean> dataBeanList = new ArrayList<>();
                for (SelectSchByTeaidRsp.DataBean item : model.data) {
                    dataBeanList.add(item);
                }
                announAdapter.notifyData(dataBeanList);
                indexAdapter.setList(dataBeanList);
            } else {
                announAdapter.notifyData(null);
                indexAdapter.setList(null);
            }

        }
    }

    @Override
    public void getWorkFail(String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", WorkFragment.class.getSimpleName());
            getData();
        } else if (BaseConstant.TYPE_PREPARES_SAVE.equals(messageEvent.getCode())) {
            getData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
