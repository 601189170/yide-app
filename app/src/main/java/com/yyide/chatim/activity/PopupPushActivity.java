package com.yyide.chatim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.SplashActivity;
import com.yyide.chatim.activity.newnotice.NoticeConfirmDetailActivity;
import com.yyide.chatim.alipush.MyMessageReceiver;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.PushModel;
import com.yyide.chatim.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class PopupPushActivity extends AndroidPopupActivity {
    static final String TAG = "PopupPushActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 实现通知打开回调方法，获取通知相关信息
     *
     * @param title   标题
     * @param summary 内容
     * @param extMap  额外参数
     */
    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        Log.d(TAG, "OnMiPushSysNoticeOpened, title: " + title + ", content: " + summary + ", extMap: " + extMap);
        try {
            if (!Utils.isAppAlive(BaseApplication.getInstance(), BaseApplication.getInstance().getPackageName())) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
            } else {
                //发送消息类型 1 消息通知 2 代办 3系统通知 4 作业 5课表
                PushModel pushModel = JSON.parseObject(JSON.toJSONString(extMap), PushModel.class);
                if ("1".equals(pushModel.getPushType())) {
                    //通知公告消息
                    if (!TextUtils.isEmpty(pushModel.getSignId())) {
                        Intent intent = new Intent(this, NoticeConfirmDetailActivity.class);
                        intent.putExtra("id", Long.parseLong(pushModel.getSignId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.startActivity(intent);
                    } else {
                        ToastUtils.showShort("消息已被撤回");
                    }
                } else if ("2".equals(pushModel.getPushType())) {
                    //待办
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
                } else if ("3".equals(pushModel.getPushType()) || "6".equals(pushModel.getPushType())) {
                    //系统通知
                    Intent intent = new Intent(this, MessageNoticeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent);
                } else if ("4".equals(pushModel.getPushType())) {
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                } else if ("5".equals(pushModel.getPushType())) {
                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MAIN, ""));
                }
            }
        } catch (Throwable throwable) {
            //throwable.fillInStackTrace();
        }
        finish();
    }
}