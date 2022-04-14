package com.yyide.chatim_pro.activity;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;
import com.yyide.chatim_pro.model.HelpItemRep;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_helpTitle)
    TextView tv_helpTitle;
    @BindView(R.id.tc_content)
    TextView tc_content;
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    public int getContentViewID() {
        return R.layout.activity_help_info_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("入门指南");
        initWebView();
        HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) getIntent().getSerializableExtra("itemBean");
        if (itemBean != null) {
            tv_helpTitle.setText(itemBean.getName());
            mWebView.loadDataWithBaseURL(null, itemBean.getMessage(), "text/html" , "utf-8", null);//加载html数据
        }
    }
    private WebSettings webSettings;

    private void initWebView() {
        webSettings = mWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        //设置默认字体大小
        webSettings.setDefaultFontSize(40);
//        webSettings.setTextZoom(120);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(true); //隐藏原生的缩放控件

        webSettings.setAllowFileAccess(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }
}
