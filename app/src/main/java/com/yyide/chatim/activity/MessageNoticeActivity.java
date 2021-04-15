package com.yyide.chatim.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.AgentInformationRsp;
import com.yyide.chatim.model.HelpRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.SpacesItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageNoticeActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter adapter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_message_notice_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("消息通知");
        initAdapter();

    }

    private void initAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<AgentInformationRsp, BaseViewHolder>(R.layout.item_message_notice) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, AgentInformationRsp o) {
                baseViewHolder.setText(R.id.tv_leave, o.getContent())
                        .setText(R.id.tv_leave_type, o.getDesc())
                        .setText(R.id.tv_start_time, o.getStartTime())
                        .setText(R.id.tv_leave_status, "审批状态：" + (o.getStatus() == 1 ? "待审批" : "已审批"))
                        .setText(R.id.tv_date, o.getStartTime())
                        .setText(R.id.tv_end_time, o.getEndTime());
                TextView textView = baseViewHolder.getView(R.id.tv_refused);
                TextView textView2 = baseViewHolder.getView(R.id.tv_agree);
                if (o.getStatus() == 2) {
                    textView.setVisibility(View.GONE);
                    textView2.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                }
                textView.setOnClickListener(v -> {//拒绝
                    o.setStatus(1);
                    o.setAgentStatus(1);
                    list.remove(o);
                    adapter.remove(o);
                    notifyDataSetChanged();
                });
                textView2.setOnClickListener(v -> {//同意
                    o.setStatus(2);
                    o.setAgentStatus(2);
                    adapter.notifyDataSetChanged();
                });
            }
        };

        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this, 20)));
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            HelpRsp model = (HelpRsp) adapter1.getData().get(position);
        });
        //initData();
    }

    private List<AgentInformationRsp> list = new ArrayList<>();

    private void initData() {
        for (int i = 0; i < 9; i++) {
            AgentInformationRsp item = new AgentInformationRsp();
            switch (i) {
                case 0:
                    item.setTitle("请假");
                    item.setContent("张宇的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 1:
                    item.setTitle("请假");
                    item.setContent("刘星的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 2:
                    item.setTitle("请假");
                    item.setContent("李沐的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 3:
                    item.setTitle("请假");
                    item.setContent("刘德云的主监护人提交的请假需要你审批");
                    item.setStatus(2);
                    break;
                case 4:
                    item.setTitle("请假");
                    item.setContent("张明宇的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 5:
                    item.setTitle("请假");
                    item.setContent("王珂的主监护人提交的请假需要你审批");
                    item.setStatus(2);
                    break;
                case 6:
                    item.setTitle("请假");
                    item.setContent("张檬的主监护人提交的请假需要你审批");
                    item.setStatus(2);
                    break;
                case 7:
                    item.setTitle("请假");
                    item.setContent("刘博的主监护人提交的请假需要你审批");
                    item.setStatus(1);
                    break;
                case 8:
                    item.setTitle("请假");
                    item.setContent("程昱的主监护人提交的请假需要你审批");
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back_layout, R.id.title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.title:
                finish();
                break;
        }
    }
}
