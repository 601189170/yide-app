package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.adapter.StudentHonorAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.StudentHonorRsp;
import com.yyide.chatim.presenter.StudentHonorPresenter;
import com.yyide.chatim.view.StudentHonorView;

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


public class StudentHonorFragment extends BaseMvpFragment<StudentHonorPresenter> implements StudentHonorView {

    private View mBaseView;
    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.grid)
    RecyclerView mHot;
    StudentHonorAdapter announAdapter;
    IndexAdapter indexAdapter;

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
        announAdapter = new StudentHonorAdapter(announRoll);
        announRoll.setHintView(null);
        announAdapter.setOnItemClickListener(() -> {
            //startActivity(new Intent(getContext(), StudentHonorListActivity.class));
            Intent intent = new Intent(view.getContext(), WebViewActivity.class);
            intent.putExtra("url", BaseConstant.STUDENT_HONOR_URL);
            view.getContext().startActivity(intent);
        });
        iv_bg.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", BaseConstant.CLASS_PHOTO_URL);
            view.getContext().startActivity(intent);
        });
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

        if (SpData.getClassInfo() != null) {
            mvpPresenter.getStudentHonorList(SpData.getClassInfo().classesId);
        }
    }

    @Override
    protected StudentHonorPresenter createPresenter() {
        return new StudentHonorPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", ClassHonorFragment.class.getSimpleName());
            if (SpData.getClassInfo() != null) {
                mvpPresenter.getStudentHonorList(SpData.getClassInfo().classesId);
            }
        }
    }

    @Override
    public void getStudentHonorSuccess(StudentHonorRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            List<String> imgs = new ArrayList<>();
            //StudentHonorRsp.DataBean data = model.getData().getRecords();
            if (model.getData() != null && model.getData().getRecords() != null && model.getData().getRecords().size() > 0) {
                iv_bg.setVisibility(View.GONE);
                for (StudentHonorRsp.DataBean.RecordsBean bean : model.getData().getRecords()) {
                    if (bean.getWorksUrl() != null && bean.getWorksUrl().size() > 0) {
                        for (String itme : bean.getWorksUrl()) {
                            imgs.add(itme);
                        }
                    }
                }
            } else {
                iv_bg.setVisibility(View.VISIBLE);
            }

            if (imgs.size() > 5) {
                List<String> strings = imgs.subList(0, 5);
                indexAdapter.setList(strings);
                announAdapter.notifyData(strings);
            } else {
                List<String> img = imgs;
                indexAdapter.setList(img);
                announAdapter.notifyData(img);
            }
        }
    }

    @Override
    public void getStudentHonorFail(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
