package com.yyide.chatim.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yyide.chatim.R;

public class RxPermissionUtils {

    /**
     * 拨打电话权限
     * @param context
     * @param phone
     */
    public static void callPhone(Context context, String phone) {
        if(TextUtils.isEmpty(phone)) return;
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(granted -> {
            if (granted) {
                if (!TextUtils.isEmpty(phone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    context.startActivity(intent);
                } else {
                    ToastUtils.showShort("空手机号，无法拨打电话");
                }
            } else {
                // 权限被拒绝
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage(R.string.permission_call_phone)
                        .setPositiveButton("开启", (dialog, which) -> {
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                            context.startActivity(localIntent);
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }

}
