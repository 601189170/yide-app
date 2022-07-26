package com.yyide.chatim_pro.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.notice.presenter.NoticeDetailPresenter;
import com.yyide.chatim_pro.activity.notice.view.NoticeDetailView;
import com.yyide.chatim_pro.base.BaseMvpActivity;
import com.yyide.chatim_pro.model.BaseRsp;
import com.yyide.chatim_pro.model.NoticeDetailRsp;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeDetailActivity extends BaseMvpActivity<NoticeDetailPresenter> implements NoticeDetailView {
    private static final String TAG = "NoticeDetailActivity";
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

    @BindView(R.id.gp_bottom)
    Group gp_bottom;

    private int TYPE_CONFIRM = 1;//已确认
    private int TYPE_UNCONFIRM = 2;//未确认
    private int TYPE_STATISTICAL = 3;//通知统计
    private int TYPE_JG_PUSH = 4;//极光推送通知详情

    private long id;
    private int type;
    private String status;

    private String signId;
    private int totalNumber;
    private int readNumber;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText(R.string.notice_confirm_detail_title);
        tv_notice_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getLongExtra("id", 0);
        signId = getIntent().getStringExtra("signId");
        status = getIntent().getStringExtra("status");
        Log.e(TAG, "onCreate: type:" + type + "signId:"+signId +",id:" + id + ",status:" + status);
        final String sendObject = getIntent().getStringExtra("sendObject");
        if (sendObject != null  && (sendObject.equals("学生") || sendObject.equals("2"))){
            gp_bottom.setVisibility(View.GONE);
        }
        //initView(type);
        initData();
    }

    private void initView(int type) {
        if ("1".equals(status) && type == 1) {
            tv_confirm.setSelected(false);
            tv_confirm.setClickable(false);
            tv_confirm.setText("已确认");
            tv_confirm.setBackground(this.getDrawable(R.drawable.bg_corners_gray2_18));
            tv_confirm.setTextColor(this.getResources().getColor(R.color.black10));
        }
        if (type == TYPE_CONFIRM) {
            tv_confirm_number.setVisibility(View.GONE);
            tv_notice_confirm.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.VISIBLE);
        } else if (type == TYPE_UNCONFIRM) {
            tv_confirm_number.setVisibility(View.GONE);
            tv_notice_confirm.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.VISIBLE);
        } else if (type == TYPE_STATISTICAL) {//TYPE_STATISTICAL
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
        loadNoticeDetail();
    }

    private void loadNoticeDetail() {
        if (!TextUtils.isEmpty(signId)) {//推送消息ID
            mvpPresenter.noticeDetail(id, Long.parseLong(signId), TYPE_JG_PUSH);
        } else {
            mvpPresenter.noticeDetail(id, 0, type);
        }
    }

    @OnClick(R.id.back_layout)
    public void click() {
        Intent intent = getIntent();
        intent.putExtra("update", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.tv_confirm)
    public void confirm() {
        //去确认
        mvpPresenter.updateMyNoticeDetails(id);
    }

    @OnClick(R.id.tv_notice_confirm)
    public void confirmStatistics() {
        //进入通知统计界面
        Intent intent = new Intent(this, NoticeConfirmDetailActivity.class);
        intent.putExtra("totalNumber", totalNumber);
        intent.putExtra("readNumber", readNumber);
        if (TextUtils.isEmpty(signId)){
            intent.putExtra("signId", id);
        }else {
            try {
                final Long value = Long.valueOf(signId);
                intent.putExtra("signId", value);
            }catch (NumberFormatException exception){
                ToastUtils.showShort("不好意思，出错啦，无法查看通知统计");
                return;
            }
        }

        startActivity(intent);
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

    @Override
    public void noticeDetail(NoticeDetailRsp noticeDetailRsp) {
        NoticeDetailRsp.DataBean data = noticeDetailRsp.getData();
        Log.e(TAG, "noticeDetail: " + noticeDetailRsp.toString());
        if (noticeDetailRsp.getCode() == 200) {
            if (data != null) {
                initView(type);
                id = data.getId();//出推送过过来的通知确认
                String productionTime = DateUtils.formatTime(data.getProductionTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm");
                tv_notice_title.setText(data.getTitle());
                tv_notice_author.setText(String.format(this.getString(R.string.notice_release_obj_text), data.getProductionTarget()));
                tv_notice_time.setText(productionTime);
                final String content = data.getContent();
                tv_notice_content.setText(StringUtils.firstLineRetractText(this,content));

                //notice_confirm_number_text
                String string = getString(R.string.notice_confirm_number_text);
                String confirm_number = String.format(string, data.getReadNumber(), data.getTotalNumber());
                tv_confirm_number.setText(confirm_number);
                signId = data.getSignId() + "";
                totalNumber = data.getTotalNumber();
                readNumber = data.getReadNumber();
                status = data.getStatus();
                Log.e(TAG, "noticeDetail: status=" + ("1".equals(status)) + ",type=" + type);
                if ("1".equals(status) && (type == 1 || type == TYPE_CONFIRM || type == TYPE_JG_PUSH || type == TYPE_UNCONFIRM)) {
                    Log.e(TAG, "noticeDetail: " + type);
                    tv_confirm.setSelected(false);
                    tv_confirm.setClickable(false);
                    tv_confirm.setText("已确认");
                    tv_confirm.setBackground(this.getDrawable(R.drawable.bg_corners_gray2_18));
                    tv_confirm.setTextColor(this.getResources().getColor(R.color.black10));
                }
            }
            return;
        }
        ToastUtils.showShort("获取数据失败！");
        new Handler().postDelayed(() -> {
            finish();
        }, 1000);
    }

    @Override
    public void noticeDetailFail(String msg) {
        ToastUtils.showShort(msg);
        new Handler().postDelayed(() -> {
            finish();
        }, 1000);

    }

    @Override
    public void updateMyNotice(BaseRsp baseRsp) {
        if (baseRsp.getCode() == 200) {
            //ToastUtils.showShort("确认" + baseRsp.getMsg());
            //请求成功后，刷新界面
            loadNoticeDetail();
//            new Handler().postDelayed(() -> {
//                Intent intent = getIntent();
//                intent.putExtra("update", true);
//                setResult(RESULT_OK, intent);
//                finish();
//            }, 1000);
        }
    }

    @Override
    public void updateFail(String msg) {
        ToastUtils.showShort(msg);
        new Handler().postDelayed(() -> {
            finish();
        }, 1000);
    }
}