package com.yyide.chatim.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppManagerActivity;
import com.yyide.chatim.activity.MessageNoticeActivity;
import com.yyide.chatim.adapter.AppAdapter;
import com.yyide.chatim.adapter.AppItemAdapter;
import com.yyide.chatim.adapter.MyAppItemAdapter;
import com.yyide.chatim.adapter.RecylAppAdapter;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.leave.LeaveActivity;
import com.yyide.chatim.model.APPBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


public class AppFragment extends BaseFragment {


    @BindView(R.id.mygrid)
    GridView mygrid;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.right_btn)
    LinearLayout rightBtn;
    private View mBaseView;
    RecylAppAdapter recylAppAdapter;

    AppAdapter appAdapter;
    boolean sc = true;
    MyAppItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.app_layout, container, false);
        return mBaseView;
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MyAppItemAdapter();
        mygrid.setAdapter(adapter);
        mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.list.get(position).id.equals("99")){
//                    startActivity(new Intent(mActivity,));

                        startActivity(new Intent(mActivity, AppManagerActivity.class));

                }
            }
        });

        List<APPBean> myAPPlist=new ArrayList<>();
        myAPPlist.add(new APPBean("请假","1","1"));
        myAPPlist.add(new APPBean("请假","1","1"));
        myAPPlist.add(new APPBean("请假","1","1"));
        myAPPlist.add(new APPBean("请假","1","1"));
        myAPPlist.add(new APPBean("请假","1","1"));
        myAPPlist.add(new APPBean("编辑","99","1"));

        adapter.notifyData(myAPPlist);

        recylAppAdapter = new RecylAppAdapter();
        recy.setAdapter(recylAppAdapter);
        List<String> list = new ArrayList<>();
        list.add("教学工作");
        list.add("家校沟通");
        list.add("教务管理");
        list.add("校园生活");
        list.add("智能硬件");
        list.add("德育评价");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(linearLayoutManager);
        recylAppAdapter.notifydata(list);
        recylAppAdapter.setOnItemClickListener(new RecylAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                sc = false;
                recylAppAdapter.setPosition(position);
                listview.setSelection(position);
//                listview.setSelectionFromTop(position);
//                listview.smoothScrollToPosition(position);
            }
        });

        appAdapter = new AppAdapter();
        listview.setAdapter(appAdapter);
        appAdapter.notifyData(list);
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sc = true;
                return false;
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiblePosition = view.getFirstVisiblePosition();
//                view.getF

//                if (scrollPosition != firstVisibleItem) {
//                    adapter.setSelectItem(firstVisibleItem);
//                    mLeft.setSelectionFromTop(firstVisibleItem, 40);
//                    scrollPosition = firstVisibleItem;
//                }
                if (sc) {
                    recylAppAdapter.setPosition(firstVisiblePosition);
                }


//                Log.e("TAG", "firstVisiblePosition: "+ JSON.toJSONString(firstVisiblePosition));

            }
        });


//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int firstVisiblePosition = parent.getFirstVisiblePosition();
//                Log.e("TAG", "onItemClick: "+ JSON.toJSONString(firstVisiblePosition));
//            }
//        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,LeaveActivity.class));
            }
        });
    }


}
