package com.yyide.chatim.notice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.notice.presenter.NoticeScopePresenter;
import com.yyide.chatim.notice.tree.TreeAdapter;
import com.yyide.chatim.notice.tree.TreeItem;
import com.yyide.chatim.notice.view.NoticeScopeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeScopeActivity extends BaseMvpActivity<NoticeScopePresenter> implements NoticeScopeView {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.tv_selected_member)
    TextView tv_selected_member;
    private List<TreeItem> checkTreeList = new ArrayList<>();
    private TreeAdapter treeAdapter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_scope;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_scope_title);
        initView();
        initRecyclerView();
    }

    private void initView() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mRecyclerView.isComputingLayout()) {
                    isAllCheckBox(isChecked);
                }
            }
        });
    }

    //全选
    @SuppressLint("StringFormatInvalid")
    private void isAllCheckBox(boolean isChecked) {
        int number = 0;
        checkTreeList.clear();
        List<TreeItem> data = treeAdapter.getData();
        for (TreeItem checkBean : data) {
            checkBean.isCheck = isChecked;
            if (checkBean != null && checkBean.child != null && !checkBean.child.isEmpty()) {
                number += checkBean.child.size();
                for (TreeItem childBean : checkBean.child) {
                    childBean.isCheck = isChecked;
                }
            }
        }
        treeAdapter.notifyDataSetChanged();
        if (isChecked) {
            checkTreeList.addAll(data);
        } else {
            number = 0;
        }
        tv_selected_member.setText(getString(R.string.notice_select_number, isChecked ? number + checkTreeList.size() : 0));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        treeAdapter = new TreeAdapter(R.layout.fragment_notice_scope_item, initList());
        mRecyclerView.setAdapter(treeAdapter);
        treeAdapter.setOnCheckListener(new TreeAdapter.CheckItemListener() {
            @Override
            public void itemChecked(TreeItem checkBean, boolean isChecked) {
                //处理Item点击选中回调事件
                if (isChecked) {
                    //选中处理
                    if (!checkTreeList.contains(checkBean)) {
                        checkTreeList.add(checkBean);
                    }
                } else {
                    //未选中处理
                    if (checkTreeList.contains(checkBean)) {
                        checkTreeList.remove(checkBean);
                    }
                }

                //判断列表数据是否全部选中
                if (checkTreeList.size() == treeAdapter.getData().size()) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                tv_selected_member.setText(getString(R.string.notice_select_number, checkTreeList.size()));
            }
        });
    }

    @Override
    protected NoticeScopePresenter createPresenter() {
        return new NoticeScopePresenter(this);
    }

    @OnClick(R.id.back_layout)
    public void click() {
        finish();
    }

    @OnClick(R.id.btn_complete)
    public void complete() {

    }

    @Override
    public void showError() {

    }

    private List<TreeItem> initList() {
        List<TreeItem> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TreeItem item = new TreeItem();
            item.title = "第一级" + i;
            for (int j = 0; j < 6; j++){
                TreeItem itemChild = new TreeItem();
                itemChild.title = "第二级" + j;
                item.child.add(itemChild);
            }
            list.add(item);
        }
        return list;
    }
}