package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.NoticeTemplateListFragment;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.presenter.TodoFragmentPresenter;
import com.yyide.chatim.view.TodoFragmentView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TodoMsgPageFragment extends BaseMvpFragment<TodoFragmentPresenter> implements TodoFragmentView {
    private static final String TAG = "TodoMsgFragment";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private View mBaseView;
    private BaseQuickAdapter adapter;
    private List<TodoRsp.DataBean.RecordsBean> list = new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public static TodoMsgPageFragment newInstance(String param1) {
        TodoMsgPageFragment fragment = new TodoMsgPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.e(TAG,"mParam1:"+mParam1);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_todo_msg_page, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<TodoRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.message_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, TodoRsp.DataBean.RecordsBean o) {
                holder.setText(R.id.tv_leave, o.getContent())
                        .setText(R.id.tv_leave_type, "请假类型：没有字段")
                        .setText(R.id.tv_start_time, o.getCreatedDateTime())
                        .setText(R.id.tv_leave_status, "审批状态：" + (o.getStatus().equals("0") ? "待审批" : "已审批"))
                        .setText(R.id.tv_date, o.getCreatedDateTime())
                        .setText(R.id.tv_end_time, o.getCreatedDateTime());
                TextView textView = holder.getView(R.id.tv_refused);
                TextView textView2 = holder.getView(R.id.tv_agree);
                if (o.getStatus().equals("1")) {
                    textView.setVisibility(View.GONE);
                    textView2.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                }
                textView.setOnClickListener(v -> {//拒绝

                });
                textView2.setOnClickListener(v -> {//同意

                });

            }
        };
        recyclerview.setAdapter(adapter);
        mvpPresenter.getMyNoticePage(1, 20, mParam1);
    }

    @Override
    protected TodoFragmentPresenter createPresenter() {
        return new TodoFragmentPresenter(this);
    }

    @Override
    public void getMyNoticePageSuccess(TodoRsp noticeHomeRsp) {
        Log.e(TAG, "getMyNoticePageSuccess: " + noticeHomeRsp.toString());
        if (noticeHomeRsp.getCode() == 200) {
            list.clear();
            List<TodoRsp.DataBean.RecordsBean> data = noticeHomeRsp.getData().getRecords();
            list.addAll(data);
            adapter.setList(list);
        }
    }

    @Override
    public void getMyNoticePageFail(String msg) {
        Log.e(TAG, "getMyNoticePageFail: " + msg);
        ToastUtils.showShort(msg);
    }
}
