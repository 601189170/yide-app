package com.yyide.chatim_pro.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;

/**************************************
 * *** http://weibo.com/lixiaodaoaaa **
 * *** create at 2017/5/18   23:45 ****
 * *******  by:lixiaodaoaaa  **********
 **************************************/

public class PColumn extends View {
    int MAX = 100;//最大
    int corner = 20;
    float data = 0;//显示的数
    float tempData = 0;
    int textPadding = 20;
    Paint mPaint;
    int mColor;

    Context mContext;

    public PColumn(Context context) {
        super(context);
        mContext = context;
    }

    public PColumn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public PColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = mContext.getResources().getColor(R.color.colorPrimary);
        mPaint.setColor(mColor);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (data == 0) {
            mPaint.setTextSize(getWidth() / 2);
            RectF oval3 = new RectF(0, getHeight() - SizeUtils.px2dp(20f), getWidth(), getHeight());// 设置个新的长方形
            canvas.drawRoundRect(oval3, SizeUtils.px2dp(corner), SizeUtils.px2dp(corner), mPaint);

            canvas.drawText("0",
                    getWidth() * 0.5f - mPaint.measureText("0") * 0.5f,
                    getHeight() - SizeUtils.px2dp(20) - 2 * SizeUtils.px2dp(textPadding),
                    mPaint);
            return;
        }

        //防止数值很大的的时候，动画时间过长
        float step = data / 100 + 1;

        if (tempData < data - step) {
            tempData = tempData + step;
        } else {
            tempData = data;
        }
        //画圆角矩形
        String S = tempData + "%";
        //一个字和两,三个字的字号相同
//        if (S.length() < 4) {
        mPaint.setTextSize(SizeUtils.sp2px(9f));
//        } else {
//            mPaint.setTextSize(getWidth() / (S.length() - 1));
//        }
        float textH = mPaint.ascent() + mPaint.descent();
        float MaxH = getHeight() - textH - 2 * SizeUtils.px2dp(textPadding);
        //圆角矩形的实际高度
        float realH = MaxH / MAX * tempData;
        RectF oval3 = new RectF(0, getHeight() - realH, getWidth(), getHeight());// 设置个新的长方形
        mPaint.setColor(mColor);
        canvas.drawRoundRect(oval3, SizeUtils.px2dp(corner), SizeUtils.px2dp(corner), mPaint);
        //写数字
        mPaint.setColor(Color.parseColor("#909399"));
        canvas.drawText(S,
                getWidth() * 0.5f - mPaint.measureText(S) * 0.5f,
                getHeight() - realH - 2 * SizeUtils.px2dp(textPadding),
                mPaint);
        if (tempData != data) {
            postInvalidate();
        }
    }

    public void setData(float data, int MAX) {
        this.data = data;
        tempData = 0;
        this.MAX = MAX;
        postInvalidate();
    }

}
