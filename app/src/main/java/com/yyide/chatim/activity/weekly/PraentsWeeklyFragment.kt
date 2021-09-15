package com.yyide.chatim.activity.weekly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleBinding
import com.yyide.chatim.databinding.FragmentSchoolWeeklyBinding

/**
 *
 * 家长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class PraentsWeeklyFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PraentsWeeklyFragment().apply {}
    }
}