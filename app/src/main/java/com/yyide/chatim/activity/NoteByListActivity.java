package com.yyide.chatim.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.TabRecyAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.fragment.NoteByListFragment;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoteTabBean;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.NoteByListBookView;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class NoteByListActivity extends BaseMvpActivity<NoteBookByListPresenter> implements NoteByListBookView {


    @BindView(R.id.back)
    TextView back;

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    FrameLayout content;
    int index = 1;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    TabRecyAdapter tabRecyAdapter;
    List<NoteTabBean> listTab=new ArrayList<>();
    listByAppRsp.DataBean.ListBean listBean;
    @Override
    public int getContentViewID() {
        return R.layout.activity_notebylist_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data=getIntent().getStringExtra("list");
        listBean=JSON.parseObject(data, listByAppRsp.DataBean.ListBean.class);
        Log.e("TAG", "listdata: "+data );
        Log.e("TAG", "listdata: "+listBean );
        title.setText("通讯录");
        tabRecyAdapter=new TabRecyAdapter();
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initDeptFragment();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);

        recyclerview.setAdapter(tabRecyAdapter);
        recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this,10)));

        tabRecyAdapter.setOnItemClickListener(new TabRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position == 0) {//点击回到一级部门,清除所有回退栈
                    if (tabRecyAdapter.list.size() > 1) {
                        getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(1).tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else {
                    getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(position).tag, 0);
                }
                tabRecyAdapter.remove(position);
//                listTab=tabRecyAdapter.list;

            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                BackSp();
            }
        });

        initDeptFragment();
//        mvpPresenter.NoteBookByList(departmentId,"","","","10","1");
//        mvpPresenter.NoteBookByList("1194","","","","10","1");
    }

    @Override
    protected NoteBookByListPresenter createPresenter() {
        return new NoteBookByListPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void initDeptFragment() {
        int num=getSupportFragmentManager().getBackStackEntryCount();
        int index=num;
        Fragment noteByListFragment = new NoteByListFragment();

        FragmentManager manager = getSupportFragmentManager();
        NoteTabBean noteTabBean =new NoteTabBean();
        noteTabBean.tag=index+"";

                Bundle bundle = new Bundle();
                if (listBean!=null){
                    bundle.putString("id", String.valueOf(listBean.id));
                    bundle.putString("data", String.valueOf(listBean.list));
                    noteTabBean.name=listBean.name;
                    if (listBean.list.size()==0){
                        noteTabBean.islast=true;
                    }else {
                        noteTabBean.islast=false;
                    }

                }else {
                    bundle.putString("id", "");
                    bundle.putString("data", "");
                }

                noteByListFragment.setArguments(bundle);


        manager.beginTransaction()
                .replace(R.id.content, noteByListFragment)
                .addToBackStack(String.valueOf(index))
                .commit();


//        if (index==2){
//            listTab.add(new NoteTabBean("tag"+index,index+"",true));
//        }else {
//            listTab.add(new NoteTabBean("tag"+index,index+"",false));
            listTab.add(noteTabBean);
//        }


        tabRecyAdapter.notifydata(listTab);
//        index++;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            isBack();



            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    void BackSp(){

        int num=getSupportFragmentManager().getBackStackEntryCount();
        for (int i=0;i<num;i++){
            if (i==num-1){
                FragmentManager.BackStackEntry backStackEntry=getSupportFragmentManager().getBackStackEntryAt(i);
                String name=backStackEntry.getName();
                for (NoteTabBean noteTabBean : tabRecyAdapter.list) {
                    if (noteTabBean.tag.equals(name)){
                        tabRecyAdapter.remove(i);
//                        listTab.remove(i);
                    }
                }
            }
        }

    }

    void isBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

    }

    @Override
    public void showError() {

    }

    @Override
    public void TeacherlistRsp(TeacherlistRsp rsp) {
        Log.e("TAG", "TeacherlistRsp: "+JSON.toJSONString(rsp) );
        if (rsp.code== BaseConstant.REQUEST_SUCCES2){

        }

    }

    @Override
    public void TeacherlistRspFail(String rsp) {

    }
}
