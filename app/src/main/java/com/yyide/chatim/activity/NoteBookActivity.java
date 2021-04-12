package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.NoBookItemAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.model.selectListByAppRsp;
import com.yyide.chatim.presenter.NoteBookPresenter;
import com.yyide.chatim.view.NoteBookView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteBookActivity extends BaseMvpActivity<NoteBookPresenter> implements NoteBookView {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.listview)
    ListView listview;
    NoBookItemAdapter adapter;
    @BindView(R.id.listview2)
    ListView listview2;
    NoBookItemAdapter adapter2;
    @BindView(R.id.pName)
    TextView pName;
    public String TAG="tag";
    @Override
    public int getContentViewID() {
        return R.layout.activity_note_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通讯录");
        mvpPresenter.listByApp();
        adapter = new NoBookItemAdapter();
        adapter2 = new NoBookItemAdapter();
        listview.setAdapter(adapter);
        listview2.setAdapter(adapter2);
//        List<String> list=new ArrayList<>();
//        list.add("教务处");
//        list.add("教务处");
//        adapter.notifyData(list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent();
//                Log.e("TAG", "onItemClick: "+JSON.toJSONString(adapter.list.get(position).id) );
//                intent.putExtra("departmentId",adapter.list.get(position).id);
//                intent.putExtra("list",JSON.toJSONString(adapter.getItem(position).list));
//                intent.putExtra("id",JSON.toJSONString(adapter.getItem(position).id));
//                intent.setClass(NoteBookActivity.this,NoteByListActivity.class);
//                startActivity(intent);
                setPostData(adapter.getItem(position).list, String.valueOf(adapter.getItem(position).id),adapter.getItem(position).name);

            }
        });
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPostData(adapter2.getItem(position).list, String.valueOf(adapter2.getItem(position).id),adapter2.getItem(position).name);

//                Intent intent=new Intent();
//
////                Log.e("TAG", "onItemClick: "+JSON.toJSONString(adapter.list.get(position).id) );
//                intent.putExtra("list",JSON.toJSONString(adapter2.getItem(position).list));
//                intent.putExtra("id",JSON.toJSONString(adapter2.getItem(position).id));
//                intent.setClass(NoteBookActivity.this,NoteByListActivity.class);
//                startActivity(intent);
            }
        });
//        mvpPresenter.selectListByApp();
    }
    void setPostData(List<listByAppRsp.DataBean.ListBean.ZBListBean> list,String id,String name){
        Intent intent=new Intent();
        for (int i = 0; i < list.size(); i++) {
            intent.putExtra(i+"",list.get(i));
        }
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("size",list.size());
        intent.setClass(NoteBookActivity.this,NoteByListActivity.class);
        startActivity(intent);
    }
    @Override
    protected NoteBookPresenter createPresenter() {
        return new NoteBookPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back_layout, R.id.title,R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.title:
                finish();
                break;
            case R.id.ll_search:
                startActivity(new Intent(this,BookSearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void listByApp(listByAppRsp rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
            List<listByAppRsp.DataBean.ListBean> listBeans1=new ArrayList<>();
            List<listByAppRsp.DataBean.ListBean> listBeans2=new ArrayList<>();
            if (rsp.data.size()>0){
                pName.setText(rsp.data.get(0).parentName);

                for (listByAppRsp.DataBean.ListBean listBean : rsp.data.get(0).list) {
                    if (listBean.type.equals("0")){
                        listBeans1.add(listBean);
//                        adapter.notifyData(listBean);
                    }else {
                        listBeans2.add(listBean);
//                        adapter2.notifyData(listBean.list);
                    }
                }
            }
            adapter.notifyData(listBeans1);
            adapter2.notifyData(listBeans2);
//            for (listByAppRsp.DataBean datum : rsp.data) {
//
//                //type==> 0 行政部門 1：學生會或家委會
//                if (datum.type.equals("0")) {
//                    adapter.notifyData(datum.list);
//                } else {
//                    adapter2.notifyData(datum.list);
//                }
//                pName.setText(datum.parentName);
//            }

        }
    }

    @Override
    public void listByAppDataFail(String rsp) {

    }

    @Override
    public void selectListByAppRsp(selectListByAppRsp rsp) {
        Log.e(TAG, "selectListByAppRsp: "+ JSON.toJSONString(rsp) );
    }

    @Override
    public void selectListByAppRspFail(String rsp) {

    }
}
