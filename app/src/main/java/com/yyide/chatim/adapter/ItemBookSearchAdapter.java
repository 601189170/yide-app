package com.yyide.chatim.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.PersonInfoActivity;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.UserInfoRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.RxPermissionUtils;
import com.yyide.chatim.utils.StringUtils;

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
        GlideUtil.loadImageHead(context, "", holder.ivHead);
        holder.tv_name.setText(bean.getName());
        if ("1".equals(bean.getUserType())) {
            holder.tv_classname.setText(bean.getDepartmentName());
        } else {
            holder.tv_classname.setText(bean.getClassesName());
        }
        if (!TextUtils.isEmpty(bean.getPhone())) {
            holder.iv_call.setVisibility(View.VISIBLE);
        } else {
            holder.iv_call.setVisibility(View.GONE);
        }
        holder.iv_call.setOnClickListener(v -> {
            // 已经获取权限打电话
            RxPermissionUtils.callPhone(context, bean.getPhone());
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
            Intent intent = new Intent(context, PersonInfoActivity.class);
            intent.putExtra("data", JSON.toJSONString(recordsBean));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHead)
        ImageView ivHead;//显示图片
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
