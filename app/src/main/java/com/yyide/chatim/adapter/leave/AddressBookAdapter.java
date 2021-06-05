package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.AddressBookBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 通知范围adapter，实现递归创建adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/12 11:02
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 11:02
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.ViewHolder> {
    private static final String TAG = AddressBookAdapter.class.getSimpleName();
    private Context context;
    private List<AddressBookBean> data;
    //private boolean unfold = false;

    public void setOnCheckBoxChangeListener(OnCheckBoxChangeListener onCheckBoxChangeListener) {
        this.onCheckBoxChangeListener = onCheckBoxChangeListener;
    }

    private OnCheckBoxChangeListener onCheckBoxChangeListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public AddressBookAdapter(Context context, List<AddressBookBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address_book, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);//禁止复用
        AddressBookBean bean = data.get(position);
        holder.tv_title.setText(bean.getName());
        holder.checkBox.setChecked(bean.isChecked());
        Log.e(TAG, "onBindViewHolder isUnfold: " + bean.isUnfold());
        if (bean.isUnfold()) {
            //bean.setUnfold(false);
            holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_up));
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            holder.mRecyclerView2.setVisibility(View.VISIBLE);
        } else {
            //bean.setUnfold(true);
            holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_down));
            holder.mRecyclerView.setVisibility(View.GONE);
            holder.mRecyclerView2.setVisibility(View.GONE);
        }

        if (bean.getDeptMemberList() != null) {
            holder.mRecyclerView2.setLayoutManager(new LinearLayoutManager(context));
            final AddressBookMemberAdapter addressBookMemberAdapter = new AddressBookMemberAdapter(context, bean.getDeptMemberList());
            holder.mRecyclerView2.setAdapter(addressBookMemberAdapter);
            addressBookMemberAdapter.setOnCheckBoxChangeListener(() -> {
                onCheckBoxChangeListener.change();
            });
        }
        holder.btn_level.setVisibility(View.VISIBLE);
        holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_down));

        holder.itemView.setOnClickListener(v -> {
            Log.e(TAG, "onBindViewHolder: onclick is unfold:" + bean.isUnfold());
            if (bean.isUnfold()) {
                bean.setUnfold(false);
                //holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_down));
                //holder.mRecyclerView.setVisibility(View.GONE);
            } else {
                bean.setUnfold(true);
                //holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_up));
                //holder.mRecyclerView.setVisibility(View.VISIBLE);
            }
            onCheckBoxChangeListener.change();
            onItemClickListener.onItemClick(bean);
            //new Handler().post(() -> notifyDataSetChanged());
        });

        if (bean.getList() != null && !bean.getList().isEmpty()) {
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            AddressBookAdapter adapter = new AddressBookAdapter(context, bean.getList());
//            holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            holder.mRecyclerView.setAdapter(adapter);
            adapter.setOnCheckBoxChangeListener(() -> {
                onCheckBoxChangeListener.change();
            });
            adapter.setOnItemClickListener(bean1 -> {
                onItemClickListener.onItemClick(bean1);
            });
        }
        //选中层级
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //bean.setUnfold(isChecked);
            //recursionChecked(bean, isChecked);
            //onCheckBoxChangeListener.change();
            //new Handler().post(() -> notifyDataSetChanged());
        });

    }

    public interface OnCheckBoxChangeListener {
        void change();
    }

    public interface OnItemClickListener {
        void onItemClick(AddressBookBean addressBookBean);
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private void recursionChecked(AddressBookBean noticeScopeBean, boolean isChecked) {
        noticeScopeBean.setChecked(isChecked);
        if (noticeScopeBean.getList() != null) {
            for (AddressBookBean scopeBean : noticeScopeBean.getList()) {
                recursionChecked(scopeBean, isChecked);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox)
        CheckBox checkBox;//显示图片
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.recyclerview)
        RecyclerView mRecyclerView;

        @BindView(R.id.recyclerview2)
        RecyclerView mRecyclerView2;
        @BindView(R.id.btn_level)
        ImageView btn_level;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
