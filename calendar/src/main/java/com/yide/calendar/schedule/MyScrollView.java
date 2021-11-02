package com.yide.calendar.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    private int mScrollY = 0;
    private boolean mClampedY = false;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //scrollY=位置0和最底y坐标;clampedY=是否到顶部或者底部
        mScrollY = scrollY;
        mClampedY = clampedY;
    }

    /**
     * 刷新到顶部和底部
     */
    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
        if (mClampedY){
            mClampedY = false;
            if (mScrollY == 0){
                if (scrollChangedListener != null){
                    scrollChangedListener.onScrollChangedTop();
                }

            }else {
                if (scrollChangedListener != null){
                    scrollChangedListener.onScrollChangedBottom();
                }
            }
        }
        Log.d("","======l==stopNestedScroll" );
    }

    public boolean isScrollTop() {
        return computeVerticalScrollOffset() == 0;
    }

    OnScrollChangedListener scrollChangedListener;

    public void setScrollChangedListener(OnScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    public interface OnScrollChangedListener{
        //到达顶部
        void onScrollChangedTop();
        //到达底部
        void onScrollChangedBottom();
    }
}