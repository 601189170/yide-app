package com.yyide.chatim.chat.scenes;

import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.liteav.model.CallModel;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.rtmp.TXLiveBase;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.NewMainActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.chat.helper.IBaseLiveListener;
import com.yyide.chatim.chat.helper.TUIKitLiveListenerManager;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;

public class SceneManager {
    private static final String TAG = SceneManager.class.getSimpleName();

    public static void init(BaseApplication application, String licenseUrl, String licenseKey) {
        if (application != null && licenseUrl != null && licenseKey != null) {
            TXLiveBase.getInstance().setLicence(application, licenseUrl, licenseKey);
        }

        TUIKitLiveListenerManager.getInstance().registerCallListener(new SceneLiveController());

    }

    static class SceneLiveController implements IBaseLiveListener {

        @Override
        public void handleOfflinePushCall(Intent intent) {
            if (intent == null) {
                return;
            }
            final CallModel model = (CallModel) intent.getSerializableExtra(Constants.CALL_MODEL);
            if (model != null) {
                if (TextUtils.isEmpty(model.groupId)) {
                    DemoLog.e(TAG, "AVCall groupId is empty");
                } else {
                    ((TRTCAVCallImpl) (TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance()))).
                            processInvite(model.callId, model.sender, model.groupId, model.invitedList, model.data);
                }
            }
        }

        @Override
        public void handleOfflinePushCall(OfflineMessageBean bean) {
            if (bean == null || bean.content == null) {
                return;
            }
            final CallModel model = new Gson().fromJson(bean.content, CallModel.class);
            DemoLog.i(TAG, "bean: " + bean + " model: " + model);
            if (model != null) {
                long timeout = V2TIMManager.getInstance().getServerTime() - bean.sendTime;
                if (timeout >= model.timeout) {
                    ToastUtil.toastLongMessage(BaseApplication.getInstance().getString(R.string.call_time_out));
                } else {
                    String callId = model.callId;
                    if (TextUtils.isEmpty(model.groupId)) {
                        callId = ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance())).c2cCallId;
                    }
                    ((TRTCAVCallImpl) (TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance()))).
                            processInvite(callId, bean.sender, model.groupId, model.invitedList, bean.content);
                }
            }
        }

        @Override
        public void redirectCall(OfflineMessageBean bean) {
            if (bean == null || bean.content == null) {
                return;
            }
            final CallModel model = new Gson().fromJson(bean.content, CallModel.class);
            DemoLog.i(TAG, "bean: " + bean + " model: " + model);
            if (model != null) {
                model.sender = bean.sender;
                model.data = bean.content;
                long timeout = V2TIMManager.getInstance().getServerTime() - bean.sendTime;
                if (timeout >= model.timeout) {
                    ToastUtil.toastLongMessage(BaseApplication.getInstance().getString(R.string.call_time_out));
                } else {
                    if (TextUtils.isEmpty(model.groupId)) {
                        Intent mainIntent = new Intent(BaseApplication.getInstance(), NewMainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        BaseApplication.getInstance().startActivity(mainIntent);
                    } else {
                        V2TIMSignalingInfo info = new V2TIMSignalingInfo();
                        info.setInviteID(model.callId);
                        info.setInviteeList(model.invitedList);
                        info.setGroupID(model.groupId);
                        info.setInviter(bean.sender);
                        V2TIMManager.getSignalingManager().addInvitedSignaling(info, new V2TIMCallback() {

                            @Override
                            public void onError(int code, String desc) {
                                DemoLog.e(TAG, "addInvitedSignaling code: " + code + " desc: " + desc);
                            }

                            @Override
                            public void onSuccess() {
                                Intent mainIntent = new Intent(BaseApplication.getInstance(), NewMainActivity.class);
                                mainIntent.putExtra(Constants.CALL_MODEL, model);
                                mainIntent.putExtra(Constants.IS_OFFLINE_PUSH_JUMP, true);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                BaseApplication.getInstance().startActivity(mainIntent);
                            }
                        });
                    }
                }
            }
        }

        @Override
        public Fragment getSceneFragment() {
//            return new ScenesFragment();
            return null;
        }

        @Override
        public void refreshUserInfo() {
            TUIKitLive.refreshLoginUserInfo(null);
        }

        @Override
        public boolean isDialingMessage(V2TIMMessage message) {
            CallModel callModel = CallModel.convert2VideoCallData(message);
            boolean isDialing = false;
            if (callModel != null && callModel.action == CallModel.VIDEO_CALL_ACTION_DIALING) {
                isDialing = true;
            }
            return isDialing;
        }

        @Override
        public Intent putCallExtra(Intent intent, String key, V2TIMMessage message) {
            CallModel callModel = CallModel.convert2VideoCallData(message);
            if (callModel != null && intent != null) {
                intent.putExtra(key, callModel);
            }
            return intent;
        }
    }

}
