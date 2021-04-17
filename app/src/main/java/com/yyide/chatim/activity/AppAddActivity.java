package com.yyide.chatim.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.presenter.AppAddPresenter;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.view.AppAddView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

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
    private int positionIndex;

    private BaseQuickAdapter<AppAddRsp.DataBean.RecordsBean, BaseViewHolder> baseQuickAdapter;

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
        baseQuickAdapter = new BaseQuickAdapter<AppAddRsp.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_app_add) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, AppAddRsp.DataBean.RecordsBean item) {
                ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
                TextView tv_add = holder.getView(R.id.tv_add);
                if (!TextUtils.isEmpty(item.getImg())) {
                    GlideUtil.loadCircleImage(AppAddActivity.this, item.getImg(), iv_app_icon);
                }
                holder.setText(R.id.tv_name, item.getName());
                if (item.getIsAdd()) {
                    tv_add.setClickable(false);
                    tv_add.setText("已添加");
                    tv_add.setBackgroundResource(R.drawable.app_add_bg);
                    tv_add.setCompoundDrawablePadding(0);
                    tv_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tv_add.setTextColor(getResources().getColor(R.color.text_999999));
                } else {
                    tv_add.setClickable(true);
                    tv_add.setText("添加");
                    tv_add.setCompoundDrawablePadding(10);
                    tv_add.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_app_add), null, null, null);
                    tv_add.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_add.setBackgroundResource(R.drawable.app_add_bg_blue);
                    tv_add.setOnClickListener(v -> {
                        recordsBean = item;
                        positionIndex = holder.getAdapterPosition();
                        mvpPresenter.addApp(item.getId());
                    });
                }
            }
        };

        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppAddRsp.DataBean.RecordsBean item = (AppAddRsp.DataBean.RecordsBean) adapter.getItem(position);

        });
        recyclerview.setAdapter(baseQuickAdapter);
    }

    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Override
    public void getAppAppListSuccess(AppAddRsp model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2 && model.getData() != null) {
            baseQuickAdapter.setList(model.getData().getRecords());
        }
    }

    @Override
    public void getAppAppListFail(String msg) {

    }

    @Override
    public void addAppSuccess(ResultBean model) {
        ToastUtils.showShort(model.getMsg());
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (recordsBean != null) {
                recordsBean.setIsAdd(true);
//                baseQuickAdapter.notifyItemChanged(positionIndex, recordsBean);
                baseQuickAdapter.notifyDataSetChanged();
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