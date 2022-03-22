package com.yyide.chatim.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import butterknife.BindView
import androidx.viewpager.widget.ViewPager
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.base.KTBaseFragment
import com.yyide.chatim.databinding.TodomsgFragmentBinding
import com.yyide.chatim.fragment.TodoMsgPageFragment
import com.yyide.chatim.fragment.leave.AskForLeaveListFragment
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment
import java.util.ArrayList

class TodoMsgFragment : KTBaseFragment<TodomsgFragmentBinding>(TodomsgFragmentBinding::inflate) {
    private val fragments: MutableList<Fragment> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        fragments.add(TodoMsgPageFragment.newInstance(3))
        fragments.add(TodoMsgPageFragment.newInstance(0))
        fragments.add(TodoMsgPageFragment.newInstance(1))
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