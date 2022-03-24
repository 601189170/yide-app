package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PreparesLessonActivity;
import com.yyide.chatim.adapter.MyTableAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.LayoutMytableFragmnetBinding;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.MyTablePresenter;
import com.yyide.chatim.view.MyTableView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MyTableFragment extends BaseMvpFragment<MyTablePresenter> implements MyTableView {

    MyTableAdapter adapter;
    TableTimeAdapter timeAdapter;
    private List<SelectSchByTeaidRsp.DataBean> list = new ArrayList<>();
    private int weekDay;

    private LayoutMytableFragmnetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        binding = LayoutMytableFragmnetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyTableAdapter(R.layout.mytable_item);
        binding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listview.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty);
        timeAdapter = new TableTimeAdapter();
        binding.tableMyTop.grid.setAdapter(timeAdapter);
//        if (SpData.getIdentityInfo().weekNum <= 0) {
//            tv_week.setText("");
//        } else {
//            tv_week.setText(getString(R.string.weekNum, SpData.getIdentityInfo().weekNum + ""));
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                weekDay = i + 1;
            }
        }
        binding.tableMyTop.grid.setOnItemClickListener((parent, view12, position, id) -> {
            timeAdapter.setPosition(position);
            weekDay = position + 1;
            adapter.setList(getTableList(list, position + 1));
        });

        adapter.setOnItemClickListener((adapter, view1, position) -> {
            //处理学生无法点击查看备课
            if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
                SelectSchByTeaidRsp.DataBean item = (SelectSchByTeaidRsp.DataBean) adapter.getItem(position);
                Intent intent = new Intent(mActivity, PreparesLessonActivity.class);
                intent.putExtra("dateTime", timeAdapter.getItem(timeAdapter.position).dataTime);
                intent.putExtra("dataBean", item);
                startActivity(intent);
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(this::getData);
        binding.swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));


        binding.tableMyReturnCurrent.setOnClickListener(v -> {
            getData();
        });
        getData();
    }

    private List<SelectSchByTeaidRsp.DataBean> getTableList(List<SelectSchByTeaidRsp.DataBean> list, int weekTime) {
        List<SelectSchByTeaidRsp.DataBean> dataBeans = new ArrayList<>();
        if (list != null) {
            for (SelectSchByTeaidRsp.DataBean item : list) {
                if (item.weekTime == weekTime) {
                    dataBeans.add(item);
                }
            }
        }
        return dataBeans;
    }

    @Override
    protected MyTablePresenter createPresenter() {
        return new MyTablePresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_PREPARES_SAVE.equals(messageEvent.getCode())) {
            getData();
        }
    }

    private void getData() {
        if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
            if (SpData.getClassInfo() != null) {
                mvpPresenter.selectClassInfoByClassId(SpData.getClassInfo().classesId);
            }
        } else {
            mvpPresenter.SelectSchByTeaid();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void SelectSchByTeaid(SelectSchByTeaidRsp rsp) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS && rsp.data != null) {
            list = rsp.data;
            adapter.setList(getTableList(list, weekDay));
        }
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "SelectSchByTeaidFail: " + JSON.toJSONString(msg));
    }
}
