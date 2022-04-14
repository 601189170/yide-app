package com.yyide.chatim_pro.view;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesFlowItemDecoration extends RecyclerView.ItemDecoration {
    private int rightSpace;
    private int bottomSpace;


    public SpacesFlowItemDecoration(int rightSpace,int bottomSpace) {
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0)
            outRect.top = 0;
    }

    public static int px2dp(float dpValue) {
        return (int) (0.5f + dpValue / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}