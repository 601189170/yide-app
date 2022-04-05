package com.yyide.chatim.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PreparesLessonActivity;
import com.yyide.chatim.adapter.MyTableAdapter;
import com.yyide.chatim.adapter.TableTimeAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpFragment;
import com.yyide.chatim.databinding.LayoutMytableFragmnetBinding;
import com.yyide.chatim.dialog.TablePopUp;

import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.table.ChildrenItem;
import com.yyide.chatim.model.table.ListItem;
import com.yyide.chatim.model.table.MyTableBean;
import com.yyide.chatim.model.table.MyTableListItem;
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

import razerdp.basepopup.BasePopupWindow;


public class MyTableFragment extends BaseMvpFragment<MyTablePresenter> implements MyTableView {

    MyTableAdapter adapter;
    TableTimeAdapter timeAdapter;
    private List<MyTableListItem> list = new ArrayList<>();
    private int weekDay = 0;

    private boolean first = true;
    private int thisWeek = 0;

    private LayoutMytableFragmnetBinding binding;

    // 当前所选周数
    private ChildrenItem selectWeek;

    private TablePopUp weekPopUp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        binding = LayoutMytableFragmnetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        adapter = new MyTableAdapter(R.layout.mytable_item);
        binding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listview.setAdapter(adapter);
        binding.empty.tvDesc.setText("您今天没有课程");
       /* adapter.setEmptyView(R.layout.empty);
        TextView tipTextView = adapter.getEmptyLayout().findViewById(R.id.tv_desc);
        tipTextView.setText("您今天没有课程");*/

        timeAdapter = new TableTimeAdapter();
        timeAdapter.setOnItemClickListener((view12, position) -> {
            timeAdapter.setPosition(position);
            weekDay = position;
            adapter.setList(getTableList(list, weekDay));
        });
        binding.tableMyTop.grid.setAdapter(timeAdapter);

        binding.tableMyTop.className.setOnClickListener(v -> {
            if (weekPopUp.isShowing()) {
                weekPopUp.dismiss();
            } else {
                binding.tableMyTop.className.setTextColor(0xFF11C685);
                binding.tableMyTop.classNameLogo.setImageResource(R.mipmap.table_week_button_pack);
                weekPopUp.showPopupWindow(v);
            }
        });

        binding.tableMyTop.classNameLogo.setOnClickListener(v -> {
            if (weekPopUp.isShowing()) {
                weekPopUp.dismiss();
            } else {
                binding.tableMyTop.className.setTextColor(0xFF11C685);
                binding.tableMyTop.classNameLogo.setImageResource(R.mipmap.table_week_button_pack);
                weekPopUp.showPopupWindow(v);
            }
        });


        setToday();


        adapter.setOnItemClickListener((adapter, view1, position) -> {
            //处理学生无法点击查看备课
            /*if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
                SelectSchByTeaidRsp.DataBean item = (SelectSchByTeaidRsp.DataBean) adapter.getItem(position);
                Intent intent = new Intent(mActivity, PreparesLessonActivity.class);
                intent.putExtra("dateTime", timeAdapter.getItem(timeAdapter.position).dataTime);
                intent.putExtra("dataBean", item);
                startActivity(intent);
            }*/
        });
        binding.swipeRefreshLayout.setOnRefreshListener(this::getData);
        binding.swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));

        binding.tableMyReturnCurrent.setOnClickListener(v -> {
            selectWeek = null;
            first = true;
            getData();
        });
        getData();
    }

    private void setToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date(System.currentTimeMillis());
        for (int i = 0; i < timeAdapter.list.size(); i++) {
            if (timeAdapter.list.get(i).day.equals(simpleDateFormat.format(date))) {
                timeAdapter.setPosition(i);
                timeAdapter.setToday(i);
                weekDay = i;
            }
        }
    }


    private void initView() {

        weekPopUp = new TablePopUp(this);
        weekPopUp.setPopupGravity(Gravity.BOTTOM);
        weekPopUp.setSubmitCallBack(data -> {
            if (data != null) {
                selectWeek = data;
                binding.tableMyTop.className.setText(selectWeek.getName());
                getData();
            }
        });

        weekPopUp.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.tableMyTop.className.setTextColor(0xFF666666);
                binding.tableMyTop.classNameLogo.setImageResource(R.mipmap.table_week_button);
            }
        });

    }

    private List<ListItem> getTableList(List<MyTableListItem> list, int weekTime) {
        List<ListItem> dataBeans = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            MyTableListItem itemList = list.get(weekTime);
            if (itemList != null && itemList.getList() != null){
                dataBeans.addAll(itemList.getList());
            }
        }
        if (dataBeans.isEmpty()){
            binding.empty.getRoot().setVisibility(View.VISIBLE);
            binding.listview.setVisibility(View.INVISIBLE);
        }else {
            binding.empty.getRoot().setVisibility(View.GONE);
            binding.listview.setVisibility(View.VISIBLE);
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
        /*if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
            if (SpData.getClassInfo() != null) {
                mvpPresenter.selectClassInfoByClassId(SpData.getClassInfo().classesId);
            }
        } else {
            mvpPresenter.SelectSchByTeaid();
        }*/
        if (selectWeek == null){
            mvpPresenter.getMyTimeTable(null);
        }else {
            mvpPresenter.getMyTimeTable(selectWeek.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        weekPopUp.setSubmitCallBack(null);
        timeAdapter.setOnItemClickListener(null);
    }

    @Override
    public void SelectSchByTeaid(MyTableBean rsp) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "SelectSchByTeaid: " + JSON.toJSONString(rsp));
        if (rsp != null && rsp.getList() != null) {
            list.clear();
            list.addAll(rsp.getList());


            final int getThisWeek = rsp.getThisWeek();
            if (first){
                thisWeek = getThisWeek;
                first = false;
            }

            if (getThisWeek == thisWeek){
                binding.tableMyReturnCurrent.setVisibility(View.INVISIBLE);
            }else{
                binding.tableMyReturnCurrent.setVisibility(View.VISIBLE);
            }

            // 设置总周数
            List<ChildrenItem> data = new ArrayList<>();
            for (int i = 0; i < rsp.getWeekTotal(); i++) {
                String weekNum = String.valueOf(i + 1);
                ChildrenItem bean = new ChildrenItem( "第" + weekNum + "周","",weekNum);
                data.add(bean);
            }
            if (getThisWeek == 0) {
                binding.tableMyTop.className.setText("选择周次");
            }else {
                selectWeek = data.get(getThisWeek - 1);
                binding.tableMyTop.className.setText(selectWeek.getName());
            }
            weekPopUp.setData(data, selectWeek);
            List<TimeUtil.WeekDay> toWeekDayList = TimeUtil.getWeekDay(rsp.getWeekList());
            timeAdapter.notifyData(toWeekDayList);
            setToday();
            adapter.setList(getTableList(list, weekDay));
        }
    }

    @Override
    public void SelectSchByTeaidFail(String msg) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Log.e("TAG", "SelectSchByTeaidFail: " + JSON.toJSONString(msg));
    }
}
