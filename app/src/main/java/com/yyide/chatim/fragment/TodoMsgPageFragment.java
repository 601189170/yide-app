package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.common.reflect.TypeToken;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.presenter.TodoFragmentPresenter;
import com.yyide.chatim.view.TodoFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TodoMsgPageFragment extends BaseMvpFragment<TodoFragmentPresenter> implements TodoFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TodoMsgFragment";
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private View mBaseView;
    private TodoAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private int mParam1; //待办状态 1 已办 0待办 3全部
    private int pageNum = 1;
    private int pageSize = 15;

    public static TodoMsgPageFragment newInstance(int param1) {
        TodoMsgPageFragment fragment = new TodoMsgPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1, -1);
            Log.e(TAG, "mParam1:" + mParam1);
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
        initView();
    }

    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TodoAdapter();
        adapter.setEmptyView(R.layout.empty);
        recyclerview.setAdapter(adapter);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
            adapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            pageNum++;
            mvpPresenter.getMessageTransaction(pageNum, pageSize, String.valueOf(mParam1));
        });
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mvpPresenter.getMessageTransaction(pageNum, pageSize, String.valueOf(mParam1));
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        mvpPresenter.getMessageTransaction(pageNum, pageSize, String.valueOf(mParam1));
    }

    public class TodoAdapter extends BaseMultiItemQuickAdapter<TodoRsp.DataBean.RecordsBean, BaseViewHolder> implements LoadMoreModule {

        public TodoAdapter() {
            addItemType(TodoRsp.DataBean.RecordsBean.ITEM_TYPE_MESSAGE, R.layout.message_item);
            addItemType(TodoRsp.DataBean.RecordsBean.ITEM_TYPE_TODO, R.layout.message_item);
            addItemType(TodoRsp.DataBean.RecordsBean.ITEM_TYPE_NOTICE, R.layout.message_item);
        }

        @Override
        protected void convert (@NotNull BaseViewHolder holder, TodoRsp.DataBean.RecordsBean o){
            switch (o.getItemType()){
                case TodoRsp.DataBean.RecordsBean.ITEM_TYPE_MESSAGE:
                    //setTodoItem(holder, o);
                    break;
                case TodoRsp.DataBean.RecordsBean.ITEM_TYPE_TODO:
                    setTodoItem(holder, o);
                    break;
                case TodoRsp.DataBean.RecordsBean.ITEM_TYPE_NOTICE:
                    //setTodoItem(holder, o);
                    break;
            }
        }

        private void setTodoItem(@NotNull BaseViewHolder holder, TodoRsp.DataBean.RecordsBean o) {
            holder.setText(R.id.tv_leave, o.getFirstData())
                    .setText(R.id.tv_title, o.getTitle());
            //处理内容解析
            try {
                if (TodoRsp.DataBean.RecordsBean.IS_TEXT_TYPE.equals(o.getIsText())) {
                    holder.setText(R.id.tv_leave_type, o.getContent());
                } else {
                    String content = o.getContent();
                    if(!TextUtils.isEmpty(content)){
                        Type type = new TypeToken<List<String>>(){}.getType();
                        List<String> strings = JSON.parseObject(content, type);
                        StringBuffer stringBuffer = new StringBuffer();
                        if(strings != null){
                            for (int i = 0; i < strings.size(); i++){
                                stringBuffer.append(strings.get(i));
                                if(i < strings.size() -1){
                                    stringBuffer.append("\n").append("\n");
                                }
                            }
                        }
                        holder.setText(R.id.tv_leave_type, stringBuffer.toString());
                    }
                }
            } catch (Exception e){
                Log.d("setTodoItem", o.getIsText());
                Log.d("setTodoItem", o.getContent());
                e.printStackTrace();
            }
            TextView textView2 = holder.getView(R.id.tv_agree);
            //如果身份是家长隐藏按钮
            if ("1".equals(o.getStatus())
                    || "2".equals(o.getStatus())
                    || "3".equals(o.getStatus())
                    || (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status))) {
                textView2.setVisibility(View.GONE);
            } else {
                textView2.setVisibility(View.VISIBLE);
            }

            textView2.setOnClickListener(v -> {//同意
                Intent intent = new Intent(getContext(), LeaveFlowDetailActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("id", o.getCallId());
                startActivity(intent);
            });
        }
    }

    @Override
    protected TodoFragmentPresenter createPresenter() {
        return new TodoFragmentPresenter(this);
    }

    @Override
    public void getMyNoticePageSuccess(TodoRsp noticeHomeRsp) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e(TAG, "getMyNoticePageSuccess: " + noticeHomeRsp.toString());
        if (BaseConstant.REQUEST_SUCCES2 == noticeHomeRsp.getCode() && noticeHomeRsp.getData() != null) {
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MESSAGE_TODO_NUM, noticeHomeRsp.getData().getTotal() + ""));
            List<TodoRsp.DataBean.RecordsBean> data = noticeHomeRsp.getData().getRecords();
            if (pageNum == 1) {
                adapter.setList(data);
            } else {
                adapter.addData(data);
            }
            if (noticeHomeRsp.getData().getRecords() != null) {
                if (noticeHomeRsp.getData().getRecords().size() < pageSize) {
                    //如果不够一页,显示没有更多数据布局
                    adapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    adapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }
    }

    @Override
    public void getMyNoticePageFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e(TAG, "getMyNoticePageFail: " + msg);
        ToastUtils.showShort(msg);
    }
}
