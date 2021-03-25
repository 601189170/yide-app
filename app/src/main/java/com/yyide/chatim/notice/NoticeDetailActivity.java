package com.yyide.chatim.notice;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.notice.presenter.NoticeDetailPresenter;
import com.yyide.chatim.notice.view.NoticeDetailView;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeDetailActivity extends BaseMvpActivity<NoticeDetailPresenter> implements NoticeDetailView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_notice_content)
    TextView tv_notice_content;

    private int TYPE_CONFIRM = 1;//已确认
    private int TYPE_UNCONFIRM = 2;//未确认
    private int TYPE_STATISTICAL = 3;//通知统计

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("通知详情");
        tv_notice_content.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @OnClick(R.id.back_layout)
    public void click(){
        finish();
    }

    @Override
    protected NoticeDetailPresenter createPresenter() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}