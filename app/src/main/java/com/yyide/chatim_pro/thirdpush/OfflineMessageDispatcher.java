package com.yyide.chatim_pro.thirdpush;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageContainerBean;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;
import com.yyide.chatim_pro.BaseApplication;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SplashActivity;
import com.yyide.chatim_pro.chat.ChatActivity;
import com.yyide.chatim_pro.chat.helper.IBaseLiveListener;
import com.yyide.chatim_pro.chat.helper.TUIKitLiveListenerManager;
import com.yyide.chatim_pro.utils.BrandUtil;
import com.yyide.chatim_pro.utils.Constants;
import com.yyide.chatim_pro.utils.DemoLog;

import java.util.Map;
import java.util.Set;

public class OfflineMessageDispatcher {

    private static final String TAG = OfflineMessageDispatcher.class.getSimpleName();

    public static OfflineMessageBean parseOfflineMessage(Intent intent) {
        DemoLog.i(TAG, "intent: " + intent);
        if (intent == null) {
            return null;
        }
        Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle);
        if (bundle == null) {
            String ext = VIVOPushMessageReceiverImpl.getParams();
            if (!TextUtils.isEmpty(ext)) {
                return getOfflineMessageBeanFromContainer(ext);
            }
            return null;
        } else {
            String ext = bundle.getString("ext");
            DemoLog.i(TAG, "push custom data ext: " + ext);
            if (TextUtils.isEmpty(ext)) {
                if (BrandUtil.isBrandXiaoMi()) {
                    ext = getXiaomiMessage(bundle);
                    return getOfflineMessageBeanFromContainer(ext);
                } else if (BrandUtil.isBrandOppo()) {
                    ext = getOPPOMessage(bundle);
                    return getOfflineMessageBean(ext);
                }
            } else {
                return getOfflineMessageBeanFromContainer(ext);
            }
            return null;
        }
    }

    private static String getXiaomiMessage(Bundle bundle) {
        MiPushMessage miPushMessage = (MiPushMessage) bundle.getSerializable(PushMessageHelper.KEY_MESSAGE);
        if (miPushMessage == null) {
            return null;
        }
        Map extra = miPushMessage.getExtra();
        return extra.get("ext").toString();
    }

    private static String getOPPOMessage(Bundle bundle) {
        Set<String> set = bundle.keySet();
        if (set != null) {
            for (String key : set) {
                Object value = bundle.get(key);
                DemoLog.i(TAG, "push custom data key: " + key + " value: " + value);
                if (TextUtils.equals("entity", key)) {
                    return value.toString();
                }
            }
        }
        return null;
    }

    private static OfflineMessageBean getOfflineMessageBeanFromContainer(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return null;
        }
        OfflineMessageContainerBean bean = null;
        try {
            bean = new Gson().fromJson(ext, OfflineMessageContainerBean.class);
        } catch (Exception e) {
            DemoLog.w(TAG, "getOfflineMessageBeanFromContainer: " + e.getMessage());
        }
        if (bean == null) {
            return null;
        }
        return offlineMessageBeanValidCheck(bean.entity);
    }

    private static OfflineMessageBean getOfflineMessageBean(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return null;
        }
        OfflineMessageBean bean = new Gson().fromJson(ext, OfflineMessageBean.class);
        return offlineMessageBeanValidCheck(bean);
    }

    private static OfflineMessageBean offlineMessageBeanValidCheck(OfflineMessageBean bean) {
        if (bean == null) {
            return null;
        } else if (bean.version != 1
                || (bean.action != OfflineMessageBean.REDIRECT_ACTION_CHAT
                && bean.action != OfflineMessageBean.REDIRECT_ACTION_CALL)) {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            String label = String.valueOf(packageManager.getApplicationLabel(BaseApplication.getInstance().getApplicationInfo()));
            ToastUtil.toastLongMessage(BaseApplication.getInstance().getString(R.string.you_app) + label + BaseApplication.getInstance().getString(R.string.low_version));
            DemoLog.e(TAG, "unknown version: " + bean.version + " or action: " + bean.action);
            return null;
        }
        return bean;
    }

    public static boolean redirect(final OfflineMessageBean bean) {
        if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CHAT) {
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setType(bean.chatType);
            chatInfo.setId(bean.sender);
            chatInfo.setChatName(bean.nickname);
            Intent chatIntent = new Intent(BaseApplication.getInstance(), ChatActivity.class);
            chatIntent.putExtra(Constants.CHAT_INFO, chatInfo);
            chatIntent.putExtra(Constants.IS_OFFLINE_PUSH_JUMP, true);
            chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent mainIntent = new Intent(BaseApplication.getInstance(), SplashActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getInstance().startActivities(new Intent[]{mainIntent, chatIntent});
            return true;
        } else if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CALL) {
            IBaseLiveListener baseCallListener = TUIKitLiveListenerManager.getInstance().getBaseCallListener();
            if (baseCallListener != null) {
                baseCallListener.redirectCall(bean);
            }
        }
        return true;
    }
}
