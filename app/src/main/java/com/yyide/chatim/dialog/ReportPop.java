package com.yyide.chatim.dialog;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.utils.LoadingTools;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Handle;

import java.util.ArrayList;
import java.util.List;


/**
 * 举报
 */

public class ReportPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;

    public ReportPop(Activity context) {
        this.context = context;
        init();
    }

    private OnCheckCallBack mOnCheckCallBack;

    public void setOnCheckCallBack(OnCheckCallBack mOnCheckCallBack) {
        this.mOnCheckCallBack = mOnCheckCallBack;
    }

    public interface OnCheckCallBack {
        void onCheckCallBack(GetUserSchoolRsp.DataBean.FormBean classBean);
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_report_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
        FrameLayout bg = mView.findViewById(R.id.bg);
        RecyclerView listview = mView.findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(context));
        listview.setAdapter(adapter);
        adapter.setList(initList());
        adapter.setOnItemClickListener((adapter, view, position) -> {
            LoadingTools loadingTools = new LoadingTools(context);
            loadingTools.showLoading();
            new Handler().postDelayed(() -> {
                ToastUtils.showShort("举报已被受理");
                loadingTools.closeLoading();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }, 1500);
        });
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
        TextView tv_cancel = mView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        // 获取当前Activity的window
        Activity activity = (Activity) mView.getContext();
        if (activity != null) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 0.7f;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        popupWindow.setOnDismissListener(() -> {
            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            Log.e("TAG", "onDismiss==>: ");
            if (mWindow != null) {
                WindowManager.LayoutParams params = mWindow.getAttributes();
                params.alpha = 1.0f;
                mWindow.setAttributes(params);
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
    }

    private List<String> initList() {
        List<String> lists = new ArrayList<>();
        lists.add("发布色情/违法信息");
        lists.add("存在欺诈骗钱行为");
        lists.add("冒充他人");
        lists.add("账号可能被盗用了");
        lists.add("侵犯未成年人权益");
        lists.add("存在其他违规行为");
        return lists;
    }

    private BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_report) {

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            holder.setText(R.id.tv_text, s);
        }
    };

}
