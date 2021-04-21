package com.yyide.chatim.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.utils.TakePicUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class WebViewActivity extends BaseActivity {

    @Override
    public int getContentViewID() {
        return R.layout.activity_webview;
    }

    String currentUrl;
    private FrameLayout fl_webview;
    private ProgressBar pb_webview;
    private WebSettings webSettings;
    private WebView mWebView;
    private String type;
    private ValueAnimator pbAnim;

    private android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private android.webkit.ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUrl = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        initView();

        initAnimtor();

        initWebView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        fl_webview = findViewById(R.id.fl_webview);
        pb_webview = findViewById(R.id.pb_webview);
    }

    private void initAnimtor() {
        pbAnim = ValueAnimator.ofFloat(0f, 70f);
        pbAnim.setDuration(3000);
        pbAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        //pbAnim.addUpdateListener(animation -> pb_webview.setProgress(animation.getAnimatedValue()));
        pbAnim.start();
    }

    private void initWebView() {
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
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbAnim.cancel();
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(pb_webview.getProgress(), 100f);
                    valueAnimator.setDuration((long) (1500 * (1 - pb_webview.getProgress() / 100f)));
                    //alueAnimator.addUpdateListener(animation -> pb_webview.setProgress((Integer) animation.getAnimatedValue()));
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            pb_webview.setVisibility(View.GONE);
                        }
                    });
                    valueAnimator.start();
                }

            }

            /**
             * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
             */
            private void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg) {
                Log.e("WangJ", "运行方法 openFileChooser-1");
                // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
                mUploadCallbackBelow = uploadMsg;
                takePhoto();
            }

            /**
             * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
             */
            public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
             */
            public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * API >= 21(Android 5.0.1)回调此方法
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                Log.e("WangJ", "运行方法 onShowFileChooser");
                // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
                mUploadCallbackAboveL = valueCallback;
                takePhoto();
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
                    pbAnim.start();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.loadUrl("javascript:sendH5Event('" + "setToken" + "','" + SpData.User().token + "')");
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

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);


        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseBelow");
        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }

    @JavascriptInterface
    public void postMessage(String msg) {
        ToastUtils.showShort("msg" + msg);
    }

    @JavascriptInterface
    public void backApp(){
        finish();
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