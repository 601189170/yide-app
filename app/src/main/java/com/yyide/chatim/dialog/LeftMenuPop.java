package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.CacheDoubleUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.yyide.chatim.LoginActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PowerActivity;
import com.yyide.chatim.activity.ResetPassWordActivity;
import com.yyide.chatim.activity.UserActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.utils.FileCacheUtils;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;


/**
 * Created by Administrator on 2019/5/15.
 */

public class LeftMenuPop extends PopupWindow implements View.OnClickListener {
    Activity context;
    View mView;
    PopupWindow popupWindow;
    Window mWindow;
    private TextView user_class;
    private TextView head_name;
    private TextView user_identity;
    private TextView user_name;
    private TextView tv_cache;
    private ImageView head_img;

    public LeftMenuPop(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_leftuser_pop, null);
//        final PopupWindow popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.layout);

        FrameLayout bg = mView.findViewById(R.id.bg);
        head_name = mView.findViewById(R.id.head_name);
        user_identity = mView.findViewById(R.id.user_sf);
        user_class = mView.findViewById(R.id.user_class);
        user_name = mView.findViewById(R.id.user_name);
        head_img = mView.findViewById(R.id.head_img);
        tv_cache = mView.findViewById(R.id.tv_cache);
        mView.findViewById(R.id.iv_close).setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        LinearLayout layout1 = mView.findViewById(R.id.layout1);
        LinearLayout layout2 = mView.findViewById(R.id.layout2);
        LinearLayout layout3 = mView.findViewById(R.id.layout3);
        LinearLayout layout4 = mView.findViewById(R.id.layout4);
        LinearLayout layout5 = mView.findViewById(R.id.layout5);
        LinearLayout layout6 = mView.findViewById(R.id.layout6);
        LinearLayout layout7 = mView.findViewById(R.id.layout7);
        LinearLayout layout8 = mView.findViewById(R.id.layout8);
        mView.findViewById(R.id.exit).setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout7.setOnClickListener(this);
        layout8.setOnClickListener(this);
        setData();
//        new BottomMenuPop(context);
        layout.setOnTouchListener((v, event) -> true);
        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(false);//获取焦点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
        popupWindow.setClippingEnabled(false);
        // 获取当前Activity的window
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setFocusable(true);
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

                return true;
            }
            return false;
        });

        popupWindow.setOnDismissListener(() -> {
            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            if (mWindow != null) {
                WindowManager.LayoutParams params = mWindow.getAttributes();
                params.alpha = 1.0f;
                mWindow.setAttributes(params);
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        if (context != null) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow = context.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 0.7f;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
    }

    private void setData() {
        context.runOnUiThread(() -> {
            setCache();
            user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : "");
            user_identity.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().schoolName + "  " + SpData.getIdentityInfo().getIdentity() : "");
            head_name.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().realname : "");
            GlideUtil.loadImageHead(context, SpData.getIdentityInfo().img, head_img);
        });
    }

    private void setCache() {
        try {
            tv_cache.setText(FileCacheUtils.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if (popupWindow != null)
            if (context != null) {
                //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
                mWindow = context.getWindow();
                WindowManager.LayoutParams params = mWindow.getAttributes();
                params.alpha = 0.7f;
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mWindow.setAttributes(params);
            }
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
    }

    public void hide() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public boolean isshow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        //hide();
        switch (v.getId()) {
            case R.id.layout1://切换班级
                new SwichClassPop(context).setOnCheckCallBack(() -> user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : ""));
                break;
            case R.id.layout2://切换身份（学校）
                new SwichSchoolPop(context).setOnCheckCallBack(() -> {
                    setData();
                });
                break;
            case R.id.layout3://我的信息
                hide();
                context.startActivity(new Intent(context, UserActivity.class));
                break;
            case R.id.layout4://修改密码
                context.startActivity(new Intent(context, ResetPassWordActivity.class));
                break;
            case R.id.layout5://帮助中心
                hide();
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_CHECK_HELP_CENTER, ""));
                break;
            case R.id.layout6://权限设置
                context.startActivity(new Intent(context, PowerActivity.class));
                break;
            case R.id.layout7://清理缓存
                FileCacheUtils.clearAllCache(context);
                ToastUtils.showShort("缓存已清理");
                setCache();
                break;
            case R.id.layout8://版本更新
//                ToastUtils.showShort("已是最新版本");
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_APP, ""));
                break;
            case R.id.exit://退出登录
                SPUtils.getInstance().remove(BaseConstant.LOGINNAME);
                SPUtils.getInstance().remove(BaseConstant.PASSWORD);
                SPUtils.getInstance().remove(BaseConstant.JG_ALIAS_NAME);
                //退出登录IM
                TUIKit.logout(new IUIKitCallBack() {

                    @Override
                    public void onSuccess(Object data) {
                        //Log.d();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        //Log.d();
                    }
                });
                dismiss();
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finish();
                break;
        }
    }


}
