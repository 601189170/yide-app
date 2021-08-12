package com.yyide.chatim.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.yyide.chatim.LoginActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.PowerActivity;
import com.yyide.chatim.activity.ResetPassWordActivity;
import com.yyide.chatim.activity.UserActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.alipush.AliasUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.utils.FileCacheUtils;
import com.yyide.chatim.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.disposables.Disposable;

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
    private TextView tvVersion;
    private ImageView head_img;
    private ImageView ivClass, ivIdentity;
    private TextView my_info;
    private LinearLayout layout1, layout2;

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

        LinearLayout layout = mView.findViewById(R.id.layout);

        FrameLayout bg = mView.findViewById(R.id.bg);
        head_name = mView.findViewById(R.id.head_name);
        user_identity = mView.findViewById(R.id.user_sf);
        user_class = mView.findViewById(R.id.user_class);
        user_name = mView.findViewById(R.id.user_name);
        head_img = mView.findViewById(R.id.head_img);
        tv_cache = mView.findViewById(R.id.tv_cache);
        my_info = mView.findViewById(R.id.my_info);
        ivClass = mView.findViewById(R.id.iv_class);
        ivIdentity = mView.findViewById(R.id.iv_identity);
        tvVersion = mView.findViewById(R.id.tv_version);
        mView.findViewById(R.id.iv_close).setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        layout1 = mView.findViewById(R.id.layout1);
        layout2 = mView.findViewById(R.id.layout2);
        LinearLayout layout3 = mView.findViewById(R.id.layout3);
        LinearLayout layout4 = mView.findViewById(R.id.layout4);
        LinearLayout layout5 = mView.findViewById(R.id.layout5);
        LinearLayout layout6 = mView.findViewById(R.id.layout6);
        LinearLayout layout7 = mView.findViewById(R.id.layout7);
        LinearLayout layout8 = mView.findViewById(R.id.layout8);
        LinearLayout layout9 = mView.findViewById(R.id.layout9);
        LinearLayout layout10 = mView.findViewById(R.id.layout10);

        mView.findViewById(R.id.exit).setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout6.setOnClickListener(this);
        layout7.setOnClickListener(this);
        layout8.setOnClickListener(this);
        layout9.setOnClickListener(this);
        layout10.setOnClickListener(this);
        head_img.setOnClickListener(this);
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
            tvVersion.setText("V" + AppUtils.getAppVersionName());
            if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 1) {
                ivClass.setVisibility(View.VISIBLE);
                layout1.setEnabled(true);
            } else {
                layout1.setEnabled(false);
                ivClass.setVisibility(View.INVISIBLE);
            }
            if (SpData.Schoolinfo() != null && SpData.Schoolinfo().data != null && SpData.Schoolinfo().data.size() > 1) {
                ivIdentity.setVisibility(View.VISIBLE);
                layout2.setEnabled(true);
            } else {
                layout2.setEnabled(false);
                ivIdentity.setVisibility(View.INVISIBLE);
            }

            if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                my_info.setText("学生信息");
            } else {
                my_info.setText("我的信息");
            }
            //判断是否为家长
            if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                if (SpData.getClassInfo() != null) {
                    user_class.setText(!TextUtils.isEmpty(SpData.getClassInfo().classesStudentName) ? SpData.getClassInfo().classesStudentName : "无");
                } else {
                    user_class.setText("无");
                }
            } else {
                user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : "无");
            }
            setIdentity();
            head_name.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().realname : "");
            GlideUtil.loadImageHead(context, SpData.getIdentityInfo().img, head_img);
        });
    }

    public void setHeadImg(String path) {
        if (head_img != null) {
            GlideUtil.loadImageHead(context, path, head_img);
        }
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
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public boolean isShow() {
        return popupWindow != null && popupWindow.isShowing();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //hide();
        switch (v.getId()) {
            case R.id.layout1://切换班级
                if (SpData.getIdentityInfo().form != null && SpData.getIdentityInfo().form.size() > 0) {
                    new SwitchClassesStudentPop(context).setOnCheckCallBack(() -> {
                        setIdentity();
                        if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                            user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesStudentName : "");
                        } else {
                            user_class.setText(SpData.getClassInfo() != null ? SpData.getClassInfo().classesName : "");
                        }
                        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
                    });
                }
                break;
            case R.id.layout2://切换身份（学校）
                new SwitchSchoolPop(context).setOnCheckCallBack(this::setData);
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
            case R.id.layout9:
                break;
            case R.id.layout10:
                WebViewActivity.startTitle(context, BaseConstant.PRIVACY_URL, context.getString(R.string.privacy_title));
//                context.startActivity(new Intent(context, PrivacyActivity.class));
                break;
            case R.id.head_img:
                if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                    //rxPermission();
                }
                break;
            case R.id.exit://退出登录
                SPUtils.getInstance().remove(BaseConstant.PASSWORD);
                SPUtils.getInstance().remove(BaseConstant.JG_ALIAS_NAME);
                SPUtils.getInstance().remove(SpData.LOGINDATA);
                //清空消息推送别名
                AliasUtil.clearAlias();
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
//                SPUtils.getInstance().remove(BaseConstant.LOGINNAME);
                hide();
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finish();
                break;
        }
    }

    Disposable mDisposable;

    private void rxPermission() {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
        mDisposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                new BottomHeadMenuPop(context);
            } else {
                // 权限被拒绝
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage(R.string.permission_file)
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

    private void setIdentity() {
        //切换班级判断老师或班主任
        if (SpData.getIdentityInfo() != null
                && (GetUserSchoolRsp.DataBean.TYPE_CLASS_TEACHER.equals(SpData.getIdentityInfo().status)
                || GetUserSchoolRsp.DataBean.TYPE_TEACHER.equals(SpData.getIdentityInfo().status))
                && SpData.getClassInfo() != null
                && "N".equals(SpData.getClassInfo().teacherInd)) {
            user_identity.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().schoolName + "  " + "老师" : "");
        } else {
            user_identity.setText(SpData.getIdentityInfo() != null ? SpData.getIdentityInfo().schoolName + "  " + SpData.getIdentityInfo().getIdentity() : "");
        }
    }

}
