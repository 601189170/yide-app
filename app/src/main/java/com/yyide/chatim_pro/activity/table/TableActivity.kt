package com.yyide.chatim_pro.activity.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityTableTitleBinding
import com.yyide.chatim_pro.fragment.ClassTableFragment
import com.yyide.chatim_pro.fragment.MyTableFragment
import com.yyide.chatim_pro.fragment.SiteTableFragment

/**
 * 班级课表
 *
 * @update 2022年2月21日
 * @Author lrz
 * @Version 2.0
 */
class TableActivity : KTBaseActivity<ActivityTableTitleBinding>(ActivityTableTitleBinding::inflate) {

    var fragmentList = mutableListOf<Fragment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        binding.top.title.text = "课表"
        binding.top.backLayout.setOnClickListener { finish() }
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    private fun initViewPager() {
        fragmentList = ArrayList()
        fragmentList.add(MyTableFragment())
        fragmentList.add(ClassTableFragment())
        fragmentList.add(SiteTableFragment())
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getItemCount(): Int {
                return fragmentList.size
            }
        }

        TabLayoutMediator(binding.tablayout, binding.viewpager, true, false) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "我的课表"
                }
                1 -> {
                    tab.text = "班级课表"
                }
                else -> {
                    tab.text = "场地课表"
                }
            }
        }.attach()
    }

}