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
import com.google.gson.Gson;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.TabRecyAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.fragment.NoteByListFragment;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.NoteTabBean;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.ZBean;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.presenter.NoteBookByListPresenter;
import com.yyide.chatim.utils.StatusBarUtils;
import com.yyide.chatim.view.NoteByListBookView;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    List<listByAppRsp.DataBean.ListBean> listBean = new ArrayList<>();

    String id;
    String name;
    private String organization;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notebylist_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String data=getIntent().getStringExtra("list");

        int sum = getIntent().getIntExtra("size", 0);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        organization = getIntent().getStringExtra("organization");
        for (int i = 0; i < sum; i++) {
            listByAppRsp.DataBean.ListBean bean = (listByAppRsp.DataBean.ListBean) getIntent().getSerializableExtra(i + "");
            Log.e("TAG", "ZBListBean: " + JSON.toJSONString(bean));
            listBean.add(bean);
        }

        Log.e("TAG", "listBean==》sum: " + JSON.toJSONString(listBean));

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
                    if (tabRecyAdapter.list.size() > 1) {
                        getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(1).tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else {
                    getSupportFragmentManager().popBackStackImmediate(tabRecyAdapter.list.get(position).tag, 0);
                }
                tabRecyAdapter.remove(position);

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.tv_book, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
            case R.id.tv_book:
                finish();
                break;
        }
    }

    public void initDeptFragment() {
        int num = getSupportFragmentManager().getBackStackEntryCount();
        int index = num;
        Fragment noteByListFragment = new NoteByListFragment();

        FragmentManager manager = getSupportFragmentManager();
        NoteTabBean noteTabBean = new NoteTabBean();
        noteTabBean.tag = index + "";
        noteTabBean.name = name;
        noteTabBean.organization = organization;
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        for (int i = 0; i < listBean.size(); i++) {
            bundle.putSerializable(i + "", listBean.get(i));
        }
        if (listBean.size() == 0) {
            noteTabBean.islast = "1";
        } else {
            noteTabBean.islast = "2";
        }

        bundle.putInt("size", listBean.size());
        noteByListFragment.setArguments(bundle);

        manager.beginTransaction()
                .replace(R.id.content, noteByListFragment)
                .addToBackStack(String.valueOf(index))
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
                    if (noteTabBean.tag.equals(name)) {
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
}
