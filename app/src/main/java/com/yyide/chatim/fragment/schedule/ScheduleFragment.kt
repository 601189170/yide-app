package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleBinding
import com.yyide.chatim.view.DialogUtil

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
        //initFragmentContainer()
        setFragment(3)
        fragmentScheduleBinding.btnSearch.setOnClickListener {

        }

        fragmentScheduleBinding.btnSetting.setOnClickListener {
            DialogUtil.showScheduleMenuDialog(requireContext(),fragmentScheduleBinding.btnSetting){
                setFragment(it)
            }
        }
    }

    private fun setFragment(index: Int) {
        if (!(index ==  1 || index == 2 || index == 3)){
            return
        }
        val fm: FragmentManager = childFragmentManager
        val ft = fm.beginTransaction()
        var fg1 = fm.findFragmentByTag("f1")
        var fg2 = fm.findFragmentByTag("f2")
        var fg3 = fm.findFragmentByTag("f3")
        if (fg1 != null) ft.hide(fg1)
        if (fg2 != null) ft.hide(fg2)
        if (fg3 != null) ft.hide(fg3)

        when (index) {
            3 -> {
                if (fg1 == null) {
                    fg1 = ScheduleListFragment()
                    ft.add(R.id.fl_content, fg1, "f1")
                } else {
                    ft.show(fg1)
                }
            }
            1 -> {
                if (fg2 == null) {
                    fg2 = ScheduleDayFragment()
                    ft.add(R.id.fl_content, fg2, "f2")
                } else {
                    ft.show(fg2)
                }
            }

            2 -> {
                if (fg3 == null) {
                    fg3 = ScheduleMonthFragment()
                    ft.add(R.id.fl_content, fg3, "f3")
                } else {
                    ft.show(fg3)
                }
            }
            else -> {
            }
        }
        ft.commitAllowingStateLoss()
    }

    private fun initFragmentContainer() {
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
    }
}