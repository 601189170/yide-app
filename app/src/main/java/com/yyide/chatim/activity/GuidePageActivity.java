package com.yyide.chatim.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityGuidePageBinding;
import com.yyide.chatim.fragment.GuidePageFragment;

import java.util.ArrayList;
import java.util.List;

public class GuidePageActivity extends BaseActivity {

    private ActivityGuidePageBinding pageBinding;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public int getContentViewID() {
        return R.layout.activity_guide_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageBinding = ActivityGuidePageBinding.inflate(getLayoutInflater());
        setContentView(pageBinding.getRoot());
        initViewPager();
    }

    private void initViewPager() {
        fragments.add(GuidePageFragment.newInstance(0));
        fragments.add(GuidePageFragment.newInstance(1));
        fragments.add(GuidePageFragment.newInstance(2));
        fragments.add(GuidePageFragment.newInstance(3));
        pageBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pageBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.isEmpty() ? 0 : fragments.size();
            }
        });
    }
}