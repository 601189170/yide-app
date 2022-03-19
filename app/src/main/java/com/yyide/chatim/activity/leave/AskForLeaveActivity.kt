package com.yyide.chatim.activity.leave

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import butterknife.OnClick
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAskForLeaveBinding
import com.yyide.chatim.fragment.leave.AskForLeaveListFragment
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment

/**
 * 请假
 */
class AskForLeaveActivity :
    KTBaseActivity<ActivityAskForLeaveBinding>(ActivityAskForLeaveBinding::inflate) {

    var fragments: MutableList<BaseMvpFragment<*>> = ArrayList()
    override fun getContentViewID(): Int {
        return R.layout.activity_ask_for_leave
    }

    override fun initView() {
        super.initView()
        binding.top.title.setText(R.string.ask_for_leave)
        binding.top.backLayout.setOnClickListener { finish() }
        initViewPager()
    }

    @OnClick(R.id.back_layout)
    fun click() {
        finish()
    }

    private fun initViewPager() {
        fragments.add(RequestLeaveStaffFragment.newInstance("staff ask for leave"))
        fragments.add(AskForLeaveListFragment.newInstance("ask for leave record"))
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("我要请假")
        mTitles.add("请假记录")
        binding.viewpager.offscreenPageLimit = fragments.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                supportFragmentManager,
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