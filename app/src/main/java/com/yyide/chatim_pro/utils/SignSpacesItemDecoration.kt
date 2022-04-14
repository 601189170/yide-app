package com.yyide.chatim_pro.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author shenzhibin
 * @date 2022/4/1 9:54
 * @description recyclerview 间隔
 */
class SignSpacesItemDecoration : RecyclerView.ItemDecoration {

    private var transSpaces: Int = 0


    constructor(space: Int) {
        transSpaces = space
    }

    constructor(space: Int, context: Context) : this(space) {
        transSpaces = dip2px(space.toFloat(), context)
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = transSpaces
    }

    fun dip2px(dpValue: Float, context: Context): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}