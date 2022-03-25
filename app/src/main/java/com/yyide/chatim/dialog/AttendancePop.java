package com.yyide.chatim.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.widget.ArrayWheelAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttendancePop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private SelectClasses mSelectClasses;

    public AttendancePop(Activity context, BaseQuickAdapter adapter, String title) {
        this.context = context;
        init(title, adapter);
    }

    private void init(String dialogTitle, BaseQuickAdapter adapter) {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bttom_pop, null);
        TextView cancel = mView.findViewById(R.id.tv_cancel);
        TextView title = mView.findViewById(R.id.tv_title);
        title.setText(dialogTitle);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        RecyclerView mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapter);
        cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (mSelectClasses != null) {
                mSelectClasses.OnSelectClassesListener(position);
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
            return false;
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

    private int index = 0;

    private final BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder> adapter = new BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, GetUserSchoolRsp.DataBean.FormBean item) {
            if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
                baseViewHolder.setText(R.id.className, item.classesStudentName);
            } else {
                baseViewHolder.setText(R.id.className, item.classesName);
            }
            if (adapter.getItemCount() - 1 == getItemPosition(item)) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
            baseViewHolder.getView(R.id.select).setVisibility(index == getItemPosition(item) ? View.VISIBLE : View.GONE);
        }
    };

    public void setIndex(int index) {
        this.index = index;
        adapter.notifyItemChanged(index);
    }

    public interface SelectClasses {
        void OnSelectClassesListener(int index);
    }

    public void setOnSelectListener(SelectClasses selectClasses) {
        this.mSelectClasses = selectClasses;
    }
}
