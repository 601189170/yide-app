package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.TabRecyAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.fragment.NoteByListFragment;
import com.yyide.chatim.model.NoteTabBean;
import com.yyide.chatim.model.ListByAppRsp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteByListActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    TabRecyAdapter tabRecyAdapter;
    List<NoteTabBean> listTab = new ArrayList<>();
    ArrayList<ListByAppRsp.DataBean.ListBean> listBean = new ArrayList<>();

    private String id;
    private String name;
    private String organization;
    private String type;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notebylist_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        organization = getIntent().getStringExtra("organization");
        type = getIntent().getStringExtra("type");
        listBean = getIntent().getParcelableArrayListExtra("listBean");
        title.setText("通讯录");
        tabRecyAdapter = new TabRecyAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);

        recyclerview.setAdapter(tabRecyAdapter);
        //recyclerview.addItemDecoration(new SpacesItemDecoration(StatusBarUtils.dip2px(this,10)));

        tabRecyAdapter.setOnItemClickListener(new TabRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {//点击回到一级部门,清除所有回退栈
//                    if (tabRecyAdapter.list.size() > 1) {
//                        getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(1).tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    }
                    getSupportFragmentManager().popBackStack();
                    finish();
                } else {
                    getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(position).tag, 0);
                    tabRecyAdapter.remove(position);
                    BackSp();
                }
            }
        });

//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                BackSp();
//            }
//        });

        initDeptFragment();
//        mvpPresenter.NoteBookByList(departmentId,"","","","10","1");
//        mvpPresenter.NoteBookByList("1194","","","","10","1");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.back_layout, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.ll_search:
                startActivity(new Intent(this, BookSearchActivity.class));
                break;
        }
    }

    public void initDeptFragment() {
        String schoolName = getIntent().getStringExtra("schoolName");
        int num = getSupportFragmentManager().getBackStackEntryCount();
        int index = num;
        FragmentManager manager = getSupportFragmentManager();
        Fragment noteByListFragment = new NoteByListFragment();
        if (!TextUtils.isEmpty(schoolName) && !"origin".equals(type)) {//第一个条目
            NoteTabBean noteTab = new NoteTabBean();
            noteTab.name = schoolName;
            noteTab.islast = "2";
            noteTab.tag = index + "";
            listTab.add(noteTab);
            manager.beginTransaction()
                    .replace(R.id.content, noteByListFragment)
                    .addToBackStack(String.valueOf(index))
                    .commit();
        }

        NoteTabBean noteTabBean = new NoteTabBean();
        noteTabBean.tag = (index + 1) + "";
        noteTabBean.name = name;
        noteTabBean.organization = organization;
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putParcelableArrayList("listBean", listBean);
        if (listBean.size() == 0) {
            noteTabBean.islast = "1";
        } else {
            noteTabBean.islast = "2";
        }
        bundle.putString("islast", noteTabBean.islast);
        bundle.putString("organization", organization);
        noteByListFragment.setArguments(bundle);

        manager.beginTransaction()
                .replace(R.id.content, noteByListFragment)
                .addToBackStack(String.valueOf(index + 1))
                .commit();

        listTab.add(noteTabBean);
        tabRecyAdapter.notifydata(listTab);
    }

    public void initDeptFragment2(Bundle bundle) {
        int num = getSupportFragmentManager().getBackStackEntryCount();
        int index = num;
        Fragment noteByListFragment = new NoteByListFragment();
        FragmentManager manager = getSupportFragmentManager();
        NoteTabBean noteTabBean = new NoteTabBean();
        noteTabBean.tag = index + "";
        noteTabBean.name = bundle.getString("name");
        noteTabBean.islast = bundle.getString("islast");
        Log.e("TAG", "initDeptFragment2==》: " + JSON.toJSONString(bundle));
        noteByListFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.content, noteByListFragment)
                .addToBackStack(String.valueOf(index))
                .commit();
        listTab.add(noteTabBean);
        Log.e("TAG", "initDeptFragment2==>: " + JSON.toJSONString(listTab));
        tabRecyAdapter.notifydata(listTab);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void BackSp() {
        int num = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < num; i++) {
            if (i == num - 1) {
                FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(i);
                String name = backStackEntry.getName();
                for (NoteTabBean noteTabBean : tabRecyAdapter.list) {
                    if (name.equals(noteTabBean.tag)) {
                        listTab = tabRecyAdapter.remove(i);
                    }
                }
            }
        }
    }

    void isBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 2) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
