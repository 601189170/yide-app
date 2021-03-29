package com.yyide.chatim.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.HelpInfoActivity;
import com.yyide.chatim.activity.HelpListActivity;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


public class HelpFragment extends BaseFragment {


    @BindView(R.id.rmtext)
    TextView rmtext;
    @BindView(R.id.jjtext)
    TextView jjtext;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab1)
    FrameLayout tab1;
    @BindView(R.id.tab2)
    FrameLayout tab2;

    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.help_layout, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rmtext.setText("视频和文字教程,\n帮助你快速上手");
        jjtext.setText("便捷省心的应用\n操作技巧");
        initAdapter();

    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        BaseQuickAdapter adapter = new BaseQuickAdapter<HelpRsp, BaseViewHolder>(R.layout.item_help) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, HelpRsp o) {
                baseViewHolder
                        .setText(R.id.title, "如何维护组织架构?")
                        .setText(R.id.info, o.msg);
            }
        };

        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(mActivity, 20)));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                HelpRsp model = (HelpRsp) adapter.getData().get(position);
//                ToastUtils.showShort(model.msg);
                startActivity(new Intent(mActivity, HelpInfoActivity.class));
            }
        });
        List<HelpRsp> list = new ArrayList<>();
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        list.add(new HelpRsp());
        adapter.setList(list);
    }


    @OnClick({R.id.tab1, R.id.tab2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                startActivity(new Intent(mActivity, HelpListActivity.class));
                break;
            case R.id.tab2:

                break;
        }
    }
}
