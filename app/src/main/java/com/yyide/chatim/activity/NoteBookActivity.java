package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.adapter.NoBookItemAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.selectListByAppRsp;
import com.yyide.chatim.presenter.NoteBookPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.NoteBookView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteBookActivity extends BaseMvpActivity<NoteBookPresenter> implements NoteBookView {

    @BindView(R.id.edit)
    TextView edit;
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
    @BindView(R.id.original)
    FrameLayout original;
    @BindView(R.id.student)
    FrameLayout student;

    @BindView(R.id.pName)
    TextView pName;
    public String TAG = "tag";
    private String schoolType = "";
    private String mSchoolName = "";

    @Override
    public int getContentViewID() {
        return R.layout.activity_note_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通讯录");
        mvpPresenter.listByApp();
        GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
        if (identityInfo != null) {
            schoolType = identityInfo.schoolType;
            if ("Y".equalsIgnoreCase(identityInfo.schoolType)) {
                mvpPresenter.universitySelectListByApp();
            } else {
                mvpPresenter.selectListByApp();
            }
            mSchoolName = identityInfo.schoolName;
            pName.setText(identityInfo.schoolName);
//            if (!TextUtils.isEmpty(identityInfo.img)) {
//                GlideUtil.loadImageHead(NoteBookActivity.this, identityInfo.img, img);
//            }
        }
        adapter = new NoBookItemAdapter();
        adapter2 = new NoBookItemAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener((parent, view, position, id) -> setPostData(adapter.getItem(position).list, adapter.getItem(position).parentName, String.valueOf(adapter.getItem(position).id), adapter.getItem(position).name, "staff", ""));
        listview2.setOnItemClickListener((parent, view, position, id) -> setPostData(adapter2.getItem(position).list, adapter2.getItem(position).parentName, String.valueOf(adapter2.getItem(position).id), adapter2.getItem(position).name, "student", ""));
    }

    /**
     * @param list
     * @param id
     * @param name
     * @param organization 教职工staff  学生student
     */
    void setPostData(ArrayList<ListByAppRsp.DataBean.ListBean> list, String schoolName, String id, String name, String organization, String type) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("listBean", list);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("schoolName", mSchoolName);
        intent.putExtra("schoolType", schoolType);
        intent.putExtra("type", type);
        intent.putExtra("organization", organization);
        intent.setClass(NoteBookActivity.this, NoteByListActivity.class);
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

    @OnClick({R.id.back_layout, R.id.title, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
            case R.id.title:
                finish();
                break;
            case R.id.ll_search:
                startActivity(new Intent(this, BookSearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void listByApp(ListByAppRsp rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            //学校组织结构
            ArrayList<ListByAppRsp.DataBean.ListBean> listBeans1 = new ArrayList<>();
            if (rsp.data.size() > 0) {
                if (!TextUtils.isEmpty(rsp.data.get(0).schoolLogo)) {
                    GlideUtil.loadImageHead(NoteBookActivity.this, rsp.data.get(0).schoolLogo, img);
                }
                for (ListByAppRsp.DataBean.ListBean listBean : rsp.data.get(0).list) {
                    listBeans1.add(listBean);
                }
                original.setOnClickListener(v -> setPostData(listBeans1, rsp.data.get(0).parentName, String.valueOf(rsp.data.get(0).id), rsp.data.get(0).name, "staff", "origin"));
            }
            adapter.notifyData(listBeans1);
        }
    }

    @Override
    public void selectListByApp(ListByAppRsp rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            //小初高组织结构
            ArrayList<ListByAppRsp.DataBean.ListBean> listBeans = new ArrayList<>();
            if (rsp.data != null && rsp.data.size() > 0) {
                for (ListByAppRsp.DataBean listBean : rsp.data) {
                    ListByAppRsp.DataBean.ListBean item = new ListByAppRsp.DataBean.ListBean();
                    item.name = listBean.name;
                    item.id = listBean.id;
                    item.list = listBean.list;
                    listBeans.add(item);
                }
                student.setOnClickListener(v -> setPostData(listBeans, mSchoolName, rsp.data.get(0).id + "", mSchoolName, "student", "origin"));
            }
            listview2.setAdapter(adapter2);
            adapter2.notifyData(listBeans);
        }
    }

    @Override
    public void universityListByApp(ListByAppRsp rsp) {
        Log.e("TAG", "listByAppRsp: " + JSON.toJSONString(rsp));
        if (rsp.code == BaseConstant.REQUEST_SUCCESS) {
            //通讯录（大学）组织结构
            List<ListByAppRsp.DataBean.ListBean> listBeans1 = new ArrayList<>();
            if (rsp.data.size() > 0) {
                for (ListByAppRsp.DataBean.ListBean listBean : rsp.data.get(0).list) {
                    listBeans1.add(listBean);
                }
                student.setOnClickListener(v -> setPostData(rsp.data.get(0).list, mSchoolName, String.valueOf(rsp.data.get(0).id), mSchoolName, "student", "origin"));
            }
            listview2.setAdapter(adapter2);
            adapter2.notifyData(listBeans1);
        }
    }


    @Override
    public void listByAppDataFail(String rsp) {

    }

    @Override
    public void selectListByAppRsp(selectListByAppRsp rsp) {
        Log.e(TAG, "selectListByAppRsp: " + JSON.toJSONString(rsp));
    }

    @Override
    public void selectListByAppRspFail(String rsp) {

    }
}
