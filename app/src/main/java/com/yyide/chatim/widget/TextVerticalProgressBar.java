package com.yyide.chatim.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.SizeUtils;

public class TextVerticalProgressBar extends ProgressBar {
    private Paint mPaint;
    private int mRealHeight;
    private double mProgress = 0;

    public TextVerticalProgressBar(Context context) {
        super(context);
        init();
    }

    public TextVerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextVerticalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(SizeUtils.sp2px(8f));
        mPaint.setColor(Color.parseColor("#909399"));
    }

    @Override
    public synchronized void setProgress(int progress) {
        mProgress = progress;
        setProgress(mProgress);
        super.setProgress(progress);
    }

    public void setProgress(double progress) {
        mProgress = progress;
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mRealHeight = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = mProgress + "%";
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int x = (getWidth() / 2)  - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(text, x, y, mPaint);
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            float heightExcludeText = getPaddingTop() + getPaddingBottom() + 20;
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            float textHeight = Math.abs(metrics.bottom - metrics.top + metrics.leading);
            result = (int) Math.max(heightExcludeText, textHeight);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
