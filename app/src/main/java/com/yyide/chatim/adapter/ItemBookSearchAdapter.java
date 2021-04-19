package com.yyide.chatim.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.model.StudentHonorBean;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.UserInfoRsp;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 通讯录搜索adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemBookSearchAdapter extends RecyclerView.Adapter<ItemBookSearchAdapter.ViewHolder> {
    private Context context;
    private List<UserInfoRsp.DataBean> data;

    public ItemBookSearchAdapter(Context context, List<UserInfoRsp.DataBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserInfoRsp.DataBean bean = data.get(position);
        holder.tv_name.setText(bean.getName());
        if ("1".equals(bean.getUserType())) {
            holder.tv_classname.setText(bean.getDepartmentName());
        } else {
            holder.tv_classname.setText(bean.getClassesName());
        }
        if(!TextUtils.isEmpty(bean.getPhone())){
            holder.iv_call.setVisibility(View.VISIBLE);
        } else {
            holder.iv_call.setVisibility(View.GONE);
        }

        holder.iv_call.setOnClickListener(v -> {
            //打电话
            if (!TextUtils.isEmpty(bean.getPhone())) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + bean.getPhone());
                intent.setData(data);
                context.startActivity(intent);
            } else {
                ToastUtils.showShort("手机号为空，无法波打电话");
            }
        });
        holder.iv_user_detail.setOnClickListener(v -> {
            TeacherlistRsp.DataBean.RecordsBean recordsBean = new TeacherlistRsp.DataBean.RecordsBean();
            recordsBean.name = bean.getName();
            recordsBean.phone = bean.getPhone();
            recordsBean.email = bean.getEmail();
            recordsBean.sex = bean.getSex();
            recordsBean.classesName = bean.getClassesName();
            recordsBean.subjects = bean.getSubjects();
            recordsBean.primaryGuardianPhone = bean.getPrimaryGuardianPhone();
            recordsBean.deputyGuardianPhone = bean.getDeputyGuardianPhone();
            recordsBean.userType = bean.getUserType();

            //去详情页
            Intent intent = new Intent();
            intent.putExtra("data", JSON.toJSONString(recordsBean));
            intent.setClass(context, PersonInfoActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile)
        ImageView iv_profile;//显示图片
        @BindView(R.id.iv_user_detail)
        ImageView iv_user_detail;
        @BindView(R.id.iv_call)
        ImageView iv_call;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_classname)
        TextView tv_classname;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
