package com.yyide.chatim.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yyide.chatim.R;
import com.yyide.chatim.net.GetData;



/**
 * Created by Hao on 2017/11/21.
 */

public class GlideUtil {

    public static String DataUrl(String url) {
        return TextUtils.isEmpty(url) ? "" : (url.startsWith("http") ? url : (GetData.imageUrl() + url));
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .centerInside().placeholder(R.mipmap.de1).error(R.mipmap.de1);
        Glide.with(context).load(GlideUtil.DataUrl(url)).apply(myOptions).into(imageView);

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
