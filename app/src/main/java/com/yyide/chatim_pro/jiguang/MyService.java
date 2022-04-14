package com.yyide.chatim_pro.jiguang;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

//import cn.jpush.android.service.JCommonService;
// 极光推送释放代码
//public class MyService extends JCommonService {
public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
