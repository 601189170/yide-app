package com.yyide.chatim.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.adapter.TabRecyAdapter;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.fragment.NoteByListFragment;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.NoteTabBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteByListActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    TabRecyAdapter tabRecyAdapter;
    List<NoteTabBean> listTab = new ArrayList<>();
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> listBean = new ArrayList<>();
    ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO> nowBean = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
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

        String str = getIntent().getStringExtra("listBean");
        String str2 = getIntent().getStringExtra("nowBean");
        listBean= (ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO>) JSON.parseObject(str,new TypeReference<List<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO>>(){});
        nowBean= (ArrayList<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO>) JSON.parseObject(str2,new TypeReference<List<ListByAppRsp2.DataDTO.DeptVOListDTO.EmployeeAddBookDTOListDTO>>(){});


        title.setText("通讯录");
        Log.e("TAG", "NoteByListActivity: "+ JSON.toJSONString(listBean));
        Log.e("TAG", "NoteByListActivity==>: "+ JSON.toJSONString(nowBean));
        tabRecyAdapter = new TabRecyAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(tabRecyAdapter);
        tabRecyAdapter.setOnItemClickListener((view, position) -> {
            if (position == 0) {//点击回到一级部门,清除所有回退栈
                finish();
            } else {
                listTab = tabRecyAdapter.remove(position + 1);
                fragments = remove(position + 1);
            }
        });

        recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                recyclerview.smoothScrollToPosition(tabRecyAdapter.getItemCount());
            }
        });
        initDeptFragment();
    }


    public List<Fragment> remove(int position) {
        List<Fragment> noteTabBeans = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (position > i) {
                noteTabBeans.add(fragments.get(i));
            }
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, noteTabBeans.get(noteTabBeans.size() - 1))
                .commit();
        return noteTabBeans;
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

    private void initDeptFragment() {
        String schoolName = getIntent().getStringExtra("schoolName");
        FragmentManager manager = getSupportFragmentManager();
        if (!TextUtils.isEmpty(schoolName) && !"origin".equals(type)) {//第一个条目
            Fragment noteByListFragment = new NoteByListFragment();
            NoteTabBean noteTab = new NoteTabBean();
            noteTab.name = schoolName;
            noteTab.islast = "2";
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putParcelableArrayList("listBean", listBean);
            bundle.putParcelableArrayList("nowBean", nowBean);
            noteTab.islast = "2";
            bundle.putString("islast", noteTab.islast);
            bundle.putString("organization", organization);
            noteByListFragment.setArguments(bundle);
            manager.beginTransaction()
                    .replace(R.id.content, noteByListFragment)
                    .commit();
            listTab.add(noteTab);
            fragments.add(noteByListFragment);
        }
        Fragment noteByListFragment = new NoteByListFragment();
        NoteTabBean noteTabBean = new NoteTabBean();
        noteTabBean.name = name;
        noteTabBean.organization = organization;
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putParcelableArrayList("listBean", listBean);
        bundle.putParcelableArrayList("nowBean", nowBean);
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
                .commit();
        listTab.add(noteTabBean);
        fragments.add(noteByListFragment);
        tabRecyAdapter.notifydata(listTab);
    }

    public void initDeptFragmentNew(Bundle bundle) {
        Fragment noteByListFragment = new NoteByListFragment();
        FragmentManager manager = getSupportFragmentManager();
        NoteTabBean noteTabBean = new NoteTabBean();
        noteTabBean.name = bundle.getString("name");
        noteTabBean.islast = bundle.getString("islast");
        Log.e("NoteByListActivity", "initDeptFragment2==》: " + JSON.toJSONString(bundle));
        noteByListFragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.content, noteByListFragment)
                .commit();
        listTab.add(noteTabBean);
        fragments.add(noteByListFragment);
        Log.e("NoteByListActivity", "initDeptFragment2==>: " + JSON.toJSONString(listTab));
        tabRecyAdapter.notifydata(listTab);
        Log.e("NoteByListActivity", "backSp count==>: " + getSupportFragmentManager().getBackStackEntryCount());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void isBack() {
        if ("origin".equals(type)) {
            if (fragments.size() > 1) {
                listTab = tabRecyAdapter.remove(listTab.size() - 1);
                fragments = remove(fragments.size() - 1);
            } else {
                finish();
            }
        } else {
            if (fragments.size() > 2) {
                listTab = tabRecyAdapter.remove(listTab.size() - 1);
                fragments = remove(fragments.size() - 1);
            } else {
                finish();
            }
        }
    }
}
