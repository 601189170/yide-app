package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.ClassAnnounAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ClassesPhotoBannerRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.HomeBannerPresenter;
import com.yyide.chatim.view.HomeBannerView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


public class BannerFragment extends BaseMvpFragment<HomeBannerPresenter> implements HomeBannerView {

    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    RecyclerView mHot;
    private View mBaseView;
    ClassAnnounAdapter announAdapter;
    IndexAdapter indexAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_banner_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
//        mvpPresenter.getMyData();
        indexAdapter = new IndexAdapter();
        announAdapter = new ClassAnnounAdapter(announRoll);
        announRoll.setHintView(null);
//        announAdapter.notifyData(dataBeans);
//        indexAdapter.setList(dataBeans);

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
//        if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().classesId)) {
//            mvpPresenter.getClassPhotoList(SpData.getClassInfo().classesId, SpData.getIdentityInfo().schoolId);
//        }
        //initAdapter();
    }

    @Override
    protected HomeBannerPresenter createPresenter() {
        return new HomeBannerPresenter(this);
    }


    @Override
    public void getClassBannerListSuccess(ClassesPhotoBannerRsp model) {
        if (model != null && model.getData() != null) {
            if (model.getData().size() >= 5) {
                List<ClassesPhotoBannerRsp.DataBean> dataBeans = model.getData().subList(0, 5);
                announAdapter.notifyData(dataBeans);
                indexAdapter.setList(dataBeans);
            } else {
                announAdapter.notifyData(model.getData());
                indexAdapter.setList(model.getData());
            }
        }
    }

    @Override
    public void getClassBannerListFail(String msg) {
        Log.d("getClassBannerListFail", msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", BannerFragment.class.getSimpleName());

            //        if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().classesId)) {
            mvpPresenter.getClassPhotoList(SpData.getClassInfo().classesId, SpData.getIdentityInfo().schoolId);
//        }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
