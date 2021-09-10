package com.yyide.chatim.view;

import android.content.Context;
import android.opengl.Visibility;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class YDVideo extends StandardGSYVideoPlayer {
    public YDVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public YDVideo(Context context) {
        super(context);
        init();
    }

    public YDVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveDataFullscreenButtonClick liveDataClick;//点击全屏按钮回调

    /**
     * 恢复暂停状态
     */
    public void onResume() {
        GSYVideoManager.onResume();
    }

    /**
     * 暂停状态
     */
    public void onPause() {
        GSYVideoManager.onPause();
    }

    /**
     * 接口回调
     *
     * @param liveDataClick
     */
    public void setOnFullscreenButtonClick(LiveDataFullscreenButtonClick liveDataClick) {
        this.liveDataClick = liveDataClick;
    }

    /* 重写方法自定义layout id与video_layout_standard.xml一致 不重新使用系统默认布局*/
//    @Override
//    public int getLayoutId() {
//        return R.layout.test;
//    }

    /* 初始化操作 */
    private void init() {
        //EXOPlayer内核，支持格式更多
//        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
//        CacheFactory.setCacheManager(ProxyCacheManager.class);
        //系统内核模式
//        PlayerFactory.setPlayManager(SystemPlayerManager.class);
        //ijk内核，默认模式
        PlayerFactory.setPlayManager(IjkPlayerManager.class);
        //exo缓存模式，支持m3u8，只支持exo
//        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
//        代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager.class);
        settingsVideo();
    }

    /* 一些播放器的设置  做一些UI的隐藏  可根据自己需求*/
    public void settingsVideo() {
        GSYVideoType.enableMediaCodec();//使能硬解码，播放前设置
        Debuger.enable();//打开GSY的Log
        //隐藏一些UI
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mLockScreen, GONE);
        setViewShowState(mLoadingProgressBar, GONE);
        setViewShowState(mTopContainer, GONE);
        setViewShowState(mThumbImageView, GONE);
        setViewShowState(mBottomProgressBar, VISIBLE);
        //显示一些UI   进度  时间  当前时间 全屏 返回  加载Loading  暂停开始
        setViewShowState(mStartButton, GONE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
        setViewShowState(mFullscreenButton, VISIBLE);
        setViewShowState(mBackButton, GONE);
        setViewShowState(mProgressBar, VISIBLE);
        setViewShowState(mCurrentTimeTextView, VISIBLE);
        setViewShowState(mTotalTimeTextView, VISIBLE);
        //setEnlargeImageRes(R.drawable.full);
        // setShrinkImageRes(R.drawable.full);
    }

    //拦截事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liveDataClick != null) {
                    liveDataClick.onClick();
                }
            }
        });
        return super.dispatchTouchEvent(ev);
    }

    /*  取消 双击暂停 */
//    @Override
//    protected void touchDoubleUp() {
//        super.touchDoubleUp();
//    }

    public interface LiveDataFullscreenButtonClick {
        void onClick();
    }
}
