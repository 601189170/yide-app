package com.yyide.chatim.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.NoticeItemFragment;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.fragment.ClassTableFragment;
import com.yyide.chatim.fragment.MyTableFragment;
import com.yyide.chatim.fragment.SiteTableFragment;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.TemplateTypeRsp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TableActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;
    List<Fragment> fragmentList;
    @Override
    public int getContentViewID() {
        return R.layout.activity_table_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("课表");
        initViewPager();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new MyTableFragment());
        fragmentList.add(new ClassTableFragment());
        fragmentList.add(new SiteTableFragment());

        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
//                if(position == 0){
//                    return new MyTableFragment();
//                } else {
//                    return new ClassTableFragment();
//                }
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true,false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("我的课表");
                } else if (position == 1) {
                    tab.setText("班级课表");
                } else {
                    tab.setText("场地课表");
                }
            }
        }).attach();
    }

    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }


}
