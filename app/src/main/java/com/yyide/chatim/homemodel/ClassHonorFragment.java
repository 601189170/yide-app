package com.yyide.chatim.homemodel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.ClassesHonorPhotoListActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.adapter.ClassPhotoAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ClassesPhotoBannerRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.ClassPhotoPresenter;
import com.yyide.chatim.view.ClassPhotoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class ClassHonorFragment extends BaseMvpFragment<ClassPhotoPresenter> implements ClassPhotoView {

    private View mBaseView;
    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    RecyclerView mHot;
    ClassPhotoAdapter announAdapter;
    IndexAdapter indexAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_class_honor_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
//        mvpPresenter.getMyData();
        indexAdapter = new IndexAdapter();
        announAdapter = new ClassPhotoAdapter(announRoll);
        announRoll.setHintView(null);
        announAdapter.setOnItemClickListener(new ClassPhotoAdapter.ItemClickListener() {
            @Override
            public void OnItemClickListener() {
//                startActivity(new Intent(getContext(), ClassesHonorPhotoListActivity.class));
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                intent.putExtra("url", BaseConstant.CLASS_PHOTO_URL);
                view.getContext().startActivity(intent);
            }
        });

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
        getData();
    }

    @Override
    protected ClassPhotoPresenter createPresenter() {
        return new ClassPhotoPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            Log.d("HomeRefresh", ClassHonorFragment.class.getSimpleName());
            getData();
        }
    }

    private void getData() {
        if (SpData.getClassInfo() != null) {
            mvpPresenter.getClassPhotoList(SpData.getIdentityInfo().schoolId + "", SpData.getClassInfo().classesId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getStudentHonorSuccess(ClassesPhotoBannerRsp model) {
        if (BaseConstant.REQUEST_SUCCES2 == model.getCode()) {
            if (model.getData() != null) {
                if (model.getData().size() > 5) {
                    List<ClassesPhotoBannerRsp.DataBean> data = model.getData().subList(0, 5);
                    indexAdapter.setList(data);
                    announAdapter.notifyData(data);
                } else {
                    indexAdapter.setList(model.getData());
                    announAdapter.notifyData(model.getData());
                }
            }
        }
    }

    @Override
    public void getStudentHonorFail(String msg) {

    }
}
