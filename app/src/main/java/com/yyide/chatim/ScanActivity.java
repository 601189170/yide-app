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
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.activity.ScanLoginActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.activity.scancode.BindingEquipmentActivity;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;
import com.yyide.chatim.view.ScanLoginView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Log.e(TAG, "onScanQRCodeSuccess: " + result);
        final JSONObject jsonObject = JSON.parseObject(result);
        final String scanUrl = jsonObject.getString("scanUrl");
        if (!TextUtils.isEmpty(scanUrl)) {
            if (scanUrl.contains("/brand/class-brand-management/Android/verify")) {
                //app验证接口
                final String classesId = jsonObject.getString("classesId");
                final String siteId = jsonObject.getString("siteId");
                final String checkId = jsonObject.getString("checkId");
                verify(checkId,classesId,siteId);
            } else if (scanUrl.contains("/brand/class-brand-management/app/loginCheck/scan/")) {
                final String code = scanUrl.substring(scanUrl.lastIndexOf("/")+1);
                Log.e(TAG, "code: "+code );
                //扫码登录 0:已绑定 1 未绑定
                final String equipmentSerialNumber = jsonObject.getString("equipmentSerialNumber");
                final String brandStatus = jsonObject.getString("brandStatus");
                final String bindStatus = jsonObject.getString("bindStatus");
                Intent intent = new Intent(this, BindingEquipmentActivity.class);
                intent.putExtra("equipmentSerialNumber", equipmentSerialNumber);
                intent.putExtra("brandStatus", brandStatus);
                intent.putExtra("code", code);
                intent.putExtra("brandStatus", brandStatus);
                intent.putExtra("bindStatus", bindStatus);
                startActivity(intent);
            } else if (result.startsWith("http") || result.equals("https")) {
                jupm(this, WebViewActivity.class, "url", result);
            }
        }


//        if (!TextUtils.isEmpty(result) && (result.contains("equipmentSerialNumber") && result.contains("brandStatus"))) {
//            //{"scanUrl":"/management/cloud-system/app/user/scan/loginId","equipmentSerialNumber":"equipmentSerialNumber","brandStatus":"brandStatus"}
//            WebViewActivity.start(this, BaseConstant.SCAN_URL, result);
//        } else if (!TextUtils.isEmpty(result) && result.contains("/management/cloud-system/app/user/scan/")) {
//            jupm(this, ScanLoginActivity.class, "result", result);
//        } else if (!TextUtils.isEmpty(result) && (result.startsWith("http") || result.equals("https"))) {
//            jupm(this, WebViewActivity.class, "url", result);
//        } else {
//            ToastUtils.showShort(result);
//        }
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

    private void verify(String checkId,String classesId,String siteId) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("checkId", checkId);
        map.put("classesId", classesId);
        map.put("siteId", siteId);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        DingApiStores dingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
        dingApiStores.brandVerify(body).enqueue(new Callback<BaseRsp>() {
            @Override
            public void onResponse(@NonNull Call<BaseRsp> call, @NonNull Response<BaseRsp> response) {
                Log.e(TAG, "onResponse: "+response.body());
                final BaseRsp baseRsp = response.body();
                if (baseRsp != null){
                    ToastUtils.showShort(baseRsp.getMsg());
                }else {
                    ToastUtils.showShort("验证失败!");
                }

            }

            @Override
            public void onFailure(@NonNull Call<BaseRsp> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                ToastUtils.showShort("验证失败!");
            }
        });
    }

}
