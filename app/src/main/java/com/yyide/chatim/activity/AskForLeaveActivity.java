package com.yyide.chatim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.fragment.leave.AskForLeaveListFragment;
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lombok.val;

public class AskForLeaveActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;
    List<BaseMvpFragment> fragments = new ArrayList<>();
    @Override
    public int getContentViewID() {
        return R.layout.activity_ask_for_leave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.ask_for_leave);
        initViewPager();
    }

    private void initViewPager() {
        RequestLeaveStaffFragment requestLeaveStaffFragment = RequestLeaveStaffFragment.newInstance("staff ask for leave");
        fragments.add(requestLeaveStaffFragment);
        AskForLeaveListFragment askForLeaveListFragment = AskForLeaveListFragment.newInstance("ask for leave record");
        fragments.add(askForLeaveListFragment);

        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return fragments.get(0);
                } else {
                    return fragments.get(1);
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里需要根据position修改tab的样式和文字等
                if (position == 0) {
                    tab.setText(R.string.my_ask_for_leave);
                } else {
                    tab.setText(R.string.leave_record);
                }
            }
        }).attach();
    }
}