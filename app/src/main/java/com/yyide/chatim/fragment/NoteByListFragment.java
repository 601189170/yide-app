package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteByListActivity;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.adapter.NoteItemAdapter;
import com.yyide.chatim.adapter.NotelistAdapter;
import com.yyide.chatim.adapter.NotelistAdapter2;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.view.NoteByListBookView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;


public class NoteByListFragment extends BaseMvpFragment<NoteBookByListPresenter> implements NoteByListBookView {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private View mBaseView;

    private NotelistAdapter adapter;
    private String id;
    private NotelistAdapter2 adapter2;
    private int pageNum = 1;
    private int pageSize = 20;
    private NoteItemAdapter noteItemAdapter;
    private String islast;
    private int type;
    private int sum;
    private String organization;
    List<listByAppRsp.DataBean.ListBean> listBean = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id = getArguments().getString("id");
        sum = getArguments().getInt("size");
        islast = getArguments().getString("islast");
        organization = getArguments().getString("organization");
        listBean.clear();
        for (int i = 0; i < sum; i++) {
            listByAppRsp.DataBean.ListBean bean = (listByAppRsp.DataBean.ListBean) getArguments().getSerializable(i + "");
            Log.e("TAG", "ZBListBeanfragment: " + JSON.toJSONString(bean));
            bean.itemType = 1;
            listBean.add(bean);
        }
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NotelistAdapter();
        adapter2 = new NotelistAdapter2();
        listview.setAdapter(adapter);
        if (listBean.size() == 0) {
            type = 2;
            listview.setVisibility(View.GONE);
        } else {
            listview.setVisibility(View.VISIBLE);
            type = 1;
            adapter.notifydata(listBean);
        }
        //initView();
        if("2".equals(islast) && "student".equals(organization)){//代表还要下一级不查询当前Id数据
            id = "0";
        }
        sendRequset();
        listview.setOnItemClickListener((parent, view1, position, id) -> {
            if (type == 1) {
                NoteByListActivity activity = (NoteByListActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("size", adapter.list.get(position).list != null ? adapter.list.get(position).list.size() : 0);
                bundle.putString("id", String.valueOf(adapter.list.get(position).id));
                bundle.putString("name", String.valueOf(adapter.list.get(position).name));
                bundle.putString("organization", organization);
                if (adapter.list.get(position).list != null && adapter.list.get(position).list.size() == 0) {
                    bundle.putString("islast", "1");
                } else {
                    if (adapter.list.get(position).list == null) {
                        bundle.putString("islast", "1");
                    } else {
                        bundle.putString("islast", "2");
                    }
                }

                if (adapter.list.get(position).list != null && adapter.list.get(position).list.size() > 0) {
                    for (int i = 0; i < adapter.list.get(position).list.size(); i++) {
                        bundle.putSerializable(i + "", adapter.list.get(position).list.get(i));
                    }
                }
                activity.initDeptFragment2(bundle);
            }

        });
        initView();
    }

    private void sendRequset() {
        //不穿ID表示查询当下你所属部门人员或学生列表
        if (!TextUtils.isEmpty(organization) && "staff".equals(organization)) {
            mvpPresenter.NoteBookByList(id, 30, pageNum);
        } else {
            mvpPresenter.getStudentList(id, 30, pageNum);
        }
    }

    private void initView() {
        noteItemAdapter = new NoteItemAdapter();
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(noteItemAdapter);
        noteItemAdapter.setEmptyView(R.layout.empty);
        noteItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra("data", JSON.toJSONString(noteItemAdapter.getItem(position)));
            intent.putExtra("organization", organization);
            intent.setClass(mActivity, PersonInfoActivity.class);
            startActivity(intent);
        });

        noteItemAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉加载时取消下拉刷新
                noteItemAdapter.getLoadMoreModule().setEnableLoadMore(true);
                //请求数据
                pageNum++;
                sendRequset();
            }
        });
        noteItemAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        noteItemAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    protected NoteBookByListPresenter createPresenter() {
        return new NoteBookByListPresenter(this);
    }

    @Override
    public void TeacherlistRsp(TeacherlistRsp rsp) {
        Log.e("TAG", "TeacherlistRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            Log.e("TAG", "records: " + JSON.toJSONString(rsp.data.records));
//            adapter2.notifdata(rsp.data.records);
            noteItemAdapter.setList(rsp.data.records);
//            noteItemAdapter.addData(rsp.data.records);
        }
    }

    @Override
    public void TeacherlistRspFail(String rsp) {

    }

    @Override
    public void studentListRsp(TeacherlistRsp rsp) {
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            Log.e("TAG", "records: " + JSON.toJSONString(rsp.data.records));
//            adapter2.notifdata(rsp.data.records);
            noteItemAdapter.addData(rsp.data.records);
        }
    }

    @Override
    public void studentListRspFail(String msg) {

    }
}
