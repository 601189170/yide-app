package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.ScheduleLabelManageActivity
import com.yyide.chatim.activity.schedule.ScheduleSearchActivity
import com.yyide.chatim.activity.schedule.ScheduleSettingsActivity
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.FragmentScheduleBinding
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程主页
 */
class ScheduleFragment : Fragment() {
    lateinit var fragmentScheduleBinding: FragmentScheduleBinding
    val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
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
            startActivity(Intent(activity, ScheduleSearchActivity::class.java))
        }

        fragmentScheduleBinding.btnSetting.setOnClickListener {
            DialogUtil.showScheduleMenuDialog(
                requireContext(),
                fragmentScheduleBinding.btnSetting
            ) {
                if (it == 4) {
                    startActivity(Intent(requireContext(), ScheduleLabelManageActivity::class.java))
                    return@showScheduleMenuDialog
                }

                if (it == 5){
                    startActivity(Intent(requireContext(),ScheduleSettingsActivity::class.java))
                    return@showScheduleMenuDialog
                }
                setFragment(it)
            }
        }
        fragmentScheduleBinding.tvCurrentDate.text = DateTime.now().toStringTime("yyyy年MM月")
        scheduleViewModel.curDateTime.observe(requireActivity(),{
            loge("更新日期：${it.toStringTime()}")
            fragmentScheduleBinding.tvCurrentDate.text = it.toStringTime("yyyy年MM月")
        })

    }

    private fun setFragment(index: Int) {
        if (!(index == 0 || index == 1 || index == 2 || index == 3)) {
            return
        }
        val fm: FragmentManager = childFragmentManager
        val ft = fm.beginTransaction()
        var fg0 = fm.findFragmentByTag("f0")
        var fg1 = fm.findFragmentByTag("f1")
        var fg2 = fm.findFragmentByTag("f2")
        var fg3 = fm.findFragmentByTag("f3")
        if (fg0 != null) ft.hide(fg0)
        if (fg1 != null) ft.hide(fg1)
        if (fg2 != null) ft.hide(fg2)
        if (fg3 != null) ft.hide(fg3)

        when (index) {
            0 -> {
                if (fg0 == null) {
                    fg0 = ScheduleTodayFragment()
                    ft.add(R.id.fl_content, fg0, "f0")
                } else {
                    ft.show(fg0)
                }
            }
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
}