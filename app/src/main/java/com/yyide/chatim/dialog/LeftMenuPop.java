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

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.LoginActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PowerActivity;
import com.yyide.chatim.activity.ResetPassWordActivity;
import com.yyide.chatim.activity.UserActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.presenter.EventType;
import com.yyide.chatim.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2019/5/15.
 */

public class LeftMenuPop extends PopupWindow implements View.OnClickListener {
    Activity context;
    View mView;
    PopupWindow popupWindow;
    Window mWindow;

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

        FrameLayout bg = (FrameLayout) mView.findViewById(R.id.bg);
        TextView head_name = (TextView) mView.findViewById(R.id.head_name);
        TextView user_identity = (TextView) mView.findViewById(R.id.user_sf);
        TextView user_class = (TextView) mView.findViewById(R.id.user_class);
        ImageView head_img = (ImageView) mView.findViewById(R.id.head_img);
        LinearLayout layout1 = (LinearLayout) mView.findViewById(R.id.layout1);
        LinearLayout layout2 = (LinearLayout) mView.findViewById(R.id.layout2);
        LinearLayout layout3 = (LinearLayout) mView.findViewById(R.id.layout3);
        LinearLayout layout4 = (LinearLayout) mView.findViewById(R.id.layout4);
        LinearLayout layout5 = (LinearLayout) mView.findViewById(R.id.layout5);
        LinearLayout layout6 = (LinearLayout) mView.findViewById(R.id.layout6);
        LinearLayout layout7 = (LinearLayout) mView.findViewById(R.id.layout7);
        LinearLayout layout8 = (LinearLayout) mView.findViewById(R.id.layout8);
        mView.findViewById(R.id.exit).setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout7.setOnClickListener(this);
        layout8.setOnClickListener(this);

        user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : "");
        user_identity.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().schoolName + "\t" + SpData.getIdentityInfo().getIdentity() : "");

        head_name.setText(SpData.UserName());
        if (!TextUtils.isEmpty(SpData.UserPhoto())) {
            GlideUtil.loadImage(context, SpData.UserPhoto(), head_img);
        }
        Log.e("TAG", "UserName: " + JSON.toJSONString(SpData.UserName()));
        Log.e("TAG", "UserPhoto: " + JSON.toJSONString(SpData.UserPhoto()));
//        new BottomMenuPop(context);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bg.setBackgroundColor(context.getResources().getColor(R.color.float_transparent));
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
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
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }

                    return true;
                }
                return false;
            }
        });

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //如果设置了背景变暗，那么在dissmiss的时候需要还原
                if (mWindow != null) {
                    WindowManager.LayoutParams params = mWindow.getAttributes();
                    params.alpha = 1.0f;
                    mWindow.setAttributes(params);
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
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
        hide();
        switch (v.getId()) {
            case R.id.layout1://切换班级
                new SwichClassPop(context);
                break;
            case R.id.layout2://切换身份（学校）
                new SwichSchoolPop(context);
                break;
            case R.id.layout3://我的信息
                context.startActivity(new Intent(context, UserActivity.class));
                break;
            case R.id.layout4://修改密码
                context.startActivity(new Intent(context, ResetPassWordActivity.class));
                break;
            case R.id.layout5://帮助中心
                EventBus.getDefault().post(new EventType(BaseConstant.TYPE_CHECK_HELP_CENTER, ""));
                break;
            case R.id.layout6://权限设置
                context.startActivity(new Intent(context, PowerActivity.class));
                break;
            case R.id.layout7://清理缓存
                break;
            case R.id.layout8://版本更新
                break;
            case R.id.exit://退出登录
                SPUtils.getInstance().clear();
                dismiss();
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finish();
                break;
        }
    }
}
