package com.yyide.chatim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PreparesLessonActivity;
import com.yyide.chatim.adapter.MyTableAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.presenter.MyTablePresenter;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.view.MyTableView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


public class MyTableFragment extends BaseMvpFragment<MyTablePresenter> implements MyTableView {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.classlayout)
    FrameLayout classlayout;
    @BindView(R.id.className)
    TextView className;
    @BindView(R.id.tv_week)
    TextView tv_week;
    private View mBaseView;

    MyTableAdapter adapter;
    TableTimeAdapter timeAdapter;
    private List<SelectSchByTeaidRsp.DataBean> list = new ArrayList<>();
    private int weekDay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.layout_mytable_fragmnet, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        adapter = new MyTableAdapter();
        listview.setAdapter(adapter);
//        List<SelectSchByTeaidRsp.DataBean> list=new ArrayList<>();
        timeAdapter = new TableTimeAdapter();
        grid.setAdapter(timeAdapter);
        tv_week.setText(TimeUtil.getWeek() + "周");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                weekDay = i + 1;
            }
        }
        grid.setOnItemClickListener((parent, view12, position, id) -> {
            timeAdapter.setPosition(position);
            weekDay = position + 1;
            adapter.notifyData(getTableList(list, position + 1));
        });
        listview.setOnItemClickListener((parent, view1, position, id) -> {
            //处理学生无法点击查看备课
            if (SpData.getIdentityInfo() != null && !GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                SelectSchByTeaidRsp.DataBean item = adapter.getItem(position);
                Intent intent = new Intent(mActivity, PreparesLessonActivity.class);
                intent.putExtra("dateTime", timeAdapter.getItem(timeAdapter.position).dataTime);
                intent.putExtra("dataBean", item);
                startActivity(intent);
            }
        });
        classlayout.setVisibility(View.GONE);
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
        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
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
        Log.e("TAG", "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2 && rsp.data != null) {
            list = rsp.data;
            adapter.notifyData(getTableList(rsp.data, weekDay));
        }
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        Log.e("TAG", "SelectSchByTeaidFail: " + JSON.toJSONString(msg));
    }
}
