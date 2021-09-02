package com.yyide.chatim.alipush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.alibaba.sdk.android.push.notification.PushData;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.MainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SplashActivity;
import com.yyide.chatim.activity.MessageNoticeActivity;
import com.yyide.chatim.activity.newnotice.NoticeConfirmDetailActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.jiguang.ExampleUtil;
import com.yyide.chatim.jiguang.LocalBroadcastManager;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.PushModel;
import com.yyide.chatim.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyMessageReceiver extends MessageReceiver {
    private int requestCode = 1;
    private int messageId = 1;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        processCustomMessage(context, title, summary, JSON.toJSONString(extraMap));
        // final Intent intent = new Intent(context, MyMessageReceiver.class);
        //intent.setAction("notification_clicked");
        //intent.putExtra("extras",JSON.toJSONString(extraMap));
        //showNotice(context,title,summary,intent);
        PushModel pushModel = JSON.parseObject(JSON.toJSONString(extraMap), PushModel.class);
        if (pushModel != null) {
            if ("2".equals(pushModel.getPushType())) {
                //更新首页消息待办
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_MESSAGE_TODO, ""));
            }
        }
    }

    @Override
    public void onMessage(Context context, @NonNull CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        try {
            if (!Utils.isAppAlive(BaseApplication.getInstance(), BaseApplication.getInstance().getPackageName())) {
                Intent intent = new Intent(context, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else {
                //发送消息类型 1 消息通知 2 代办 3系统通知 4 作业 5课表
                PushModel pushModel = JSON.parseObject(extraMap, PushModel.class);
                if ("1".equals(pushModel.getPushType())) {
                    //通知公告消息
                    if (!TextUtils.isEmpty(pushModel.getSignId())) {
                        Intent intent = new Intent(context, NoticeConfirmDetailActivity.class);
                        intent.putExtra("id", Long.parseLong(pushModel.getSignId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.showShort("消息已被撤回");
                    }
                } else if ("2".equals(pushModel.getPushType())) {
                    //跳转至待办
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
                } else if ("3".equals(pushModel.getPushType()) || "6".equals(pushModel.getPushType())) {
                    //系统通知
                    Intent intent = new Intent(context, MessageNoticeActivity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
//                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                } else if ("4".equals(pushModel.getPushType())) {
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                } else if ("5".equals(pushModel.getPushType())) {
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                } else {
                    //其他统一跳到首页
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        onNotificationOpened(context, title, summary, extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, String title, String summary, String extras) {
        if (MainActivity.isForeground) {
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, summary);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }

    private void showNotice(Context context, String title, String msg, Intent intent) {
        requestCode++;
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "1");
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Notification notification = builder
                .setSmallIcon(R.mipmap.ic_launcher_logo)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setOngoing(true)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(messageId++, notification);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
        Log.e("MyMessageReceiver", "onNotificationRemoved");
        if (!TextUtils.isEmpty(action) && "notification_clicked".equals(action)) {
            //处理点击事件
            final String extras = intent.getStringExtra("extras");
            onNotificationOpened(context, "", "", extras);
        }
    }
}