package com.yyide.chatim.activity.leave;

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
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.fragment.leave.AskForLeaveListFragment;
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment;
import com.yyide.chatim.fragment.leave.RequestLeaveStudentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    private void initViewPager() {
        if (SpData.getIdentityInfo().staffIdentity()) {
            //教职工入口
            fragments.add(RequestLeaveStaffFragment.newInstance("staff ask for leave"));
        }else {
            fragments.add(RequestLeaveStudentFragment.newInstance("student ask for leave"));
        }
        fragments.add(AskForLeaveListFragment.newInstance("ask for leave record"));
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