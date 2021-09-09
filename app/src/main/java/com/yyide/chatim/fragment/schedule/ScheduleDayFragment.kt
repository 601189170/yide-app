package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleDayBinding
/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/日
 */
class ScheduleDayFragment : Fragment() {
    lateinit var fragmentScheduleDayBinding: FragmentScheduleDayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleDayBinding = FragmentScheduleDayBinding.inflate(layoutInflater)
        return fragmentScheduleDayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}