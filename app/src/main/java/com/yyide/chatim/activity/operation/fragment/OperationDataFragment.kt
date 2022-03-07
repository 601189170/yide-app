package com.yyide.chatim.activity.operation.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.viewmodel.OperationTearcherViewModel
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.databinding.OperationDataFragmentBinding

/**
 * @Desc 作业-数据
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationDataFragment : Fragment() {

    companion object {
        fun newInstance() = OperationDataFragment()
    }

    private lateinit var viewModel: OperationTearcherViewModel
    private lateinit var viewBinding: OperationDataFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationDataFragmentBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationTearcherViewModel::class.java)
        initView()
    }

    private fun initView() {
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerView.adapter = mAdapter
    }

    private val mAdapter =
        object :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: String) {
                val viewBind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                val read = getString(R.string.operation_read_html, 0, 40)
                val completed = getString(R.string.operation_completed_html, 0, 40)
                Html.fromHtml(read)
                Html.fromHtml(completed)
            }
        }

}