package com.yyide.chatim;




import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;


import com.yyide.chatim.home.ConversationFragment;
import com.yyide.chatim.home.Fragment1;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
//    @BindView(R.id.content)
//    FrameLayout content;
    private View mBaseView;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragment=new Fragment1();

        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, fragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, new CustumFragment()).commitAllowingStateLoss();
//        showNotice(this);



    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }



    private void showNotice(Context context) {
//        String title = indexAd.getTitle();
        // 使用remoteViews去加载自定义布局
        RemoteViews remoteViews = new RemoteViews(AppUtils.getAppPackageName(), R.layout.notification_custom);
//        remoteViews.setTextViewText(R.id.tv_title, title);
//        remoteViews.setTextViewText(R.id.tv_price, "￥"+indexAd.getEndPrice()+"元");
//        remoteViews.setTextViewText(R.id.tv_time, TimeUtils.tsToMs(indexAd.getTime()));
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.fff,null);//将资源文件转化为bitmap
        remoteViews.setImageViewBitmap(R.id.iv_pic, bitmap);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context,NoticeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("渠道ID", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "渠道ID");
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder
                .setSmallIcon(R.mipmap.leak_canary_icon)
                .setContentTitle("aaa")
                .setContentText("bbb")
                .setContentIntent(contentIntent)
                .setContent(remoteViews)
                .setAutoCancel(true)
                .build();
        manager.notify(0, notification);
    }
}
