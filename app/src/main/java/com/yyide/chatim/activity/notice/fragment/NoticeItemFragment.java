package com.yyide.chatim.activity.notice.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeConfirmListFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeConfirmListFragmentView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.view.FootView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class NoticeItemFragment extends BaseMvpFragment<NoticeConfirmListFragmentPresenter> implements NoticeConfirmListFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NoticeItemFragment";
    BaseQuickAdapter adapter;
    private static final String ARG_PARAM1 = "confirmType";
    private static final String ARG_PARAM2 = "signId";
    private static final String ARG_PARAM3 = "size";
    private int confirmType;
    private long signId;
    private int size;
    private boolean refresh = false;
    private int curIndex = 1;
    private int pages = 0;
    private int total = 15;
    private List<ConfirmDetailRsp.DataBean.RecordsBean> recordsBeans = new ArrayList<>();
    private FootView footView;
    private LinearLayout blank_page;
    private SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Customize parameter initialization
    public static NoticeItemFragment newInstance(int confirmType,long signId,int size) {
        NoticeItemFragment fragment = new NoticeItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, confirmType);
        args.putLong(ARG_PARAM2, signId);
        args.putInt(ARG_PARAM3, size);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            confirmType = getArguments().getInt(ARG_PARAM1);
            signId = getArguments().getLong(ARG_PARAM2);
            size = getArguments().getInt(ARG_PARAM3);
            Log.e(TAG,"confirmType:"+confirmType+",signId:"+signId+",size:"+size);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
        final RecyclerView recyclerView = view.findViewById(R.id.list);
        blank_page = view.findViewById(R.id.blank_page);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        initAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(monScrollListener);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        mvpPresenter.getConfirmDetails(confirmType,signId,1,total);
    }
    public void showBlankPage() {
        if (recordsBeans.isEmpty()) {
            blank_page.setVisibility(View.VISIBLE);
        } else {
            blank_page.setVisibility(View.GONE);
        }
    }
    private void initAdapter(){
        adapter = new BaseQuickAdapter<ConfirmDetailRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.fragment_notice_item) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, ConfirmDetailRsp.DataBean.RecordsBean str) {
                baseViewHolder.setText(R.id.tv_notice_scope_name, str.getName());
                //GlideUtil.loadImage(getContext(), "https://www.thecrazyprogrammer.com/wp-content/uploads/2017/05/Android-Glide-Tutorial-with-Example-1.png", baseViewHolder.findView(R.id.iv_pic));
                //ImageView view = baseViewHolder.findView(R.id.iv_pic);
                //view.setImageDrawable(getActivity().getDrawable(R.drawable.icon_leave));
                String userName = str.getName();
                if (!TextUtils.isEmpty(userName)) {
                    if (userName.length()<=2){
                        baseViewHolder.setText(R.id.tv_pic,str.getName());
                    }else {
                        baseViewHolder.setText(R.id.tv_pic,userName.substring(userName.length()-2));
                    }
                }
            }
        };
        footView = new FootView(getActivity());

    }

    @Override
    public void noticeConfirmList(ConfirmDetailRsp confirmDetailRsp) {
        Log.e(TAG, "noticeConfirmList: "+confirmDetailRsp.toString() );
        if (confirmDetailRsp.getCode() == 200) {
            if (refresh) {
                recordsBeans.clear();
                refresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
            pages = confirmDetailRsp.getData().getPages();
            List<ConfirmDetailRsp.DataBean.RecordsBean> records = confirmDetailRsp.getData().getRecords();
            recordsBeans.addAll(records);
            adapter.setList(recordsBeans);
            adapter.notifyDataSetChanged();
            showBlankPage();
        }
    }

    @Override
    public void noticeConfirmListFail(String msg) {
            showBlankPage();
    }

    @Override
    protected NoticeConfirmListFragmentPresenter createPresenter() {
        return new NoticeConfirmListFragmentPresenter(this);
    }

    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener monScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (adapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (adapter.getFooterLayoutCount() == 0 && pages > 0){
                        adapter.setFooterView(footView);
                    }
                    if (curIndex >= pages) {
                        footView.setLoading(false);
                        //ToastUtils.showShort("没有更多数据了！");
                        return;
                    }
                    footView.setLoading(true);
                    //发送网络请求获取更多数据
                    mvpPresenter.getConfirmDetails(confirmType,signId,++curIndex,total);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        curIndex = 1;
        refresh = true;
        mvpPresenter.getConfirmDetails(confirmType,signId,1,total);
    }
}