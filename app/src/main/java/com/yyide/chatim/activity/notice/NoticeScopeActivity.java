package com.yyide.chatim.activity.notice;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter;
import com.baozi.treerecyclerview.adpater.TreeRecyclerType;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeSelectItemGroup;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.activity.notice.cart.CartBean;
import com.yyide.chatim.activity.notice.cart.CartItem;
import com.yyide.chatim.activity.notice.presenter.NoticeScopePresenter;
import com.yyide.chatim.activity.notice.view.NoticeScopeView;

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
    private TreeRecyclerAdapter adapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_EXPAND);

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_scope;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_scope_title);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        List<CartBean> beans = new ArrayList<>();
        beans.add(new CartBean(3));

        List<TreeItem> groupItem = ItemHelperFactory.createItems(beans);

        adapter.getItemManager().replaceAllItem(groupItem);
        adapter.setOnItemClickListener((viewHolder, position) -> {
            //因为外部和内部会冲突
            TreeItem item = adapter.getData(position);
            if (item != null) {
                item.onClick(viewHolder);
            }
            notifyPrice();
        });
        initView();
        notifyPrice();
    }

    /**
     * 更新价格
     */
    public void notifyPrice() {
        boolean isSelectAll = false;//默认全选
        int number = 0;
        for (TreeItem item : adapter.getData()) {
            if (item instanceof TreeSelectItemGroup) {
                TreeSelectItemGroup group = (TreeSelectItemGroup) item;
                if (!group.isSelect()) {//是否有选择的子类
                    //有一个没选则不是全选
                    isSelectAll = false;
                    continue;
                }
                if (!group.isSelectAll()) {//是否全选了子类
                    //有一个没选则不是全选
                    isSelectAll = false;
                }
                List<TreeItem> selectItems = group.getSelectItems();
                for (TreeItem child : selectItems) {
                    if (child instanceof CartItem) {
                        Integer data = (Integer) child.getData();
                        number += data;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        tv_selected_member.setText(getString(R.string.notice_select_number, number));
        checkBox.setChecked(isSelectAll);
    }

    public void initView() {
        checkBox.setOnClickListener(v -> {
            for (TreeItem item : adapter.getData()) {
                if (item instanceof TreeSelectItemGroup) {
                    TreeSelectItemGroup group = (TreeSelectItemGroup) item;
                    group.selectAll(((CheckBox) v).isChecked());
                }
            }
            notifyPrice();
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

}