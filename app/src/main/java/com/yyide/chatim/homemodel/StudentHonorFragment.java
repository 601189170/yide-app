package com.yyide.chatim.homemodel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.StudentHonorListActivity;
import com.yyide.chatim.adapter.ClassAnnounAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.model.ClassesBannerRsp;
import com.yyide.chatim.model.EventMessage;

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
import okhttp3.OkHttpClient;


public class StudentHonorFragment extends BaseFragment {

    private View mBaseView;
    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    RecyclerView mHot;
    ClassAnnounAdapter announAdapter;
    IndexAdapter indexAdapter;
    private int[] imgs = {R.drawable.student_1,
            R.drawable.student_2,
            R.drawable.student_3,
            R.drawable.student_4,
            R.drawable.student_5};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_student_honor_fragmnet, container, false);
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
        announAdapter.setOnItemClickListener(new ClassAnnounAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener() {
                startActivity(new Intent(getContext(), StudentHonorListActivity.class));
            }
        });
        //模拟数据
        List<ClassesBannerRsp.DataBean> dataBeans = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            ClassesBannerRsp.DataBean item = new ClassesBannerRsp.DataBean();
            item.setClassifyId(imgs[i]);
            dataBeans.add(item);
        }
        announAdapter.notifyData(dataBeans);
        indexAdapter.setList(dataBeans);
        announRoll.setPlayDelay(5000);
        announRoll.setAdapter(announAdapter);
        mHot.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mHot.setAdapter(indexAdapter);
        ViewPager viewPager = announRoll.getViewPager();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            Log.d("HomeRefresh", ClassHonorFragment.class.getSimpleName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
