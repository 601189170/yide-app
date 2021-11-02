package com.yyide.chatim.widget;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

public class CircleFrameLayout extends FrameLayout {
    /**
     * 圆形的半径
     */
    private int radius;

    public CircleFrameLayout(Context context) {
        this(context, null);
    }

    public CircleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * ViewOutlineProvider 在view的尺寸，padding，margin发生变化后会调用
         */
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                Rect rect = new Rect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                outline.setRoundRect(rect, radius);
            }
        });
        setClipToOutline(true);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidateOutline();
    }
}