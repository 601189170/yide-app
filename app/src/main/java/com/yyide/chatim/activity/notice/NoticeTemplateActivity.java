package com.yyide.chatim.activity.notice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.activity.notice.presenter.NoticeTemplatePresenter;
import com.yyide.chatim.activity.notice.view.NoticeTemplateView;
import com.yyide.chatim.model.TemplateTypeRsp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeTemplateActivity extends BaseMvpActivity<NoticeTemplatePresenter> implements NoticeTemplateView {
    private static final String TAG = "NoticeTemplateActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager2 mViewpager;
    int currentIndex;

    List<TemplateTypeRsp.DataBean.RecordsBean> list = new ArrayList<>();
    List<NoticeTemplateListFragment> listFragments = new ArrayList<>();
    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_template;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_template_title);
        //initViewPager();
        mvpPresenter.getTemplateType();
    }

    private void initViewPager() {
        mViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

//        list.add("全部");
//        list.add("热门");
//        list.add("活动");
//        list.add("安全");
//        list.add("假期");

        mViewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                TemplateTypeRsp.DataBean.RecordsBean recordsBean = list.get(currentIndex);
                Log.e(TAG, "createFragment: "+position );
                return listFragments.get(position);//我的通知
            }

            @Override
            public int getItemCount() {
                return list.isEmpty() ? 0 : list.size();
            }
        });
        new TabLayoutMediator(mTablayout, mViewpager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里需要根据position修改tab的样式和文字等
                Log.e(TAG, "onConfigureTab: "+position );
                TemplateTypeRsp.DataBean.RecordsBean recordsBean = list.get(currentIndex);
                currentIndex = position;
                tab.setText(list.get(position).getTempName());
            }
        }).attach();
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.ll_add)
    public void addClick() {
        jupm(this, NoticeCreateActivity.class);
        finish();
    }

    @Override
    protected NoticeTemplatePresenter createPresenter() {
        return new NoticeTemplatePresenter(this);
    }

    @Override
    public void showError() {

    }

    @Override
    public void templateType(TemplateTypeRsp templateTypeRsp) {
        Log.e(TAG, "templateType: "+templateTypeRsp.toString() );
        if (templateTypeRsp.getCode() == 200){
            list.clear();
            List<TemplateTypeRsp.DataBean.RecordsBean> records = templateTypeRsp.getData().getRecords();
            list.addAll(records);
            if (list.size()>4){
                mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }else {
                mTablayout.setTabMode(TabLayout.MODE_FIXED);
            }
            for (TemplateTypeRsp.DataBean.RecordsBean recordsBean : list) {
                listFragments.add(NoticeTemplateListFragment.newInstance("",recordsBean.getTempName()+"",recordsBean.getId()));
            }
            initViewPager();
        }
    }

    @Override
    public void templateTypeFail(String msg) {
        Log.e(TAG, "templateTypeFail: "+msg );
        mTablayout.setVisibility(View.GONE);
    }
}