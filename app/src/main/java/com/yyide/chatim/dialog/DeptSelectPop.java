package com.yyide.chatim.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private int type;
    private OnCheckedListener onCheckedListener;
    private OnCheckedListener2 onCheckedListener2;
    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }
    public void setOnCheckedListener(OnCheckedListener2 onCheckedListener2) {
        this.onCheckedListener2 = onCheckedListener2;
    }

    public interface OnCheckedListener {
        void onOnCheckedListener(String id, String dept);
    }
    public interface OnCheckedListener2{
        void onOnCheckedListener(LeaveDeptRsp.DataBean dataBean);
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
        this.type = type;
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
            case 4:
                tvPopTitle.setText("切换学生");
                Drawable drawable = context.getResources().getDrawable(R.drawable.swich_person);
                //设置图片大小，必须设置
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvPopTitle.setCompoundDrawables(drawable, null, null, null);
                break;
            case 5:
                tvPopTitle.setText("选择学期");
                break;
            case 6:
                type = 6;
                tvPopTitle.setText("选择周次");
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
        final DeptSelectAdapter deptSelectAdapter = new DeptSelectAdapter(context,type, dataBeansList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(deptSelectAdapter);
        //val classOptional = dataBeansList.stream().filter { it.isDefault == 1 }.findFirst()
        final Optional<LeaveDeptRsp.DataBean> first = dataBeansList.stream().filter(it -> it.getIsDefault() == 1).findFirst();
        if (first.isPresent() && type == 6) {
            final LeaveDeptRsp.DataBean dataBean = first.get();
            final String deptId = dataBean.getDeptId();
            int position = Integer.parseInt(deptId) - 1;
            final int firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
            final int lastItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
            if (position < firstItem || position > lastItem) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(position, 0);
            } else {
                int movePosition = position - firstItem;
                final int top = recyclerView.getChildAt(movePosition).getTop();
                recyclerView.scrollBy(0, top);
            }
        }


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
                    if (onCheckedListener != null){
                        onCheckedListener.onOnCheckedListener(dataBean2.getDeptId(),dataBean2.getDeptName());
                    }

                    if (onCheckedListener2 != null){
                        onCheckedListener2.onOnCheckedListener(dataBean2);
                    }
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
        private int type;

        public void setOnClickedListener(DeptSelectAdapter.OnClickedListener onClickedListener) {
            this.onClickedListener = onClickedListener;
        }

        private OnClickedListener onClickedListener;

        public DeptSelectAdapter(Context context,int type, List<LeaveDeptRsp.DataBean> data) {
            this.context = context;
            this.data = data;
            this.type = type;
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
            holder.tv_title.setTextColor(b?holder.tv_title.getContext().getResources().getColor(R.color.colorPrimary):holder.tv_title.getContext().getResources().getColor(R.color.text_212121));
            if (type == 6 && dataBean.isCurWeek()) {
                holder.tvCurrentWeek.setVisibility(View.VISIBLE);
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.tvCurrentWeek.setVisibility(View.GONE);
            }
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
            @BindView(R.id.tv_current_week)
            TextView tvCurrentWeek;
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
