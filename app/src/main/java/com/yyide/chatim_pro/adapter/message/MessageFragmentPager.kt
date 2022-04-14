package com.yyide.chatim_pro.adapter.message

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yyide.chatim_pro.activity.message.MessagePushActivity
import com.yyide.chatim_pro.activity.message.fragment.LostFoundFragment
import com.yyide.chatim_pro.activity.message.fragment.NoticeFragment

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 15:48
 * @description 信息发布的fragment适配器
 */
class MessageFragmentPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<Fragment> = SparseArray()
    init {
        fragments.put(MessagePushActivity.PAGE_NOTICE, NoticeFragment.newInstance())
        fragments.put(MessagePushActivity.PAGE_LOST, LostFoundFragment.newInstance())
    }
    override fun getItemCount(): Int {
        return fragments.size()
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}