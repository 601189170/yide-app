package com.yyide.chatim.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.yyide.chatim.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VerticalTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private int index = -1;
    private Context context;
    private Timer timer;
    private OnItemClickListener itemClickListener;
    private List<String> textList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next();
                    updateText();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //自定义View的构造方法
    public VerticalTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (timer == null) {
            timer = new Timer();
        }
        //实现ViewSwitcher.ViewFactory接口方法，创建出TextView并启动动画
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_animation));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_animation));
    }

    public void setResources(List<String> res) {
        textList = res;
    }

    //这个是自定义View的启动点，从外面传进来的间隔时间，并以此来开启这个定时任务器
    public void setTextStillTime(long time) {
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.scheduleAtFixedRate(new MyTask(), 1, time);
        }
    }

    //启动任务，每间隔time时间发送一个消息给handler更新文字
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }

    private int next() {
        int flag = index + 1;
        if (flag > textList.size() - 1) {
            flag = flag - textList.size();
        }
        return flag;
    }

    private void updateText() {
        setText(textList.get(index));
    }

    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.parseColor("#1F2833"));
        tv.setTextSize(14);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setClickable(true);
        tv.setOnClickListener(v -> {
            if (itemClickListener != null && textList.size() > 0 && index != -1) {
                itemClickListener.onItemClick(index % textList.size());
            }
        });
        return tv;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int var1);
    }
}
