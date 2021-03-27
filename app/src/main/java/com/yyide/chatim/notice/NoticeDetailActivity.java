package com.yyide.chatim.notice;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
    @BindView(R.id.tv_notice_title)
    TextView tv_notice_title;
    @BindView(R.id.tv_notice_author)
    TextView tv_notice_author;
    @BindView(R.id.tv_notice_time)
    TextView tv_notice_time;
    @BindView(R.id.tv_notice_content)
    TextView tv_notice_content;
    @BindView(R.id.tv_confirm_number)
    TextView tv_confirm_number;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.tv_notice_confirm)
    TextView tv_notice_confirm;

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
        title.setText(R.string.notice_confirm_detail_title);
        tv_notice_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        int type = getIntent().getIntExtra("type", 0);
        initView(type);
        initData();
    }

    private void initView(int type) {
       if(type == TYPE_CONFIRM){
           tv_confirm_number.setVisibility(View.GONE);
           tv_notice_confirm.setVisibility(View.GONE);
           tv_confirm.setVisibility(View.VISIBLE);
       } else if(type == TYPE_UNCONFIRM){
           tv_confirm_number.setVisibility(View.GONE);
           tv_notice_confirm.setVisibility(View.GONE);
           tv_confirm.setVisibility(View.VISIBLE);
       } else if(type == TYPE_STATISTICAL){//TYPE_STATISTICAL
           tv_confirm.setVisibility(View.GONE);
           tv_confirm_number.setVisibility(View.VISIBLE);
           tv_notice_confirm.setVisibility(View.VISIBLE);
       }
    }

    private void initData() {
//        tv_notice_title.setText();
//        tv_notice_author.setText();
//        tv_notice_time.setText();
//        tv_notice_content.setText();
    }

    @OnClick(R.id.back_layout)
    public void click() { finish(); }

    @OnClick(R.id.tv_confirm)
    public void confirm(){

    }

    @Override
    protected NoticeDetailPresenter createPresenter() {
        return new NoticeDetailPresenter(this);
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