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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.SwichClassAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LeaveApprovalBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 选择学生
 */

public class SwitchStudentPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    public int index = 0;
    private List<LeaveApprovalBean.LeaveClassBean> dataList = null;

    public SwitchStudentPop(Activity context, List<LeaveApprovalBean.LeaveClassBean> dataList, int index) {
        this.context = context;
        this.dataList = dataList;
        this.index = index;
        init();
    }

    public interface OnSelectCallBack {
        void onSelectCallBacks(int position);
    }

    private OnSelectCallBack mOnSelectCallBack;

    public void setOnSelectCallBack(OnSelectCallBack mOnSelectCallBack) {
        this.mOnSelectCallBack = mOnSelectCallBack;
    }

    private void init() {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_student_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);

        FrameLayout bg = mView.findViewById(R.id.bg);
        RecyclerView listview = mView.findViewById(R.id.recyclerView);
        ImageView imageView = mView.findViewById(R.id.ivClose);
        imageView.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        listview.setLayoutManager(new LinearLayoutManager(context));
        listview.setAdapter(adapter);
        adapter.setList(dataList);
        setIndex(index);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            setIndex(position);
            SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(adapter.getItem(position)));
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_HOME_CHECK_IDENTITY, ""));
            if (mOnSelectCallBack != null) {
                mOnSelectCallBack.onSelectCallBacks(position);
            }
        });

        popupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
            return false;
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

    private final BaseQuickAdapter<LeaveApprovalBean.LeaveClassBean, BaseViewHolder> adapter =
            new BaseQuickAdapter<LeaveApprovalBean.LeaveClassBean, BaseViewHolder>(R.layout.swich_class_item) {

                @Override
                protected void convert(@NonNull BaseViewHolder baseViewHolder, LeaveApprovalBean.LeaveClassBean item) {
                    baseViewHolder.setText(R.id.className, item.getStudentName());
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

}
