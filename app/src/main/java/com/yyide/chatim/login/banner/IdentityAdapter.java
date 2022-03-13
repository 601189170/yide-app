package com.yyide.chatim.login.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.yyide.chatim.R;
import com.yyide.chatim.model.IdentityBean;
import com.yyide.chatim.model.SchoolRsp;

import java.util.List;

/**
 * @Package: com.example.testproject.utils.banner
 * @ClassName: SchoolAdapter
 * @Author: szj
 * @CreateDate: 8/23/21 1:05 PM
 */
public class IdentityAdapter extends PagerAdapter {
    private final List<IdentityBean> mData;
    private final Context mContext;

    public IdentityAdapter(Context ctx, List<IdentityBean> data) {
        this.mContext = ctx;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();// 返回无限个
        //return mData.length;// 返回数据的个数
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {//子View显示
        View view = View.inflate(container.getContext(), R.layout.item_identity, null);
        ImageView imageView = view.findViewById(R.id.ivHead);
        IdentityBean schoolChildren = mData.get(position);
//        imageView.setOnClickListener(view1 -> Toast.makeText(mContext, "当前条目：" + position % mData.size(), Toast.LENGTH_SHORT).show());
        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(schoolChildren.getIdentityName());
        if ("2".equals(schoolChildren.getIdentity())) {
            imageView.setImageResource(R.mipmap.icon_parents_head);
        } else if ("1".equals(schoolChildren.getIdentity())) {
            imageView.setImageResource(R.mipmap.icon_techer_head);
        }
        container.addView(view);//添加到父控件
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;// 过滤和缓存的作用
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);//从viewpager中移除掉
    }
}
