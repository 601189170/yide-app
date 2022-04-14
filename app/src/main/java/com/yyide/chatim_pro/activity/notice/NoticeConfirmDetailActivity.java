package com.yyide.chatim_pro.activity.notice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.notice.fragment.NoticeItemFragment;
import com.yyide.chatim_pro.base.BaseMvpActivity;
import com.yyide.chatim_pro.activity.notice.presenter.NoticeConfirmDetailPresenter;
import com.yyide.chatim_pro.activity.notice.view.NoticeConfirmDetailView;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeConfirmDetailActivity extends BaseMvpActivity<NoticeConfirmDetailPresenter> implements NoticeConfirmDetailView {
    private static final String TAG = "NoticeConfirmDetailActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;
    private long signId;
    private int totalNumber;
    private int readNumber;
    @Override
    public int getContentViewID() {
        return R.layout.activity_confirm_detail;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_confirm_detail_title);
        Intent intent = getIntent();
        totalNumber = intent.getIntExtra("totalNumber", 0);
        readNumber = intent.getIntExtra("readNumber", 0);
        signId = intent.getLongExtra("signId", 0);
        Log.e(TAG, "onCreate: totalNumber="+ totalNumber+", readNumber="+readNumber+",signId="+signId);
        initViewPager();
    }

    private void initViewPager() {
        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0){
                    return NoticeItemFragment.newInstance(0,signId,totalNumber-readNumber);
                } else {
                    return NoticeItemFragment.newInstance(1,signId,readNumber);
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
                if(position == 0){
                    tab.setText("未确认("+(totalNumber-readNumber)+")");
                } else {
                    tab.setText("已确认("+readNumber+")");
                }
            }
        }).attach();
    }

    @OnClick(R.id.back_layout)
    public void click(){
        finish();
    }

    @Override
    protected NoticeConfirmDetailPresenter createPresenter() {
        return new NoticeConfirmDetailPresenter(this);
    }

    @Override
    public void showError() {

    }
}