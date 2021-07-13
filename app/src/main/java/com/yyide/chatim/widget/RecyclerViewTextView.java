package com.yyide.chatim.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecyclerViewTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RecyclerViewTextView(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int offset = getLineCount() * getLineHeight();
        if(offset > getHeight()){
            getParent().requestDisallowInterceptTouchEvent(true);//处理父控件拦截事件
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);//处理父控件拦截事件
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        int offset = getLineCount() * getLineHeight();
        Log.d("RecyclerViewTextView", "onScrollChanged: offset" + offset);
        if((offset - getHeight()) <= vert){
            Log.d("RecyclerViewTextView", "onScrollChanged: down" + vert);
            getParent().requestDisallowInterceptTouchEvent(false);//处理父控件拦截事件
        } else if(vert <= 1){
            Log.d("RecyclerViewTextView", "onScrollChanged: up" + vert);
            getParent().requestDisallowInterceptTouchEvent(false);//处理父控件拦截事件
        }

    }
}
