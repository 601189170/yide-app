package com.yyide.chatim_pro.adapter.leave;

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

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.AddressBookBean;
import com.yyide.chatim_pro.model.AddressBookRsp;

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
public class AddressBookMemberAdapter extends RecyclerView.Adapter<AddressBookMemberAdapter.ViewHolder> {
    private static final String TAG = AddressBookMemberAdapter.class.getSimpleName();
    private Context context;
    private List<AddressBookRsp.DataBean> data;
    //private boolean unfold = false;

    public void setOnCheckBoxChangeListener(OnCheckBoxChangeListener onCheckBoxChangeListener) {
        this.onCheckBoxChangeListener = onCheckBoxChangeListener;
    }

    private OnCheckBoxChangeListener onCheckBoxChangeListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public AddressBookMemberAdapter(Context context, List<AddressBookRsp.DataBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address_book_member, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);//禁止复用
        AddressBookRsp.DataBean bean = data.get(position);
        holder.tv_title.setText(bean.getTeacherName());
        //选中层级
        holder.checkBox.setChecked(bean.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bean.setChecked(isChecked);
            onCheckBoxChangeListener.change();
        });
    }

    public interface OnCheckBoxChangeListener {
        void change();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int level);
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
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
