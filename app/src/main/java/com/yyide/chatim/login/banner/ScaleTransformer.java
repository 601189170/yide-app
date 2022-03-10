package com.yyide.chatim.login.banner;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.yyide.chatim.R;

/**
 * @Package: com.example.testproject.utils.banner
 * @ClassName: ScaleTransformer
 * @Author: szj
 * @CreateDate: 8/23/21 5:12 PM
 * TODO 自己模仿写的 缩放[中间大,两边小]ViewPager
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final float MAX_SCALE = 1.0f;//0缩放
    private static final float MIN_SCALE = 0.85f;//0.85缩放


    @Override
    public void transformPage(@NonNull View view, float position) {

        /*
         * 当不滑动状态下:
         *      position = -1 左侧View
         *      position = 0 当前View
         *      position = 1 右侧View
         *
         * 当滑动状态下:
         *  向左滑动: [ position < 0 && position > -1]
         *    左侧View      position < -1
         *    当前View    0 ~ -1
         *    右侧View   1 ~ 0
         *
         * 向右滑动:[position > 0 && position < 1 ]
         *   左侧View  -1 < position < 0
         *   当前View  0 ~ 1
         *   右侧View  position > 1
         */

        TextView textView = view.findViewById(R.id.tvName);
        View viewBg = view.findViewById(R.id.viewBg);
        if (position != 0) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            textView.setTextColor(Color.parseColor("#999999"));
            viewBg.setVisibility(View.VISIBLE);
            textView.setTextSize(13);
        } else {
            view.setScaleX(MAX_SCALE);
            view.setScaleY(MAX_SCALE);
            textView.setTextColor(Color.parseColor("#333333"));
            viewBg.setVisibility(View.INVISIBLE);
            textView.setTextSize(15);
        }
        if (position < 1) {
            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
            Log.i("szjScaleFactor", scaleFactor + "\tposition:" + position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
    }
}
