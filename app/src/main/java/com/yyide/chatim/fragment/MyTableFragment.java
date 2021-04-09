package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.PreparesLessonActivity;
import com.yyide.chatim.adapter.MyTableAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.MyTablePresenter;
import com.yyide.chatim.view.MyTableView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;


public class MyTableFragment extends BaseMvpFragment<MyTablePresenter> implements MyTableView {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.classlayout)
    FrameLayout classlayout;
    private View mBaseView;

    MyTableAdapter adapter;
    TableTimeAdapter timeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_mytable_fragmnet, container, false);
        return mBaseView;
    }

    private String weekDay;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MyTableAdapter();
        listview.setAdapter(adapter);
//        List<SelectSchByTeaidRsp.DataBean> list=new ArrayList<>();
        timeAdapter = new TableTimeAdapter();
        grid.setAdapter(timeAdapter);

        mvpPresenter.SelectSchByTeaid();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
//                weekDay = timeAdapter.list.get(i).day;
            }
        }
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeAdapter.setPosition(position);
//                weekDay = timeAdapter.getItem(position).day;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectSchByTeaidRsp.DataBean item = adapter.getItem(position);
                Intent intent = new Intent(mActivity, PreparesLessonActivity.class);
                intent.putExtra("dataBean", item);
                startActivity(intent);
            }
        });
        classlayout.setVisibility(View.GONE);
    }

    @Override
    protected MyTablePresenter createPresenter() {
        return new MyTablePresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_PREPARES_SAVE.equals(messageEvent.getCode())) {
            mvpPresenter.SelectSchByTeaid();
        }
    }

    @Override
    public void SelectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e("TAG", "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            adapter.notifyData(rsp.data);
        }
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        Log.e("TAG", "SelectSchByTeaidFail: " + JSON.toJSONString(msg));
    }
}
