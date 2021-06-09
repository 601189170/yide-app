package com.yyide.chatim.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.notice.presenter.NoticeTemplateListFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeTemplateListFragmentView;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.activity.notice.presenter.NoticeAnnouncementFragmentPresenter;
import com.yyide.chatim.activity.notice.view.NoticeAnnouncementFragmentView;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.TemplateListRsp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeTemplateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeTemplateListFragment extends BaseMvpFragment<NoticeTemplateListFragmentPresenter> implements NoticeTemplateListFragmentView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NoticeTemplateListFragm";
    List<TemplateListRsp.DataBean.RecordsBean> list = new ArrayList<>();
    BaseQuickAdapter adapter;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.blank_page)
    LinearLayout blank_page;
    private boolean refresh = false;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "tempId";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String name;
    private long tempId;
    //加载过就不在自动加载
    private boolean load;

    public NoticeTemplateListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NoticeAnnouncementListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeTemplateListFragment newInstance(String param1,String name,long tempId) {
        NoticeTemplateListFragment fragment = new NoticeTemplateListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, name);
        args.putLong(ARG_PARAM3, tempId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            tempId = getArguments().getLong(ARG_PARAM3);
            Log.e(TAG,"name:"+name+",tempId:"+tempId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: "+load);
        load = false;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseQuickAdapter<TemplateListRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_notice_template) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, TemplateListRsp.DataBean.RecordsBean o) {
                baseViewHolder
                        .setText(R.id.tv_title, o.getName())
                        //.setText(R.id.tv_desc, o.getName())
                        .setText(R.id.tv_notice_content, Html.fromHtml(o.getContent()));
            }
        };

        adapter.setOnItemClickListener((adapter, view1, position) -> {
            Log.e(TAG, "onItemClick: "+position );
            TemplateListRsp.DataBean.RecordsBean recordsBean = (TemplateListRsp.DataBean.RecordsBean)adapter.getData().get(position);
            Intent intent = new Intent(getActivity(), NoticeCreateActivity.class);
            intent.putExtra("name",recordsBean.getName());
            intent.putExtra("content",recordsBean.getContent());
            intent.putExtra("template",true);
            startActivity(intent);
            getActivity().finish();
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setAdapter(adapter);
        //mvpPresenter.noticeTemplateList(name,tempId);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: "+load );
        if (!load){
            mvpPresenter.noticeTemplateList(name,tempId);
        }
    }

    @Override
    protected NoticeTemplateListFragmentPresenter createPresenter() {
        return new NoticeTemplateListFragmentPresenter(this);
    }


    @Override
    public void noticeTemplateList(TemplateListRsp templateListRsp) {
        Log.e(TAG, "noticeTemplateList: " + templateListRsp.toString());
        if (refresh){
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        if (templateListRsp.getCode() == 200) {
            TemplateListRsp.DataBean data = templateListRsp.getData();
            List<TemplateListRsp.DataBean.RecordsBean> records = data.getRecords();
            if (!records.isEmpty()){
                load = true;
                list.clear();
                list.addAll(records);
                adapter.setList(list);
            }
        }
        showBlankPage();
    }
    public void showBlankPage(){
        if(list.isEmpty()){
            blank_page.setVisibility(View.VISIBLE);
        }else {
            blank_page.setVisibility(View.GONE);
        }
    }
    @Override
    public void noticeTemplateListFail(String msg) {
        Log.e(TAG, "noticeTemplateListFail: " + msg);
        showBlankPage();
        refresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        mvpPresenter.noticeTemplateList(name,tempId);
    }
}