package com.yyide.chatim;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.activity.ScanLoginActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.net.DingApiStores;
import com.yyide.chatim.view.ScanLoginView;


import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.zbarview)
    ZBarView zbarview;
    int i;
    private String TAG = ScanActivity.class.getSimpleName();

    @Override
    public int getContentViewID() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("扫码");
        zbarview.setDelegate(this);
        back_layout.setOnClickListener(v -> finish());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                zbarview.openFlashlight();
//            }
//        },5000);
//        zbarview.startCamera();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        if (!TextUtils.isEmpty(result) && (result.contains("equipmentSerialNumber") && result.contains("brandStatus"))) {
            //{"scanUrl":"/management/cloud-system/app/user/scan/loginId","equipmentSerialNumber":"equipmentSerialNumber","brandStatus":"brandStatus"}
            WebViewActivity.start(this, BaseConstant.SCAN_URL, result);
        } else if (!TextUtils.isEmpty(result) && result.contains("/management/cloud-system/app/user/scan/")) {
            jupm(this, ScanLoginActivity.class, "result", result);
        } else if (!TextUtils.isEmpty(result) && (result.startsWith("http") || result.equals("https"))) {
            jupm(this, WebViewActivity.class, "url", result);
        } else {
            ToastUtils.showShort(result);
        }
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
//        Toast.makeText(activity, "打开相机出错", Toast.LENGTH_SHORT).show();
    }

    public void stopSM() {
        if (zbarview != null) {
            zbarview.stopCamera();
        }
    }

    public void setSM() {
        if (zbarview != null) {
            zbarview.showScanRect();
            zbarview.startSpot();
        }

        if (zbarview != null && i == 0) {
            zbarview.startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
//            //显示扫描框
            //开始识别
            i = 1;
        }
    }

    private void rxPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);

        mDisposable = rxPermissions
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(permission -> {
                            if (permission.granted) {
                                setSM();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // Denied permission without ask never again
//                                Toast.makeText(ScanActivity.this,
//                                        "Denied permission without ask never again",
//                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // 权限被拒绝
                                new AlertDialog.Builder(ScanActivity.this)
                                        .setTitle("提示")
                                        .setMessage(R.string.permission_camera)
                                        .setPositiveButton("开启", (dialog, which) -> {
                                            Intent localIntent = new Intent();
                                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivity(localIntent);
                                        })
                                        .setNegativeButton("取消", null)
                                        .create().show();
                            }
                        },
                        t -> Log.e(TAG, "onError", t),
                        () -> Log.i(TAG, "OnComplete"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        rxPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSM();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopSM();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (zbarview != null) {
            zbarview.stopCamera();
            zbarview.onDestroy();
        }
    }

    @SuppressLint("MissingPermission")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

}
