package com.yyide.chatim.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.NoBookItemAdapter;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.presenter.NoteBookPresenter;
import com.yyide.chatim.view.NoteBookView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteBookActivity extends BaseMvpActivity<NoteBookPresenter> implements NoteBookView {


    @BindView(R.id.edit)
    EditText edit;

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
    @Override
    public int getContentViewID() {
        return R.layout.activity_note_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通讯录");
        mvpPresenter.listByApp();
        adapter=new NoBookItemAdapter();
        listview.setAdapter(adapter);
        List<String> list=new ArrayList<>();
        list.add("教务处");
        list.add("教务处");
        adapter.notifyData(list);

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


    @OnClick({R.id.back_layout, R.id.title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.title:
                finish();
                break;
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void listByApp(listByAppRsp rsp) {


    }

    @Override
    public void listByAppDataFail(String rsp) {

    }
}
