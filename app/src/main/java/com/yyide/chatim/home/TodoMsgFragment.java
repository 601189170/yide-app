package com.yyide.chatim.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.MessageAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AgentInformationRsp;
import com.yyide.chatim.utils.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TodoMsgFragment extends BaseFragment {

    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private View mBaseView;
    private BaseQuickAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.todomsg_fragment, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<AgentInformationRsp, BaseViewHolder>(R.layout.message_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, AgentInformationRsp o) {
                holder.setText(R.id.tv_leave, o.getContent())
                        .setText(R.id.tv_leave_type, o.getDesc())
                        .setText(R.id.tv_start_time, o.getStartTime())
                        .setText(R.id.tv_leave_status, "审批状态：" + (o.getStatus() == 1 ? "待审批" : "已审批"))
                        .setText(R.id.tv_end_time, o.getEndTime());
                TextView textView = holder.getView(R.id.tv_refused);
                TextView textView2 = holder.getView(R.id.tv_agree);
                if (o.getStatus() == 2) {
                    textView.setVisibility(View.GONE);
                    textView2.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                }
                textView.setOnClickListener(v -> {

                });
                textView2.setOnClickListener(v -> {

                });

            }
        };
        recyclerview.setAdapter(adapter);
        initData();
        setTab(0);
    }

    private void initData() {
        List<AgentInformationRsp> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AgentInformationRsp item = new AgentInformationRsp();
            switch (i) {
                case 0:
                    item.setTitle("请假");
                    item.setContent("张宇的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 2:
                    item.setTitle("请假");
                    item.setContent("刘星的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 3:
                    item.setTitle("请假");
                    item.setContent("李沐的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 4:
                    item.setTitle("请假");
                    item.setContent("刘德云的主监护人提交的请假需要你审批");
                    item.setStatus(2);
                    break;
                case 5:
                    item.setTitle("请假");
                    item.setContent("张明宇的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 6:
                    item.setTitle("请假");
                    item.setContent("王珂的主监护人提交的请假需要你审批");
                    item.setStatus(2);
                    break;
            }
            item.setDesc("请假类型：事假");
            item.setStartTime("开始时间：" + DateUtils.stampToDate(System.currentTimeMillis()));
            item.setEndTime("结束时间：" + DateUtils.stampToDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
            list.add(item);
        }
        adapter.setList(list);
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
        switch (position) {
            case 0:
                tab1.setChecked(true);
                break;
            case 1:
                tab2.setChecked(true);
                break;
            case 2:
                tab3.setChecked(true);
                break;
        }

    }

}
