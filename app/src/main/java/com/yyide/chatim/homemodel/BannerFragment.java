package com.yyide.chatim.homemodel;

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
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ClassesBannerRsp;
import com.yyide.chatim.presenter.HomeBannerPresenter;
import com.yyide.chatim.view.HomeBannerView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
//        mvpPresenter.getMyData();
        indexAdapter = new IndexAdapter();
        announRoll.setHintView(null);

        announAdapter = new ClassAnnounAdapter(announRoll);
        announRoll.setPlayDelay(5000);
        announRoll.setAdapter(announAdapter);
        mHot.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, true));
        mHot.setAdapter(indexAdapter);
        ViewPager viewPager = announRoll.getViewPager();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int dex = announAdapter.list.size() % (viewPager.getCurrentItem());
                Log.e("TAG", "onPageSelected==>: " + dex);
                indexAdapter.setIndex(dex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mvpPresenter.getClassPhotoList(SpData.getClassInfo() != null ? SpData.getClassInfo().classesId + "" : "", SpData.getIdentityInfo().schoolId);
    }

    @Override
    protected HomeBannerPresenter createPresenter() {
        return new HomeBannerPresenter(this);
    }


    @Override
    public void getClassBannerListSuccess(ClassesBannerRsp model) {
        if (model != null && model.getData() != null) {
            if (model.getData().size() >= 5) {
                List<ClassesBannerRsp.DataBean> dataBeans = model.getData().subList(0, 5);
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
}
