package com.yyide.chatim.activity.weekly.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.databinding.FragmentSchoolWeeklyBinding
import com.yyide.chatim.dialog.WeeklyTopPop

/**
 *
 * 校长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolWeeklyFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolWeeklyFragment().apply {}
    }

    private fun initView() {
        viewBinding.tvEvent.setOnClickListener {
            val dialog = WeeklyTopPop(activity, viewBinding.tvEvent)
            dialog.setOnSelectDialogMenu(object : WeeklyTopPop.DialogMenu {
                override fun onClickMenuListener(id: Long) {

                }
            })
        }
        viewBinding.tvTime.setOnClickListener {
            val dialog = WeeklyTopPop(activity, viewBinding.tvTime)
            dialog.setOnSelectDialogMenu(object : WeeklyTopPop.DialogMenu {
                override fun onClickMenuListener(id: Long) {

                }
            })
        }
    }
}