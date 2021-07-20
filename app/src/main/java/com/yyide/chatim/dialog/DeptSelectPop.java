package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.yyide.chatim.R;
import com.yyide.chatim.adapter.leave.LeaveReasonTagAdapter;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.utils.ButtonUtils;

import java.util.List;
import java.util.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeptSelectPop extends PopupWindow {
    Activity context;
    PopupWindow popupWindow;
    Window mWindow;
    private List<LeaveDeptRsp.DataBean> dataBeansList;
    private OnCheckedListener onCheckedListener;
    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public interface OnCheckedListener {
        void onOnCheckedListener(long id, String dept);
    }

    /**
     *
     * @param context
     * @param type 弹框类型 1选择部门 2，选择班级，3，选择考勤事件
     * @param dataBeansList
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public DeptSelectPop(Activity context, int type,List<LeaveDeptRsp.DataBean> dataBeansList) {
        if (ButtonUtils.isFastDoubleClick(context.hashCode(),1500)){
            return;
        }
        this.context = context;
        this.dataBeansList = dataBeansList;
        init(type);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(int type) {
        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_dept_select_bttom_pop, null);
        TextView cancel = mView.findViewById(R.id.cancel);
        ConstraintLayout bg = mView.findViewById(R.id.bg);
        RecyclerView recyclerView = mView.findViewById(R.id.departments);
        //设置弹框的标题和图标
        TextView tvPopTitle = mView.findViewById(R.id.tv_pop_title);
        switch (type){
            case 1:
                tvPopTitle.setText("选择部门");
                break;
            case 2:
                tvPopTitle.setText("选择班级");
                break;
            case 3:
                tvPopTitle.setText("选择考勤事件");
                break;
            default:
                break;
        }
        bg.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        //初始化列表
        final DeptSelectAdapter deptSelectAdapter = new DeptSelectAdapter(context, dataBeansList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(deptSelectAdapter);
        deptSelectAdapter.setOnClickedListener(position -> {
            //恢复状态
            for (LeaveDeptRsp.DataBean dataBean : dataBeansList) {
                dataBean.setIsDefault(0);
            }
            final LeaveDeptRsp.DataBean dataBean = dataBeansList.get(position);
            dataBean.setIsDefault(1);
            deptSelectAdapter.notifyDataSetChanged();

            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                final Optional<LeaveDeptRsp.DataBean> optionalDataBean = dataBeansList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
                if (optionalDataBean.isPresent()){
                    final LeaveDeptRsp.DataBean dataBean2 = optionalDataBean.get();
                    onCheckedListener.onOnCheckedListener(dataBean2.getDeptId(),dataBean2.getDeptName());
                }
            }
        });
        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setAnimationStyle(R.style.popwin_anim_style2);
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

    public static class DeptSelectAdapter extends RecyclerView.Adapter<DeptSelectAdapter.ViewHolder> {
        private Context context;
        private List<LeaveDeptRsp.DataBean> data;

        public void setOnClickedListener(DeptSelectAdapter.OnClickedListener onClickedListener) {
            this.onClickedListener = onClickedListener;
        }

        private OnClickedListener onClickedListener;

        public DeptSelectAdapter(Context context, List<LeaveDeptRsp.DataBean> data) {
            this.context = context;
            this.data = data;

        }

        @NonNull
        @Override
        public DeptSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dept_select,parent,false);
            return new DeptSelectAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeptSelectAdapter.ViewHolder holder, int position) {
            LeaveDeptRsp.DataBean dataBean = data.get(position);
            holder.tv_title.setText(dataBean.getDeptName());
            final boolean b = dataBean.getIsDefault() == 1;
            holder.checkBox.setVisibility(b?View.VISIBLE:View.GONE);
            holder.itemView.setOnClickListener(v -> {
                onClickedListener.onClicked(position);
            });

            holder.checkBox.setOnClickListener(v->{
                onClickedListener.onClicked(position);
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_title)
            TextView tv_title;
            @BindView(R.id.checkBox)
            ImageView checkBox;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public interface OnClickedListener{
            void onClicked(int position);
        }
    }

}