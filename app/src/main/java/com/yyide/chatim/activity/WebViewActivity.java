package com.yyide.chatim.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.Utils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.WebModel;

public class WebViewActivity extends BaseActivity {

    String currentUrl;
    private FrameLayout fl_webview;
    private ProgressBar pb_webview;
    private WebSettings webSettings;
    private WebView mWebView;
    private String type;
    private TextView tvTitle;
    private ImageView imageView;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private static final String PARAM_URL = "url";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_JSON = "json";
    private static final String PARAM_TITLE = "title";
    private String json;
    private String title;

    @Override
    public int getContentViewID() {
        return R.layout.activity_webview;
    }

    public static void start(Context context, String url, String json) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_JSON, json);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param url     HTTP地址
     * @param title   标题-会获取网页title
     */
    public static void startTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUrl = getIntent().getStringExtra(PARAM_URL);
        title = getIntent().getStringExtra(PARAM_TITLE);
        type = getIntent().getStringExtra(PARAM_TYPE);
        json = getIntent().getStringExtra(PARAM_JSON);
        initView();
        initWebView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        View view = findViewById(R.id.top);
        fl_webview = findViewById(R.id.fl_webview);
        pb_webview = findViewById(R.id.pb_webview);
        tvTitle = findViewById(R.id.title);
        imageView = findViewById(R.id.back_layout);
        if (currentUrl.contains("/classcardapp/")) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        imageView.setImageResource(R.drawable.schedule_add_label_close_dialog_icon);
        imageView.setOnClickListener(v -> finish());
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
//        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
//            mWebView.loadUrl("javascript:sendH5Event('" + "softHeight" + "','" + height + "')");
//        });
        mWebView = new WebView(Utils.getApp().getApplicationContext());

        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);
        fl_webview.addView(mWebView, 0);

        webSettings = mWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);

//        webSettings.setMixedContentMode(WebSettings.);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setUseWideViewPort(true);
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
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && !title.equals("登录跳转")) {
                    tvTitle.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb_webview.setVisibility(View.GONE);
                } else {
                    pb_webview.setProgress(newProgress);
                }
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
        mWebView.loadUrl(currentUrl);
        mWebView.addJavascriptInterface(WebViewActivity.this, "android");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (pb_webview.getVisibility() == View.GONE) {
                    pb_webview.setProgress(0);
                    pb_webview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (SpData.User() != null) {
                    Log.d("onPageFinished", "SpData.User().getToken(:" + SpData.User().data.accessToken);
                    mWebView.loadUrl("javascript:sendH5Event('" + "setToken" + "','" + SpData.User().data.accessToken + "')");
                    if (SpData.getIdentityInfo() != null) {
                        mWebView.loadUrl("javascript:sendH5Event('" + "setSchoolId" + "','" + SpData.getIdentityInfo().schoolId + "')");
                    }
                    mWebView.loadUrl("javascript:sendH5Event('" + "setScanJson" + "','" + json + "')");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });
    }

    @JavascriptInterface
    public String postMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            WebModel webModel = JSON.parseObject(msg, WebModel.class);
            if (webModel != null) {
                if ("backApp".equalsIgnoreCase(webModel.enentName)) {
                    finish();
                } else if ("getToken".equalsIgnoreCase(webModel.enentName)) {
                    return SpData.User() != null ? SpData.User().data.accessToken : "";
                }
            }
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            // Uri result = (((data == null) || (resultCode != RESULT_OK)) ? null : data.getData());
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //这里uploadMessage跟uploadMessageAboveL在不同系统版本下分别持有了
            //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
            //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    // 2.回调方法触发本地选择文件
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");//图片上传
//        i.setType("file/*");//文件上传
        i.setType("*/*");//文件上传
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            fl_webview.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }
}