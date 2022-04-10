package com.yyide.chatim.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.AppAddAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.selectParentStudent;
import com.yyide.chatim.presenter.AppAddPresenter;
import com.yyide.chatim.view.AppAddView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class AppAddActivity extends BaseMvpActivity<AppAddPresenter> implements AppAddView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private int pageSize = 20;
    private int pageNum = 1;
    private AppAddRsp.DataBean.RecordsBean recordsBean;
    private AppAddAdapter addAdapter;
    @Override
    public int getContentViewID() {
        return R.layout.activity_app_add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("添加");
        initView();
        mvpPresenter.getAppAddList(pageSize, pageNum);


    }

    @Override
    protected AppAddPresenter createPresenter() {
        return new AppAddPresenter(this);
    }

    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        addAdapter = new AppAddAdapter();
        addAdapter.setAddClickListener(item -> {
            recordsBean = item;
            mvpPresenter.addApp(item.getId());
        });
        addAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            //上拉加载时取消下拉刷新
            addAdapter.getLoadMoreModule().setEnableLoadMore(true);
            //请求数据
            pageNum++;
            mvpPresenter.getAppAddList(pageSize, pageNum);
        });
        addAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        addAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        recyclerview.setAdapter(addAdapter);
    }

    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.back_layout) {
            finish();
        }
    }

    @Override
    public void getAppAppListSuccess(AppAddRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS && model.getData() != null) {
            if (pageNum == 1) {
                if (model != null && model.getData() != null) {
                    addAdapter.setList(model.getData().getRecords());
                }
            } else {
                if (model != null && model.getData() != null) {
                    addAdapter.addData(model.getData().getRecords());
                }
            }
            if (model.getData() != null && model.getData().getRecords() != null) {
                if (model.getData().getRecords().size() < pageSize) {
                    //如果不够一页,显示没有更多数据布局
                    addAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    addAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }
    }

    @Override
    public void getAppAppListFail(String msg) {

    }

    @Override
    public void addAppSuccess(ResultBean model) {
        ToastUtils.showShort(model.getMessage());
        if (model.getCode() == BaseConstant.REQUEST_SUCCESS) {
            if (recordsBean != null) {
                recordsBean.setIsAdd(true);
//                baseQuickAdapter.notifyItemChanged(positionIndex, recordsBean);
                addAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_APP_MANAGER, ""));
            }
        }
    }

    @Override
    public void addAppFail(String msg) {

    }

    @Override
    public void showError() {

    }
}