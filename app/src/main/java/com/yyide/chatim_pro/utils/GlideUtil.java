package com.yyide.chatim_pro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.net.GetData;

import java.security.MessageDigest;

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

    /**
     * 加载第四秒的帧数作为封面
     * url就是视频的地址
     */
    public static void loadCover(Context context, String url, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().frame(1000000).centerCrop())
                .load(url)
                .into(imageView);
    }

    public static void loadImageHead(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .into(imageView);
    }

    public static void loadImageHead(Context context, String url, ImageView imageView, int radius) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(url)
                .centerCrop()
                .apply(roundOptions)
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .into(imageView);
    }

    //圆图
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .circleCrop()
                .placeholder(R.drawable.default_head_round)
                .error(R.drawable.default_head_round)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    public static void loadImageRadius2(Context context, String path, ImageView img, int radius) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(path)
                .centerCrop()
                .apply(roundOptions)
                .placeholder(R.drawable.bg_corners_blue_2)
                .error(R.drawable.bg_corners_blue_2)
                .into(img);
    }

    //圆图
    public static void loadCircleImage(Context context, Drawable drawable, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .circleCrop().placeholder(R.drawable.bg_corners_blue100).error(R.drawable.bg_corners_blue100);
        Glide.with(context).load(drawable).apply(myOptions).into(imageView);
    }

    public static void loadImageRadius(Context context, String path, ImageView img, int radius) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(path)
                .centerCrop()
                .apply(roundOptions)
                .placeholder(R.drawable.bg_corners_white_10)
                .error(R.drawable.bg_corners_white_10)
                .into(img);
    }

    /**
     * 是否图片顶部开始显示
     *
     * @param context
     * @param path
     * @param img
     * @param radius
     * @param isTop
     */
    public static void loadImageRadius(Context context, String path, ImageView img, int radius, boolean isTop) {
        RequestOptions roundOptions = new RequestOptions()
                .transform(new RoundedCorners(radius));
        Glide.with(context).load(path)
                .centerCrop()
                .apply(roundOptions)
                .transform(new FitTopTransformation())
                .placeholder(R.mipmap.icon_placeholder)
                .error(R.mipmap.icon_placeholder)
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

    public static class FitTopTransformation extends BitmapTransformation {

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return Bitmap.createBitmap(toTransform, 0, 0, outWidth, outHeight);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
