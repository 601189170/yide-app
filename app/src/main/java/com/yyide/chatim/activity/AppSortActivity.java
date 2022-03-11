package com.yyide.chatim.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.AppSortAdapter;
import com.yyide.chatim.adapter.helper.SimpleItemTouchHelperCallback;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.ActivityAppSortBinding;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.AppSortPresenter;
import com.yyide.chatim.view.AppSortView;

import org.greenrobot.eventbus.EventBus;

public class AppSortActivity extends BaseMvpActivity<AppSortPresenter> implements AppSortView, AppSortAdapter.OnDragStartListener {
    private ActivityAppSortBinding mViewBinding;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public int getContentViewID() {
        return R.layout.activity_app_sort;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityAppSortBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        initView();
    }

    private void initView() {
        MyAppListRsp appList = (MyAppListRsp) getIntent().getSerializableExtra("appBean");
        mViewBinding.top.title.setText("排序");
        mViewBinding.top.tvRight.setText(R.string.confirm);
        mViewBinding.top.tvRight.setVisibility(View.VISIBLE);
        //剔除添加Item
        if (appList != null && appList.getData() != null && appList.getData().size() > 0) {
            appList.getData().remove(appList.getData().size() - 1);
        }
        AppSortAdapter adapter = new AppSortAdapter(appList.getData(), this);
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerview.setHasFixedSize(true);
        mViewBinding.recyclerview.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mViewBinding.recyclerview);
        mViewBinding.top.tvRight.setOnClickListener(v -> mvpPresenter.appSort(adapter.getParams()));
        mViewBinding.top.backLayout.setOnClickListener(v -> finish());
    }

    @Override
    protected AppSortPresenter createPresenter() {
        return new AppSortPresenter(this);
    }

    @Override
    public void sendAppSortSuccess(ResultBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_APP_MANAGER, ""));
            finish();
        }
    }

    @Override
    public void sendAppSortFail(String msg) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void onDragStarted(AppSortAdapter.ItemViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }
}