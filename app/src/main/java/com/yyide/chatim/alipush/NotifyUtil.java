package com.yyide.chatim.alipush;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * @Description: java类作用描述
 * @Author: liu tao
 * @CreateDate: 2021/8/2 10:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/2 10:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NotifyUtil {
    public static void openNotificationSettings(Context context) {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }

    /**
     * 判断通知权限是否打开
     * @param context
     * @return
     */
    public static boolean checkNotificationsEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkNotificationsChannelEnabled(Context context, String channelID) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) {
            return false;
        }
        NotificationChannel channel = manager.getNotificationChannel(channelID);
        if (channel == null){
            return false;
        }
        return !(channel.getImportance() == NotificationManager.IMPORTANCE_NONE);
    }
}
