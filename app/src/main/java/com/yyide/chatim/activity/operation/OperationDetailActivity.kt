package com.yyide.chatim.activity.operation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.fragment.OperationChildFragment
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityOperationDetailBinding
import com.yyide.chatim.model.Detail

class OperationDetailActivity :
    KTBaseActivity<ActivityOperationDetailBinding>(ActivityOperationDetailBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewPager()

    }

    override fun initView() {
        binding.top.title.text = getString(R.string.operation_detail_title)
        binding.top.backLayout.setOnClickListener { finish() }
    }

    private fun initViewPager() {
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("已反馈")
        mTitles.add("未反馈")
        mTitles.add("已完成")
        mTitles.add("未读")
        binding.viewpager.offscreenPageLimit = mTitles.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int): Fragment {
                return OperationChildFragment.newInstance()
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