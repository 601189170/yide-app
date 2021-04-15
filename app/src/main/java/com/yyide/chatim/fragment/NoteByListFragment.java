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
import com.yyide.chatim.R;
import com.yyide.chatim.activity.NoteByListActivity;
import com.yyide.chatim.activity.PersonInfoActivity;
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

import androidx.annotation.Nullable;

import butterknife.BindView;


public class NoteByListFragment extends BaseMvpFragment<NoteBookByListPresenter> implements NoteByListBookView {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.listview2)
    ListView listview2;
    private View mBaseView;

    NotelistAdapter adapter;
    String id;
    NotelistAdapter2 adapter2;

    int type;
    int sum;
    private String organization;
    List<listByAppRsp.DataBean.ListBean> listBean = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_notebylist_fragmnet, container, false);
        id = getArguments().getString("id");
        sum = getArguments().getInt("size");
        organization = getArguments().getString("organization");
        listBean.clear();
        for (int i = 0; i < sum; i++) {
            listByAppRsp.DataBean.ListBean bean = (listByAppRsp.DataBean.ListBean) getArguments().getSerializable(i + "");
            Log.e("TAG", "ZBListBeanfragment: " + JSON.toJSONString(bean));
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
        listview2.setAdapter(adapter2);
        if (listBean.size() == 0) {
            type = 2;
            listview.setVisibility(View.GONE);
            listview2.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.VISIBLE);
            type = 1;
            adapter.notifydata(listBean);
        }
        if (!TextUtils.isEmpty(organization) && "staff".equals(organization)) {
            mvpPresenter.NoteBookByList(id, "", "", "", "10", "1");
        } else {
            mvpPresenter.getStudentList(id);
        }
        listview.setOnItemClickListener((parent, view1, position, id) -> {
            if (type == 1) {
                NoteByListActivity activity = (NoteByListActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("size", adapter.list.get(position).list != null ? adapter.list.get(position).list.size() : 0);
                bundle.putString("id", String.valueOf(adapter.list.get(position).id));
                bundle.putString("name", String.valueOf(adapter.list.get(position).name));
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
        listview2.setOnItemClickListener((parent, view12, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra("data", JSON.toJSONString(adapter2.getItem(position)));
            intent.putExtra("organization", organization);
            intent.setClass(mActivity, PersonInfoActivity.class);
            startActivity(intent);
        });
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
            adapter2.notifdata(rsp.data.records);
        }
    }

    @Override
    public void TeacherlistRspFail(String rsp) {

    }

    @Override
    public void studentListRsp(TeacherlistRsp rsp) {
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            Log.e("TAG", "records: " + JSON.toJSONString(rsp.data.records));
            adapter2.notifdata(rsp.data.records);
        }
    }

    @Override
    public void studentListRspFail(String msg) {

    }
}
