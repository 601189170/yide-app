package com.yyide.chatim;


import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.base.BaseActivity;


import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate{

    @BindView(R.id.zbarview)
    ZBarView zbarview;
    int i;

    @Override
    public int getContentViewID() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zbarview.setDelegate(this);
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
//        ToastUtils.showShort("onScanQRCodeSuccess==>"+result.toString());
//        if (result.contains("id:")) {
//            ResultRsp bean = JSON.parseObject(result, ResultRsp.class);
//        if (isshow)
//            Identity(DecryptUtils.decrypt(result));
//        }
        if (zbarview!=null) new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                zbarview.startSpot();
            }
        },3000);

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
            if (zbarview!=null){
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
