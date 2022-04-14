package com.yyide.chatim_pro.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim_pro.R

class LeftWipeRecyclerView : RecyclerView {
    //滚动动画，之所以不用Scroller，是因为Scroller 需要配合ondraw方法使用，而我只需要对child view scroll
    var scroller: ValueAnimator

    var touchSlop = 0 //最小的滑动单位，超过这个值才被认为是有效的滑动  系统值

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        //定义回弹效果的动画
        scroller = ValueAnimator()
        scroller.setInterpolator(OvershootInterpolator(5f))
        layoutManager = LinearLayoutManager(context)

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    override fun requestLayout() {
        super.requestLayout()
        if (mSwipeTargetView != null) {
            mSwipeTargetView?.scrollTo(0, 0)
            mSwipeTargetView = null
            isFirstCheckMoved = false
            isShouldCheckTouch = true
        }
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is LinearLayoutManager) {
            super.setLayoutManager(layout)
        } else {
            super.setLayoutManager(LinearLayoutManager(context))

        }
    }


    var speed = 1 // 回弹的速度系数

    //是否进行过水平方向移动
    //也就是基于这个判断touch event up 是否要传递给child 触发点击
    var isHorScrolled = false

    //滑动的View
    var mSwipeTargetView: View? = null

    //判断是否为第一次收到有效的touch Move
    //避免奇怪的滑动
    var isFirstCheckMoved: Boolean = false

    //如果进行了y轴方向的滚动，就不处理事件分发，
    // 交由普通RecyclerView的处理
    var isShouldCheckTouch: Boolean = true

    var lastPos = FloatArray(2)//记录上一个touch点坐标

    var scrollRange = 0 //记录滑动范围


    var isClickOtherItem = false //显示隐藏区域时 是否点击了其他位置


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //如果在滚动动画中 不接受任何touch事件
        if (isScrolling) return true

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                //down事件初始化
                isClickOtherItem = false
                isHorScrolled = false
                isFirstCheckMoved=false
                lastPos[0] = ev?.x
                lastPos[1] = ev?.y

                //判断如果有 滑动区域显示，这个时候touch
                // 漏出的View有效
                //其他的View touch事件拦截，将露出的View 滚回初始状态
                if (mSwipeTargetView != null) {
                    var clickRect = Rect()
                    mSwipeTargetView!!.getHitRect(clickRect)

                    if (!clickRect.contains(ev?.x.toInt(), ev?.y.toInt())) {
                        scrollToIdle(true)
                        isFirstCheckMoved = false
                        isShouldCheckTouch = true
                        //isClickOtherItem 设置为true 拦截事件传递
                        //{@link #onInterceptTouchEvent}
                        isClickOtherItem = true

                    }
                    return super.dispatchTouchEvent(ev)

                }


            }
            MotionEvent.ACTION_MOVE -> {
                if (isShouldCheckTouch) {
                    //检查是否是滚动的最小单位
                    if (Math.hypot(ev.x.toDouble() - lastPos[0], ev.y.toDouble() - lastPos[1]) > touchSlop) {
                        //是否是第一次滚动
                        //这样避免在recylerview 本身滚动的同时左右滑动手指引起奇怪的判断和View状态
                        if (!isFirstCheckMoved) {
                            //如果满足条件 检查是否是x方向还是y方向滚动
                            if (checkScollHorizon(ev.x, ev.y)) {
                                //第一次滚动 为水平方向滚动
                                //那么就找到并赋值当前的滑动View
                                if (mSwipeTargetView == null) {
                                    mSwipeTargetView = findItemByPostion(lastPos[0].toInt(), lastPos[1].toInt())
                                }
                            }
                            isFirstCheckMoved = true
                        }

                        if (mSwipeTargetView != null) {
                            //横向滑动超过一定水平，接下来的点击事件就不要了
                            if (Math.abs(ev.x.toDouble() - lastPos[0]) > touchSlop) {
                                isHorScrolled = true
                            }

                            if (scrollHor(ev)) {
                                return true
                            }
                        } else {
                            //没有滑动目标，那么就不再处理move 事件，
                            //交给recyclerview父类方法处理事件分发
                            lastPos[0] = ev.x
                            lastPos[1] = ev.y
                            isShouldCheckTouch = false
                        }
                    } else {
//                        滑动距离小于滑动判断临界
//                        为了效果流程，满足 x方向，并且滑动view不为Null，就横向滑动
                        return scrollHor(ev)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {

//                松手时
                isShouldCheckTouch = true
                if (mSwipeTargetView != null) {

                    //滑动view滚动到指定位置
                    // 默认 OR 显示全部隐藏视图
                    scrollToIdle()

                    //如果水平点击过 事件不向下传递
                    if (isHorScrolled) {
                        isHorScrolled = false
                        return true
                    }
                }


            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {

        // isClickOtherItem 是否拦截事件传递
        return super.onInterceptTouchEvent(e) || isClickOtherItem
    }


    //有滑动view后，滑动view跟随手指移动，要注意的是
    // 手指左滑，ev?.x - lastPos[0]<0
    // 而 要显示右边的区域 scrollx 要 >0
    //所以 mSwipeTargetView!!.scrollX - scrollvalue
    //即左滑显示右边
    private fun scrollHor(ev: MotionEvent?): Boolean {
        if (ev == null) return false
        if (mSwipeTargetView != null) {

            if (checkScollHorizon(ev?.x, ev?.y)) {
                var scrollvalue = (ev?.x - lastPos[0]).toInt()
                var scrolltarget = mSwipeTargetView!!.scrollX - scrollvalue
                if (scrolltarget >= scrollRange) {
                    scrolltarget = scrollRange
                } else if (scrolltarget < 0) {
                    scrolltarget = 0
                }
                mSwipeTargetView?.scrollTo(scrolltarget, 0)
            }
            lastPos[0] = ev?.x
            lastPos[1] = ev?.y
        }
        return true
    }

    //检查是否是x方向  判断依据 x方向是y方向的两倍
    fun checkScollHorizon(x: Float, y: Float): Boolean {
        return Math.abs(y - lastPos[1]) * 2 < Math.abs(x - lastPos[0])
    }

    //查找到点击的view
    //遍历 所以的child 的 hitrect
    //找到满足坐标在里面的view
    fun findItemByPostion(x: Int, y: Int): View? {


        var firstchildIndex = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (firstchildIndex < 0) {
            return null
        }

        val count = getChildCount();
        val itemRect = Rect()
        for (i in 0 until count) {
            val child = getChildAt(i);
            if (child != null && child.getVisibility() == View.VISIBLE) {
                child.getHitRect(itemRect);
                if (itemRect.contains(x, y)) {

                    //viewholderitem 需要标示隐藏区域的id为 R.id.scrollable，这样定位隐藏区域的width
                    scrollRange = child.findViewById<View>(R.id.scrollable).width

                    return child
                }
            }
        }
        return null
    }


    private var isScrolling: Boolean = false

    //滑动的view 松手或者点击别的区域 滚动到合适的位置
    //需要注意 如果是拖回起初位置时，手动释放掉view
    fun scrollToIdle(forceBase: Boolean = false) {
        if (mSwipeTargetView != null) {
            var isBackToZero = forceBase || scrollRange > mSwipeTargetView!!.scrollX * 2
            var scrollTarget = if (isBackToZero) 0 else scrollRange
            if (mSwipeTargetView!!.scrollX == scrollTarget) {
                if (scrollTarget == 0) {
                    mSwipeTargetView = null
                }
                return
            }
            scroller.setIntValues(mSwipeTargetView!!.scrollX, scrollTarget)
            scroller.addUpdateListener {
                mSwipeTargetView?.scrollTo(it.animatedValue as Int, 0)
            }
            scroller.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    mSwipeTargetView?.scrollTo(scrollTarget, 0)
                    if (scrollTarget == 0) {
                        mSwipeTargetView = null
                    }
                    scroller.removeAllUpdateListeners()
                    scroller.removeAllListeners()
                    isScrolling = false
                }
            })
            scroller.setDuration(Math.abs(scrollRange) / 2 * speed.toLong())
            scroller.start()
            isScrolling = true
        }
    }

    //释放资源
    override fun onDetachedFromWindow() {
        mSwipeTargetView=null
        scroller.cancel()
        scroller.removeAllUpdateListeners()
        scroller.removeAllListeners()
        super.onDetachedFromWindow()

    }


}