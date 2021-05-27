package com.yyide.chatim.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.ClassesHonorPhotoListActivity;
import com.yyide.chatim.net.GetData;

/**
 * Created by Hao on 2017/11/21.
 */

public class GlideUtil {

    public static String DataUrl(String url) {
        return TextUtils.isEmpty(url) ? "" : (url.startsWith("http") ? url : (GetData.imageUrl() + url));
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

    public static void loadImageHead(Context context, String url, ImageView imageView) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(12));
        Glide.with(context).load(url)
                .centerCrop()
                .apply(roundOptions)
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .into(imageView);
    }

    //圆图
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .circleCrop().placeholder(R.mipmap.de1).error(R.mipmap.de1);
        Glide.with(context).load(GlideUtil.DataUrl(url)).apply(myOptions).into(imageView);
    }

    //圆图
    public static void loadCircleImage(Context context, Drawable drawable, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .circleCrop().placeholder(R.mipmap.de1).error(R.mipmap.de1);
        Glide.with(context).load(drawable).apply(myOptions).into(imageView);
    }

    public static void loadImageRadius(Context context, String path, ImageView img, int radius) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(path)
                .centerCrop()
                .apply(roundOptions)
                .placeholder(R.mipmap.icon_class)
                .error(R.mipmap.icon_class)
                .into(img);
    }


    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px转dp
    public static int px2dip(Context mcontext, float pxValue) {
        final float scale = mcontext.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }
}
