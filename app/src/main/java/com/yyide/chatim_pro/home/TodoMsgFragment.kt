package com.yyide.chatim_pro.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim_pro.base.KTBaseFragment
import com.yyide.chatim_pro.databinding.TodomsgFragmentBinding
import com.yyide.chatim_pro.fragment.TodoMsgPageFragment
import java.util.ArrayList

class TodoMsgFragment : KTBaseFragment<TodomsgFragmentBinding>(TodomsgFragmentBinding::inflate) {
    private val fragments: MutableList<Fragment> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        fragments.add(TodoMsgPageFragment.newInstance(0))
        fragments.add(TodoMsgPageFragment.newInstance(1))
        fragments.add(TodoMsgPageFragment.newInstance(2))
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("全部")
        mTitles.add("待办")
        mTitles.add("已办")

        binding.viewpager.offscreenPageLimit = fragments.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                childFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        binding.slidingTabLayout.setViewPager(binding.viewpager)
        binding.slidingTabLayout.currentTab = 0
        binding.slidingTabLayout.notifyDataSetChanged()
    }
}