package com.yyide.chatim.activity.newnotice;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityNoticePushDetailBinding;
import com.yyide.chatim.databinding.NoticePreviewBinding;
import com.yyide.chatim.dialog.SwitchNoticeTimePop;

public class NoticeTemplateReleaseDetailActivity extends BaseActivity {

    private ActivityNoticePushDetailBinding pushDetailBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_push_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushDetailBinding = ActivityNoticePushDetailBinding.inflate(getLayoutInflater());
        setContentView(pushDetailBinding.getRoot());
        initView();
    }

    private void initView() {
        pushDetailBinding.include.title.setText(R.string.notice_release_title);
        pushDetailBinding.include.tvRight.setText(R.string.notice_preview_title);
        pushDetailBinding.include.tvRight.setVisibility(View.VISIBLE);
        pushDetailBinding.include.backLayout.setOnClickListener(v -> finish());
        pushDetailBinding.include.tvRight.setOnClickListener(v -> showPreView());
        pushDetailBinding.switchPush.setChecked(true);
        pushDetailBinding.switchPush.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pushDetailBinding.clThing.setVisibility(View.INVISIBLE);
            } else {
                pushDetailBinding.clThing.setVisibility(View.VISIBLE);
            }
        });
        pushDetailBinding.clThing.setOnClickListener(v -> showSelectTime());
        pushDetailBinding.clSelect.setOnClickListener(v -> {
        });
    }

    private void showPreView() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        NoticePreviewBinding previewBinding = NoticePreviewBinding.inflate(getLayoutInflater());
        alertDialog.setView(previewBinding.getRoot()).create();
        final AlertDialog dialog = alertDialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (ScreenUtils.getScreenHeight() * 0.75);   //高度设置为屏幕的0.3
        //p.width = (int) (d.getWidth() * 0.5);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效
        //设置主窗体背景颜色为黑色
        previewBinding.tvNoticeContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        previewBinding.getRoot().setOnClickListener(v -> dialog.dismiss());
        dialog.setOnDismissListener(v -> getWindow().getDecorView().setAlpha(1));
        dialog.setOnShowListener(dialog1 -> getWindow().getDecorView().setAlpha(0));
        //previewBinding.tvNoticeTitle.setText("");
        //previewBinding.tvNoticeContent.setText("");
    }

    private void showSelectTime() {
        new SwitchNoticeTimePop(this);
        //showTime();
    }


    private TimePickerDialog mDialogAll;

    private void showTime() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择日期时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_212121))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorPrimary))
                .setWheelItemTextSize(12)
                .build();
        mDialogAll.show(getSupportFragmentManager(), "all");
    }
}