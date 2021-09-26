package com.yyide.chatim.activity.weekly.details.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class HotAdapter(list: MutableList<View>) : PagerAdapter() {
    var viewLists = mutableListOf<View>()

    init {
        this.viewLists = list
    }

    override fun getCount(): Int {
        return viewLists.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(viewLists.get(position))
        return viewLists[position]
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(viewLists[position])
    }
}