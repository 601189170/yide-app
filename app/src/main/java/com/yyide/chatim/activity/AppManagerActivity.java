package com.yyide.chatim.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class AppManagerActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fz)
    FrameLayout fz;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public int getContentViewID() {
        return R.layout.activity_app_manager_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("应用管理");
        initAdapter();
    }
    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter adapter = new BaseQuickAdapter<HelpRsp, BaseViewHolder>(R.layout.item_manager) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, HelpRsp o) {
//                baseViewHolder
//                        .setText(R.id.title,"如何维护组织架构?")
//                        .setText(R.id.info, o.msg);
            }
        };

        recyclerview.setAdapter(adapter);
//        recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this,20)));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                HelpRsp model = (HelpRsp) adapter.getData().get(position);
//                ToastUtils.showShort(model.msg);
//                startActivity(new Intent(mActivity, HelpInfoActivity.class));
            }
        });
        List<HelpRsp> list=new ArrayList<>();
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        adapter.setList(list);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
