package com.yyide.chatim.activity.operation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.databinding.OperationFragmentBinding

/**
 * @Desc 作业-家长
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationFragment : Fragment() {

    companion object {
        fun newInstance() = OperationFragment()
    }

    private lateinit var viewModel: OperationViewModel
    private lateinit var viewBinding: OperationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)
        initView()
    }

    private fun initView() {
        viewBinding.top.title.text = getString(R.string.operation_title)
        viewBinding.top.backLayout.setOnClickListener { activity?.finish() }
        viewBinding.top.ivEdit.setOnClickListener { }
        viewBinding.top.ivRight.setOnClickListener { }
        viewBinding.tvClassName.setOnClickListener { }

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