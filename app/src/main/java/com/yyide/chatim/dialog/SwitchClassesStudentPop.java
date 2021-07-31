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

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Administrator on 2019/5/15.
 */

public class SwitchClassesStudentPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    public int index = -1;

    public SwitchClassesStudentPop(Activity context) {
        this.context = context;
        init();
    }

    private OnCheckCallBack mOnCheckCallBack;

    public void setOnCheckCallBack(OnCheckCallBack mOnCheckCallBack) {
        this.mOnCheckCallBack = mOnCheckCallBack;
    }

    private void init() {
        int index = 0;
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_student_pop, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);

        FrameLayout bg = mView.findViewById(R.id.bg);
        RecyclerView listview = mView.findViewById(R.id.recyclerView);
        TextView tv_cancel = mView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        listview.setLayoutManager(new LinearLayoutManager(context));
        listview.setAdapter(adapter);
        //保存班级ID用于切换班级业务逻辑使用
        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().form != null) {
            adapter.setList(SpData.getIdentityInfo().form);
            List<GetUserSchoolRsp.DataBean.FormBean> list = SpData.getIdentityInfo().form;
            for (int i = 0; i < list.size(); i++) {
                if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                    //家长默认选择班级
                    if (!TextUtils.isEmpty(SpData.getClassInfo().classesStudentName)
                            && SpData.getClassInfo().classesStudentName.equals(list.get(i).classesStudentName)) {
                        index = i;
                        break;
                    }
                } else {
                    //教师默认选择班级
                    if (!TextUtils.isEmpty(SpData.getClassInfo().classesId)
                            && SpData.getClassInfo().classesId.equals(list.get(i).classesId)) {
                        index = i;
                        break;
                    }
                }

            }
        }
        setIndex(index);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            setIndex(position);
            SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(adapter.getItem(position)));
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""));
            EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_HOME_CHECK_IDENTITY, ""));
            if (mOnCheckCallBack != null) {
                mOnCheckCallBack.onCheckCallBack();
            }
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

    private final BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder> adapter = new BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, GetUserSchoolRsp.DataBean.FormBean item) {
            if (SpData.getIdentityInfo() != null && GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
                baseViewHolder.setText(R.id.className, item.classesStudentName);
            } else {
                baseViewHolder.setText(R.id.className, item.classesName);
            }

            if ("Y".equals(item.teacherInd)) {
                baseViewHolder.getView(R.id.name).setVisibility(View.VISIBLE);
            } else {
                baseViewHolder.getView(R.id.name).setVisibility(View.INVISIBLE);
            }

            if (adapter.getItemCount() - 1 == baseViewHolder.getAdapterPosition()) {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
            }
            baseViewHolder.getView(R.id.select).setVisibility(index == baseViewHolder.getAdapterPosition() ? View.VISIBLE : View.GONE);
        }
    };

    public void setIndex(int index) {
        this.index = index;
        adapter.notifyItemChanged(index);
    }

}
