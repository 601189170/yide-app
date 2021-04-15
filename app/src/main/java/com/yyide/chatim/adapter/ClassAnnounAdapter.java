package com.yyide.chatim.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.ClassesHonorPhotoListActivity;
import com.yyide.chatim.model.ClassesBannerRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

/**
 * Created by Hao on 2017/11/29.
 */

public class ClassAnnounAdapter extends LoopPagerAdapter {

    public List<ClassesBannerRsp.DataBean> list = new ArrayList<>();
    Activity context;

    public ClassAnnounAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public ClassAnnounAdapter(RollPagerView viewPager, Activity context) {
        super(viewPager);
        this.context = context;
    }

    private ClassesBannerRsp.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item, null);

        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);

        //GlideUtil.loadImage(view.getContext(), getItem(position).getUrl(), img);
        img.setOnClickListener(v -> {
            if (mItemClickListener != null) mItemClickListener.OnItemClickListener();
        });
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(container.getContext()).load(container.getResources().getDrawable((getItem(position).getClassifyId()))).apply(options).into(img);
        return view;
    }

    public interface ItemClickListener {
        void OnItemClickListener();
    }

    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getRealCount() {
        return list.size();
    }

    public void notifyData(List<ClassesBannerRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
