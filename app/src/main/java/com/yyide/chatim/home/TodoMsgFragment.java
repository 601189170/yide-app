package com.yyide.chatim.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.fragment.TodoMsgPageFragment;
import com.yyide.chatim.model.TodoRsp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TodoMsgFragment extends BaseFragment {
    private static final String TAG = "TodoMsgFragment";
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private View mBaseView;
    private int type;
    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.todomsg_fragment, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments() != null ? getArguments().getInt("type", 0) : 0;

        fragments.add(TodoMsgPageFragment.newInstance(3));
        fragments.add(TodoMsgPageFragment.newInstance(0));
        fragments.add(TodoMsgPageFragment.newInstance(1));
        viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "全部";
                    case 1:
                        return "待办";
                    case 2:
                        return "已办";
                }
                return null;
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (type > 0) {
            setTab(type);
        } else {
            setTab(0);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: hidden = " + hidden);
        if (!hidden) {
            type = getArguments().getInt("type", 0);
            Log.e(TAG, "onHiddenChanged: " + type);
            if (type != 0) {
                setTab(type);
            }
        }
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                setTab(0);
                break;
            case R.id.tab2:
                setTab(1);
                break;
            case R.id.tab3:
                setTab(2);
                break;
        }
    }

    void setTab(int position) {
        tab1.setChecked(false);
        tab2.setChecked(false);
        tab3.setChecked(false);
        switch (position) {//0待办 1 已拒绝 3全部
            case 0:
                viewpager.setCurrentItem(0);
                tab1.setChecked(true);
                break;
            case 1:
                viewpager.setCurrentItem(1);
                tab2.setChecked(true);
                break;
            case 2:
                viewpager.setCurrentItem(2);
                tab3.setChecked(true);
                break;
        }
    }
}
