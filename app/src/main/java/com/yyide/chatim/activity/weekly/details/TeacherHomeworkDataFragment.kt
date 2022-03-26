package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherHomeworkDataWeeklyBinding
import com.yyide.chatim.databinding.FragmentTeacherHomeworkWeeklyBinding
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.utils.DateUtils

/**
 *
 * 教师/班主任/家长 作业数据
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherHomeworkDataFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherHomeworkDataWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherHomeworkDataWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherHomeworkDataFragment().apply {

            }
    }

    private fun initView() {
        viewBinding.dataRecyclerview.layoutManager = LinearLayoutManager(activity)
        viewBinding.dataRecyclerview.adapter = adapter
        val datas = mutableListOf<String>()
        datas.add("1")
        datas.add("1")
        datas.add("1")
        datas.add("1")
        datas.add("1")
        adapter.setList(datas)
    }

    private val adapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_homework_data) {
            override fun convert(holder: BaseViewHolder, item: String) {

            }

        }

}