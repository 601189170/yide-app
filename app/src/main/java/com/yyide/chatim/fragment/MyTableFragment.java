package com.yyide.chatim.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.MyTableAdapter;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.MyTablePresenter;
import com.yyide.chatim.view.MyTableView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;


public class MyTableFragment extends BaseMvpFragment<MyTablePresenter> implements MyTableView {


    @BindView(R.id.listview)
    ListView listview;
    private View mBaseView;

    MyTableAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_mytable_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MyTableAdapter();
        listview.setAdapter(adapter);
        List<String> list=new ArrayList<>();
        list.add("");
        list.add("");
        adapter.notifyData(list);
        mvpPresenter.SelectSchByTeaid();

    }

    @Override
    protected MyTablePresenter createPresenter() {
        return new MyTablePresenter(this);
    }


    @Override
    public void SelectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e("TAG", "SelectSchByTeaid: " + JSON.toJSONString(rsp));
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        Log.e("TAG", "SelectSchByTeaidFail: " + JSON.toJSONString(msg));
    }
}
