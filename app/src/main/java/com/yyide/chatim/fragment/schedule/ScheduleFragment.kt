package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleBinding

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程主页
 */
class ScheduleFragment : Fragment() {
    lateinit var fragmentScheduleBinding: FragmentScheduleBinding
    val fragments = mutableListOf<Fragment>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentScheduleBinding = FragmentScheduleBinding.inflate(layoutInflater)
        return fragmentScheduleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragments.add(ScheduleListFragment())
        fragments.add(ScheduleDayFragment())
        fragments.add(ScheduleMonthFragment())

        fragmentScheduleBinding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        fragmentScheduleBinding.viewpager.isUserInputEnabled = false
        fragmentScheduleBinding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        fragmentScheduleBinding.tabLayout.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.tab_list -> {
                    fragmentScheduleBinding.viewpager.currentItem = 0
                }
                R.id.tab_day -> {
                    fragmentScheduleBinding.viewpager.currentItem = 1
                }
                R.id.tab_month -> {
                    fragmentScheduleBinding.viewpager.currentItem = 2
                }
            }
        }

        fragmentScheduleBinding.btnSearch.setOnClickListener {

        }

        fragmentScheduleBinding.btnSetting.setOnClickListener {

        }
    }
}