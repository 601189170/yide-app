package com.yyide.chatim.homemodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jude.rollviewpager.RollPagerView;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.ClassAnnounAdapter;
import com.yyide.chatim.adapter.IndexAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.utils.InitPieChart;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import okhttp3.OkHttpClient;


public class BannerFragment extends BaseFragment {

    @BindView(R.id.announRoll)
    RollPagerView announRoll;
    @BindView(R.id.grid)
    GridView grid;
    private View mBaseView;

    OkHttpClient mOkHttpClient = new OkHttpClient();

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
        indexAdapter=new IndexAdapter();
        announRoll.setHintView(null);

        announAdapter = new ClassAnnounAdapter(announRoll);
        announRoll.setPlayDelay(5000);
        announRoll.setAdapter(announAdapter);
        grid.setAdapter(indexAdapter);
        ViewPager viewPager=announRoll.getViewPager();
        List<String> list=new ArrayList<>();
        list.add("http://120.76.189.190/upload/202006/10/1591775760673251938.jpg");
        list.add("http://120.76.189.190/upload/202006/10/1591775786553875903.jpg");
        list.add("http://120.76.189.190/upload/202006/10/1591775770677904123.jpg");
        announAdapter.notifyData(list);
        if (list.size()>0){
            indexAdapter.setIndex(0);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int dex = (viewPager.getCurrentItem())%announAdapter.list.size();
                Log.e("TAG", "onPageSelected==>: "+dex);
                indexAdapter.setIndex(dex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
